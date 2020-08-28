package com.epam.ms.service;

import com.epam.ms.queue.QueueHandler;
import com.epam.ms.repository.DefaultNutritionProgramRepository;
import com.epam.ms.repository.domain.DefaultNutritionProgram;
import com.epam.ms.service.exception.ServiceException;
import com.epam.ms.user.UserProfileHandler;
import com.epam.ms.user.dto.UserProfile;
import com.epam.ms.user.UserServiceMediator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.nonNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultNutritionProgramService {
    private static final String NUTRITION_PROGRAM_CREATED_EVENT = "nutrition.created";
    private static final String NUTRITION_PROGRAM_UPDATED_EVENT = "nutrition.updated";

    @NonNull
    private DefaultNutritionProgramRepository repository;
    @NonNull
    private QueueHandler queueHandler;
    @NonNull
    private UserServiceMediator mediator;
    @NonNull
    private UserProfileHandler handler;

    public List<DefaultNutritionProgram> getAll(Integer minCalories, Integer maxCalories) {
        return nonNull(minCalories) && nonNull(maxCalories)
                ? repository.findByCaloriesBetweenOrderByCaloriesDesc(minCalories, maxCalories)
                : repository.findAll();
    }

    public DefaultNutritionProgram create(DefaultNutritionProgram entity) {
        DefaultNutritionProgram createdProgram = repository.save(entity);
        queueHandler.sendEventToQueue(NUTRITION_PROGRAM_CREATED_EVENT, createdProgram);
        return createdProgram;
    }

    public DefaultNutritionProgram findById(String id) {
        return repository.findById(id).orElse(null);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    public List<DefaultNutritionProgram> findForUser(String userId) {
        List<UserProfile> userProfiles = mediator.getUserProfile(userId);
        if(userProfiles.isEmpty()) {
            throw new ServiceException(String.format("User profile for %s is not completed", userId));
        }
        UserProfile profile = userProfiles.get(0);
        int minCalories = handler.calculateMinCaloriesForUsersGoal(profile);
        int maxCalories = handler.calculateMaxCaloriesForUsersGoal(profile);

        return repository.findByCaloriesBetweenOrderByCaloriesDesc(minCalories, maxCalories);
    }

    public DefaultNutritionProgram update(String id, DefaultNutritionProgram program) {
        Optional<DefaultNutritionProgram> existingProgram = repository.findById(id);
        if(existingProgram.isPresent()) {
            DefaultNutritionProgram currentProgram = existingProgram.get();
            copyProgramData(program, currentProgram);
            queueHandler.sendEventToQueue(NUTRITION_PROGRAM_UPDATED_EVENT, currentProgram);
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
}
