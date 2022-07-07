package com.example.investmenttracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;


import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class StockInfoActivity extends AppCompatActivity {

    private ImageButton backArrow, favoriteStockButton;
    private Button oneDay, fiveDay, oneMonth, sixMonth;
    private TextView stockSymbol, stockName, stockPrice, percentChangeTV, dollarChange,
            volumeDisplay, marketCapDisplay, industryDisplay, sectorDisplay;
    //alpha vantage api key--> WS68118E77I6RWSK
    private String name, symbol, lastSale, netChange, percentChange,
            volume, marketCap, country, ipoYear, industry, sector;
    private LinkedHashMap<String, String> timeSeriesHash;
    private String apiKey;
    private SharedPreferences sharedPref;
    private boolean isStarChecked = false;
    private ArrayList<FinancialSecurity> favoriteStocks = new ArrayList<>();
    private String function;
    final String SHARED_PREF_API_KEY = "apiKey";
    ArrayList<CandleEntry> candleEntries = new ArrayList<>();
    private ArrayList<String> labels = new ArrayList<>();
    private CandleStickChart chart;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_info);

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        Bundle bundle = new Bundle();
//        HomeFragment homeFragment = new HomeFragment();
//        bundle.putString("message", "mymessage");
//        homeFragment.setArguments(bundle);
//        fragmentTransaction.replace(R.id.frameLayout, homeFragment).commit();

        backArrow = findViewById(R.id.backArrow);
        favoriteStockButton = findViewById(R.id.favoriteStockButton);
        oneDay = findViewById(R.id.oneDay);
        fiveDay = findViewById(R.id.fiveDay);
        oneMonth = findViewById(R.id.oneMonth);
        sixMonth = findViewById(R.id.sixMonth);
        stockSymbol = findViewById(R.id.stockSymbol);
        stockName = findViewById(R.id.stockName);
        stockPrice = findViewById(R.id.lastSale);
        percentChangeTV = findViewById(R.id.percentChange);
        dollarChange = findViewById(R.id.dollarChange);
        volumeDisplay = findViewById(R.id.volumeDisplay);
        marketCapDisplay = findViewById(R.id.marketCapDisplay);
        industryDisplay = findViewById(R.id.industryDisplay);
        sectorDisplay = findViewById(R.id.sectorDisplay);

        Intent intent = getIntent();
        name = intent.getExtras().getString("name");
        symbol = intent.getExtras().getString("symbol");
        lastSale = intent.getExtras().getString("lastSale");
        netChange = intent.getExtras().getString("netChange");
        percentChange = intent.getExtras().getString("percentChange");
        volume = intent.getExtras().getString("volume");
        marketCap = intent.getExtras().getString("marketCap");
        country = intent.getExtras().getString("country");
        ipoYear = intent.getExtras().getString("ipoYear");
        industry = intent.getExtras().getString("industry");
        sector = intent.getExtras().getString("sector");

        if (Double.parseDouble(percentChange.replace("%", "")) >= 0) {
            dollarChange.setTextColor(Color.rgb(0, 255, 0));
            percentChangeTV.setTextColor(Color.rgb(0, 255, 0));
            percentChangeTV.setBackgroundResource(R.drawable.green_rectangle);
            percentChangeTV.setText("+" + percentChange);
            dollarChange.setText("+" + netChange);
        } else if (Double.parseDouble(percentChange.replace("%", "")) < 0) {
            dollarChange.setTextColor(Color.rgb(255, 0, 0));
            dollarChange.setText(netChange);
            percentChangeTV.setTextColor(Color.rgb(255, 0, 0));
            percentChangeTV.setBackgroundResource(R.drawable.red_rectangle);
        }

        String formattedVolume = truncateNumber(Float.parseFloat(volume));
        String formattedMarketCap = truncateNumber(Float.parseFloat(marketCap));
        stockSymbol.setText(symbol);
        stockName.setText(name);
        stockPrice.setText(lastSale);
        percentChangeTV.setText(percentChange);
        volumeDisplay.setText(formattedVolume);
        marketCapDisplay.setText(formattedMarketCap);
        industryDisplay.setText(industry);
        sectorDisplay.setText(sector);

        sharedPref = getPreferences(Context.MODE_PRIVATE);
        apiKey = sharedPref.getString(SHARED_PREF_API_KEY, "WS68118E77I6RWSK");

        function = "function=TIME_SERIES_INTRADAY&interval=5min";
        url = "https://www.alphavantage.co/query?" + function + "&symbol="+ symbol + "&apikey=" + apiKey;

        chart = findViewById(R.id.chart);
        getData(url);


       oneDay.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               oneDay.setSelected(true);
               fiveDay.setSelected(false);
               fiveDay.setBackgroundResource(R.drawable.transparent_oval_button);
               fiveDay.setTextColor(Color.WHITE);
               oneMonth.setSelected(false);
               oneMonth.setBackgroundResource(R.drawable.transparent_oval_button);
               oneMonth.setTextColor(Color.WHITE);
               sixMonth.setSelected(false);
               sixMonth.setBackgroundResource(R.drawable.transparent_oval_button);
               sixMonth.setTextColor(Color.WHITE);
               oneDay.setBackgroundResource(R.drawable.white_oval_button);
               oneDay.setTextColor(Color.DKGRAY);
               function = "function=TIME_SERIES_INTRADAY&interval=5min";
               url = "https://www.alphavantage.co/query?" + function + "&symbol="+ symbol + "&apikey=" + apiKey;
               getData(url);
           }
       });

        fiveDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fiveDay.setSelected(true);
                oneDay.setSelected(false);
                oneDay.setBackgroundResource(R.drawable.transparent_oval_button);
                oneDay.setTextColor(Color.WHITE);
                oneMonth.setSelected(false);
                oneMonth.setBackgroundResource(R.drawable.transparent_oval_button);
                oneMonth.setTextColor(Color.WHITE);
                sixMonth.setSelected(false);
                sixMonth.setBackgroundResource(R.drawable.transparent_oval_button);
                sixMonth.setTextColor(Color.WHITE);
                fiveDay.setBackgroundResource(R.drawable.white_oval_button);
                fiveDay.setTextColor(Color.DKGRAY);
                function = "function=TIME_SERIES_INTRADAY&interval=30min";
                url = "https://www.alphavantage.co/query?" + function + "&symbol="+ symbol + "&apikey=" + apiKey;
                getData(url);
            }
        });

        oneMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneMonth.setSelected(true);
                oneDay.setSelected(false);
                oneDay.setBackgroundResource(R.drawable.transparent_oval_button);
                oneDay.setTextColor(Color.WHITE);
                fiveDay.setSelected(false);
                fiveDay.setBackgroundResource(R.drawable.transparent_oval_button);
                fiveDay.setTextColor(Color.WHITE);
                sixMonth.setSelected(false);
                sixMonth.setBackgroundResource(R.drawable.transparent_oval_button);
                sixMonth.setTextColor(Color.WHITE);
                oneMonth.setBackgroundResource(R.drawable.white_oval_button);
                oneMonth.setTextColor(Color.DKGRAY);
                function = "function=TIME_SERIES_DAILY";
                url = "https://www.alphavantage.co/query?" + function + "&symbol="+ symbol + "&apikey=" + apiKey;
                getData(url);
            }
        });


        sixMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sixMonth.setSelected(true);
                oneDay.setSelected(false);
                oneDay.setBackgroundResource(R.drawable.transparent_oval_button);
                oneDay.setTextColor(Color.WHITE);
                fiveDay.setSelected(false);
                fiveDay.setBackgroundResource(R.drawable.transparent_oval_button);
                fiveDay.setTextColor(Color.WHITE);
                oneMonth.setSelected(false);
                oneMonth.setBackgroundResource(R.drawable.transparent_oval_button);
                oneMonth.setTextColor(Color.WHITE);
                sixMonth.setBackgroundResource(R.drawable.white_oval_button);
                sixMonth.setTextColor(Color.DKGRAY);
                function = "function=TIME_SERIES_WEEKLY";
                url = "https://www.alphavantage.co/query?" + function + "&symbol="+ symbol + "&apikey=" + apiKey;
                getData(url);
            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
//        Bundle bundle = new Bundle();
//        bundle.putString("message", "mymessage");
//        HomeFragment fragment = new HomeFragment();
//        fragment.setArguments(bundle);
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.frameLayout1, new HomeFragment())
//                .commit();
        favoriteStockButton.setOnClickListener(new View.OnClickListener() {
            int count = 0;
            @Override
            public void onClick(View view) {
                count++;
                if (count % 2 != 0) {
                    isStarChecked = true;
                    favoriteStockButton.setImageResource(R.drawable.ic_baseline_star_24);
                } else {
                    isStarChecked = false;
                    favoriteStockButton.setImageResource(R.drawable.ic_baseline_star_border_24);
                }

            }
        });

    }


    private void getData(String url) {

        chart.setNoDataText("Fetching data");

        // instantiate and initialize volley requestQueue
        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        // instantiate jsonObjectRequest
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("url", url);
                // extract timeSeries part of the JSON
                JSONObject timeSeries = getTimeSeries(response);

                // create chart
                showChart(timeSeries);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("getData", "Something went wrong: "+ error.getMessage());
                Toast.makeText(getApplicationContext(), "Unable to perform request", Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(jsonObjectRequest);

    }


    private void showChart(JSONObject timeSeries) {

        // get all the keys
        Iterator<String> iterator = timeSeries.keys();

        // iterate through all the keys and store them in the temp ArrayList
        ArrayList<String> temp = new ArrayList<>();

        while (iterator.hasNext()) {
            String key = iterator.next();
            temp.add(key);
        }

        // reverse array order
        for (int i = temp.size() - 1; i >= 0; i--) {
            labels.add(temp.get(i));
        }

        // populate candleEntries
        int i = 0;
        for (String label : labels) {
            try {
                JSONObject entryJSON = timeSeries.getJSONObject(label);
                addEntry(entryJSON, i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("entered ", String.valueOf(i));
            i++;
        }

        // initialize candleDataSet and pass candleEntries
        CandleDataSet dataSet = new CandleDataSet(candleEntries, "stock");


        // instantiate CandleData passing dataSet
        CandleData candleData = new CandleData(dataSet);

        // set labels
        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new XAxisFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(1f);
        xAxis.setLabelRotationAngle(90f);


        // set colors
        dataSet.setDrawIcons(false);
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet.setShadowColor(Color.DKGRAY);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setShadowWidth(0.7f);
        dataSet.setDecreasingColor(Color.RED);
        dataSet.setDecreasingPaintStyle(Paint.Style.FILL);
        dataSet.setIncreasingColor(Color.rgb(122, 242, 84));
        dataSet.setIncreasingPaintStyle(Paint.Style.FILL);
        dataSet.setNeutralColor(Color.BLUE);

        chart.setDescription(null);

        chart.setData(candleData);
        chart.invalidate();


    }


    private void addEntry(JSONObject entryJSON, int i) {
        // get open
        float open = 0;
        try {
            open = Float.valueOf(entryJSON.getString("1. open"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // get high
        float high = 0;
        try {
            high = Float.valueOf(entryJSON.getString("2. high"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // get low
        float low = 0;
        try {
            low = Float.valueOf(entryJSON.getString("3. low"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // get close
        float close = 0;
        try {
            close = Float.valueOf(entryJSON.getString("4. close"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        candleEntries.add(i, new CandleEntry((float) i, high, low, open, close));

    }

    private JSONObject getTimeSeries(JSONObject jsonObject) {

        String keys[] = new String[2];


        // list available keys
        Iterator<?> iterator = jsonObject.keys();
        int i = 0;
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            keys[i] = key;
            i++;
        }

        // extract Meta Data from jsonObject
        String metadata = "";
        try {
            metadata = jsonObject.getString("Meta Data");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject timeSeries = null;
        try {
            timeSeries = jsonObject.getJSONObject((keys[1]));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //showChart(timeSeries);
        return timeSeries;

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // get fullscreen
        hide();
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // sets chart view to fullscreen
        findViewById(R.id.chart).setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    public String truncateNumber(float floatNumber) {
        long million = 1000000L;
        long billion = 1000000000L;
        long trillion = 1000000000000L;
        long number = Math.round(floatNumber);
        if ((number >= million) && (number < billion)) {
            float fraction = calculateFraction(number, million);
            return Float.toString(fraction) + " M";
        } else if ((number >= billion) && (number < trillion)) {
            float fraction = calculateFraction(number, billion);
            return Float.toString(fraction) + " B";
        }
        return Long.toString(number);
    }

    public float calculateFraction(long number, long divisor) {
        long truncate = (number * 10L + (divisor / 2L)) / divisor;
        float fraction = (float) truncate * 0.10F;
        return fraction;
    }
//    name = intent.getExtras().getString("name");
//    symbol = intent.getExtras().getString("symbol");
//    lastSale = intent.getExtras().getString("lastSale");
//    netChange = intent.getExtras().getString("netChange");
//    percentChange = intent.getExtras().getString("percentChange");
//    volume = intent.getExtras().getString("volume");
//    marketCap = intent.getExtras().getString("marketCap");
//    country = intent.getExtras().getString("country");
//    ipoYear = intent.getExtras().getString("ipoYear");
//    industry = intent.getExtras().getString("industry");
//    sector = intent.getExtras().getString("sector");

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getLastSale() {
        return lastSale;
    }

    public String getNetChange() {
        return netChange;
    }

    public String getPercentChange() {
        return percentChange;
    }

    public String getVolume() {
        return volume;
    }

    public String getMarketCap() {
        return marketCap;
    }

    public String getCountry() {
        return country;
    }

    public String getIpoYear() {
        return ipoYear;
    }

    public String getIndustry() {
        return industry;
    }

    public String getSector() {
        return sector;
    }

    public boolean getIsStarChecked() {
        return isStarChecked;
    }
}