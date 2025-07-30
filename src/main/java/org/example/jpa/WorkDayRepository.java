package org.example.jpa;

import org.example.entity.WorkDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface WorkDayRepository extends JpaRepository<WorkDay, Long> {
    @Query("""
    SELECT wd FROM WorkDay wd 
    WHERE wd.workDate >= :from AND wd.workDate <= :to
    """)
    List<WorkDay> getAllWorkDayThisWeek(LocalDate from, LocalDate to);
}
