package org.example.dto;

import org.example.UnitStatus;

import java.time.DayOfWeek;
import java.util.List;

public class MilitaryUnitDto {
    private Long id;
    private String name;
    private String lastName;
    private int totalWorkDays;
    private int weekWorkDay;
    private int involvingProcent;
    private List<WorkDayDto> weekWorkDayList;
    private UnitStatus unitStatus;

    public boolean hasWorkDay(DayOfWeek day) {
        if (weekWorkDayList == null) return false;
        for (WorkDayDto w : weekWorkDayList) {
            if (w.getDate().getDayOfWeek().equals(day)) {
                return true;
            }
        }
        return false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getTotalWorkDays() {
        return totalWorkDays;
    }

    public void setTotalWorkDays(int totalWorkDays) {
        this.totalWorkDays = totalWorkDays;
    }

    public int getWeekWorkDay() {
        return weekWorkDay;
    }

    public void setWeekWorkDay(int weekWorkDay) {
        this.weekWorkDay = weekWorkDay;
    }

    public int getInvolvingProcent() {
        return involvingProcent;
    }

    public void setInvolvingProcent(int involvingProcent) {
        this.involvingProcent = involvingProcent;
    }

    public List<WorkDayDto> getWeekWorkDayList() {
        return weekWorkDayList;
    }

    public void setWeekWorkDayList(List<WorkDayDto> weekWorkDayList) {
        this.weekWorkDayList = weekWorkDayList;
    }

    public UnitStatus getUnitStatus() {
        return unitStatus;
    }

    public void setUnitStatus(UnitStatus unitStatus) {
        this.unitStatus = unitStatus;
    }
}
