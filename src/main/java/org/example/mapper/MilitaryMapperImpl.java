package org.example.mapper;

import org.example.UnitStatus;
import org.example.dto.MilitaryUnitDto;
import org.example.dto.WorkDayDto;
import org.example.entity.MilitaryUnit;
import org.example.entity.WorkDay;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class MilitaryMapperImpl implements MilitaryMapper {
    @Override
    public MilitaryUnitDto fromEntityToDto(MilitaryUnit unit, List<WorkDay> weekWorkDayDtoList, List<WorkDay> workDayDtoList) {
        int totalWorkDays = CollectionUtils.isEmpty(workDayDtoList) ? 0 : workDayDtoList.size();
        int weekWorkDay = CollectionUtils.isEmpty(weekWorkDayDtoList) ? 0 : weekWorkDayDtoList.size();
        MilitaryUnitDto mu = new MilitaryUnitDto();
        mu.setId(unit.getId());
        mu.setName(unit.getName());
        mu.setLastName(unit.getLastName());
        mu.setTotalWorkDays(totalWorkDays);
        mu.setWeekWorkDay(weekWorkDay);
        mu.setInvolvingProcent(Math.round((float) weekWorkDay * 100 / 6));
        mu.setUnitStatus(UnitStatus.getUnitStatus(unit.getUnitStatus()));
        return mu;
    }

    @Override
    public WorkDayDto fromEntityToDto(WorkDay day, List<WorkDay> weekWorkDayDtoList, List<WorkDay> workDayDtoList) {
        WorkDayDto workDayDto = new WorkDayDto();
        workDayDto.setId(day.getId());
        workDayDto.setDate(day.getWorkDate());
        workDayDto.setMilitaryUnit(fromEntityToDto(day.getMilitaryUnit(), weekWorkDayDtoList, workDayDtoList));
        return workDayDto;
    }

    @Override
    public MilitaryUnit fromDtoToEntity(MilitaryUnitDto unitDto) {
        MilitaryUnit mu = new MilitaryUnit();
        mu.setName(unitDto.getName());
        mu.setLastName(unitDto.getLastName());
        mu.setTotalWorkDays(unitDto.getTotalWorkDays());
        mu.setWeekWorkDay(unitDto.getWeekWorkDay());
        mu.setInvolvingProcent(unitDto.getInvolvingProcent());
        mu.setUnitStatus(unitDto.getUnitStatus().getId());
        return mu;
    }

    @Override
    public WorkDay fromDtoToEntity(WorkDayDto dayDto) {
        WorkDay workDay = new WorkDay();
        workDay.setWorkDate(dayDto.getDate());
        workDay.setMilitaryUnit(fromDtoToEntity(dayDto.getMilitaryUnit()));
        return workDay;
    }
}
