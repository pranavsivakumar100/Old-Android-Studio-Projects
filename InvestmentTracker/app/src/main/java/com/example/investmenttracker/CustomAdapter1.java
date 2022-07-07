package com.example.investmenttracker;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;

public class CustomAdapter1 extends ArrayAdapter<FinancialSecurity> {

    private TextView tickerSymbol, companyName, stockPrice, netChange, percentChange;
    private Context mainActivityContext;
    private ArrayList<FinancialSecurity> stocksList;
    private ArrayList<FinancialSecurity> mostActiveStocksList;

    public CustomAdapter1(@NonNull Context context, int resource, @NonNull ArrayList<FinancialSecurity> objects1, ArrayList<FinancialSecurity> objects2) {
       super(context, resource, objects1);
       this.mainActivityContext = context;
       stocksList = objects1;
       mostActiveStocksList = objects2;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        LayoutInflater layoutInflater = (LayoutInflater) mainActivityContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View adapterLayout = layoutInflater.inflate(R.layout.adapter_layout, null);

        tickerSymbol = adapterLayout.findViewById(R.id.tickerSymbol);
        companyName = adapterLayout.findViewById(R.id.companyName);
        stockPrice = adapterLayout.findViewById(R.id.stockPrice);
        netChange = adapterLayout.findViewById(R.id.netChange);
        percentChange = adapterLayout.findViewById(R.id.pctChange);

        ArrayList<Integer> gatherInfo1 = new ArrayList<>();

        for (int i = 0; i < stocksList.size(); i++) {
            gatherInfo1.add(Integer.parseInt(stocksList.get(i).getVolume()));
        }

        Collections.sort(gatherInfo1);
        for (int i = 0; i < stocksList.size(); i++) {
            if (Integer.parseInt(stocksList.get(i).getVolume()) == gatherInfo1.get(stocksList.size()-1) ||
                    Integer.parseInt(stocksList.get(i).getVolume()) == gatherInfo1.get(stocksList.size()-2) ||
                    Integer.parseInt(stocksList.get(i).getVolume()) == gatherInfo1.get(stocksList.size()-3) ||
                    Integer.parseInt(stocksList.get(i).getVolume()) == gatherInfo1.get(stocksList.size()-4) ||
                    Integer.parseInt(stocksList.get(i).getVolume()) == gatherInfo1.get(stocksList.size()-5) ||
                    Integer.parseInt(stocksList.get(i).getVolume()) == gatherInfo1.get(stocksList.size()-6)) {
                mostActiveStocksList.add(stocksList.get(i));
            }
        }

        //Log.d("size4", "size: " + mostActiveStocksList.size());


        if (position < 6) {
            tickerSymbol.setText(mostActiveStocksList.get(position).getTickerSymbol());
            companyName.setText(mostActiveStocksList.get(position).getName());
            stockPrice.setText(mostActiveStocksList.get(position).getLastSale());
            netChange.setText(mostActiveStocksList.get(position).getNetChange());
            percentChange.setText(mostActiveStocksList.get(position).getPercentChange());
            if (Double.parseDouble(mostActiveStocksList.get(position).getPercentChange().replace("%", "")) >= 0) {
                netChange.setTextColor(Color.rgb(0, 255, 0));
                percentChange.setTextColor(Color.rgb(0, 255, 0));
                percentChange.setBackgroundResource(R.drawable.green_rectangle);
                percentChange.setText("+"+mostActiveStocksList.get(position).getPercentChange());
                netChange.setText("+"+mostActiveStocksList.get(position).getNetChange());
            } else if (Double.parseDouble(mostActiveStocksList.get(position).getPercentChange().replace("%", "")) < 0) {
                netChange.setTextColor(Color.rgb(255, 0, 0));
                percentChange.setTextColor(Color.rgb(255, 0, 0));
                percentChange.setBackgroundResource(R.drawable.red_rectangle);
            }
            //netChange.setBackgroundResource(R.drawable.red_rectangle);
        }
        mostActiveStocksList.clear();
        notifyDataSetChanged();
        return adapterLayout;
    }

    public ArrayList<FinancialSecurity> getMostActiveStocksList() {
        return mostActiveStocksList;
    }
}