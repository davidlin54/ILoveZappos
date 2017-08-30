package com.davidlin54.ilovezappos;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Lin on 2017-08-28.
 */

public class MainPresenterImpl implements MainPresenter {
    private String API_BASE_URL = "https://www.bitstamp.net/api/v2/";

    private MainView mView;
    private BitstampClient mClient;


    public MainPresenterImpl(MainView view) {
        this.mView = view;

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        );

        Retrofit retrofit =
                builder
                        .client(
                                httpClient.build()
                        )
                        .build();

        mClient = retrofit.create(BitstampClient.class);
    }

    private Thread networkThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while(true) {
                List<Transaction> transactions = null;
                OrderBook orderBook = null;
                TickerHour tickerHour = null;
                // fetch trans history
                try {
                    transactions = fetchTransactionHistory();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // fetch order book
                try {
                    orderBook = fetchOrderBook();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // fetch ticker hour
                try {
                    tickerHour = fetchTickerHour();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // update view
                mView.updateData(transactions, orderBook, tickerHour);

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    });

    private List<Transaction> fetchTransactionHistory() throws IOException {
        Call<List<Transaction>> call = mClient.transactions();
        return call.execute().body();
    }

    private OrderBook fetchOrderBook() throws IOException {
        Call<OrderBook> call = mClient.order_book();
        return call.execute().body();
    }

    private TickerHour fetchTickerHour() throws IOException {
        Call<TickerHour> call = mClient.ticker_hour();
        return call.execute().body();
    }

    @Override
    public void startFetchingData() {
        networkThread.start();
    }

    @Override
    public void stopFetchingData() {
        networkThread.interrupt();
    }
}
