package com.samayu.prodcastc.businessObjects.connect;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.samayu.prodcastc.BuildConfig;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sarathan732 on 8/5/2017.
 */

public class ProdcastServiceManager {

    public  ProdcastService getClient(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response response = chain.proceed(request);
                String body = response.body().string();
                System.out.println(body);

                return response.newBuilder()
                        .body(ResponseBody.create(response.body().contentType(), body))
                        .build();
            }
        });

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();





        System.out.println( "Connecting to URL "+ BuildConfig.SERVER_URL );
        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(BuildConfig.SERVER_URL)
                        .addConverterFactory(
                                GsonConverterFactory.create(gson));


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