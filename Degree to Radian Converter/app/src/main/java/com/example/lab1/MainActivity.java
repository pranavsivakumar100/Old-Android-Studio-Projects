package com.example.lab1;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar(findViewById(R.id.toolbar));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            Toast.makeText(this, "Lab Assignment #1 - Degree to Radian Converter", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_clear) {
            EditText editTextInFileName = findViewById(R.id.edit_infile);
            EditText editTextOutFileName = findViewById(R.id.edit_outfile);
            TextView textViewResults = findViewById(R.id.text_main);

            editTextInFileName.setText("");
            editTextOutFileName.setText("");
            textViewResults.setText("Status.");

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void processPress(View view) throws java.io.IOException {
        EditText editTextInFileName = findViewById(R.id.edit_infile);
        EditText editTextOutFileName = findViewById(R.id.edit_outfile);
        TextView textViewResults = findViewById(R.id.text_main);

        String inFileName = editTextInFileName.getText().toString().trim();
        String outFileName = editTextOutFileName.getText().toString().trim();

        if (inFileName.isEmpty()) {
            Toast.makeText(this, "Please enter an input file name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (outFileName.isEmpty()) {
            Toast.makeText(this, "Please enter an output file name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!inFileName.endsWith(".txt")) {
            inFileName += ".txt";
        }

        if (!outFileName.endsWith(".txt")) {
            outFileName += ".txt";
        }

        try {
            InputStream inputStream = getAssets().open(inFileName);

            Scanner scanner = new Scanner(new InputStreamReader(inputStream));

            ArrayList<Double> doubleList = new ArrayList<>();

            while (scanner.hasNext()) {
                if (scanner.hasNextDouble()) {
                    double value = scanner.nextDouble();
                    doubleList.add(value);
                } else {
                    scanner.next();
                }
            }

            scanner.close();
            inputStream.close();

            double[] doubleArray = new double[doubleList.size()];
            for (int i = 0; i < doubleList.size(); i++) {
                doubleArray[i] = doubleList.get(i);
            }

            convert_radians(doubleArray, doubleArray.length);

            StringBuilder resultBuilder = new StringBuilder();
            resultBuilder.append("Radian values from file ").append(inFileName).append(":\n\n");

            for (int i = 0; i < doubleArray.length; i++) {
                resultBuilder.append(String.format("%.6f", doubleArray[i])).append("\n");
            }

            textViewResults.setText(resultBuilder.toString());

            File externalDir = getExternalFilesDir(null);
            File outputFile = new File(externalDir, outFileName);

            try {
                PrintWriter writer = new PrintWriter(new FileWriter(outputFile));

                for (int i = 0; i < doubleArray.length; i++) {
                    writer.println(String.format("%.6f", doubleArray[i]));
                }

                writer.close();

                textViewResults.append("\n\nValues successfully written to file: " + outputFile.getAbsolutePath());
            } catch (IOException e) {
                textViewResults.append("\n\nError writing to file: " + e.getMessage());
            }

        } catch (IOException e) {
            textViewResults.setText("Error: Could not read file " + inFileName + "\n" + e.getMessage());
        }
    }

    public void convert_radians(double[] a, int num_vals) {
        for (int i = 0; i < num_vals; i++) {
            a[i] = Math.toRadians(a[i]);
        }
    }
}