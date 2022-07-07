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
import java.util.Comparator;

public class CustomAdapter3 extends ArrayAdapter<FinancialSecurity> {
    private TextView tickerSymbol, companyName, stockPrice, netChange, percentChange;
    private Context mainActivityContext;
    private ArrayList<FinancialSecurity> stocksList;
    private ArrayList<FinancialSecurity> topLosers;

    public CustomAdapter3(@NonNull Context context, int resource, @NonNull ArrayList<FinancialSecurity> objects1, ArrayList<FinancialSecurity> objects2) {
        super(context, resource, objects1);
        this.mainActivityContext = context;
        stocksList = objects1;
        topLosers = objects2;
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

        ArrayList<Double> gatherInfo1 = new ArrayList<>();

        for (int i = 0; i < stocksList.size(); i++) {
            String percentChange = stocksList.get(i).getPercentChange();
            if (!percentChange.isEmpty()) {
                percentChange = percentChange.replace("%", "");
                gatherInfo1.add(Double.parseDouble(percentChange));
            }
        }
        Collections.sort(gatherInfo1);

        for (int i = 0; i < gatherInfo1.size(); i++) {
            if (!stocksList.get(i).getPercentChange().isEmpty()) {
                if (Double.parseDouble(stocksList.get(i).getPercentChange().replace("%","")) == gatherInfo1.get(0) ||
                        Double.parseDouble(stocksList.get(i).getPercentChange().replace("%","")) == gatherInfo1.get(1) ||
                        Double.parseDouble(stocksList.get(i).getPercentChange().replace("%","")) == gatherInfo1.get(2) ||
                        Double.parseDouble(stocksList.get(i).getPercentChange().replace("%","")) == gatherInfo1.get(3) ||
                        Double.parseDouble(stocksList.get(i).getPercentChange().replace("%","")) == gatherInfo1.get(4) ||
                        Double.parseDouble(stocksList.get(i).getPercentChange().replace("%","")) == gatherInfo1.get(5)) {
                    topLosers.add(stocksList.get(i));
                }
            }
        }
        Collections.sort(topLosers, new Comparator<FinancialSecurity>(){
            public int compare(FinancialSecurity f1, FinancialSecurity f2) {
                double f1_ = Double.parseDouble(f1.getPercentChange().replace("%", ""));
                double f2_ = Double.parseDouble(f2.getPercentChange().replace("%", ""));
                return Double.compare(f1_, f2_);
            }
        });
        //Collections.reverse(topLosers);
        Log.d("sizeeee", topLosers.size()+"");
        if (position < 6) {
            tickerSymbol.setText(topLosers.get(position).getTickerSymbol());
            companyName.setText(topLosers.get(position).getName());
            stockPrice.setText(topLosers.get(position).getLastSale());
            netChange.setText(topLosers.get(position).getNetChange());
            percentChange.setText(topLosers.get(position).getPercentChange());
            if (Double.parseDouble(topLosers.get(position).getPercentChange().replace("%", "")) >= 0) {
                netChange.setTextColor(Color.rgb(0, 255, 0));
                percentChange.setTextColor(Color.rgb(0, 255, 0));
                percentChange.setBackgroundResource(R.drawable.green_rectangle);
                percentChange.setText("+"+topLosers.get(position).getPercentChange());
                netChange.setText("+"+topLosers.get(position).getNetChange());
            } else if (Double.parseDouble(topLosers.get(position).getPercentChange().replace("%", "")) < 0) {
                netChange.setTextColor(Color.rgb(255, 0, 0));
                percentChange.setTextColor(Color.rgb(255, 0, 0));
                percentChange.setBackgroundResource(R.drawable.red_rectangle);
            }
        }
        topLosers.clear();
        notifyDataSetChanged();
        return adapterLayout;
    }

    public ArrayList<FinancialSecurity> getTopLosers() {
        return topLosers;
    }
}
