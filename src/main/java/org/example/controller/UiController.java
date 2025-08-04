package org.example.controller;

import org.example.dto.MilitaryUnitDto;
import org.example.dto.WorkDayDto;
import org.example.jpa.MilitaryUnitRepository;
import org.example.jpa.WorkDayRepository;
import org.example.service.DataImportServiceImpl;
import org.example.service.interf.MilitaryUnitService;
import org.example.service.interf.UnitProviderService;
import org.example.service.interf.WorkDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class UiController {
    private final WorkDayRepository workRepo;
    private final DataImportServiceImpl importService;
    private final UnitProviderService unitProviderService;
    private final MilitaryUnitService militaryUnitService;
    private final WorkDayService workDayService;

    public UiController(WorkDayRepository workRepo,
                        DataImportServiceImpl importService,
                        UnitProviderService unitProviderService,
                        MilitaryUnitService militaryUnitService,
                        WorkDayService workDayService) {
        this.workRepo = workRepo;
        this.importService = importService;
        this.unitProviderService = unitProviderService;
        this.militaryUnitService = militaryUnitService;
        this.workDayService = workDayService;
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

    @GetMapping("/ui/new-unit")
    public String showNewUnitForm() {
        return "new-unit"; // шаблон new-unit.html
    }

    @PostMapping("/ui/create-unit")
    public String createUnit(
            @RequestParam String lastName,
            @RequestParam String name,
            @RequestParam String status) {

        militaryUnitService.createNewUnit(lastName, name, status);
        return "redirect:/ui/units";
    }

    // Відкрити форму редагування (id передається з форми через параметр unitId)
    @GetMapping("/ui/edit-unit")
    public String editUnit(@RequestParam("unitId") Long unitId, Model model) {
        MilitaryUnitDto unit = militaryUnitService.getById(unitId);
        model.addAttribute("unit", unit);
        return "edit-unit";
    }

    // Оновити дані юніта
    @PostMapping("/ui/update-unit")
    public String updateUnit(
            @RequestParam Long id,
            @RequestParam String lastName,
            @RequestParam String name,
            @RequestParam String status) {
        militaryUnitService.updateUnit(id, lastName, name, status);
        return "redirect:/ui/units";
    }

    @PostMapping("/ui/save-work-days")
    public String saveWorkDays(@RequestParam Map<String, String> formParams) {
        workDayService.saveWorkDay(formParams);
        return "redirect:/ui/units";
    }
}
