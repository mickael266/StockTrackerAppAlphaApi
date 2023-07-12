package com.example.stock_tracker_app_alphaapi;

import androidx.annotation.Nullable;

public class Stock implements Cloneable {
    private String symbol;
    private String price;
    private String change;

    public Stock(String symbol, String price, String change) {
        this.symbol = symbol;
        this.price = price;
        this.change = change;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        Stock stock2 = (Stock) obj;
        return this.symbol.equals(stock2.symbol);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.symbol != null ? this.symbol.hashCode() : 0);
        return hash;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    @Override
    public Stock clone() {
        return new Stock(this.symbol, this.price, this.change);
    }
}