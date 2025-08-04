package org.example;

import java.util.Arrays;

public enum UnitStatus {
    AVAILABLE(1),
    ON_SICK_LEAVE(2),
    REMOVED(3);

    private final int id;

    UnitStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static UnitStatus getUnitStatus(int id) {
        return Arrays.stream(UnitStatus.values())
                .filter(v -> v.id == id).findFirst()
                .orElse(null);
    }
}
