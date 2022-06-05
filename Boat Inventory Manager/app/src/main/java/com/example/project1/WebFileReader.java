package com.example.project1;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WebFileReader extends AsyncTask<String, Void, List<String>> {
    private OnWebFileReadListener listener;
    private Exception exception;

    public interface OnWebFileReadListener {
        void onWebFileRead(List<String> lines);
        void onWebFileError(Exception e);
    }

    public WebFileReader(OnWebFileReadListener listener) {
        this.listener = listener;
    }

    @Override
    protected List<String> doInBackground(String... urls) {
        List<String> lines = new ArrayList<>();
        try {
            URL url = new URL(urls[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
                reader.close();
            } else {
                throw new IOException("HTTP error code: " + responseCode);
            }
            connection.disconnect();
        } catch (Exception e) {
            exception = e;
            return null;
        }
        return lines;
    }

    @Override
    protected void onPostExecute(List<String> lines) {
        if (exception != null) {
            listener.onWebFileError(exception);
        } else {
            listener.onWebFileRead(lines);
        }
    }
}