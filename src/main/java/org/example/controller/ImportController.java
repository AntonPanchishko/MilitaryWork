package org.example.controller;

import org.example.service.DataImportServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImportController {

    private final DataImportServiceImpl service;

    @Autowired
    public ImportController(DataImportServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/import")
    public String importData() throws Exception {
        service.importData();
        return "Імпорт завершено!";
    }
}
