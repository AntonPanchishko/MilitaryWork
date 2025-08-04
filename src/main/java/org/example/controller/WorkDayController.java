package org.example.controller;

import org.example.dto.MilitaryUnitDto;
import org.example.dto.WorkDayDto;
import org.example.entity.MilitaryUnit;
import org.example.entity.WorkDay;
import org.example.jpa.WorkDayRepository;
import org.example.mapper.MilitaryMapper;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/work-days")
public class WorkDayController {
    private final WorkDayRepository workDayRepository;
    private final MilitaryMapper mapper;

    public WorkDayController(WorkDayRepository workDayRepository, MilitaryMapper mapper) {
        this.workDayRepository = workDayRepository;
        this.mapper = mapper;
    }

    @GetMapping
    public List<WorkDayDto> getAll() {
        LocalDate today = LocalDate.now();

        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);

        List<WorkDay> workDays = workDayRepository.findAll();
        List<WorkDay> weekWorkDayList = workDayRepository.getAllWorkDayThisWeek(startOfWeek, endOfWeek);
        Map<Long, List<WorkDay>> workDayMap = workDays.stream().collect(Collectors.groupingBy(w -> w.getMilitaryUnit().getId()));
        Map<Long, List<WorkDay>> weekWorkDayMap = weekWorkDayList.stream().collect(Collectors.groupingBy(w -> w.getMilitaryUnit().getId()));

        return workDays.stream()
                .map(wd -> {
                    WorkDayDto dto = new WorkDayDto();
                    dto.setId(wd.getId());
                    dto.setDate(wd.getWorkDate());
                    if (wd.getMilitaryUnit() != null) {
                        MilitaryUnit unit = wd.getMilitaryUnit();
                        Long unitId = wd.getMilitaryUnit().getId();
                        MilitaryUnitDto unitDto = mapper.fromEntityToDto(unit, weekWorkDayMap.get(unitId), workDayMap.get(unitId));
                        dto.setMilitaryUnit(unitDto);
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
