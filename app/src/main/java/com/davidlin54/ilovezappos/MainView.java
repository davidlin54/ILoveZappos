package com.davidlin54.ilovezappos;

import java.util.List;

/**
 * Created by Lin on 2017-08-28.
 */

public interface MainView {
    void updateTransactionHistory(List<Transaction> transactions);
    void updateOrderBook(OrderBook orderBook);
    void updateTickerHour(TickerHour tickerHour);
}
