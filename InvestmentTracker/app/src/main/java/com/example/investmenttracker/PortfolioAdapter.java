package com.example.investmenttracker;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PortfolioAdapter extends ArrayAdapter<FinancialSecurity> {

    private TextView companyTicker, stockCompanyName, shareQuantity, totalPrice, totalPortfolioVal;
    private Context mainActivityContext;
    private ArrayList<FinancialSecurity> purchasedStocks;
    private ArrayList<FinancialSecurity> allStocks;
    private double totalPortfolioValue = 0.00;

    public PortfolioAdapter(@NonNull Context context, int resource, @NonNull ArrayList<FinancialSecurity> objects1, @NonNull ArrayList<FinancialSecurity> objects2) {
        super(context, resource, objects1);
        mainActivityContext = context;
        allStocks = objects1;
        purchasedStocks = objects2;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        LayoutInflater layoutInflater = (LayoutInflater) mainActivityContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View adapterLayout = layoutInflater.inflate(R.layout.portfolio_adapter_layout, null);

        companyTicker = adapterLayout.findViewById(R.id.companyTicker);
        stockCompanyName = adapterLayout.findViewById(R.id.stockCompanyName);
        shareQuantity = adapterLayout.findViewById(R.id.shareQuantity);
        totalPrice = adapterLayout.findViewById(R.id.totalPrice);


        Log.d("sizzze", purchasedStocks.size()+"");
        for (FinancialSecurity f : purchasedStocks) {
            double stockValueInPortfolio = Double.parseDouble(f.getLastSale().replace("$", ""))*f.getShares();
            companyTicker.setText(f.getTickerSymbol());
            stockCompanyName.setText(f.getName());
            totalPrice.setText("$"+String.valueOf(Double.parseDouble(f.getLastSale().replace("$", ""))*f.getShares()));
            shareQuantity.setText("Shares: "+String.valueOf(f.getShares()));
        }
//        companyTicker.setText(purchasedStocks.get(position).getTickerSymbol());
//        stockCompanyName.setText(purchasedStocks.get(position).getName());
//        totalPrice.setText("$"+String.valueOf(Double.parseDouble(purchasedStocks.get(position).getLastSale().replace("$", ""))*purchasedStocks.get(position).getShares()));
//        shareQuantity.setText(String.valueOf(purchasedStocks.get(position).getShares()));

        notifyDataSetChanged();
        return adapterLayout;
    }
}
