package com.davidlin54.ilovezappos;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Lin on 2017-08-28.
 */

public class PriceAmountPair {
    private BigDecimal price;
    private double amount;

    public PriceAmountPair(List<String> pair){
        price = new BigDecimal(pair.get(0));
        amount = Double.valueOf(pair.get(1));
    }

    public BigDecimal getPrice() {
        return price;
    }

    public double getAmount() {
        return amount;
    }
}
