package com.example.stock_tracker_app_alphaapi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;

public class StockAdapter extends RecyclerView.Adapter < StockAdapter.Viewholder > {
    // array list to store all items
    private ArrayList<Stock> stockArrayList;
    private Context activity;
    private Viewholder holder;
    private static final String KEY_STOCKS = "stocks";

    private SharedPreferences sharedPreferences;

    // object of type ItemClicked to get our mainActivity using context passed in androidVersionAdapter
    public interface OnClickListener {

        void onItemClick(int position);
    }

    public void setStocksList(ArrayList<Stock> stocks) {
        stockArrayList = stocks;
        notifyDataSetChanged();
    }

    //set an interface

    //in this we will set our context i.e. mainActivity to activity object and arrayList
    public StockAdapter(Context context, ArrayList<Stock> List, SharedPreferences sharedPreferences, MainActivity mainActivity) {
        stockArrayList = List; //arrayList
        activity =  context; //mainActivity reference
        this.sharedPreferences=sharedPreferences;

    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView stockName, stockPrice, stockChange;
        Button btn_delete;

        //for textView name and version of android
        //declate viewholder


        public Viewholder(@NonNull View itemView) {
            super(itemView);
            stockName = itemView.findViewById(R.id.stockName); //find and set android name TextView
            stockPrice = itemView.findViewById(R.id.stockPrice); //find and set android version textView
            stockChange = itemView.findViewById(R.id.stockChange); //find and set android icon ImageView
            btn_delete = itemView.findViewById(R.id.btn_delete);

        }
    }

    //onCreateViewHolder method to return and set Viewholder
    @NonNull
    @Override
    public StockAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_card, parent, false);
        return new Viewholder(v); //return viewholder
    }

    //onBindViewHolder method to bind different resourses with our holder
    @Override
    public void onBindViewHolder(@NonNull StockAdapter.Viewholder holder, @SuppressLint("RecyclerView") int position) {
        this.holder = holder;

        holder.itemView.setTag(stockArrayList.get(position)); //get the position in the arrayList

        holder.stockName.setText(stockArrayList.get(position).getSymbol());
        holder.stockPrice.setText(stockArrayList.get(position).getPrice());
        holder.stockChange.setText(stockArrayList.get(position).getChange());

//        openNewActivity(stockArrayList.get(position).getSymbol());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivity(stockArrayList.get(position).getSymbol());
            }
        });

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    stockArrayList.remove(position);
                    saveStocksToSharedPref();
                    notifyItemRemoved(position);
            }
        });
    }

        public void openNewActivity (String symbol){
            Intent intent = new Intent((Context) activity, ChartActivity.class);
            intent.putExtra("symbol", symbol);
            ((Context) activity).startActivity(intent);
        }

    @Override
    public int getItemCount() {
        return stockArrayList.size();
    }
    public void saveStocksToSharedPref( ) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String stocksJson = gson.toJson(stockArrayList);
            editor.putString(KEY_STOCKS, stocksJson);
            editor.apply();
    }
}
