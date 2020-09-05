package com.epam.ms.service.calculator;

import com.epam.ms.config.ConfigProperties;
import com.epam.ms.service.calculator.model.UserProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CaloriesCalculator {
    @Autowired
    private ConfigProperties properties;

    public int calculateMinCaloriesForUsersGoal(UserProfile profile) {
        double factor = 1;
        switch (profile.getGoal()) {
            case SLIMMING:
            case CUT:
            case BODY_RELIEF:
                factor = 0.8;
                break;
            case BULK:
                factor = 1.1;
                break;
        }

        return (int) Math.round(calculateCalorieRate(profile) * factor);
    }

    public int calculateMaxCaloriesForUsersGoal(UserProfile profile) {
        double factor = 1;
        switch (profile.getGoal()) {
            case SLIMMING:
            case CUT:
            case BODY_RELIEF:
                factor = 0.99;
                break;
            case BULK:
                factor = 1.2;
                break;
            case MAINTENANCE:
                factor = 1.08;
                break;

        }
        return (int) Math.round(calculateCalorieRate(profile) * factor);
    }

    private double calculateCalorieRate(UserProfile profile) {
        double factor = 1;
        switch (profile.getPhysicalActivity()) {
            case SEDENTARY_LIFESTYLE:
                factor = 1.2;
                break;
            case LOW_ACTIVITY:
                factor = 1.375;
                break;
            case ACTIVE_LIFESTYLE:
                factor = 1.555;
                break;
        }

        double caloriesRate = (properties.getWeightFactor() * profile.getWeight()
                + properties.getHeightFactor() * profile.getHeight()
                + properties.getAgeFactor() * profile.getAge()
                + properties.getDeduction()) * factor;
        log.info("The calories rate for {} is: {}", profile.getId(), caloriesRate);
        return caloriesRate;
    }
}
