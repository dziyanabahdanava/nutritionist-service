package com.epam.ms.controller;

import com.epam.ms.repository.entity.DefaultNutritionProgram;
import com.epam.ms.service.DefaultNutritionProgramService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class DefaultNutritionProgramController {
    private static final String CREATED_USER_URI = "/users/%s";

    @Autowired
    private DefaultNutritionProgramService service;

    @GetMapping
    public List<DefaultNutritionProgram> getAll(@RequestParam(required = false) @Min(1) Integer minCalories, @RequestParam(required = false) @Min(2) Integer maxCalories) {
        return service.getAll(minCalories, maxCalories);
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable Long id) {
        DefaultNutritionProgram program = service.getById(id);
        return isNull(program)
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(program);
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody DefaultNutritionProgram user) {
        DefaultNutritionProgram createdProgram = service.create(user);
        return ResponseEntity.created(
                URI.create(String.format(CREATED_USER_URI, createdProgram.getId())))
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody DefaultNutritionProgram program) {
        Long createdProgramId = service.update(id, program);
        return nonNull(createdProgramId)
                ? ResponseEntity.created(URI.create(String.format(CREATED_USER_URI, createdProgramId.toString()))).build()
                : ResponseEntity.noContent().build();

    }

}
