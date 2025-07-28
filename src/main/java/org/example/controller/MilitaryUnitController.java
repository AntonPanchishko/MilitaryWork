package org.example.controller;

import org.example.entity.MilitaryUnit;
import org.example.jpa.MilitaryUnitRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/units")
public class MilitaryUnitController {

    private final MilitaryUnitRepository repository;

    public MilitaryUnitController(MilitaryUnitRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<MilitaryUnit> getAll() {
        return repository.findAll();
    }
}
