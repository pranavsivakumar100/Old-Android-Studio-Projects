package com.example.orientationlistviewproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private TextView text1, text2;
    ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        list = new ArrayList<String>();

        list.add("Drake");
        list.add("Travis Scott");
        list.add("Playboi Carti");
        list.add("Kanye West");
        list.add("Lil Baby");
        list.add("J. Cole");



    }
}