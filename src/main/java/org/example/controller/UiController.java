package org.example.controller;

import org.example.dto.MilitaryUnitDto;
import org.example.jpa.MilitaryUnitRepository;
import org.example.jpa.WorkDayRepository;
import org.example.service.DataImportServiceImpl;
import org.example.service.interf.UnitProviderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UiController {

    private final MilitaryUnitRepository unitRepo;
    private final WorkDayRepository workRepo;
    private final DataImportServiceImpl importService;
    private final UnitProviderService unitProviderService;

    public UiController(MilitaryUnitRepository unitRepo,
                        WorkDayRepository workRepo,
                        DataImportServiceImpl importService,
                        UnitProviderService unitProviderService) {
        this.unitRepo = unitRepo;
        this.workRepo = workRepo;
        this.importService = importService;
        this.unitProviderService = unitProviderService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/ui/units")
    public String units(Model model) {
        model.addAttribute("units", unitRepo.findAll());
        return "military-units";
    }

    @GetMapping("/ui/work-days")
    public String workDays(Model model) {
        model.addAttribute("workDays", workRepo.findAll());
        return "work-days";
    }

    @GetMapping("/ui/import")
    public String importData(Model model) throws Exception {
        importService.importData();
        model.addAttribute("message", "Імпорт завершено успішно!");
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
}
