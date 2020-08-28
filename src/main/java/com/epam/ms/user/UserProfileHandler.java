package com.epam.ms.user;

import com.epam.ms.user.dto.UserProfile;
import org.springframework.stereotype.Component;

@Component
public class UserProfileHandler {
    private static final double WEIGHT_FACTOR = 9.99;
    private static final double HEIGHT_FACTOR = 6.25;
    private static final double AGE_FACTOR = -4.92;
    private static final double DEDUCTION = -161;

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
        return (WEIGHT_FACTOR * profile.getAge()
                + HEIGHT_FACTOR * profile.getHeight()
                + AGE_FACTOR * profile.getAge()
                + DEDUCTION) * factor;
    }
}
