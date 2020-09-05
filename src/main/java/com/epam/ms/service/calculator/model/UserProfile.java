package com.epam.ms.service.calculator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {
    private String id;
    private int age;
    private int height;
    private int weight;
    private Goal goal;
    private PhysicalActivity physicalActivity;
}