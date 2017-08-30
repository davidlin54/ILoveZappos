package com.davidlin54.ilovezappos;

import java.util.List;

/**
 * Created by Lin on 2017-08-28.
 */

public interface MainView {
    void updateData(List<Transaction> transactions, OrderBook orderBook, TickerHour tickerHour);
}
