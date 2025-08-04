package org.example.mapper;

import org.example.dto.MilitaryUnitDto;
import org.example.dto.WorkDayDto;
import org.example.entity.MilitaryUnit;
import org.example.entity.WorkDay;

import java.util.List;

public interface MilitaryMapper {
    MilitaryUnitDto fromEntityToDto(MilitaryUnit unit, List<WorkDay> weekWorkDayDtoList, List<WorkDay> workDayDtoList);
    WorkDayDto fromEntityToDto(WorkDay day, List<WorkDay> weekWorkDayDtoList, List<WorkDay> workDayDtoList);

    MilitaryUnit fromDtoToEntity(MilitaryUnitDto unitDto);
    WorkDay fromDtoToEntity(WorkDayDto dayDto);
}
