package com.epam.ms.controller;

import com.epam.ms.repository.domain.DefaultNutritionProgram;
import com.epam.ms.service.DefaultNutritionProgramService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.net.URI;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * REST controller exposes an endpoint to work with base nutrition programs
 * @author Dziyana Bahdanava
 */
@RestController
@RequestMapping("/default_nutrition_program")
@Slf4j
@RequiredArgsConstructor
public class DefaultNutritionProgramController {
    @NonNull
    private DefaultNutritionProgramService service;

    @GetMapping
    public List<DefaultNutritionProgram> findAll(@RequestParam(required = false) @Min(1) Integer minCalories, @RequestParam(required = false) @Min(2) Integer maxCalories) {
        return service.getAll(minCalories, maxCalories);
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable String id) {
        DefaultNutritionProgram program = service.findById(id);
        log.debug("Trying to find a program with id {}", id);
        return isNull(program)
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(program);
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody DefaultNutritionProgram user) {
        DefaultNutritionProgram createdProgram = service.create(user);
        String id = createdProgram.getId();
        log.info("A new default nutrition program is created: /default_nutrition_program/{}", id);
        return ResponseEntity.created(
                URI.create(String.format("/default_nutrition_program/%s", id)))
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        log.info("The default nutrition program with id {} is deleted", id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody DefaultNutritionProgram program) {
        DefaultNutritionProgram createdProgram = service.update(id, program);
        if(nonNull(createdProgram)) {
            log.info("The default nutrition program with id {} is updated", id);
            return ResponseEntity.noContent().build();
        } else {
            log.error("The default nutrition program with id {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }
}
