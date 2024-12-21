package com.example.sgb.utils;

public class CustomResponse<T> {
    private T data;
    private String message;
    private boolean error;
    private int statusCode;

    public CustomResponse() {
    }

    public CustomResponse(T data, String message, boolean error, int statusCode) {
        this.data = data;
        this.message = message;
        this.error = error;
        this.statusCode = statusCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
