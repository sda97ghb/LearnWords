package com.divanoapps.learnwords.data.dogfish;

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
