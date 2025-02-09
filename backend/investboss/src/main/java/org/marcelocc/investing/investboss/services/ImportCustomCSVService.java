package org.marcelocc.investing.investboss.services;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.marcelocc.investing.investboss.models.csv.CustomCSV;
import org.marcelocc.investing.investboss.repositories.CsvRepository;
import org.springframework.stereotype.Service;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;

import lombok.SneakyThrows;

@Service
public class ImportCustomCSVService {

    private CsvRepository csvRepository;
    private final HeaderColumnNameTranslateMappingStrategy<CustomCSV> strategy = new HeaderColumnNameTranslateMappingStrategy<>();
    private final Map<String, String> columnsCSVFile = Map.of(
            "date", "date",
            "high", "high",
            "low", "low",
            "close", "close");

    public ImportCustomCSVService(CsvRepository csvRepository) {
        this.csvRepository = csvRepository;
        this.strategy.setType(CustomCSV.class);
        this.strategy.setColumnMapping(columnsCSVFile);
    }

    @SneakyThrows
    public String importDataCustomCSV(InputStream fileUpload) {
        
        CSVReader reader = new CSVReader(new InputStreamReader(fileUpload));
        CsvToBean<CustomCSV> csvToBean = new CsvToBean<>();
        csvToBean.setCsvReader(reader);
        csvToBean.setMappingStrategy(strategy);
        List<CustomCSV> data = csvToBean.parse();
        String idConsulta = UUID.randomUUID().toString();
        data.forEach(row -> row.setIdConsulta(idConsulta));
        csvRepository.deleteAll();
        csvRepository.saveAll(data);
        return idConsulta;
    }

    public void deleteImportData(String idConsulta) {
        csvRepository.deleteByIdConsulta(idConsulta);
    }
}
