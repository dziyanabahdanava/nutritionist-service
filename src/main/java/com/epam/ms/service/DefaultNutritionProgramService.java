package com.epam.ms.service;

import com.epam.ms.repository.DefaultNutritionProgramRepository;
import com.epam.ms.repository.entity.DefaultNutritionProgram;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.nonNull;

@Service
public class DefaultNutritionProgramService {
    @Autowired
    private DefaultNutritionProgramRepository repository;

    public List<DefaultNutritionProgram> getAll(Integer minCalories, Integer maxCalories) {
        return nonNull(minCalories) && nonNull(maxCalories)
                ? repository.findTopByCaloriesBetweenOrderByCaloriesDesc(minCalories, maxCalories)
                : (List) repository.findAll();
    }

    public DefaultNutritionProgram create(DefaultNutritionProgram entity) {
        return repository.save(entity);
    }

    public List<DefaultNutritionProgram> getAll() {
        return (List)repository.findAll();
    }

    public DefaultNutritionProgram getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Long update(Long id, DefaultNutritionProgram program) {
        Optional<DefaultNutritionProgram> existingProgram = repository.findById(id);
        if(!existingProgram.isPresent()) {
            return repository.save(program).getId();
        } else {
            DefaultNutritionProgram currentProgram = existingProgram.get();
            copyProgramData(program, currentProgram);
            repository.save(currentProgram);
            return null;
        }
    }

    private void copyProgramData(DefaultNutritionProgram from, DefaultNutritionProgram to) {
        to.setPath(from.getPath());
        to.setCalories(from.getCalories());
        to.setNumberOfDays(from.getNumberOfDays());
    }
}
