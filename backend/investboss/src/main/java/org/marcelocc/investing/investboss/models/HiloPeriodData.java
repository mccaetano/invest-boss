package org.marcelocc.investing.investboss.models;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "of")
public class HiloPeriodData implements Comparable<HiloPeriodData> {
    private int period;
    private double totalStart, totalEnd, totalResult, percentProfit, 
        profitAverage, lossAverage, percentLoss, profitability, payoff;
    private LocalDate startAnalisysDate;
    private int totalOperations, totalLoss, totalProfit;

    @Override
    public int compareTo(HiloPeriodData other) {
        return Double.compare(this.payoff, other.payoff);
    }

}
