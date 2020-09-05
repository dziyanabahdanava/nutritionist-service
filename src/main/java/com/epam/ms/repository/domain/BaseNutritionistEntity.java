package com.epam.ms.repository.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;


@Data
//@MappedSuperclass
public class BaseNutritionistEntity {
    @Id
    private String id;
}
