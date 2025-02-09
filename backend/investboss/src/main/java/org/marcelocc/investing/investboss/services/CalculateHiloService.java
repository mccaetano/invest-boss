package org.marcelocc.investing.investboss.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.marcelocc.investing.investboss.models.HiloPeriodData;
import org.marcelocc.investing.investboss.models.csv.CustomCSV;
import org.marcelocc.investing.investboss.repositories.CsvRepository;
import org.marcelocc.investing.investboss.utils.HiloComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalculateHiloService {

    @Autowired
    private CsvRepository csvRepository;


    public int calculateBetterPeriod(Double totalStart, String idConsulta) {        
        List<HiloPeriodData> hiloPeriodDatas = new ArrayList<>();
        for (int i = 3; i < 61; i++) {
            HiloPeriodData hiloPeriodData = populateHiloPeriod(totalStart, i, idConsulta);
            hiloPeriodDatas.add(hiloPeriodData);            
        }
        hiloPeriodDatas.sort(new HiloComparator());
        return hiloPeriodDatas.getFirst().getPeriod();
    }

    private HiloPeriodData populateHiloPeriod(Double totalStart, int period, String idConsulta) {
        final List<CustomCSV> customCSVs = csvRepository.findByIdConsultaOrderByDateDesc(idConsulta);
        final HiloPeriodData hiloPeriodData = new HiloPeriodData();
        hiloPeriodData.setPeriod(period);
        hiloPeriodData.setTotalStart(totalStart);

        int totalOperations = 0;
        int totalProfit = 0;
        int totalLoss = 0;
        double profitAverage = 0.0;
        double lossAverage = 0.0;
        for(int i = customCSVs.size() ; i > customCSVs.size(); i--) {
            CustomCSV customCSV = customCSVs.get(i);
            customCSV.setTotalMaxAverage(calculateMaxAverage(customCSVs, i, period));
            customCSV.setTotalMinAverage(calculateMinAverage(customCSVs, i, period));
            customCSV.setHilo(getHilo(customCSVs, i));
            customCSV.setQuotation(calculateQuotation(customCSVs, i));
            customCSV.setQuotation2(calculateQuotation2(customCSVs, i));
            customCSV.setPercentage(calculatePercentage(customCSVs, i));
            customCSV.setDateEnd(calculateEndDate(customCSVs, i));
            customCSV.setDateStart(calculateStartDate(customCSVs, i));
            customCSV.setTrend(calculateTrend(customCSVs, i));
            customCSV.setProfit(calculateProfit(customCSV));
            customCSV.setLoss(calculateLoss(customCSV));
            customCSV.setIfStart(calculateIfStart(customCSV, null));
            customCSV.setTotalEnd(calculateTotalEnd(customCSVs, i, totalStart));
            if (customCSV.getLoss() > 0 || customCSV.getProfit() > 0) {
                totalOperations++;
            }
            if (customCSV.getLoss() > 0) {
                totalLoss++;
            }
            if (customCSV.getProfit() > 0) {
                totalProfit++;
            }
            profitAverage += customCSV.getProfit();
            lossAverage += customCSV.getLoss();
        }        
        hiloPeriodData.setTotalEnd(customCSVs.get(0).getTotalEnd());
        hiloPeriodData.setTotalResult(hiloPeriodData.getTotalEnd() - hiloPeriodData.getTotalStart());
        hiloPeriodData.setStartAnalisysDate(customCSVs.get(customCSVs.size()).getDate());
        hiloPeriodData.setTotalOperations(totalOperations);
        hiloPeriodData.setTotalProfit(totalProfit);
        hiloPeriodData.setTotalLoss(totalLoss);
        hiloPeriodData.setPercentProfit(totalProfit / totalOperations);
        hiloPeriodData.setProfitAverage(profitAverage / customCSVs.size());
        hiloPeriodData.setPercentLoss(totalLoss / totalOperations);
        hiloPeriodData.setLossAverage(lossAverage / customCSVs.size());
        hiloPeriodData.setProfitability(
            (hiloPeriodData.getTotalEnd() - hiloPeriodData.getTotalStart()) / 
            hiloPeriodData.getTotalStart());
        hiloPeriodData.setPayoff(hiloPeriodData.getProfitAverage() / hiloPeriodData.getLossAverage());
        return hiloPeriodData;
    }

    private Double calculateMaxAverage(List<CustomCSV> customCSVs, int rowIndex, int period) {
        Double maxAverage = 0.0;
        for (int g = rowIndex; g < period; g++) {
            maxAverage += customCSVs.get(g).getHigh();
        }
        return maxAverage / period;
    }

    private Double calculateMinAverage(List<CustomCSV> customCSVs, int rowIndex, int period) {
        Double minAverage = 0.0;
        for (int g = rowIndex; g < period; g++) {
            minAverage += customCSVs.get(g).getLow();
        }
        return minAverage / period;
    }

    private String getHilo(List<CustomCSV> customCSVs, int rowIndex) {
        if (customCSVs.get(rowIndex).getClose() > customCSVs.get(rowIndex).getTotalMaxAverage()) {
            return "COMPRA";
        } else {
            if (customCSVs.get(rowIndex).getClose() < customCSVs.get(rowIndex).getTotalMinAverage()) {
                return "VENDA";
            } else {
                if (customCSVs.get(rowIndex + 1).getClose() >  customCSVs.get(rowIndex + 1).getTotalMaxAverage()) {
                    return "COMPRA";
                } else {
                    if (customCSVs.get(rowIndex + 1).getClose() < customCSVs.get(rowIndex + 1).getTotalMinAverage()) {
                        return "VENDA";
                    } else {
                        return "";
                    }
                }
            }
        }
    }

    private double calculateQuotation(List<CustomCSV> customCSVs, int rowIndex) {
        if (customCSVs.get(rowIndex).getHilo() != customCSVs.get(rowIndex + 1).getHilo()) {
            return customCSVs.get(rowIndex).getClose();
        } else {
            return 0.0;
        }
    }

    private double calculateQuotation2(List<CustomCSV> customCSVs, int rowIndex) {
        if (customCSVs.get(rowIndex).getQuotation() == 0.0) {
            return customCSVs.get(rowIndex + 1).getQuotation();
        } else {
            return customCSVs.get(rowIndex).getQuotation();
        }
    }

    private double calculatePercentage(List<CustomCSV> customCSVs, int rowIndex) {
        if (customCSVs.get(rowIndex).getQuotation() != 0.0) {
            if (customCSVs.get(rowIndex + 1).getHilo().equals("COMPRA")) {
                return (customCSVs.get(rowIndex).getQuotation2() - 
                customCSVs.get(rowIndex + 1).getQuotation2()) / customCSVs.get(rowIndex).getQuotation2();
            } else {
                if (customCSVs.get(rowIndex + 1).getHilo().equals("VENDA")) {
                    return (customCSVs.get(rowIndex + 1).getQuotation2() - 
                    customCSVs.get(rowIndex).getQuotation2()) / customCSVs.get(rowIndex +1).getQuotation2();
                } else {
                    return 0.0;
                }
            }
        } else {
            return 0.0;
        }
    }

    private LocalDate calculateEndDate(List<CustomCSV> customCSVs, int rowIndex) {
        if (customCSVs.get(rowIndex).getPercentage() != 0.0) {
            return customCSVs.get(rowIndex).getDate();
        } else {
            if (customCSVs.get(rowIndex + 1).getDateEnd() == null) {
                return customCSVs.get(rowIndex).getDate();
            } else {
                return customCSVs.get(rowIndex + 1).getDateEnd();
            }
        }
    }

    private LocalDate calculateStartDate(List<CustomCSV> customCSVs, int rowIndex) {
        if (customCSVs.get(rowIndex).getHilo() != customCSVs.get(rowIndex + 1).getHilo()) {
            return customCSVs.get(rowIndex + 1).getDateEnd();
        } else {
            return null;
        }
    }

    private int calculateTrend(List<CustomCSV> customCSVs, int rowIndex) {
        if (customCSVs.get(rowIndex + 1).getDateStart() != null) {
            return 1;
        } else {
            return customCSVs.get(rowIndex + 1).getTrend() + 1;
        }
    }

    private double calculateProfit(CustomCSV customCSV) {
        if (customCSV.getPercentage() > 0.0) {
            return customCSV.getPercentage();
        } else {
            return 0.0;
        }
    }

    private double calculateLoss(CustomCSV customCSV) {
        if (customCSV.getPercentage() < 0.0) {
            return customCSV.getPercentage();
        } else {
            return 0.0;
        }
    }

    private int calculateIfStart(CustomCSV customCSV, LocalDate analisysDate) {
        if (customCSV.getDate().equals(analisysDate)) {
            return 1;
        } else {
            return 0;
        }
    }
    

    private double calculateTotalEnd(List<CustomCSV> customCSVs, int rowIndex, double totalStart) {
        if (customCSVs.get(rowIndex).getIfStart() == 1) {
            return (customCSVs.get(rowIndex).getPercentage() + 1) * totalStart;
        } else {
            return (customCSVs.get(rowIndex).getPercentage() + 1) * customCSVs.get(rowIndex + 1).getTotalEnd();
        }
    }
}
