package com.epam.ms.queue;

import com.epam.ms.event.Event;
import com.epam.ms.repository.domain.DefaultNutritionProgram;
import com.epam.ms.service.exception.ServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;
import com.google.common.collect.ImmutableMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class QueueHandler {
    private static final String NUTRITION_EVENTS_QUEUE = "nutritionEventsQueue";
    private static final String GROUP = "nutrition";

    @NonNull
    private AmqpTemplate template;
    @NonNull
    private ObjectMapper jacksonMapper;

    public void sendEventToQueue(String eventKey, DefaultNutritionProgram program) {
        log.info("Send event {} to queue {}", eventKey, NUTRITION_EVENTS_QUEUE);
        Event event = new Event(GROUP, eventKey, ImmutableMap.of("id", program.getId(), "calories", program.getCalories()));
        try {
            template.convertAndSend(NUTRITION_EVENTS_QUEUE, jacksonMapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            throw new ServiceException(String.format("Event %s could not be serialized", event), e);
        }
    }
}
