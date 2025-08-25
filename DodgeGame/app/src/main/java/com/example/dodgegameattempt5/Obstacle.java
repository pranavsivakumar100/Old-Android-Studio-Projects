package com.example.dodgegameattempt5;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Obstacle implements GameObject {

    private Bitmap obstacle;
    private SurfaceHolder surfaceHolder;
    private Canvas canvas;
    private int topY;

    public Obstacle() {
        surfaceHolder = new SurfaceHolder() {
            @Override
            public void addCallback(Callback callback) {

            }

            @Override
            public void removeCallback(Callback callback) {

            }

            @Override
            public boolean isCreating() {
                return false;
            }

            @Override
            public void setType(int type) {

            }

            @Override
            public void setFixedSize(int width, int height) {

            }

            @Override
            public void setSizeFromLayout() {

            }

            @Override
            public void setFormat(int format) {

            }

            @Override
            public void setKeepScreenOn(boolean screenOn) {

            }

            @Override
            public Canvas lockCanvas() {
                return null;
            }

            @Override
            public Canvas lockCanvas(Rect dirty) {
                return null;
            }

            @Override
            public void unlockCanvasAndPost(Canvas canvas) {

            }

            @Override
            public Rect getSurfaceFrame() {
                return null;
            }

            @Override
            public Surface getSurface() {
                return null;
            }
        };
        obstacle = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.car);
        updatePosition();
    }

    @Override
    public void updatePosition() {
        int randomWidth = (int)(Math.random()*900) + 1;
        canvas = surfaceHolder.lockCanvas();
        canvas.drawColor(Color.BLACK);
        canvas.drawBitmap(obstacle, randomWidth, topY, null);
        if (topY >= canvas.getHeight()) {
            randomWidth = (int)(Math.random()*900) + 1;
            topY = 0;
        }
        topY+=20;
        surfaceHolder.unlockCanvasAndPost(canvas);
    }


}
