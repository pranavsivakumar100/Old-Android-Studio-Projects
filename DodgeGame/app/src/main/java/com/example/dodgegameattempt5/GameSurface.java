package com.example.dodgegameattempt5;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class GameSurface extends SurfaceView implements Runnable, SensorEventListener {

    private Thread gameThread;
    private SurfaceHolder holder;
    private Bitmap obstacle, player, road, roadScaled, damagedCar;
    private Paint paintProperty;
    private CountDownTimer timer;
    private MediaPlayer mediaPlayer;
    private SoundPool soundPool;
    private Context context_;
    private SurfaceView surfaceView;
    private long currentTime = 0;
    private int score = 0, finalScore = 0;
    private int playerPosX = 450, playerPosY = 1150, topY;
    private int increaseSpeed = 25;
    private int soundId;
    private boolean ifIntersected = false, endGame = false;
    volatile  boolean running = false;

    int screenWidth, screenHeight;

    public GameSurface(Context context) {
        super(context);
        Constants.CURRENT_CONTEXT = context;
        context_ = context;

        timer = new MyTimer(60000, 1000);
        timer.start();
        holder = getHolder();
        obstacle = BitmapFactory.decodeResource(getResources(), R.drawable.obstacle_car);
        player = BitmapFactory.decodeResource(getResources(), R.drawable.car);
        damagedCar = BitmapFactory.decodeResource(getResources(), R.drawable.damaged_car);
        road = BitmapFactory.decodeResource(getResources(), R.drawable.road);
        roadScaled = Bitmap.createScaledBitmap(road, 1080, 1823, false);


        Display screenDisplay = ((AppCompatActivity) context).getWindowManager().getDefaultDisplay();
        Point sizeOfScreen = new Point();
        screenDisplay.getSize(sizeOfScreen);
        screenWidth = sizeOfScreen.x;
        screenHeight = sizeOfScreen.y;
        paintProperty = new Paint();

        mediaPlayer = MediaPlayer.create(context, R.raw.siren);
        mediaPlayer.setLooping(true); // Set looping
        mediaPlayer.setVolume(500, 500);
        mediaPlayer.start();

        surfaceView = new SurfaceView(context);

        soundPool = new SoundPool(1000000000, AudioManager.STREAM_MUSIC, 0);
        soundId = soundPool.load(context, R.raw.hit, 1);

        SensorManager sensorManager = (SensorManager)((AppCompatActivity) context).getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometerSensor, sensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.values[0] > 0 && playerPosX >= 0) {
            playerPosX-=2.5*Math.abs(sensorEvent.values[0]);

        } else if (sensorEvent.values[0] <= 0 && playerPosX <= 810) {
            playerPosX+=2.5*Math.abs(sensorEvent.values[0]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
//700
    int count = 0;
    @Override
    public void run() {
        int rand = (int)(Math.random()*700) + 1;
        Context context;

        //boolean ifIntersected = false; 857821
        while (running) {
            Log.d("randNum", "rand: " + rand);
            if (holder.getSurface().isValid() == false) {
                continue;
            }
            Canvas canvas = holder.lockCanvas();
            Constants.CURRENT_CANVAS = canvas;
            Bitmap road = BitmapFactory.decodeResource(getResources(), R.drawable.road);
            Bitmap roadScaled = Bitmap.createScaledBitmap(road, 1080, 1823, false);

            canvas.drawBitmap(roadScaled, 0, 0, null);
            canvas.drawBitmap(obstacle, rand, topY, null);
            canvas.drawBitmap(player, playerPosX, playerPosY, null);
            paintProperty.setTextSize(90);

            Rect obstacleRect = new Rect(rand + 190, topY, 360 + rand, 500 + topY);
            Rect playerRect = new Rect(playerPosX + 40, playerPosY + 15, playerPosX + 232, playerPosY + 475);
            Paint rectPaint = new Paint();
            rectPaint.setColor(Color.TRANSPARENT);
            canvas.drawRect(obstacleRect, rectPaint);
            canvas.drawRect(playerRect, rectPaint);


            canvas.drawText("Time Left: " + String.valueOf(currentTime/1000), 15, 120, paintProperty);
            canvas.drawText(String.valueOf(score), 750, 120, paintProperty);
            Log.d("dimensions", "Width: "  + canvas.getWidth() + "Height: " + canvas.getHeight());
            if (endGame) {
                //playerRect.set(1000, 1000, 10000, 10000);
                playerPosX = 100000;
                canvas.drawColor(Color.BLACK);
                Paint newPaint = new Paint();
                newPaint.setColor(Color.WHITE);
                newPaint.setTextSize(100);
                //Canvas canvas2 = new Canvas();
                //canvas.drawColor(Color.BLACK);
                canvas.drawText("Game Over!", 338, 800, newPaint);
                canvas.drawText("You ran out of time", 170, 1000, newPaint);
                canvas.drawText("Score: " + finalScore, 350, 1200, newPaint);
            }

            if (playerRect.intersect(obstacleRect)) {
                ifIntersected = true;
                if (count == 0) {
                    canvas.drawBitmap(damagedCar, playerPosX, playerPosY, null);
                    soundPool.play(soundId, 1, 1, 0, 0, 1);
                    if (score > 0) {
                        score--;
                    }
                }
                count++;
            }
            if (ifIntersected) {
                canvas.drawBitmap(damagedCar, playerPosX, playerPosY, null);
            }
            if (topY >= canvas.getHeight()) {
                if (!ifIntersected) {
                    increaseSpeed += 2;
                    score++;
                }
                count = 0;
                ifIntersected = false;
                rand = (int)(Math.random()*700) + 1;
                topY = 0;
            }

            topY+=increaseSpeed;
            holder.unlockCanvasAndPost(canvas);
        }
    }

    public void resume() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void pause() {
        running = false;
        while (true) {
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int getSpeed() {
        return increaseSpeed;
    }

    public void setSpeed(int increaseSpeed) {
        this.increaseSpeed = increaseSpeed;
    }

    public void endGame(int varScore, String message) {
        Intent intent = new Intent(context_, EndActivity.class);
        intent.putExtra("score", varScore);
        intent.putExtra("message", message);
        context_.startActivity(intent);
        ((AppCompatActivity) context_).finish();

    }

    public class MyTimer extends CountDownTimer {

        public MyTimer(int maxTime, long stepTime) {
            super(maxTime, stepTime);
        }


        @Override
        public synchronized void onTick(long millisUntilFinished) {
            //time = millisUntilFinished / 1000;

            Log.d("timer", "Timer: "+millisUntilFinished/1000);
            currentTime = millisUntilFinished;
            //Paint paint = new Paint();
            //Canvas canvas = new Canvas();
            //paint.setColor(Color.BLACK);

            //Constants.CURRENT_CANVAS.drawPaint(paint);
            //Constants.CURRENT_CANVAS.drawText("hreiwhriwhfw", 100, 100, paint);


            //timerTV.setText(String.valueOf(millisUntilFinished / 1000));

        }

        @Override
        public void onFinish() {
            this.cancel();
            String messageTime = "You ran out of time!";
            //endGame(score, messageTime);
            finalScore = score;
            endGame = true;
        }
    }

}