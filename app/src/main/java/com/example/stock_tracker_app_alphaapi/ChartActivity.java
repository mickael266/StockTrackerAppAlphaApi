package com.example.stock_tracker_app_alphaapi;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

public class ChartActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private static final String API_KEY = "&apikey=MYIZVLDYD4L984GI";
    private StringBuilder sb = new StringBuilder("https://www.alphavantage.co/query?function=");

    private LineChart lineChart;
    private TextView stockTickerTextInputLayout;
    private RadioGroup periodRadioGroup;
    private CheckBox highCheckBox, lowCheckBox, closeCheckBox;
    private Button chartButton;
    private ArrayList<ChartEntry> chartDataList = new ArrayList<>();
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.line_chart);

        lineChart = findViewById(R.id.activity_main_linechart);
        chartButton = findViewById(R.id.activity_main_getprices);
        stockTickerTextInputLayout = findViewById(R.id.activity_main_stockticker);
        periodRadioGroup = findViewById(R.id.activity_main_period_radiogroup);
        highCheckBox = findViewById(R.id.activity_main_high);
        lowCheckBox = findViewById(R.id.activity_main_low);
        closeCheckBox = findViewById(R.id.activity_main_close);

        configureLineChart();

        requestQueue = Volley.newRequestQueue(this);

        Bundle b = getIntent().getExtras();
        String symbol = b.getString("symbol");

        stockTickerTextInputLayout.setText(symbol);

        chartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStockData();
            }
        });
    }

    private void configureLineChart() {
        Description desc = new Description();
        desc.setText("Stock Price History");
        desc.setTextSize(28);
        lineChart.setDescription(desc);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                if (index >= 0 && index < chartDataList.size()) {
                    return chartDataList.get(index).getDate();
                }
                return "";
            }
        });
    }

    public void getStockData() {
        String jasonObjectToFetch = "Time Series (Daily)";
        int dailyFlag = 0;

        int numberOfDates;
        if (periodRadioGroup.getCheckedRadioButtonId() == R.id.activity_main_period1d) {
            numberOfDates = 100;
            jasonObjectToFetch = "Time Series (5min)";
            dailyFlag = 1;
        }
        else if (periodRadioGroup.getCheckedRadioButtonId() == R.id.activity_main_period5d)
            numberOfDates = 5;
        else if (periodRadioGroup.getCheckedRadioButtonId() == R.id.activity_main_period30d)
            numberOfDates = 30;
        else
            numberOfDates = 30 * 12;

        if(dailyFlag == 0) {
            sb.append("TIME_SERIES_DAILY_ADJUSTED");
            sb.append("&symbol=" + stockTickerTextInputLayout.getText().toString());
            sb.append(API_KEY);
            fetchStockData(sb.toString(), jasonObjectToFetch, numberOfDates);
        }
        else{
            sb.append("TIME_SERIES_INTRADAY");
            sb.append("&symbol=" + stockTickerTextInputLayout.getText().toString());
            sb.append("&interval=5min");
            sb.append(API_KEY);
            fetchStockData(sb.toString(), jasonObjectToFetch, numberOfDates);
        }

    }

    private void fetchStockData(String url, String jasonObjectToFetch, int numberOfDates) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            chartDataList.clear();

                            JSONObject timeSeries = response.getJSONObject(jasonObjectToFetch);
                            Iterator<String> dateIterator = timeSeries.keys();

                            int iterations = numberOfDates;

                            while (dateIterator.hasNext()) {
                                String dateString = dateIterator.next();
                                JSONObject dataObject = timeSeries.getJSONObject(dateString);

                                float open = (float) dataObject.getDouble("1. open");
                                float high = (float) dataObject.getDouble("2. high");
                                float low = (float) dataObject.getDouble("3. low");
                                float close = (float) dataObject.getDouble("4. close");

                                ChartEntry entry = new ChartEntry(dateString, open, high, low, close);
                                chartDataList.add(entry);

                                iterations--;
                                if (iterations == 0)
                                    break;
                            }

                            Collections.sort(chartDataList);

                            ArrayList<Entry> pricesHigh = new ArrayList<>();
                            ArrayList<Entry> pricesLow = new ArrayList<>();
                            ArrayList<Entry> pricesClose = new ArrayList<>();
                            ArrayList<String> datesForCharts = new ArrayList<>();

                            int j = 0;
                            for (ChartEntry entry : chartDataList) {
                                datesForCharts.add(entry.getDate());
                                pricesHigh.add(new Entry(j, entry.getHigh()));
                                pricesClose.add(new Entry(j, entry.getClose()));
                                pricesLow.add(new Entry(j, entry.getLow()));
                                j++;
                            }

                            setLineChartData(pricesHigh, pricesLow, pricesClose, datesForCharts);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ChartActivity.this, "Error occurred while fetching data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(ChartActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(request);
    }

    private void setLineChartData(ArrayList<Entry> pricesHigh, ArrayList<Entry> pricesLow, ArrayList<Entry> pricesClose, ArrayList<String> datesForCharts) {
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        if (highCheckBox.isChecked()) {
            LineDataSet highLineDataSet = new LineDataSet(pricesHigh, stockTickerTextInputLayout.getText().toString() + " Price (High)");
            highLineDataSet.setDrawCircles(true);
            highLineDataSet.setCircleRadius(4);
            highLineDataSet.setDrawValues(false);
            highLineDataSet.setLineWidth(3);
            highLineDataSet.setColor(Color.GREEN);
            highLineDataSet.setCircleColor(Color.GREEN);
            dataSets.add(highLineDataSet);
        }

        if (lowCheckBox.isChecked()) {
            LineDataSet lowLineDataSet = new LineDataSet(pricesLow, stockTickerTextInputLayout.getText().toString() + " Price (Low)");
            lowLineDataSet.setDrawCircles(true);
            lowLineDataSet.setCircleRadius(4);
            lowLineDataSet.setDrawValues(false);
            lowLineDataSet.setLineWidth(3);
            lowLineDataSet.setColor(Color.RED);
            lowLineDataSet.setCircleColor(Color.RED);
            dataSets.add(lowLineDataSet);
        }

        if (closeCheckBox.isChecked()) {
            LineDataSet closeLineDataSet = new LineDataSet(pricesClose, stockTickerTextInputLayout.getText().toString() + " Price (Close)");
            closeLineDataSet.setDrawCircles(true);
            closeLineDataSet.setCircleRadius(4);
            closeLineDataSet.setDrawValues(false);
            closeLineDataSet.setLineWidth(3);
            closeLineDataSet.setColor(Color.rgb(255, 165, 0));
            closeLineDataSet.setCircleColor(Color.rgb(255, 165, 0));
            dataSets.add(closeLineDataSet);
        }

        LineData lineData = new LineData(dataSets);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    private static class ChartEntry implements Comparable<ChartEntry> {
        private String date;
        private float open;
        private float high;
        private float low;
        private float close;

        public ChartEntry(String date, float open, float high, float low, float close) {
            this.date = date;
            this.open = open;
            this.high = high;
            this.low = low;
            this.close = close;
        }

        public String getDate() {
            return date;
        }

        public float getOpen() {
            return open;
        }

        public float getHigh() {
            return high;
        }

        public float getLow() {
            return low;
        }

        public float getClose() {
            return close;
        }

        @Override
        public int compareTo(ChartEntry other) {
            try {
                Date thisDate = dateFormat.parse(this.date);
                Date otherDate = dateFormat.parse(other.date);
                return thisDate.compareTo(otherDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }
}
