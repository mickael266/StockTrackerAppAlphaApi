package com.example.stock_tracker_app_alphaapi;

public class APIResponse {
    private String date;
    private float open, close, high, low;

    protected APIResponse(String date, float open, float close, float high, float low) {
        this.date = date;
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
    }

    // Getters and setters

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getOpen() {
        return open;
    }

    public void setOpen(float open) {
        this.open = open;
    }

    public float getClose() {
        return close;
    }

    public void setClose(float close) {
        this.close = close;
    }

    public float getHigh() {
        return high;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public float getLow() {
        return low;
    }

    public void setLow(float low) {
        this.low = low;
    }

    public static APIResponseFactory factory() {
        return new APIResponseFactory();
    }
}
