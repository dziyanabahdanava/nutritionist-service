package com.epam.ms.repository.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
//@Entity
//@Table(name = "default_nutrition_programs", schema = "nutrition")
@Document(collection = "default_nutrition_programs")
@EqualsAndHashCode(callSuper=false)
public class DefaultNutritionProgram extends BaseNutritionistEntity {

    @Size(max = 255, message = "The path must be up to 255 characters")
    @NotNull(message = "Path cannot be null")
    private String path;

    @Min(100)
    @NotNull(message = "Calories cannot be null")
    private int calories;

    @Min(1)
    @NotNull(message = "Numbers of days cannot be null")
    private int numberOfDays;
}
