package org.example.service;

import org.example.dto.MilitaryUnitDto;
import org.example.dto.WorkDayDto;
import org.example.entity.MilitaryUnit;
import org.example.entity.WorkDay;
import org.example.jpa.MilitaryUnitRepository;
import org.example.jpa.WorkDayRepository;
import org.example.mapper.MilitaryMapper;
import org.example.service.interf.MilitaryUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MilitaryUnitServiceImpl implements MilitaryUnitService {
    private final MilitaryUnitRepository militaryUnitRepository;
    private final WorkDayRepository workDayRepository;
    private final MilitaryMapper mapper;

    public MilitaryUnitServiceImpl(MilitaryUnitRepository militaryUnitRepository,
                                   WorkDayRepository workDayRepository,
                                   MilitaryMapper mapper) {
        this.militaryUnitRepository = militaryUnitRepository;
        this.workDayRepository = workDayRepository;
        this.mapper = mapper;
    }

    @Override
    public List<MilitaryUnitDto> getMilitaryUnitDtoList() {
        LocalDate today = LocalDate.now();

        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);
        List<MilitaryUnit> militaryUnitList = militaryUnitRepository.findAll();
        List<WorkDay> workDayList = workDayRepository.getAllWorkDayThisWeek(startOfWeek, endOfWeek);
        List<MilitaryUnitDto> militaryUnitDtoList = new java.util.ArrayList<>(militaryUnitList.stream()
                .map(mapper::fromEntityToDto)
                .toList());
        List<WorkDayDto> workDayDtoList = workDayList.stream()
                .map(mapper::fromEntityToDto)
                .toList();
        Map<Long, List<WorkDayDto>> unitWorkDayDtoMap = workDayDtoList.stream()
                .collect(Collectors.groupingBy(d -> d.getMilitaryUnit().getId()));
        for (MilitaryUnitDto unitDto : militaryUnitDtoList) {
            unitDto.setWeekWorkDayList(unitWorkDayDtoMap.get(unitDto.getId()));
        }

        militaryUnitDtoList.sort(Comparator.comparing(u -> {
            switch (u.getUnitStatus()) {
                case REMOVED -> {
                    return 2;
                }
                case ON_SICK_LEAVE -> {
                    return 1;
                }
                default -> {
                    return 0;
                }
            }
        }));
        return militaryUnitDtoList;
    }
}
