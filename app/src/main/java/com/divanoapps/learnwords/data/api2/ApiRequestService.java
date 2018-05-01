package com.divanoapps.learnwords.data.api2;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiRequestService {
    static String getApiUrl() {
        return "http://10.97.138.104:8081/";
    }

    @POST("api/2.1")
    Call<ApiResponse> request(@Body Map<String, Object> body);
}
