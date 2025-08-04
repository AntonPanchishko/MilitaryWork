package org.example.service;

import org.example.entity.MilitaryUnit;
import org.example.entity.WorkDay;
import org.example.jpa.MilitaryUnitRepository;
import org.example.jpa.WorkDayRepository;
import org.example.mapper.MilitaryMapper;
import org.example.service.interf.WorkDayService;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WorkDayServiceImpl implements WorkDayService {
    private final MilitaryUnitRepository militaryUnitRepository;
    private final WorkDayRepository workDayRepository;

    public WorkDayServiceImpl(MilitaryUnitRepository militaryUnitRepository,
                                   WorkDayRepository workDayRepository) {
        this.militaryUnitRepository = militaryUnitRepository;
        this.workDayRepository = workDayRepository;
    }

    @Override
    public void saveWorkDay(Map<String, String> tableData) {
        LocalDate today = LocalDate.now();
        DayOfWeek currentDay = today.getDayOfWeek();

        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);

        List<WorkDay> weekWorkDayList = workDayRepository.getAllWorkDayThisWeek(startOfWeek, endOfWeek);

        workDayRepository.deleteAll(weekWorkDayList);

        Map<Long, List<MilitaryUnit>> unitMap = militaryUnitRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(MilitaryUnit::getId));
        List<WorkDay> newWeekWorkDayList = new ArrayList<>();

        for (Map.Entry<String, String> entry : tableData.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith("workDays[")) {
                String inner = key.substring("workDays[".length(), key.indexOf(']'));
                Long unitId = Long.parseLong(inner);
                String day = key.substring(key.indexOf(']') + 2, key.length() - 1);
                DayOfWeek targetDay = DayOfWeek.valueOf(day.toUpperCase());

                int diff = targetDay.getValue() - currentDay.getValue();
                LocalDate result = today.plusDays(diff);
                WorkDay workDay = new WorkDay();

                workDay.setMilitaryUnit(unitMap.get(unitId).get(0));
                workDay.setWorkDate(result);
                newWeekWorkDayList.add(workDay);
            }
        }
        workDayRepository.saveAll(newWeekWorkDayList);
    }
}
