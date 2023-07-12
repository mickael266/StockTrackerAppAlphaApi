package com.example.stock_tracker_app_alphaapi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements StockAdapter.OnClickListener {

    private static final String PREFS_NAME = "StockPrefs";
    private static final String KEY_STOCKS = "stocks";
    private SharedPreferences sharedPreferences;
    private RecyclerView recyclerView;
    private StockAdapter myAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Stock> stocks;
    private EditText stockInput;
    private Button searchButton;

    private RequestQueue requestQueue;
    private static final String API_KEY = "&apikey=MYIZVLDYD4L984GI";

    private StringBuilder sb = new StringBuilder("https://www.alphavantage.co/query?function=");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.stocksList);
        stockInput = findViewById(R.id.stockInput);
        searchButton = findViewById(R.id.searchButton);

        requestQueue = Volley.newRequestQueue(this);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        stocks = loadStocksFromSharedPreferences();

        myAdapter = new StockAdapter(this, stocks, sharedPreferences, this);
        recyclerView.setAdapter(myAdapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String symbol = stockInput.getText().toString().trim().toUpperCase();
                if (!TextUtils.isEmpty(symbol)) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    fetchStockData("GLOBAL_QUOTE&", "symbol=" + symbol);
                }
            }
        });
    }

    private void fetchStockData(String function, String params) {
        sb.setLength(0); // Clear the StringBuilder
        sb.append("https://www.alphavantage.co/query?function=");
        sb.append(function);
        sb.append(params);
        sb.append(API_KEY);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, sb.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject globalQuote = response.getJSONObject("Global Quote");
                            String name = globalQuote.getString("01. symbol");
                            String price = globalQuote.getString("05. price");
                            String change = globalQuote.getString("09. change");

                            Stock stockToAdd = new Stock(name, price, change);
                            addStock(stockToAdd);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        requestQueue.add(request);
    }

    public void addStock(Stock stockToAdd) {
        if (!stocks.contains(stockToAdd)) {
            stocks.add(stockToAdd);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String stocksJson = gson.toJson(stocks);
            editor.putString(KEY_STOCKS, stocksJson);
            editor.apply();

            myAdapter.notifyDataSetChanged();
        }
    }

    public void openChartActivity(String symbol) {
        Intent intent = new Intent(MainActivity.this, ChartActivity.class);
        intent.putExtra("symbol", symbol);
        startActivity(intent);
    }

    private ArrayList<Stock> loadStocksFromSharedPreferences() {
        String stocksJson = sharedPreferences.getString(KEY_STOCKS, null);
        if (stocksJson != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Stock>>() {}.getType();
            return gson.fromJson(stocksJson, type);
        }
        return new ArrayList<>();
    }

    @Override
    public void onItemClick(int position) {
        Stock selectedStock = stocks.get(position);
        openChartActivity(selectedStock.getSymbol());
    }
}
