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

        List<WorkDay> newWorkDayList = new ArrayList<>();

        List<MilitaryUnit> unitList = unitRepo.findAll();
        List<WorkDay> workDaysList = workRepo.findAll();

        for (int i = 0; i < records.size(); i++) {
            JsonObject fields = records.get(i).getAsJsonObject().getAsJsonObject("fields");
            if (fields == null) continue;

            String fullName = safeGetString(fields, "ПІБ");
            if (fullName == null || fullName.trim().isEmpty()) continue;

            String[] nameParts = fullName.split(" ");

            String lastName = nameParts[0];       // Прізвище
            String name = nameParts[1];           // Ім’я

            Optional<MilitaryUnit> unitOptional = unitList.stream().filter(u -> u.getName().equals(name) && u.getLastName().equals(lastName)).findFirst();

            MilitaryUnit unit;
            if (unitOptional.isPresent()) {
                unit = unitOptional.get(); // оновлюємо існуючий
            } else {
                unit = new MilitaryUnit(); // створюємо новий
                unit.setName(name);
                unit.setLastName(lastName);
            }
            unit.setUnitStatus(fields.get("Лікування(пройоб) чи ровний").getAsInt());

// Оновлюємо спільні поля
            unit.setTotalWorkDays(fields.get("Всього з моменту прибуття").getAsInt());
            unit.setWeekWorkDay(fields.get("Цього тижня").getAsInt());
            unit.setInvolvingProcent(fields.get("Відсоток зайобу").getAsInt());

            unitRepo.save(unit); // створить або оновить

            for (Map.Entry<String, JsonElement> entry : fields.asMap().entrySet()) {
                if (DAY_MAP.containsKey(entry.getKey().trim()) && entry.getValue().getAsString().trim().equals("1")) {
                    DayOfWeek targetDay = DAY_MAP.get(entry.getKey().trim());

                    LocalDate startOfPreviousWeek = LocalDate.now()
                            .with(java.time.temporal.TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                            /*.minusWeeks(1)*/;

                    LocalDate date = startOfPreviousWeek
                            .with(java.time.temporal.TemporalAdjusters.nextOrSame(targetDay));

                    if (workDaysList.stream()
                            .anyMatch(d -> Objects.equals(
                                    d.getMilitaryUnit().getId(), unit.getId())
                                    && d.getWorkDate().equals(date))) {
                        continue;
                    }
                    WorkDay wd = new WorkDay();
                    wd.setWorkDate(date);
                    wd.setMilitaryUnit(unit);
                    newWorkDayList.add(wd);
                }
            }
        }
        workRepo.saveAll(newWorkDayList);
    }

    private String safeGetString(JsonObject obj, String key) {
        JsonElement el = obj.get(key);
        return (el == null || el.isJsonNull()) ? null : el.getAsString();
    }
}
