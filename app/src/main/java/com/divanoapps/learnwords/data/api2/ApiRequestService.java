package com.divanoapps.learnwords.data.api2;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiRequestService {
    static String getApiUrl() {
        return "http://localhost:8081/";
    }

    @POST("api/2.0")
    Call<ApiResponse> request(@Body String body);
}
