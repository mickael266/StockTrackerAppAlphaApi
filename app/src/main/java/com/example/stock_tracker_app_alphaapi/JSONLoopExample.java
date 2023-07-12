package com.example.stock_tracker_app_alphaapi;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class JSONLoopExample extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Path to your JSON file
        String filePath = "path/to/your/json/file.json";

        // Run the JSON processing in a background task
        new JSONProcessingTask().execute(filePath);
    }

    private class JSONProcessingTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String filePath = params[0];

            // Create an instance of ObjectMapper from Jackson library
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                // Read the JSON file into a JsonNode object
                JsonNode jsonNode = objectMapper.readTree(new File(filePath));

                // Get the "Time Series (Daily)" node from the JSON
                JsonNode timeSeriesNode = jsonNode.get("Time Series (Daily)");

                // Iterate through each date in the time series
                Iterator<Map.Entry<String, JsonNode>> iterator = timeSeriesNode.fields();
                while (iterator.hasNext()) {
                    Map.Entry<String, JsonNode> entry = iterator.next();
                    String date = entry.getKey();
                    JsonNode dataNode = entry.getValue();

                    // Get the desired data for each date
                    String open = dataNode.get("1. open").asText();
                    String high = dataNode.get("2. high").asText();
                    String low = dataNode.get("3. low").asText();
                    String close = dataNode.get("4. close").asText();
                    String adjustedClose = dataNode.get("5. adjusted close").asText();
                    String volume = dataNode.get("6. volume").asText();
                    String dividendAmount = dataNode.get("7. dividend amount").asText();
                    String splitCoefficient = dataNode.get("8. split coefficient").asText();

                    // Process the data for each date
                    Log.d("JSONLoopExample", "Date: " + date);
                    Log.d("JSONLoopExample", "Open: " + open);
                    Log.d("JSONLoopExample", "High: " + high);
                    Log.d("JSONLoopExample", "Low: " + low);
                    Log.d("JSONLoopExample", "Close: " + close);
                    Log.d("JSONLoopExample", "Adjusted Close: " + adjustedClose);
                    Log.d("JSONLoopExample", "Volume: " + volume);
                    Log.d("JSONLoopExample", "Dividend Amount: " + dividendAmount);
                    Log.d("JSONLoopExample", "Split Coefficient: " + splitCoefficient);
                    Log.d("JSONLoopExample", "");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}