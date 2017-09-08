package com.davidlin54.ilovezappos;

import android.app.Application;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by david.lin1ibm.com on 2017-09-08.
 */

public class ILoveZapposApplication extends Application {
    public static BitstampClient mClient;
    public final static String API_BASE_URL = "https://www.bitstamp.net/api/v2/";

    public ILoveZapposApplication() {
        super();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(ILoveZapposApplication.API_BASE_URL)
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        );

        Retrofit retrofit =
                builder
                        .client(
                                httpClient.build()
                        )
                        .build();

        ILoveZapposApplication.mClient = retrofit.create(BitstampClient.class);
    }
}
