package org.example.dto;

import java.time.LocalDate;

public class WorkDayDto {
    private Long id;
    private LocalDate date;
    private MilitaryUnitDto militaryUnit;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public MilitaryUnitDto getMilitaryUnit() { return militaryUnit; }
    public void setMilitaryUnit(MilitaryUnitDto militaryUnit) { this.militaryUnit = militaryUnit; }
}
