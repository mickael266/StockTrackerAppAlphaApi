package com.example.stock_tracker_app_alphaapi;

public class APIResponseFactory {
    private String date;
    private float open, close, high, low;

    public APIResponseFactory setDate(String date) {
        this.date = date;
        return this;
    }

    public APIResponseFactory setOpen(float open) {
        this.open = open;
        return this;
    }

    public APIResponseFactory setClose(float close) {
        this.close = close;
        return this;
    }

    public APIResponseFactory setHigh(float high) {
        this.high = high;
        return this;
    }

    public APIResponseFactory setLow(float low) {
        this.low = low;
        return this;
    }

    public APIResponse createAPIResponse() {
        return new APIResponse(date, open, close, high, low);
    }

    public static APIResponseFactory factory() {
        return new APIResponseFactory();
    }
}
