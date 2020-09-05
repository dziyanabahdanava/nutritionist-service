package com.epam.ms.user.dto;

import lombok.Data;

@Data
public class UserProfile {
    private String id;
    private int age;
    private int height;
    private int weight;
    private Goal goal;
    private PhysicalActivity physicalActivity;
}