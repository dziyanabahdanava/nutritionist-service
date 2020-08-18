package com.epam.ms.service;

import com.epam.ms.repository.DefaultNutritionProgramRepository;
import com.epam.ms.repository.domain.DefaultNutritionProgram;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class DefaultNutritionProgramService {
    @NonNull
    private DefaultNutritionProgramRepository repository;

    public List<DefaultNutritionProgram> getAll(Integer minCalories, Integer maxCalories) {
        return nonNull(minCalories) && nonNull(maxCalories)
                ? repository.findByCaloriesBetweenOrderByCaloriesDesc(minCalories, maxCalories)
                : (List) repository.findAll();
    }

    public DefaultNutritionProgram create(DefaultNutritionProgram entity) {
        return repository.save(entity);
    }

    public List<DefaultNutritionProgram> getAll() {
        return (List)repository.findAll();
    }

    public DefaultNutritionProgram findById(String id) {
        return repository.findById(id).orElse(null);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    public DefaultNutritionProgram update(String id, DefaultNutritionProgram program) {
        Optional<DefaultNutritionProgram> existingProgram = repository.findById(id);
        if(existingProgram.isPresent()) {
            DefaultNutritionProgram currentProgram = existingProgram.get();
            copyProgramData(program, currentProgram);
            return repository.save(currentProgram);
        } else {
            return null;
        }
    }

    private void copyProgramData(DefaultNutritionProgram from, DefaultNutritionProgram to) {
        to.setPath(from.getPath());
        to.setCalories(from.getCalories());
        to.setNumberOfDays(from.getNumberOfDays());
    }
}
