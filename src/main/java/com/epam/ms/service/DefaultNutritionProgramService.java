package com.epam.ms.service;

import com.epam.ms.event.Event;
import com.epam.ms.repository.DefaultNutritionProgramRepository;
import com.epam.ms.repository.domain.DefaultNutritionProgram;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.nonNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultNutritionProgramService {
    private static final String NUTRITION_EVENTS_QUEUE = "nutritionEventsQueue";
    private static final String GROUP = "nutrition";
    private static final String NUTRITION_PROGRAM_CREATED_EVENT = "nutrition.created";
    private static final String NUTRITION_PROGRAM_UPDATED_EVENT = "nutrition.updated";

    @NonNull
    private DefaultNutritionProgramRepository repository;
    @NonNull
    private AmqpTemplate template;
    @NonNull
    private ObjectMapper jacksonMapper;

    public List<DefaultNutritionProgram> getAll(Integer minCalories, Integer maxCalories) {
        return nonNull(minCalories) && nonNull(maxCalories)
                ? repository.findByCaloriesBetweenOrderByCaloriesDesc(minCalories, maxCalories)
                : repository.findAll();
    }

    public DefaultNutritionProgram create(DefaultNutritionProgram entity) throws JsonProcessingException {
        DefaultNutritionProgram createdProgram = repository.save(entity);
        sendEventToQueue(NUTRITION_PROGRAM_CREATED_EVENT, createdProgram);
        return createdProgram;
    }

    public List<DefaultNutritionProgram> getAll() {
        return repository.findAll();
    }

    public DefaultNutritionProgram findById(String id) {
        return repository.findById(id).orElse(null);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    public DefaultNutritionProgram update(String id, DefaultNutritionProgram program) throws JsonProcessingException {
        Optional<DefaultNutritionProgram> existingProgram = repository.findById(id);
        if(existingProgram.isPresent()) {
            DefaultNutritionProgram currentProgram = existingProgram.get();
            copyProgramData(program, currentProgram);
            sendEventToQueue(NUTRITION_PROGRAM_UPDATED_EVENT, currentProgram);
            return repository.save(currentProgram);
        } else {
            return null;
        }
    }

    private void copyProgramData(DefaultNutritionProgram from, DefaultNutritionProgram to) {
        to.setPath(from.getPath());
        to.setCalories(from.getCalories());
        to.setNumberOfDays(from.getNumberOfDays());
    }

    private void sendEventToQueue(String eventKey, DefaultNutritionProgram program) throws JsonProcessingException {
        log.info("Send event {} to queue {}", eventKey, NUTRITION_EVENTS_QUEUE);
        Event event = new Event(GROUP, eventKey, ImmutableMap.of("id", program.getId(), "calories", program.getCalories()));
        template.convertAndSend(NUTRITION_EVENTS_QUEUE, jacksonMapper.writeValueAsString(event));
    }
}
