package com.ventruxinformatics.prodcast.connect;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sarathan732 on 8/5/2017.
 */

public class ProdcastServiceManager {

    public  ProdcastService getClient(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl("http://ec2-52-91-5-22.compute-1.amazonaws.com:8080")
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        );

        Retrofit retrofit =
                builder
                        .client(
                                httpClient.build()
                        )
                        .build();

        ProdcastService client =  retrofit.create(ProdcastService.class);
        return client;

    }
}
