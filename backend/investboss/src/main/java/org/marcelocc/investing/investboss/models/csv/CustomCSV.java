package org.marcelocc.investing.investboss.models.csv;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "tbCustomCSV")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomCSV {
    @Id
    private long id;
    private String idConsulta;
    private LocalDate date, dateEnd, dateStart; 
    private Double close, high, low, totalMaxAverage, totalMinAverage, 
        quotation, quotation2, percentage, profit, loss, totalEnd;
    private String hilo; 
    private int trend, ifStart;
}
