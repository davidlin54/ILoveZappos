package com.davidlin54.ilovezappos;

import java.math.BigDecimal;

/**
 * Created by Lin on 2017-08-28.
 */

public class TickerHour {
    private BigDecimal high;
    private BigDecimal last;
    private long timestamp;
    private BigDecimal bid;
    private BigDecimal vwap;
    private double volume;
    private BigDecimal low;
    private BigDecimal ask;
    private BigDecimal open;

    public TickerHour(){}

    public BigDecimal getHigh() {
        return high;
    }

    public BigDecimal getLast() {
        return last;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public BigDecimal getBid() {
        return bid;
    }

    public BigDecimal getVwap() {
        return vwap;
    }

    public double getVolume() {
        return volume;
    }

    public BigDecimal getLow() {
        return low;
    }

    public BigDecimal getAsk() {
        return ask;
    }

    public BigDecimal getOpen() {
        return open;
    }
}
