package org.example.service;

import org.example.dto.MilitaryUnitDto;
import org.example.entity.MilitaryUnit;
import org.example.entity.WorkDay;
import org.example.jpa.MilitaryUnitRepository;
import org.example.jpa.WorkDayRepository;
import org.example.service.interf.UnitProviderService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UnitProviderServiceImpl implements UnitProviderService {
    private final WorkDayRepository workDayRepository;
    private final MilitaryUnitRepository militaryUnitRepository;

    public UnitProviderServiceImpl(WorkDayRepository workDayRepository, MilitaryUnitRepository militaryUnitRepository) {
        this.workDayRepository = workDayRepository;
        this.militaryUnitRepository = militaryUnitRepository;
    }

    @Override
    public List<MilitaryUnitDto> getWorkUnits(int amount) {
        List<MilitaryUnit> units = militaryUnitRepository.getAllAvailableUnitList();
        Map<Long, List<WorkDay>> dayMap = workDayRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(d -> d.getMilitaryUnit().getId()));
        HashMap<Long, Integer> map = new HashMap<>();
        for (MilitaryUnit unit : units) {
            List<WorkDay> unitWorkDayList = dayMap.get(unit.getId());
            if (CollectionUtils.isEmpty(unitWorkDayList)) {
                map.put(unit.getId(), 0);
            } else {
                map.put(unit.getId(), unitWorkDayList.size());
            }
        }
        List<Map.Entry<Long, Integer>> smallestFive = map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(amount)
                .toList();

        return militaryUnitRepository.findAllById(
                smallestFive
                        .stream()
                        .map(Map.Entry::getKey)
                        .toList()
        ).stream().map(e -> {
            MilitaryUnitDto mu = new MilitaryUnitDto();
            mu.setId(e.getId());
            mu.setLastName(e.getLastName());
            return mu;
        }).toList();
    }
}
