package com.example.investmenttracker;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class FetchData2 extends Thread {
    private URL url1;
    private URLConnection urlConnection1;
    private InputStream inputStream1;
    private BufferedReader bufferedReader1;
    private ArrayList<FinancialSecurity> stocksList;

    public FetchData2(ArrayList<FinancialSecurity> stocksList) {
        this.stocksList = stocksList;
    }

    @Override
    public void run() {
        try {
            String jsonData1 = "";
            url1 = new URL("https://raw.githubusercontent.com/rreichel3/US-Stock-Symbols/main/nasdaq/nasdaq_full_tickers.json");
            urlConnection1 =  url1.openConnection();
            inputStream1 = urlConnection1.getInputStream();
            bufferedReader1 = new BufferedReader(new InputStreamReader(inputStream1));
            StringBuffer stringBuffer = new StringBuffer();
            String line;

            while ((line = bufferedReader1.readLine()) != null) {
                stringBuffer.append(line);
                //jsonData1 = jsonData1 + line;
            }
            Log.d("message", "messageeee");
            if (!stringBuffer.toString().isEmpty()) {
                JSONArray jsonArray1 = new JSONArray(stringBuffer.toString());
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                    String symbol = jsonObject1.getString("symbol");
                    String name = jsonObject1.getString("name");
                    String lastSale = jsonObject1.getString("lastsale");
                    String netChange = jsonObject1.getString("netchange");
                    String percentChange = jsonObject1.getString("pctchange");
                    String volume = jsonObject1.getString("volume");
                    String marketCap = jsonObject1.getString("marketCap");
                    String country = jsonObject1.getString("country");
                    String ipoYear = jsonObject1.getString("ipoyear");
                    String industry = jsonObject1.getString("industry");
                    String sector = jsonObject1.getString("sector");
                    stocksList.add(new FinancialSecurity(symbol, name, lastSale, netChange,percentChange,volume,marketCap,country,ipoYear,industry,sector,0));
                    Log.d("symbol", symbol);
                    Log.d("name", name);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

