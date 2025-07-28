package org.example.controller;

import org.example.dto.MilitaryUnitDto;
import org.example.dto.WorkDayDto;
import org.example.entity.WorkDay;
import org.example.jpa.WorkDayRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/work-days")
public class WorkDayController {

    private final WorkDayRepository repository;

    public WorkDayController(WorkDayRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<WorkDayDto> getAll() {
        List<WorkDay> workDays = repository.findAll();

        return workDays.stream()
                .map(wd -> {
                    WorkDayDto dto = new WorkDayDto();
                    dto.setId(wd.getId());
                    dto.setDate(wd.getWorkDate());

                    if (wd.getMilitaryUnit() != null) {
                        MilitaryUnitDto mu = new MilitaryUnitDto();
                        mu.setId(wd.getMilitaryUnit().getId());
                        mu.setLastName(wd.getMilitaryUnit().getName());
                        dto.setMilitaryUnit(mu);
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }
}
