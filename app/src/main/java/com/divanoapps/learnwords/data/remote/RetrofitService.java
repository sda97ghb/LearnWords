package com.divanoapps.learnwords.data.remote;

import com.divanoapps.learnwords.data.dogfish.ApiResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by dmitry on 13.05.18.
 */

public interface RetrofitService {
    @POST("api/2.1")
    Call<ApiResponse> request(@Body Map<String, Object> body);
}
