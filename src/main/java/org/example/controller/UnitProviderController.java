package org.example.controller;

import org.example.dto.MilitaryUnitDto;
import org.example.service.interf.UnitProviderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/provider")
public class UnitProviderController {
    private final UnitProviderService unitProviderService;

    public UnitProviderController(UnitProviderService unitProviderService) {
        this.unitProviderService = unitProviderService;
    }

    @GetMapping
    public List<MilitaryUnitDto> getWorkUnits(@RequestParam int amount) {
        return unitProviderService.getWorkUnits(amount);
    }
}
