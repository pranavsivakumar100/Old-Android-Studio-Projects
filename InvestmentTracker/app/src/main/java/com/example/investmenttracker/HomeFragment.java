package com.example.investmenttracker;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private ArrayList<FinancialSecurity> purchasedStocks = new ArrayList<>();
    private static final String key_names = "names";
    private String totalWorth = "";
    private ArrayList<FinancialSecurity> allStocks = new ArrayList<>();
    private EditText inputTicker, inputShares;
    private Button addStock;
    private ListView portfolioLV;
    private PortfolioAdapter portfolioAdapter;
    private  Parcelable state;
    private TextView totalPortfolioVal;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        inputTicker = getActivity().findViewById(R.id.inputTicker);
        inputShares = getActivity().findViewById(R.id.inputShares);
        addStock = getActivity().findViewById(R.id.addStock);
        portfolioLV = getActivity().findViewById(R.id.portfolioLV);
        totalPortfolioVal = getActivity().findViewById(R.id.totalPortfolioVal);

        new FetchData().start();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    public class FetchData extends Thread {
        private URL url1;
        private URLConnection urlConnection1;
        private InputStream inputStream1;
        private BufferedReader bufferedReader1;
        private URL url2;
        private URLConnection urlConnection2;
        private InputStream inputStream2;
        private BufferedReader bufferedReader2;

        public void run() {
            Thread thread1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        url1 = new URL("https://raw.githubusercontent.com/rreichel3/US-Stock-Symbols/main/nasdaq/nasdaq_full_tickers.json");
                        urlConnection1 =  url1.openConnection();
                        inputStream1 = urlConnection1.getInputStream();
                        bufferedReader1 = new BufferedReader(new InputStreamReader(inputStream1));
                        url2 = new URL("https://raw.githubusercontent.com/rreichel3/US-Stock-Symbols/main/nyse/nyse_full_tickers.json");
                        urlConnection2 =  url2.openConnection();
                        inputStream2 = urlConnection2.getInputStream();
                        bufferedReader2 = new BufferedReader(new InputStreamReader(inputStream2));

                        StringBuffer stringBuffer1 = new StringBuffer();
                        StringBuffer stringBuffer2 = new StringBuffer();
                        String line1, line2;

                        while ((line1 = bufferedReader1.readLine()) != null) {
                            stringBuffer1.append(line1);
                            //jsonData1 = jsonData1 + line;
                        }
                        while ((line2 = bufferedReader2.readLine()) != null) {
                            stringBuffer2.append(line2);
                            //jsonData1 = jsonData1 + line;
                        }

                        if (!stringBuffer1.toString().isEmpty()) {
                            JSONArray jsonArray1 = new JSONArray(stringBuffer1.toString());
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

                                allStocks.add(new FinancialSecurity(symbol, name, lastSale, netChange,percentChange,volume,marketCap,country,ipoYear,industry,sector, 0));
                            }
                        }
                        if (!stringBuffer2.toString().isEmpty()) {
                            JSONArray jsonArray2 = new JSONArray(stringBuffer2.toString());
                            for (int i = 0; i < jsonArray2.length(); i++) {
                                JSONObject jsonObject1 = jsonArray2.getJSONObject(i);
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
                                allStocks.add(new FinancialSecurity(symbol, name, lastSale, netChange,percentChange,volume,marketCap,country,ipoYear,industry,sector, 0));
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadNote();
                            addStock.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Log.d("click", "clicked");
                                    String inputTickerStr = inputTicker.getText().toString();
                                    String inputSharesStr = inputShares.getText().toString();
                                    Log.d("inputTickerStr", inputTickerStr);
                                    Log.d("inputSharesStr", inputSharesStr);
                                    boolean isValid = false;
                                    for (FinancialSecurity f : allStocks) {
                                        if (inputTickerStr.equalsIgnoreCase(f.getTickerSymbol())) {
                                            isValid = true;
                                            f.setShareCount(Integer.parseInt(inputSharesStr));
                                            purchasedStocks.add(f);
                                            totalWorth = "$"+String.valueOf(Double.parseDouble(f.getLastSale().replace("$",""))*f.getShares());
                                            saveNote();
                                            Log.d("fff", totalWorth);
                                            totalPortfolioVal.setText(totalWorth);
                                            Log.d("purchasessize", purchasedStocks.size()+"");
                                            portfolioAdapter = new PortfolioAdapter(getContext(), R.layout.portfolio_adapter_layout, allStocks, purchasedStocks);
                                            state = portfolioLV.onSaveInstanceState();
                                            portfolioLV.onRestoreInstanceState(state);
                                            portfolioLV.setAdapter(portfolioAdapter);
                                            portfolioAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                            });

                        }
                    });


                }
            });
            thread1.start();
        }
    }

    public void saveNote() {
        Map<String, Object> note = new HashMap<>();
        note.put(key_names, totalWorth);
        db.collection("Finance").document("Investment").set(note);
    }

    public void loadNote() {
        db.collection("Finance").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                String totalWorth_ = String.valueOf(d.get("names"));
                                totalPortfolioVal.setText("Total Worth: " + totalWorth_);
                            }
                        }
                    }
                });
    }
}