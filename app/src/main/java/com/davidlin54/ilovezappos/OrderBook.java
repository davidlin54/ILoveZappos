package com.davidlin54.ilovezappos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lin on 2017-08-28.
 */

public class OrderBook {
    private long timestamp;
    private List<List<String>> bids;
    private List<List<String>> asks;

    public OrderBook(){}

    public long getTimestamp() {
        return timestamp;
    }

    public List<PriceAmountPair> getBids() {
        List<PriceAmountPair> bids = new ArrayList<>();
        for (List<String> bid : this.bids) {
            bids.add(new PriceAmountPair(bid));
        }

        return bids;
    }

    public List<PriceAmountPair> getAsks() {
        List<PriceAmountPair> asks = new ArrayList<>();
        for (List<String> ask : this.asks) {
            asks.add(new PriceAmountPair(ask));
        }

        return asks;
    }

}
