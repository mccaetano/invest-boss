package org.marcelocc.investing.investboss.utils;

import java.util.Comparator;

import org.marcelocc.investing.investboss.models.HiloPeriodData;

public class HiloComparator implements Comparator<HiloPeriodData> {

    @Override
    public int compare(HiloPeriodData o1, HiloPeriodData o2) {
        return Double.compare(o1.getPayoff(), o2.getPayoff());
    }

}
