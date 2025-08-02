package org.example.controller;

import org.example.dto.MilitaryUnitDto;
import org.example.dto.WorkDayDto;
import org.example.jpa.MilitaryUnitRepository;
import org.example.jpa.WorkDayRepository;
import org.example.service.DataImportServiceImpl;
import org.example.service.interf.MilitaryUnitService;
import org.example.service.interf.UnitProviderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class UiController {
    private final MilitaryUnitRepository unitRepo;
    private final WorkDayRepository workRepo;
    private final DataImportServiceImpl importService;
    private final UnitProviderService unitProviderService;
    private final MilitaryUnitService militaryUnitService;

    public UiController(MilitaryUnitRepository unitRepo,
                        WorkDayRepository workRepo,
                        DataImportServiceImpl importService,
                        UnitProviderService unitProviderService,
                        MilitaryUnitService militaryUnitService) {
        this.unitRepo = unitRepo;
        this.workRepo = workRepo;
        this.importService = importService;
        this.unitProviderService = unitProviderService;
        this.militaryUnitService = militaryUnitService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/ui/units")
    public String units(Model model) {
        model.addAttribute("units", militaryUnitService.getMilitaryUnitDtoList());
        return "military-units";
    }

    @GetMapping("/ui/work-days")
    public String workDays(Model model) {
        model.addAttribute("workDays", workRepo.findAll());
        return "work-days";
    }

    @GetMapping("/ui/import")
    public String importData(Model model) {
        try {
            importService.importData();
            model.addAttribute("message", "Імпорт завершено успішно!");
        } catch (Exception e) {
            model.addAttribute("message", "Помилка: " + e.getMessage());
            e.printStackTrace();
        }
        return "result";
    }

    @GetMapping("/ui/provider")
    public String providerForm() {
        return "provider-form";
    }

    @PostMapping("/ui/provider")
    public String providerResult(@RequestParam int amount, Model model) {
        List<MilitaryUnitDto> units = unitProviderService.getWorkUnits(amount);
        model.addAttribute("units", units);
        return "provider-result";
    }

    @PostMapping("/ui/save-work-days")
    public String saveWorkDays(
            @RequestParam(value = "unitIds", required = false) List<Long> unitIds,
            Model model) {

        if (unitIds == null || unitIds.isEmpty()) {
            model.addAttribute("message", "Нічого не вибрано!");
            model.addAttribute("units", List.of());
            return "work-days-saved";
        }

        List<MilitaryUnitDto> units = unitProviderService.applyWorkUnit(unitIds)
                .stream()
                .map(WorkDayDto::getMilitaryUnit)
                .toList();

        model.addAttribute("message", "Попиздували на єбашатню");
        model.addAttribute("units", units);

        return "work-days-saved";
    }
}
