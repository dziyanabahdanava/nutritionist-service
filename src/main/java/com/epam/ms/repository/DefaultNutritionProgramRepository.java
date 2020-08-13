package com.epam.ms.repository;

import com.epam.ms.repository.entity.DefaultNutritionProgram;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DefaultNutritionProgramRepository extends CrudRepository<DefaultNutritionProgram, Long> {
    List<DefaultNutritionProgram> findTopByCaloriesBetweenOrderByCaloriesDesc(int min, int max);
}
