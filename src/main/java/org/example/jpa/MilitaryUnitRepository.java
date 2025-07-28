package org.example.jpa;

import org.example.entity.MilitaryUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MilitaryUnitRepository extends JpaRepository<MilitaryUnit, Long> {
    @Query("""
            SELECT mu FROM MilitaryUnit mu
            WHERE mu.unitStatus = 1
            """)
    List<MilitaryUnit> getAllAvailableUnitList();

    @Query("""
                SELECT mu FROM MilitaryUnit mu
                WHERE mu.lastName = :lastName
            """)
    Optional<MilitaryUnit> findMilitaryUnitByLastName(@Param("lastName") String lastName);

}
