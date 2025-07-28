package org.example.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.example.AirTableReader;
import org.example.UnitStatus;
import org.example.entity.MilitaryUnit;
import org.example.entity.WorkDay;
import org.example.jpa.MilitaryUnitRepository;
import org.example.jpa.WorkDayRepository;
import org.example.service.interf.DataImportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;

@Service
public class DataImportServiceImpl implements DataImportService {
    private final MilitaryUnitRepository unitRepo;
    private final WorkDayRepository workRepo;
    private static final Map<String, DayOfWeek> DAY_MAP = new HashMap<>();

    static {
        DAY_MAP.put("Понеділок", DayOfWeek.MONDAY);
        DAY_MAP.put("Вівторок", DayOfWeek.TUESDAY);
        DAY_MAP.put("Середа", DayOfWeek.WEDNESDAY);
        DAY_MAP.put("Четверг", DayOfWeek.THURSDAY);
        DAY_MAP.put("П'ятниця", DayOfWeek.FRIDAY);
        DAY_MAP.put("Суббота", DayOfWeek.SATURDAY);
    }

    public DataImportServiceImpl(MilitaryUnitRepository unitRepo, WorkDayRepository workRepo) {
        this.unitRepo = unitRepo;
        this.workRepo = workRepo;
    }

    @Override
    @Transactional
    public void importData() throws Exception {
        JsonArray records = AirTableReader.fetchRecords();

        List<WorkDay> workDayList = new ArrayList<>();

        for (int i = 0; i < records.size(); i++) {
            JsonObject fields = records.get(i).getAsJsonObject().getAsJsonObject("fields");
            if (fields == null) continue;

            String fullName = fields.get("ПІБ").getAsString();
            if (fullName == null || fullName.trim().isEmpty()) continue;

            String[] nameParts = fullName.split(" ");

            String lastName = nameParts[0];       // Прізвище
            String name = nameParts[1];           // Ім’я

            Optional<MilitaryUnit> optionalUnit = unitRepo.findMilitaryUnitByLastName(lastName);

            MilitaryUnit unit;
            if (optionalUnit.isPresent()) {
                unit = optionalUnit.get(); // оновлюємо існуючий
            } else {
                unit = new MilitaryUnit(); // створюємо новий
                unit.setName(name);
                unit.setLastName(lastName);
            }
            unit.setUnitStatus(UnitStatus.AVAILABLE);

// Оновлюємо спільні поля
            unit.setTotalWorkDays(fields.get("Всього з моменту прибуття").getAsInt());
            unit.setWeekWorkDay(fields.get("Цього тижня").getAsInt());
            unit.setInvolvingProcent(fields.get("Відсоток зайобу").getAsInt());

            unitRepo.save(unit); // створить або оновить

            for (Map.Entry<String, JsonElement> entry : fields.asMap().entrySet()) {
                if (DAY_MAP.containsKey(entry.getKey().trim()) && entry.getValue().getAsString().trim().equals("1")) {
                    DayOfWeek targetDay = DAY_MAP.get(entry.getKey().trim());

                    LocalDate startOfPreviousWeek = LocalDate.now()
                            .with(java.time.temporal.TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

                    LocalDate date = startOfPreviousWeek
                            .with(java.time.temporal.TemporalAdjusters.nextOrSame(targetDay));

                    WorkDay wd = new WorkDay();
                    wd.setWorkDate(date);
                    wd.setMilitaryUnit(unit);
                    workDayList.add(wd);
                }
            }
        }
        workRepo.saveAll(workDayList);
    }
}
