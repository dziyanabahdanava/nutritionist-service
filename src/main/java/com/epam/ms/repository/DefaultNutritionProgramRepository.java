package com.epam.ms.repository;

import com.epam.ms.repository.domain.DefaultNutritionProgram;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DefaultNutritionProgramRepository extends MongoRepository<DefaultNutritionProgram, String> {
    List<DefaultNutritionProgram> findByCaloriesBetweenOrderByCaloriesDesc(int min, int max);
}
