package com.example.dodgegameattempt5;

import android.widget.ImageView;

public class Object implements GameObject {

    private ImageView ball, frame;
    private float posX;
    private float nextX;
    private int radius;
    private int devWidth;

    public Object(ImageView ball, ImageView frame, float posX, float nextX, int devWidth) {
        this.ball = ball;
        this.frame = frame;
        this.posX = posX;
        this.nextX = nextX;
        this.devWidth = devWidth;
        radius = 25;
    }

    @Override
    public void updatePosition() {
        int frameWidth = frame.getWidth();
        //moves the ball LEFT until it crashes with the border
        if((nextX - radius) >= frameWidth / 2){
            ball.setX(nextX);
        }else {
            //"Bounces" the ball of the frame
            ball.setX(nextX + 35);
        }

        //moves the ball to the RIGHT until it crashes with the border
        //Added "- 60" to make the ball bounce on the frame and not the edge of the screen
        if ((nextX + radius) > devWidth - (frameWidth / 2) - 60) {
            ball.setX(nextX - 35);
        }
    }

    public ImageView getBallImage() {
        return ball;
    }

    public float getPosX() {
        return posX;
    }

    public float getNextX() {
        return nextX;
    }

}
