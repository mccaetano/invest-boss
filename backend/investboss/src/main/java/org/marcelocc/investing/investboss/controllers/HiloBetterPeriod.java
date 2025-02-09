package org.marcelocc.investing.investboss.controllers;

import java.io.IOException;

import org.marcelocc.investing.investboss.services.ImportCustomCSVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;



@RestController
@RequestMapping(path = "/finance/v1/api/hilobetterperiod")
public class HiloBetterPeriod {
    
    @Autowired
    private ImportCustomCSVService service;

    @PostMapping("/upload")
    public ResponseEntity<Object> uploadCustomCSV(@RequestParam MultipartFile file) throws IOException {
        service.importDataCustomCSV(file.getInputStream());
        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/calculate")
    public ResponseEntity<Object> calculateBetterPeriod() {
        return ResponseEntity.ok().body(null);
    }

    @DeleteMapping("/importdata/{idconsulta}")
    public ResponseEntity<Void> clearUploadedData(@PathVariable String idconsulta) {
        service.deleteImportData(idconsulta);
        return ResponseEntity.accepted().body(null);
    }
}
