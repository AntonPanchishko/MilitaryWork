package org.example.service.interf;

import org.example.dto.MilitaryUnitDto;

import java.util.List;

public interface UnitProviderService {
    List<MilitaryUnitDto> getWorkUnits(int amount);
}
