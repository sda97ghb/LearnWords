package com.divanoapps.learnwords.data.api2;

import org.json.JSONObject;

public class ApiResponse {
    private Object response;
    private ApiError error;

    public Object getResponse() {
        return response;
    }

    public ApiError getError() {
        return error;
    }
}
