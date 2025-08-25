package com.example.tiltdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    TextView x, y, z, bright;
    float brightnessLevel = 0;
    WindowManager.LayoutParams brightnessParam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelorometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelorometerSensor, sensorManager.SENSOR_DELAY_NORMAL);

        x = findViewById(R.id.x);
        y = findViewById(R.id.y);
        z = findViewById(R.id.z);
        bright = findViewById(R.id.brightness_id);

        brightnessParam = getWindow().getAttributes();
        bright.setText(brightnessLevel + "");

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        x.setText(sensorEvent.values[0] + "");
        z.setText(sensorEvent.values[1] + "");
        x.setText(sensorEvent.values[2] + "");

        if (brightnessLevel < 1.0f) {
            brightnessLevel += .01f;
        }
        brightnessParam.screenBrightness = brightnessLevel;
        getWindow().setAttributes(brightnessParam);
        bright.setText(brightnessLevel + "");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}