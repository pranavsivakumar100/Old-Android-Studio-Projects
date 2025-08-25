package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherapp.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    private EditText enterZipCode;
    private Button displayWeather;
    ActivityMainBinding binding;
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appId = ""; // Include your own
    Handler mainHandler = new Handler();
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        enterZipCode = findViewById(R.id.enterZipCode);
        displayWeather = findViewById(R.id.displayWeather);

        displayWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread1 = new Thread(new Runnable() {
                    public void run() {
                        FetchData fetchData = new FetchData(enterZipCode.getText().toString());
                        fetchData.run();
                    }
                });
                thread1.start();
            }
        });
    }

    public class FetchData extends Thread {
        private TextView latitudeTV, longitudeTV, cityNameTV;
        private TextView timeTV1, tempTV1, descriptionTV1, timeTV2, tempTV2,
                descriptionTV2, timeTV3, tempTV3, descriptionTV3, timeTV4, tempTV4, descriptionTV4;
        private ImageView imageView1, imageView2, imageView3, imageView4;
        private View divider;
        private RelativeLayout relativeLayout;
        // Include your own
        private final String appId = "";
        private String zipCode;
        private String jsonData1 = "", jsonData2 = "";

        public FetchData(String zipCode) {
            this.zipCode = zipCode;
        }

        public void run() {
            latitudeTV = findViewById(R.id.latitudeTV);
            longitudeTV = findViewById(R.id.longitudeTV);
            cityNameTV = findViewById(R.id.cityNameTV);
            relativeLayout = findViewById(R.id.relativeLayout);
            divider = findViewById(R.id.divider);
            timeTV1 = findViewById(R.id.timeTV1);
            tempTV1 = findViewById(R.id.tempTV1);
            descriptionTV1 = findViewById(R.id.descriptionTV1);
            timeTV2 = findViewById(R.id.timeTV2);
            tempTV2 = findViewById(R.id.tempTV2);
            descriptionTV2 = findViewById(R.id.descriptionTV2);
            timeTV3 = findViewById(R.id.timeTV3);
            tempTV3 = findViewById(R.id.tempTV3);
            descriptionTV3 = findViewById(R.id.descriptionTV3);
            timeTV4 = findViewById(R.id.timeTV4);
            tempTV4 = findViewById(R.id.tempTV4);
            descriptionTV4 = findViewById(R.id.descriptionTV4);
            imageView1 = findViewById(R.id.imageView1);
            imageView2 = findViewById(R.id.imageView2);
            imageView3 = findViewById(R.id.imageView3);
            imageView4 = findViewById(R.id.imageView4);

            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMessage("Retrieving Data");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    if (progressDialog == null) {
                        progressDialog.dismiss();
                    }
                }
            });

            try {
                URL url1 = new URL("https://api.openweathermap.org/geo/1.0/zip?zip=" + zipCode + ",us&appid=" + appId);
                HttpURLConnection httpURLConnection1 = (HttpURLConnection) url1.openConnection();
                InputStream inputStream1 = httpURLConnection1.getInputStream();
                BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(inputStream1));
                String line;
                String lat = "", lon = "";
                while ((line = bufferedReader1.readLine()) != null) {
                    jsonData1 = jsonData1 + line;
                }

                if (!jsonData1.isEmpty()) {
                    JSONObject jsonObject = new JSONObject(jsonData1);
                    String cityName = jsonObject.getString("name");
                    String latitude = jsonObject.getString("lat");
                    String longitude = jsonObject.getString("lon");
                    lat = latitude;
                    lon = longitude;
                    Log.d("coordinates", "Latitude: " + latitude + " Longitude: " + latitude);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            latitudeTV.setText("Latitude: " + latitude.toString());
                            longitudeTV.setText("Longitude: " + longitude.toString());
                            cityNameTV.setText(cityName);
                        }
                    });
                }

                URL url2 = new URL("https://api.openweathermap.org/data/2.5/onecall?lat=" + lat + "&lon="
                        + lon + "&exclude=minutely,daily&appid=" + appId);
                HttpURLConnection httpURLConnection2 = (HttpURLConnection) url2.openConnection();
                InputStream inputStream2 = httpURLConnection2.getInputStream();
                BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(inputStream2));
                String read;
                while ((read = bufferedReader2.readLine()) != null) {
                    jsonData2 = jsonData2 + read;
                }

                if (!jsonData2.isEmpty()) {
                    JSONObject jsonObject = new JSONObject(jsonData2);
                    JSONArray hourlyTime = jsonObject.getJSONArray("hourly");
                    String time1 = hourlyTime.getJSONObject(0).getString("dt"),
                            time2 = hourlyTime.getJSONObject(1).getString("dt"),
                            time3 = hourlyTime.getJSONObject(2).getString("dt"),
                            time4 = hourlyTime.getJSONObject(3).getString("dt");
                    String temp1 = hourlyTime.getJSONObject(0).getString("temp"),
                            temp2 = hourlyTime.getJSONObject(1).getString("temp"),
                            temp3 = hourlyTime.getJSONObject(2).getString("temp"),
                            temp4 = hourlyTime.getJSONObject(3).getString("temp");
                    String weatherDescription1 = "", weatherDescription2 = "", weatherDescription3 = "", weatherDescription4 = "";
                    ArrayList<String> weatherArray = new ArrayList<String>();
                    time1 = formatTime(Integer.parseInt(time1));
                    time2 = formatTime(Integer.parseInt(time2));
                    time3 = formatTime(Integer.parseInt(time3));
                    time4 = formatTime(Integer.parseInt(time4));
                    temp1 = convertTemperature(Double.parseDouble(temp1));
                    temp2 = convertTemperature(Double.parseDouble(temp2));
                    temp3 = convertTemperature(Double.parseDouble(temp3));
                    temp4 = convertTemperature(Double.parseDouble(temp4));

                    for (int i = 0; i < jsonObject.length(); i++) {
                        JSONObject jsonObject1 = hourlyTime.getJSONObject(i);
                        JSONArray weatherJSONArray = jsonObject1.getJSONArray("weather");
                        for (int j = 0; j < weatherJSONArray.length(); j++) {
                            JSONObject jsonObject2 = weatherJSONArray.getJSONObject(j);
                            weatherArray.add(jsonObject2.getString("description"));
                            if (i == 0) {
                                weatherDescription1 = weatherArray.get(i);
                                descriptionTV1.setText(weatherDescription1);
                                switch (weatherDescription1) {
                                    case "clear sky":
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView1.setVisibility(View.VISIBLE);
                                                imageView1.setImageResource(R.drawable.clear);
                                            }
                                        });

                                        break;
                                    case "light rain":
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView1.setVisibility(View.VISIBLE);
                                                imageView1.setImageResource(R.drawable.rainy);
                                            }
                                        });
                                        break;
                                    case "moderate rain":
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView1.setVisibility(View.VISIBLE);
                                                imageView1.setImageResource(R.drawable.rainy);
                                            }
                                        });
                                        break;
                                    case "heavy intensity rain":
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView1.setVisibility(View.VISIBLE);
                                                imageView1.setImageResource(R.drawable.rainy);
                                            }
                                        });
                                        break;
                                    case "scattered clouds":
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView1.setVisibility(View.VISIBLE);
                                                imageView1.setImageResource(R.drawable.cloudy);
                                            }
                                        });
                                        break;
                                    case "broken clouds":
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView1.setVisibility(View.VISIBLE);
                                                imageView1.setImageResource(R.drawable.cloudy);
                                            }
                                        });
                                        break;
                                    case "overcast clouds":
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView1.setVisibility(View.VISIBLE);
                                                imageView1.setImageResource(R.drawable.cloudy);
                                            }
                                        });
                                        break;
                                    case "few clouds":
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView1.setVisibility(View.VISIBLE);
                                                imageView1.setImageResource(R.drawable.cloudy);
                                            }
                                        });
                                        break;
                                    case "light snow":
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView1.setVisibility(View.VISIBLE);
                                                imageView1.setImageResource(R.drawable.snowing);
                                            }
                                        });
                                        break;
                                    case "snow":
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView1.setVisibility(View.VISIBLE);
                                                imageView1.setImageResource(R.drawable.snowing);
                                            }
                                        });
                                        break;
                                    case "heavy intensity snow":
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView1.setVisibility(View.VISIBLE);
                                                imageView1.setImageResource(R.drawable.snowing);
                                            }
                                        });
                                        break;
                                }
                            } else if (i == 1) {
                                weatherDescription2 = weatherArray.get(i);
                                descriptionTV2.setText(weatherDescription2);
                                switch (weatherDescription2) {
                                    case "clear sky":
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView2.setVisibility(View.VISIBLE);
                                                imageView2.setImageResource(R.drawable.clear);
                                            }
                                        });

                                        break;
                                    case "light rain":
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView2.setVisibility(View.VISIBLE);
                                                imageView2.setImageResource(R.drawable.rainy);
                                            }
                                        });
                                        break;
                                    case "moderate rain":
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView2.setVisibility(View.VISIBLE);
                                                imageView2.setImageResource(R.drawable.rainy);
                                            }
                                        });
                                        break;
                                    case "heavy intensity rain":
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView2.setVisibility(View.VISIBLE);
                                                imageView2.setImageResource(R.drawable.rainy);
                                            }
                                        });
                                        break;
                                    case "scattered clouds":
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView2.setVisibility(View.VISIBLE);
                                                imageView2.setImageResource(R.drawable.cloudy);
                                            }
                                        });
                                        break;
                                    case "broken clouds":
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView2.setVisibility(View.VISIBLE);
                                                imageView2.setImageResource(R.drawable.cloudy);
                                            }
                                        });
                                        break;
                                    case "overcast clouds":
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView2.setVisibility(View.VISIBLE);
                                                imageView2.setImageResource(R.drawable.cloudy);
                                            }
                                        });
                                        break;
                                    case "few clouds":
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView2.setVisibility(View.VISIBLE);
                                                imageView2.setImageResource(R.drawable.cloudy);
                                            }
                                        });
                                        break;
                                    case "light snow":
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView2.setVisibility(View.VISIBLE);
                                                imageView2.setImageResource(R.drawable.snowing);
                                            }
                                        });
                                        break;
                                    case "snow":
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView2.setVisibility(View.VISIBLE);
                                                imageView2.setImageResource(R.drawable.snowing);
                                            }
                                        });
                                        break;
                                    case "heavy intensity snow":
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView2.setVisibility(View.VISIBLE);
                                                imageView2.setImageResource(R.drawable.snowing);
                                            }
                                        });
                                        break;
                                }
                            } else if (i == 2) {
                                weatherDescription3 = weatherArray.get(i);
                                descriptionTV3.setText(weatherDescription3);
                                switch (weatherDescription3) {
                                    case "clear sky":
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView3.setVisibility(View.VISIBLE);
                                                imageView3.setImageResource(R.drawable.clear);
                                            }
                                        });

                                        break;
                                    case "light rain":
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView3.setVisibility(View.VISIBLE);
                                                imageView3.setImageResource(R.drawable.rainy);
                                            }
                                        });
                                        break;
                                    case "moderate rain":
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView3.setVisibility(View.VISIBLE);
                                                imageView3.setImageResource(R.drawable.rainy);
                                            }
                                        });
                                        break;
                                    case "heavy intensity rain":
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView3.setVisibility(View.VISIBLE);
                                                imageView3.setImageResource(R.drawable.rainy);
                                            }
                                        });
                                        break;
                                    case "scattered clouds":
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView3.setVisibility(View.VISIBLE);
                                                imageView3.setImageResource(R.drawable.cloudy);
                                            }
                                        });
                                        break;
                                    case "broken clouds":
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView3.setVisibility(View.VISIBLE);
                                                imageView3.setImageResource(R.drawable.cloudy);
                                            }
                                        });
                                        break;
                                    case "overcast clouds":
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView3.setVisibility(View.VISIBLE);
                                                imageView3.setImageResource(R.drawable.cloudy);
                                            }
                                        });
                                        break;
                                    case "few clouds":
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView3.setVisibility(View.VISIBLE);
                                                imageView3.setImageResource(R.drawable.cloudy);
                                            }
                                        });
                                        break;
                                    case "light snow":
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView3.setVisibility(View.VISIBLE);
                                                imageView3.setImageResource(R.drawable.snowing);
                                            }
                                        });
                                        break;
                                    case "snow":
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView3.setVisibility(View.VISIBLE);
                                                imageView3.setImageResource(R.drawable.snowing);
                                            }
                                        });
                                        break;
                                    case "heavy intensity snow":
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView3.setVisibility(View.VISIBLE);
                                                imageView3.setImageResource(R.drawable.snowing);
                                            }
                                        });
                                        break;
                                }
                            } else if (i == 3) {
                                weatherDescription4 = weatherArray.get(i);
                                descriptionTV4.setText(weatherDescription4);
                                switch (weatherDescription4) {
                                    case "clear sky":
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView4.setVisibility(View.VISIBLE);
                                                imageView4.setImageResource(R.drawable.clear);
                                            }
                                        });

                                        break;
                                    case "light rain":
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView4.setVisibility(View.VISIBLE);
                                                imageView4.setImageResource(R.drawable.rainy);
                                            }
                                        });
                                        break;
                                    case "moderate rain":
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView4.setVisibility(View.VISIBLE);
                                                imageView4.setImageResource(R.drawable.rainy);
                                            }
                                        });
                                        break;
                                    case "heavy intensity rain":
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView4.setVisibility(View.VISIBLE);
                                                imageView4.setImageResource(R.drawable.rainy);
                                            }
                                        });
                                        break;
                                    case "scattered clouds":
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView4.setVisibility(View.VISIBLE);
                                                imageView4.setImageResource(R.drawable.cloudy);
                                            }
                                        });
                                        break;
                                    case "broken clouds":
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView4.setVisibility(View.VISIBLE);
                                                imageView4.setImageResource(R.drawable.cloudy);
                                            }
                                        });
                                        break;
                                    case "overcast clouds":
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView4.setVisibility(View.VISIBLE);
                                                imageView4.setImageResource(R.drawable.cloudy);
                                            }
                                        });
                                        break;
                                    case "few clouds":
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView4.setVisibility(View.VISIBLE);
                                                imageView4.setImageResource(R.drawable.cloudy);
                                            }
                                        });
                                        break;
                                    case "light snow":
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView4.setVisibility(View.VISIBLE);
                                                imageView4.setImageResource(R.drawable.snowing);
                                            }
                                        });
                                        break;
                                    case "snow":
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView4.setVisibility(View.VISIBLE);
                                                imageView4.setImageResource(R.drawable.snowing);
                                            }
                                        });
                                        break;
                                    case "heavy intensity snow":
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView4.setVisibility(View.VISIBLE);
                                                imageView4.setImageResource(R.drawable.snowing);
                                            }
                                        });
                                        break;
                                }
                            }
                        }
                    }
                    timeTV1.setText("Time: " + time1);
                    tempTV1.setText("Temperature: " + temp1);
                    timeTV2.setText("Time: " + time2);
                    tempTV2.setText("Temperature: " + temp2);
                    timeTV3.setText("Time: " + time3);
                    tempTV3.setText("Temperature: " + temp3);
                    timeTV4.setText("Time: " + time4);
                    tempTV4.setText("Temperature: " + temp4);

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            });
        }

        /**
         * Convert epoch time to EST
         * @param timeStamp integer time stamp
         * @return converted time
         */
        public String formatTime(int timeStamp) {
            java.util.Date date = new java.util.Date((long)timeStamp*1000);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm aa");
            simpleDateFormat.setTimeZone(TimeZone.getDefault());
            String time = simpleDateFormat.format(date);
            return time;
        }

        /**
         * Convert temperature to fahrenheit
         * @param temp temperature in Kelvin
         * @return converted temperature
         */
        public String convertTemperature(double temp) {
            double convertTemp = ((temp - 273.15) * 1.8) + 32;
            DecimalFormat decimalFormat = new DecimalFormat("##.00");
            String convertedTemp = decimalFormat.format(convertTemp);
            return convertedTemp + "°";
        }
    }
}


