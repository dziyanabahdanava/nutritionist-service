package com.epam.ms.repository;

import com.epam.ms.repository.domain.DefaultNutritionProgram;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DefaultNutritionProgramRepository extends CrudRepository<DefaultNutritionProgram, String> {
    List<DefaultNutritionProgram> findByCaloriesBetweenOrderByCaloriesDesc(int min, int max);
}
