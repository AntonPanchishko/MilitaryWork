package org.example.service;

import org.example.dto.MilitaryUnitDto;
import org.example.dto.WorkDayDto;
import org.example.entity.MilitaryUnit;
import org.example.entity.WorkDay;
import org.example.jpa.MilitaryUnitRepository;
import org.example.jpa.WorkDayRepository;
import org.example.mapper.MilitaryMapper;
import org.example.service.interf.UnitProviderService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UnitProviderServiceImpl implements UnitProviderService {
    private final WorkDayRepository workDayRepository;
    private final MilitaryUnitRepository militaryUnitRepository;
    private final MilitaryMapper mapper;

    public UnitProviderServiceImpl(WorkDayRepository workDayRepository, MilitaryUnitRepository militaryUnitRepository, MilitaryMapper mapper) {
        this.workDayRepository = workDayRepository;
        this.militaryUnitRepository = militaryUnitRepository;
        this.mapper = mapper;
    }

    @Override
    public List<MilitaryUnitDto> getWorkUnits(int amount) {
        List<MilitaryUnit> units = militaryUnitRepository.getAllAvailableUnitList();
        LocalDate today = LocalDate.now();

        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);

        Map<Long, List<WorkDay>> dayMap = workDayRepository.getAllWorkDayThisWeek(
                        startOfWeek, endOfWeek
                )
                .stream()
                .collect(Collectors.groupingBy(d -> d.getMilitaryUnit().getId()));
        HashMap<Long, List<WorkDay>> map = new HashMap<>();
        for (MilitaryUnit unit : units) {
            List<WorkDay> unitWorkDayList = dayMap.get(unit.getId());
            if (CollectionUtils.isEmpty(unitWorkDayList)) {
                map.put(unit.getId(), new ArrayList<>());
            } else {
                map.put(unit.getId(), unitWorkDayList);
            }
        }
        List<Map.Entry<Long, List<WorkDay>>> smallestFive = map.entrySet()
                .stream()
                .sorted((e1, e2) -> {
                    int size1 = e1.getValue().size();
                    int size2 = e2.getValue().size();

                    int cmp = Integer.compare(size1, size2);
                    if (cmp != 0 && size1 != size2) {
                        return cmp;
                    }

                    Optional<LocalDate> minDate1 = e1.getValue().stream()
                            .map(WorkDay::getWorkDate)
                            .max(LocalDate::compareTo);
                    Optional<LocalDate> minDate2 = e2.getValue().stream()
                            .map(WorkDay::getWorkDate)
                            .max(LocalDate::compareTo);

                    if (minDate1.isEmpty() && minDate2.isEmpty()) {
                        return 0;
                    }
                    return minDate1.map(
                                        date -> minDate2.map(date::compareTo
                                    ).orElse(-1))
                            .orElse(1);

                })
                .limit(amount)
                .toList();

        return militaryUnitRepository.findAllById(
                        smallestFive
                                .stream()
                                .map(Map.Entry::getKey)
                                .toList()
                ).stream()
                .map(unit -> mapper.fromEntityToDto(unit, new ArrayList<>(), new ArrayList<>()))
                .toList();
    }

    @Override
    public List<WorkDayDto> applyWorkUnit(List<Long> unitIdList) {
        List<MilitaryUnit> militaryUnitList = militaryUnitRepository.findAllById(unitIdList);
        List<WorkDay> workDayList = new ArrayList<>();
        LocalDate now = LocalDate.now();
        for (MilitaryUnit militaryUnit : militaryUnitList) {
            WorkDay workDay = new WorkDay();
            workDay.setWorkDate(now);
            workDay.setMilitaryUnit(militaryUnit);
            workDayList.add(workDay);
        }

        return workDayRepository.saveAll(workDayList)
                .stream()
                .map(unit -> mapper.fromEntityToDto(unit, new ArrayList<>(), new ArrayList<>()))
                .toList();
    }
}
