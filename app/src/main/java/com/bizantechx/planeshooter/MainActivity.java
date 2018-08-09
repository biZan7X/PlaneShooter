package com.bizantechx.planeshooter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startgame(View view) //we are setting on click property of the button
    {
        Log.i("Imagebutton","clicked");
        Intent intent =new Intent(this,StartGame.class); // by using the intent function we are calling the startgame class as the button is clicked
        startActivity(intent);
        finish();//finish the main activity
    }
}
