package org.example.service;

import org.example.UnitStatus;
import org.example.dto.MilitaryUnitDto;
import org.example.dto.WorkDayDto;
import org.example.entity.MilitaryUnit;
import org.example.entity.WorkDay;
import org.example.jpa.MilitaryUnitRepository;
import org.example.jpa.WorkDayRepository;
import org.example.mapper.MilitaryMapper;
import org.example.service.interf.MilitaryUnitService;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
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
        List<WorkDay> weekWorkDayList = workDayRepository.getAllWorkDayThisWeek(startOfWeek, endOfWeek);

        List<WorkDay> workDayList = workDayRepository.findAll();

        Map<Long, List<WorkDay>> weekUnitWorkDayMap = weekWorkDayList.stream()
                .collect(Collectors.groupingBy(d -> d.getMilitaryUnit().getId()));
        Map<Long, List<WorkDay>> unitWorkDayMap = workDayList.stream()
                .collect(Collectors.groupingBy(d -> d.getMilitaryUnit().getId()));

        List<WorkDayDto> weekWorkDayDtoList = weekWorkDayList.stream()
                .map(workDay -> mapper.fromEntityToDto(workDay,
                        weekUnitWorkDayMap.get(workDay.getMilitaryUnit().getId()),
                        unitWorkDayMap.get(workDay.getMilitaryUnit().getId())))
                .toList();

        List<MilitaryUnitDto> militaryUnitDtoList = new ArrayList<>(militaryUnitList.stream()
                .map(u -> mapper.fromEntityToDto(u, weekUnitWorkDayMap.get(u.getId()), unitWorkDayMap.get(u.getId())))
                .toList());

        Map<Long, List<WorkDayDto>> weekUnitWorkDayDtoMap = weekWorkDayDtoList.stream()
                .collect(Collectors.groupingBy(d -> d.getMilitaryUnit().getId()));

        for (MilitaryUnitDto unitDto : militaryUnitDtoList) {
            unitDto.setWeekWorkDayList(weekUnitWorkDayDtoMap.get(unitDto.getId()));
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

    @Override
    public void createNewUnit(String lastName, String name, String status) {
        MilitaryUnit militaryUnit = new MilitaryUnit();
        militaryUnit.setUnitStatus(UnitStatus.valueOf(status).getId());
        militaryUnit.setName(name);
        militaryUnit.setLastName(lastName);
        militaryUnit.setInvolvingProcent(0);
        militaryUnit.setWeekWorkDay(0);
        militaryUnit.setTotalWorkDays(0);
        mapper.fromEntityToDto(militaryUnitRepository.save(militaryUnit), new ArrayList<>(), new ArrayList<>());
    }

    @Override
    public MilitaryUnitDto getById(Long id) {
        LocalDate today = LocalDate.now();

        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);

        List<WorkDay> weekWorkDayList = workDayRepository.getAllWorkDayThisWeek(startOfWeek, endOfWeek);

        List<WorkDay> workDayList = workDayRepository.findAll();

        Map<Long, List<WorkDay>> weekUnitWorkDayMap = weekWorkDayList.stream()
                .collect(Collectors.groupingBy(d -> d.getMilitaryUnit().getId()));
        Map<Long, List<WorkDay>> unitWorkDayMap = workDayList.stream()
                .collect(Collectors.groupingBy(d -> d.getMilitaryUnit().getId()));

        return mapper.fromEntityToDto(militaryUnitRepository.findById(id).orElse(null), weekUnitWorkDayMap.get(id), unitWorkDayMap.get(id));
    }

    @Override
    public void updateUnit(Long id, String lastName, String name, String status) {
        militaryUnitRepository.findById(id).ifPresent(unit -> {
            unit.setUnitStatus(UnitStatus.valueOf(status).getId());
            unit.setName(name);
            unit.setLastName(lastName);
            militaryUnitRepository.save(unit);
        });
    }
}
