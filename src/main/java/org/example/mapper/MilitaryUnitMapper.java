package org.example.mapper;

import org.example.dto.MilitaryUnitDto;
import org.example.entity.MilitaryUnit;
import org.springframework.stereotype.Component;

@Component
public class MilitaryUnitMapper implements MilitaryMapper {
    @Override
    public MilitaryUnitDto fromEntityToDto(MilitaryUnit unit) {
        MilitaryUnitDto mu = new MilitaryUnitDto();
        mu.setId(unit.getId());
        mu.setName(unit.getName());
        mu.setLastName(unit.getLastName());
        mu.setTotalWorkDays(unit.getTotalWorkDays());
        mu.setWeekWorkDay(unit.getWeekWorkDay());
        mu.setInvolvingProcent(unit.getInvolvingProcent());
        return mu;
    }

    @Override
    public MilitaryUnit fromDtoToEntity(MilitaryUnitDto unitDto) {
        MilitaryUnit mu = new MilitaryUnit();
        mu.setName(unitDto.getName());
        mu.setLastName(unitDto.getLastName());
        mu.setTotalWorkDays(unitDto.getTotalWorkDays());
        mu.setWeekWorkDay(unitDto.getWeekWorkDay());
        mu.setInvolvingProcent(unitDto.getInvolvingProcent());
        return mu;
    }
}
