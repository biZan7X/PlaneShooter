package com.bizantechx.planeshooter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.Image;
import android.media.SoundPool;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by bizantechx on 8/6/2018.
 */

public class GameView extends View {
    Bitmap background,tank;
    ArrayList<plane>planes;
    ArrayList<plane>planes2;
    ArrayList<Missiles>missiles;
    static int i,tankWidth,tankHeight;
    Paint scorepaint;
    final int TEXT_SIZE=60;
    Paint life;
    int health=10;
    Context context;
    SoundPool sp;
    int fire=0,point=0;
    int count = 0;
    Rect rect; // by default the co ordinates of the background are 0 but for the right and bottom it should be parent
    static int dWidth,dHieght;
    final long UPADTE_MILLIS=30;//since we cannot update t everytime so we are fixing it
    Handler handler;//helps us to do scheduled operations
    Runnable runnable;//gives the code for later execution
    public GameView(Context context) {
        super(context);
        this.context=context;
        background = BitmapFactory.decodeResource(getResources(),R.drawable.background); //we are setting the background through bitmap function .
        Display display =((Activity)getContext()).getWindowManager().getDefaultDisplay();
        Point size=new Point();
        display.getSize(size);
        dWidth=size.x;
        dHieght=size.y;
        rect = new Rect(0,0,dWidth,dHieght);//replacing the value of rightt and bottom wd parent
        scorepaint= new Paint();
        scorepaint.setColor(Color.RED);
        scorepaint.setTextAlign(Paint.Align.LEFT);
        scorepaint.setTextSize(TEXT_SIZE);
        life = new Paint();
        life.setColor(Color.GREEN);
        planes =new ArrayList<>();
        planes2=new ArrayList<>();
        missiles= new ArrayList<>();
        for(i=0;i<2;i++)
        {
            plane pplane = new plane(context);
            planes.add(pplane);
            plane2 pplane2 = new plane2(context);
            planes2.add(pplane2);
        }

        handler =new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
        tank =BitmapFactory.decodeResource(getResources(),R.drawable.tank);
        tankWidth=tank.getWidth();
        tankHeight=tank.getHeight();
        sp = new SoundPool(3, AudioManager.STREAM_MUSIC,0);
        fire=sp.load(context,R.raw.fire,1);
        point=sp.load(context,R.raw.point,1);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(health <1)
        {
           // Intent intent= new Intent(context,GameOver.class);
           // intent.putExtra("Score",(count*10));
           // context.startActivity(intent);
            ((Activity)context).finish();
        }
        //canvas.drawBitmap(background,0,0,null);
        canvas.drawBitmap(background,null,rect,null);//replaced the value with new rect settings
        for(i=0;i<planes.size();i++)//we are etarating through 0 to the size of the array
        {
            canvas.drawBitmap(planes.get(i).getbitamp(), planes.get(i).planeX, planes.get(i).planeY, null);
            planes.get(i).planeFrame++;
            if (planes.get(i).planeFrame > 14)
                planes.get(i).planeFrame = 0;

            planes.get(i).planeX -= planes.get(i).Velocity; //decreasing velocity
            if (planes.get(i).planeX < -planes.get(i).getWidth()) {// right to left
                planes.get(i).resetposition();
                health--;
            }

            canvas.drawBitmap(planes2.get(i).getbitamp(), planes2.get(i).planeX, planes2.get(i).planeY, null);
            planes2.get(i).planeFrame++;
            if (planes2.get(i).planeFrame > 9)
                planes2.get(i).planeFrame = 0;

            planes2.get(i).planeX += planes2.get(i).Velocity; //increasing velocity
            if (planes2.get(i).planeX > (dWidth+planes2.get(i).getWidth())) { //left to right so we need to kip increasing the velo
                planes2.get(i).resetposition();
                health--;
            }
            /*when the y co ordinate of missile becomes equal or less than negative missile hieght
        , then it means the missile has left the screen
         */
            for(int i=0; i<missiles.size(); i++){
                if(missiles.get(i).y > -missiles.get(i).getMissileHeight()){
                    missiles.get(i).y -= missiles.get(i).mVelocity;
                    canvas.drawBitmap(missiles.get(i).missile, missiles.get(i).x, missiles.get(i).y, null);
                    /* for collision the conditions are :-
                    if missile x co ordinate >= left edge of the plane
                    if missile right edge <= right edge of the plane
                    if missile y co ordinate >= plane y co ordinate
                    if missile y co ordiante <= plane bottom edge
                     */
                    //for plane
                    if(missiles.get(i).x >=planes.get(0).planeX && (missiles.get(i).x + missiles.get(i).getMissileWidth() <= (planes.get(0).planeX + planes.get(0).getWidth())
                    && missiles.get(i).y >=planes.get(0).planeY && missiles.get(i).y <= (planes.get(0).planeY + planes.get(0).getHeight()))){
                        planes.get(0).resetposition();
                        count++;
                        missiles.remove(i);
                        if(point!=0){
                            sp.play(point,1,1,0,0,1);
                        }
                    }
                    else if(missiles.get(i).x >=planes.get(1).planeX && (missiles.get(i).x + missiles.get(i).getMissileWidth() <= (planes.get(1).planeX + planes.get(1).getWidth())
                            && missiles.get(i).y >=planes.get(1).planeY && missiles.get(i).y <= (planes.get(1).planeY + planes.get(1).getHeight()))){
                        planes.get(1).resetposition();
                        count++;
                        missiles.remove(i);
                        if(point!=0){
                            sp.play(point,1,1,0,0,1);
                        }
                    }
                    // for planes2
                    else if(missiles.get(i).x >=planes2.get(0).planeX && (missiles.get(i).x + missiles.get(i).getMissileWidth() <= (planes2.get(0).planeX + planes2.get(0).getWidth())
                            && missiles.get(i).y >=planes2.get(0).planeY && missiles.get(i).y <= (planes2.get(0).planeY + planes2.get(0).getHeight()))){
                        planes2.get(0).resetposition();
                        count++;
                        missiles.remove(i);
                        if(point!=0){
                            sp.play(point,1,1,0,0,1);
                        }
                    }
                    else if(missiles.get(i).x >=planes2.get(1).planeX && (missiles.get(i).x + missiles.get(i).getMissileWidth() <= (planes2.get(1).planeX + planes2.get(1).getWidth())
                            && missiles.get(i).y >=planes2.get(1).planeY && missiles.get(i).y <= (planes2.get(1).planeY + planes2.get(1).getHeight()))){
                        planes2.get(1).resetposition();
                        count++;
                        missiles.remove(i);
                        if(point!=0){
                            sp.play(point,1,1,0,0,1);
                        }
                    }
                }else{
                    missiles.remove(i);
                }
            }
        }
        canvas.drawBitmap(tank,(dWidth/2 - tankWidth/2),dHieght-tankHeight,null);//tank
        canvas.drawText("score: "+(count*10),0,TEXT_SIZE,scorepaint);//score
        canvas.drawRect(dWidth-110,10,dWidth-110+10*health,TEXT_SIZE,life);//life

        handler.postDelayed(runnable,UPADTE_MILLIS);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        int action = event.getAction();
        if(action == MotionEvent.ACTION_DOWN){ //to check if the tank is tapped
            if(touchX >= (dWidth/2 - tankWidth/2) && touchX <= (dWidth/2 + tankWidth/2) && touchY >= (dHieght - tankHeight)){
                Log.i("Tank","is tapped");
                if(missiles.size()<3){ // there should be three active missiles
                    Missiles m= new Missiles(context);
                    missiles.add(m);
                    if (fire!=0){
                        sp.play(fire,1,1,0,0,1);
                    }
                }
            }
        }
        return true;
    }

}
