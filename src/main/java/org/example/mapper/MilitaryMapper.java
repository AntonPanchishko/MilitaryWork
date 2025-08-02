package org.example.mapper;

import org.example.dto.MilitaryUnitDto;
import org.example.dto.WorkDayDto;
import org.example.entity.MilitaryUnit;
import org.example.entity.WorkDay;

public interface MilitaryMapper {
    MilitaryUnitDto fromEntityToDto(MilitaryUnit unit);
    WorkDayDto fromEntityToDto(WorkDay day);

    MilitaryUnit fromDtoToEntity(MilitaryUnitDto unitDto);
    WorkDay fromDtoToEntity(WorkDayDto dayDto);


}
