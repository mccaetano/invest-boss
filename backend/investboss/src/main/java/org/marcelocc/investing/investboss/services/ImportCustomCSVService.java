package org.marcelocc.investing.investboss.services;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

import org.marcelocc.investing.investboss.models.csv.CustomCSV;
import org.marcelocc.investing.investboss.repositories.CsvRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import lombok.SneakyThrows;

@Service
public class ImportCustomCSVService {

    @Autowired
    private CsvRepository csvRepository;

    @SneakyThrows
    public String importDataCustomCSV(InputStream fileUpload, String dateFormat) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat, Locale.of("pt", "br"));
        CSVParser parser = new CSVParserBuilder()
            .withSeparator(';')
            .withIgnoreQuotations(true)
            .build();
        CSVReader reader = new CSVReaderBuilder(new InputStreamReader(fileUpload))
            .withSkipLines(0)
            .withCSVParser(parser)
            .build();
        String idConsulta = UUID.randomUUID().toString();        
        reader.skip(1);
        reader.forEach( row -> {
            CustomCSV customCSV = new CustomCSV();
            customCSV.setDate(LocalDate.parse(row[0], formatter));
            customCSV.setIdConsulta(idConsulta);
            customCSV.setHigh(Double.parseDouble(row[1]));
            customCSV.setLoss(Double.parseDouble(row[2]));
            customCSV.setClose(Double.parseDouble(row[3]));    
            csvRepository.save(customCSV);        
        }); 
        return idConsulta;
    }

    public void deleteImportData(String idConsulta) {
        csvRepository.deleteByIdConsulta(idConsulta);
    }
}
