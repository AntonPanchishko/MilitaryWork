package org.example.mapper;

import org.example.UnitStatus;
import org.example.dto.MilitaryUnitDto;
import org.example.dto.WorkDayDto;
import org.example.entity.MilitaryUnit;
import org.example.entity.WorkDay;
import org.springframework.stereotype.Component;

@Component
public class MilitaryMapperImpl implements MilitaryMapper {
    @Override
    public MilitaryUnitDto fromEntityToDto(MilitaryUnit unit) {
        MilitaryUnitDto mu = new MilitaryUnitDto();
        mu.setId(unit.getId());
        mu.setName(unit.getName());
        mu.setLastName(unit.getLastName());
        mu.setTotalWorkDays(unit.getTotalWorkDays());
        mu.setWeekWorkDay(unit.getWeekWorkDay());
        mu.setInvolvingProcent(unit.getInvolvingProcent());
        mu.setUnitStatus(UnitStatus.getUnitStatus(unit.getUnitStatus()));
        return mu;
    }

    @Override
    public WorkDayDto fromEntityToDto(WorkDay day) {
        WorkDayDto workDayDto = new WorkDayDto();
        workDayDto.setId(day.getId());
        workDayDto.setDate(day.getWorkDate());
        workDayDto.setMilitaryUnit(fromEntityToDto(day.getMilitaryUnit()));
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
