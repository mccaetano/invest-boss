package org.marcelocc.investing.investboss.models.csv;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;

@Table(name = "tbCustomCSV")
@Data
@AllArgsConstructor
public class CustomCSV {
    @Id
    private long id;
    private String idConsulta;
    private LocalDateTime date, dateEnd, dateStart; 
    private Double close, high, low, totalMaxAverage, totalMinAverage, 
        quotation, quotation2, percentage, profit, loss, totalEnd;
    private String hilo; 
    private int trend, ifStart;
}
