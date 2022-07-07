package com.example.investmenttracker;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class AnalyticsFragment extends Fragment {

    private ListView mostActiveStocksLV, topGainersLV, topLosersLV;
    private ArrayList<FinancialSecurity> nasdaqStocksList = new ArrayList<>();
    private ArrayList<FinancialSecurity> nyseStocksList = new ArrayList<>();
    private ArrayList<FinancialSecurity> allStocks = new ArrayList<>();
    private ArrayList<FinancialSecurity> mostActiveStocksList = new ArrayList<>();
    private ArrayList<FinancialSecurity> topGainers = new ArrayList<>();
    private ArrayList<FinancialSecurity> topLosers = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> priceValues;
    private CustomAdapter1 customAdapter1;
    private CustomAdapter2 customAdapter2;
    private CustomAdapter3 customAdapter3;
    private RequestQueue requestQueue;
    private Parcelable state1, state2, state3;
    private String NASDAQ_URL = "https://raw.githubusercontent.com/rreichel3/US-Stock-Symbols/main/nasdaq/nasdaq_full_tickers.json";
    private String jsonData1 = "";
    private double highestPrice = 0;
    private URL url1;
    private URLConnection urlConnection1;
    private InputStream inputStream1;
    private BufferedReader bufferedReader1;
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
//        listView = getActivity().findViewById(R.id.listview);
//        arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, name);
//        listView.setAdapter(arrayAdapter);
//    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        mostActiveStocksLV = getActivity().findViewById(R.id.listview1);
        topGainersLV = getActivity().findViewById(R.id.listview2);
        topLosersLV = getActivity().findViewById(R.id.listview3);
        priceValues = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getContext());
        new FetchData().start();



        mostActiveStocksLV.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });

        topGainersLV.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });

        topLosersLV.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_analytics, container, false);
    }


    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);
        MenuItem menuItem = menu.findItem(R.id.searchButton);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type here to search");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                arrayAdapter.getFilter().filter(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
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

        @Override
        public void run() {
            String NASDAQ_URL = "https://raw.githubusercontent.com/rreichel3/US-Stock-Symbols/main/nasdaq/nasdaq_full_tickers.json";

//            StringRequest stringRequest = new StringRequest(Request.Method.GET, NASDAQ_URL,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            try {
//                                JSONArray jsonArray1 = new JSONArray(response);
//                                for (int i = 0; i < jsonArray1.length(); i++) {
//                                    JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
////                                        String symbol = jsonObject1.getString("ticker");
////                                        String name = jsonObject1.getString("name");
////                                        String exchange = jsonObject1.getString("exchange");
//                                    String symbol = jsonObject1.getString("symbol");
//                                    String name = jsonObject1.getString("name");
//                                    String lastSale = jsonObject1.getString("lastsale");
//                                    String netChange = jsonObject1.getString("netchange");
//                                    String percentChange = jsonObject1.getString("pctchange");
//                                    String volume = jsonObject1.getString("volume");
//                                    String marketCap = jsonObject1.getString("marketCap");
//                                    String country = jsonObject1.getString("country");
//                                    String ipoYear = jsonObject1.getString("ipoyear");
//                                    String industry = jsonObject1.getString("industry");
//                                    String sector = jsonObject1.getString("sector");
//                                    //financialSecurities.add(new FinancialSecurity(symbol, name, "", "","","","","","","",""));
//                                    nasdaqStocksList.add(new FinancialSecurity(symbol, name, lastSale, netChange,percentChange,volume,marketCap,country,ipoYear,industry,sector));
//                                    //Log.d("symbol", symbol);
//                                    //Log.d("name", name);
//
//
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    error.printStackTrace();
//                }
//            });
//            requestQueue.add(stringRequest);

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
                                nasdaqStocksList.add(new FinancialSecurity(symbol, name, lastSale, netChange,percentChange,volume,marketCap,country,ipoYear,industry,sector, 0));
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
                                nyseStocksList.add(new FinancialSecurity(symbol, name, lastSale, netChange,percentChange,volume,marketCap,country,ipoYear,industry,sector, 0));
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            customAdapter1 = new CustomAdapter1(getContext(), R.layout.adapter_layout, allStocks, mostActiveStocksList);
                            state1 = mostActiveStocksLV.onSaveInstanceState();
                            mostActiveStocksLV.onRestoreInstanceState(state1);
                            mostActiveStocksLV.setAdapter(customAdapter1);
                            customAdapter1.notifyDataSetChanged();
                            mostActiveStocksList = customAdapter1.getMostActiveStocksList();

                            customAdapter2 = new CustomAdapter2(getContext(), R.layout.adapter_layout, allStocks, topGainers);
                            state2 = topGainersLV.onSaveInstanceState();
                            topGainersLV.onRestoreInstanceState(state2);
                            topGainersLV.setAdapter(customAdapter2);
                            customAdapter2.notifyDataSetChanged();

                            customAdapter3 = new CustomAdapter3(getContext(), R.layout.adapter_layout, allStocks, topLosers);
                            state3 = topLosersLV.onSaveInstanceState();
                            topLosersLV.onRestoreInstanceState(state3);
                            topLosersLV.setAdapter(customAdapter3);
                            customAdapter3.notifyDataSetChanged();
                        }
                    });

                    mostActiveStocksLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            state1 = mostActiveStocksLV.onSaveInstanceState();
                            mostActiveStocksLV.onRestoreInstanceState(state1);

                            String tickerSymbol = ((TextView) view.findViewById(R.id.tickerSymbol)).getText().toString();
                            String name = "";
                            String lastSale = "";
                            String netChange = "";
                            String percentChange = "";
                            String volume = "";
                            String marketCap = "";
                            String country = "";
                            String ipoYear = "";
                            String industry = "";
                            String sector = "";

                            for (FinancialSecurity f : allStocks) {
                                if (f.getTickerSymbol().equals(tickerSymbol)) {
                                    name = f.getName();
                                    lastSale = f.getLastSale();
                                    netChange = f.getNetChange();
                                    percentChange = f.getPercentChange();
                                    volume = f.getVolume();
                                    marketCap = f.getMarketCap();
                                    country = f.getCountry();
                                    ipoYear = f.getIpoYear();
                                    industry = f.getIndustry();
                                    sector = f.getSector();
                                }
                            }
                            Intent intent = new Intent(getContext(), StockInfoActivity.class);
                            intent.putExtra("name", name);
                            intent.putExtra("symbol", tickerSymbol);
                            intent.putExtra("lastSale", lastSale);
                            intent.putExtra("netChange", netChange);
                            intent.putExtra("percentChange", percentChange);
                            intent.putExtra("volume", volume);
                            intent.putExtra("marketCap", marketCap);
                            intent.putExtra("country", country);
                            intent.putExtra("ipoYear", ipoYear);
                            intent.putExtra("industry", industry);
                            intent.putExtra("sector", sector);
                            startActivity(intent);
                        }
                    });

                    topGainersLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            state2 = topGainersLV.onSaveInstanceState();
                            topGainersLV.onRestoreInstanceState(state2);

                            String tickerSymbol = ((TextView) view.findViewById(R.id.tickerSymbol)).getText().toString();
                            String name = "";
                            String lastSale = "";
                            String netChange = "";
                            String percentChange = "";
                            String volume = "";
                            String marketCap = "";
                            String country = "";
                            String ipoYear = "";
                            String industry = "";
                            String sector = "";

                            for (FinancialSecurity f : allStocks) {
                                if (f.getTickerSymbol().equals(tickerSymbol)) {
                                    name = f.getName();
                                    lastSale = f.getLastSale();
                                    netChange = f.getNetChange();
                                    percentChange = f.getPercentChange();
                                    volume = f.getVolume();
                                    marketCap = f.getMarketCap();
                                    country = f.getCountry();
                                    ipoYear = f.getIpoYear();
                                    industry = f.getIndustry();
                                    sector = f.getSector();
                                }
                            }
                            Intent intent = new Intent(getContext(), StockInfoActivity.class);
                            intent.putExtra("name", name);
                            intent.putExtra("symbol", tickerSymbol);
                            intent.putExtra("lastSale", lastSale);
                            intent.putExtra("netChange", netChange);
                            intent.putExtra("percentChange", percentChange);
                            intent.putExtra("volume", volume);
                            intent.putExtra("marketCap", marketCap);
                            intent.putExtra("country", country);
                            intent.putExtra("ipoYear", ipoYear);
                            intent.putExtra("industry", industry);
                            intent.putExtra("sector", sector);
                            startActivity(intent);
                        }
                    });

                    topLosersLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            state3 = topLosersLV.onSaveInstanceState();
                            topLosersLV.onRestoreInstanceState(state3);

                            String tickerSymbol = ((TextView) view.findViewById(R.id.tickerSymbol)).getText().toString();
                            String name = "";
                            String lastSale = "";
                            String netChange = "";
                            String percentChange = "";
                            String volume = "";
                            String marketCap = "";
                            String country = "";
                            String ipoYear = "";
                            String industry = "";
                            String sector = "";

                            for (FinancialSecurity f : allStocks) {
                                if (f.getTickerSymbol().equals(tickerSymbol)) {
                                    name = f.getName();
                                    lastSale = f.getLastSale();
                                    netChange = f.getNetChange();
                                    percentChange = f.getPercentChange();
                                    volume = f.getVolume();
                                    marketCap = f.getMarketCap();
                                    country = f.getCountry();
                                    ipoYear = f.getIpoYear();
                                    industry = f.getIndustry();
                                    sector = f.getSector();
                                }
                            }
                            Intent intent = new Intent(getContext(), StockInfoActivity.class);
                            intent.putExtra("name", name);
                            intent.putExtra("symbol", tickerSymbol);
                            intent.putExtra("lastSale", lastSale);
                            intent.putExtra("netChange", netChange);
                            intent.putExtra("percentChange", percentChange);
                            intent.putExtra("volume", volume);
                            intent.putExtra("marketCap", marketCap);
                            intent.putExtra("country", country);
                            intent.putExtra("ipoYear", ipoYear);
                            intent.putExtra("industry", industry);
                            intent.putExtra("sector", sector);
                            startActivity(intent);
                        }
                    });



                }
            });
            thread1.start();
        }
    }
//    @Override
//    public void onSaveInstanceState(Bundle state) {
//        super.onSaveInstanceState(state);
//        state.putParcelableArrayList("mostActiveStocks", (ArrayList<FinancialSecurity>) state.getParcelableArrayList("mostActiveStocks"));
//        state.putSerializable("topGainers", topGainers);
//        state.putSerializable("topLosers", topLosers);
//    }
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        this.mostActiveStocksList.clear();
//        this.mostActiveStocksList.addAll((ArrayList<FinancialSecurity>) savedInstanceState.getSerializable("mostActiveStocksList"));
//        this.customAdapter1.notifyDataSetChanged();
//
//        this.topGainers.clear();
//        this.topGainers.addAll((ArrayList<FinancialSecurity>) savedInstanceState.getSerializable("topGainers"));
//        this.customAdapter2.notifyDataSetChanged();
//
//        this.topLosers.clear();
//        this.topLosers.addAll((ArrayList<FinancialSecurity>) savedInstanceState.getSerializable("topLosers"));
//        this.customAdapter3.notifyDataSetChanged();
//    }

}





























