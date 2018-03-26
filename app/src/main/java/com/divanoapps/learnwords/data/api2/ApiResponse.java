package com.divanoapps.learnwords.data.api2;

import org.json.JSONObject;

public class ApiResponse {
    private JSONObject response;
    private ApiError error;

    public JSONObject getResponse() {
        return response;
    }

    public ApiError getError() {
        return error;
    }
}
