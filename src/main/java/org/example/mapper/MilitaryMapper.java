package org.example.mapper;

import org.example.dto.MilitaryUnitDto;
import org.example.entity.MilitaryUnit;

public interface MilitaryMapper {
    MilitaryUnitDto fromEntityToDto(MilitaryUnit unit);

    MilitaryUnit fromDtoToEntity(MilitaryUnitDto unitDto);
}
