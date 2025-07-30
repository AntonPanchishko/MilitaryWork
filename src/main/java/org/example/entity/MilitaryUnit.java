package org.example.entity;

import jakarta.persistence.*;
import org.example.UnitStatus;

import java.util.List;

@Entity
@Table(name = "military_unit")
public class MilitaryUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String lastName;
    private int totalWorkDays;
    private int weekWorkDay;
    private int involvingProcent;
    private int unitStatus;

    @OneToMany(mappedBy = "militaryUnit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkDay> workDays;

    // getters/setters
    public Long getId() {
        return id;
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

    public int getUnitStatus() {
        return unitStatus;
    }

    public void setUnitStatus(int unitStatus) {
        this.unitStatus = unitStatus;
    }
}
