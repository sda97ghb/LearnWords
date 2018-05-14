package com.divanoapps.learnwords.data.dogfish;

public class ApiError extends Throwable {
    public static final String SERVER = "server";
    public static final String AUTHORIZATION = "authorization";
    public static final String METHOD = "method";

    private String type;
    private int code;
    private String message;

    public ApiError() {
        this.type = SERVER;
        this.code = 0;
        this.message = "Unknown error";
    }

    public ApiError(String type, int code, String message) {
        this.type = type;
        this.code = code;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
