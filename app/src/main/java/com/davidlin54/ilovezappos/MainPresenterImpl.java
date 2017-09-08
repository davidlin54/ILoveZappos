package com.davidlin54.ilovezappos;

import android.os.Handler;
import android.util.Log;

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

    private MainView mView;

    private Handler mHandler = new Handler();

    private Runnable mFetchDataRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                fetchTransactionHistory();
                fetchOrderBook();
                fetchTickerHour();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mHandler.postDelayed(mFetchDataRunnable, 60000);
        }
    };

    public MainPresenterImpl(MainView view) {
        this.mView = view;
    }

    private void fetchTransactionHistory() throws IOException {
        Call<List<Transaction>> call = ILoveZapposApplication.mClient.transactions();
        call.enqueue(new Callback<List<Transaction>>() {
            @Override
            public void onResponse(Call<List<Transaction>> call, Response<List<Transaction>> response) {
                List<Transaction> transactions = response.body();
                mView.updateTransactionHistory(transactions);
            }

            @Override
            public void onFailure(Call<List<Transaction>> call, Throwable t) {

            }
        });
    }

    private void fetchOrderBook() throws IOException {
        Call<OrderBook> call = ILoveZapposApplication.mClient.order_book();
        call.enqueue(new Callback<OrderBook>() {
            @Override
            public void onResponse(Call<OrderBook> call, Response<OrderBook> response) {
                OrderBook orderBook = response.body();
                mView.updateOrderBook(orderBook);
            }

            @Override
            public void onFailure(Call<OrderBook> call, Throwable t) {

            }
        });
    }

    private void fetchTickerHour() throws IOException {
        Call<TickerHour> call = ILoveZapposApplication.mClient.ticker_hour();
        call.enqueue(new Callback<TickerHour>() {
            @Override
            public void onResponse(Call<TickerHour> call, Response<TickerHour> response) {
                TickerHour tickerHour = response.body();
                mView.updateTickerHour(tickerHour);
            }

            @Override
            public void onFailure(Call<TickerHour> call, Throwable t) {

            }
        });
    }

    @Override
    public void startFetchingData() {
        stopFetchingData();
        mFetchDataRunnable.run();
    }

    @Override
    public void stopFetchingData() {
        mHandler.removeCallbacks(mFetchDataRunnable);
    }

    /*
    private class DataFetchThread extends Thread {

        public DataFetchThread() {
            this(new Runnable() {
                @Override
                public void run() {
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
                }
            });
        }

        public DataFetchThread(Runnable runnable) {
            super(runnable);
        }
    }*/
}
