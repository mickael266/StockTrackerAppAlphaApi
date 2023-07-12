package com.example.stock_tracker_app_alphaapi;

import java.util.Comparator;

public class StockComperator implements Comparator<APIResponse> {

    @Override
    public int compare(APIResponse o1, APIResponse o2) {
        return o1.getDate().compareTo(o2.getDate());
    }


}
