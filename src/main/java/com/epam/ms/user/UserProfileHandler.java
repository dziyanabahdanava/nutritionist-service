package com.epam.ms.user;

import com.epam.ms.config.ConfigProperties;
import com.epam.ms.user.dto.UserProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserProfileHandler {
    @Autowired
    private ConfigProperties properties;

    public int calculateMinCaloriesForUsersGoal(UserProfile profile) {
        double factor = switch(profile.getGoal()) {
            case SLIMMING, CUT, BODY_RELIEF -> 0.8;
            case BULK -> 1.1;
            case MAINTENANCE -> 1.0;
        };
        return (int) Math.round(calculateCalorieRate(profile) * factor);
    }

    public int calculateMaxCaloriesForUsersGoal(UserProfile profile) {
        double factor = switch(profile.getGoal()) {
            case SLIMMING, CUT, BODY_RELIEF -> 0.99;
            case BULK -> 1.2;
            case MAINTENANCE -> 1.08;
        };
        return (int) Math.round(calculateCalorieRate(profile) * factor);
    }

    private double calculateCalorieRate(UserProfile profile) {
        double factor = switch(profile.getPhysicalActivity()) {
            case SEDENTARY_LIFESTYLE -> 1.2;
            case LOW_ACTIVITY -> 1.375;
            case ACTIVE_LIFESTYLE -> 1.555;
        };
        double caloriesRate = (properties.getWeightFactor() * profile.getWeight()
                + properties.getHeightFactor() * profile.getHeight()
                + properties.getAgeFactor() * profile.getAge()
                + properties.getDeduction()) * factor;
        log.info("The calories rate for {} is: {}", profile.getId(), caloriesRate);
        return caloriesRate;
    }
}
