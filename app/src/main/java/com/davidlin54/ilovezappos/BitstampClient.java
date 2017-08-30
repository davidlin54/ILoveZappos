package com.davidlin54.ilovezappos;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Lin on 2017-08-28.
 */

public interface BitstampClient {
    @GET("transactions/btcusd/")
    Call<List<Transaction>> transactions();

    @GET("order_book/btcusd/")
    Call<OrderBook> order_book();

    @GET("ticker_hour/btcusd/")
    Call<TickerHour> ticker_hour();
}
