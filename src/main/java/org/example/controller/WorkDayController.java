package org.example.controller;

import org.example.dto.MilitaryUnitDto;
import org.example.dto.WorkDayDto;
import org.example.entity.MilitaryUnit;
import org.example.entity.WorkDay;
import org.example.jpa.WorkDayRepository;
import org.example.mapper.MilitaryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/work-days")
public class WorkDayController {
    private final WorkDayRepository repository;
    private final MilitaryMapper mapper;

    public WorkDayController(WorkDayRepository repository, MilitaryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
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
                        MilitaryUnit unit = wd.getMilitaryUnit();
                        MilitaryUnitDto unitDto = mapper.fromEntityToDto(unit);
                        dto.setMilitaryUnit(unitDto);
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
