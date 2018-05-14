package com.divanoapps.learnwords.data.remote;

import com.divanoapps.learnwords.data.dogfish.ApiResponse;
import com.divanoapps.learnwords.data.dogfish.RequestExecutor;

import java.io.IOException;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dmitry on 13.05.18.
 */

public class RetrofitRequestExecutor implements RequestExecutor {
    private static String getApiUrl() {
        return "http://10.97.138.104:8081/";
    }

    private Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(getApiUrl())
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    private RetrofitService retrofitService = retrofit.create(RetrofitService.class);

    @Override
    public ApiResponse request(Map<String, Object> body) {
        try {
            return retrofitService.request(body).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
