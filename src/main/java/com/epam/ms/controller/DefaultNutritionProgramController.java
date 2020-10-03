package com.epam.ms.controller;

import com.epam.ms.repository.domain.DefaultNutritionProgram;
import com.epam.ms.service.DefaultNutritionProgramService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<List<DefaultNutritionProgram>> findAll(@RequestParam(required = false) @Min(1) Integer minCalories,
                                                                 @RequestParam(required = false) @Min(2) Integer maxCalories,
                                                                 @RequestParam (required = false) String userId) {
        return isNull(userId) ?
                ResponseEntity.ok(service.getAll(minCalories, maxCalories))
                : ResponseEntity.ok(service.findForUser(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DefaultNutritionProgram> getById(@PathVariable String id) {
        DefaultNutritionProgram program = service.findById(id);
        log.debug("Trying to find a program with id {}", id);
        return isNull(program)
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(program);
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody DefaultNutritionProgram user) throws JsonProcessingException {
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
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody DefaultNutritionProgram program) throws JsonProcessingException {
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
