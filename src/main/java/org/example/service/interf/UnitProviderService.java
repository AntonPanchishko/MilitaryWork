package org.example.service.interf;

import org.example.dto.MilitaryUnitDto;
import org.example.dto.WorkDayDto;

import java.util.List;

public interface UnitProviderService {
    List<MilitaryUnitDto> getWorkUnits(int amount);

    List<WorkDayDto> applyWorkUnit(List<Long> unitIdList);
}
