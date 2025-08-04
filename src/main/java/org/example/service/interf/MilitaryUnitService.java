package org.example.service.interf;

import org.example.dto.MilitaryUnitDto;

import java.util.List;

public interface MilitaryUnitService {
    List<MilitaryUnitDto> getMilitaryUnitDtoList();

    void createNewUnit(String lastName,
                  String name,
                  String status);

    MilitaryUnitDto getById(Long id);

    void updateUnit(Long id,
                    String lastName,
                    String name,
                    String status);
}
