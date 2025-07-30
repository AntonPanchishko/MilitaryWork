package org.example.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MilitaryUnitDto {
    private Long id;
    private String name;
    private String lastName;
    private int totalWorkDays;
    private int weekWorkDay;
    private int involvingProcent;
}
