package com.davidlin54.ilovezappos;

import java.math.BigDecimal;

/**
 * Created by Lin on 2017-08-28.
 */

public class Transaction {
    private long date;
    private long tid;
    private BigDecimal price;
    private int type;
    private double amount;

    public Transaction(){}

    public long getDate() {
        return date;
    }

    public long getTid() {
        return tid;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }
}
