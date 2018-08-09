package com.bizantechx.planeshooter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by bizantechx on 8/9/2018.
 */

public class Missiles {
    int x,y;
    int mVelocity;
    Bitmap missile;
    public int getMissileWidth(){
        return missile.getWidth();
    }
    public int getMissileHeight(){
        return missile.getHeight();
    }

    public Missiles(Context context) {
        missile= BitmapFactory.decodeResource(context.getResources(),R.drawable.missile);
        x=GameView.dWidth/2 - getMissileWidth()/2;
        y=GameView.dHieght- GameView.tankHeight- getMissileHeight()/2;
        mVelocity=50;
    }
}
