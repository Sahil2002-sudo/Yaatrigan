package com.example.carpooling;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN_TIME_OUT=2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences shr= PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

       // SharedPreferences.Editor et1=shr.edit();
       // et1.putString("islogin","0");
      //  et1.commit();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(MainActivity.this,Search.class);
                //Intent is used to switch from one activity to another.

                startActivity(i);
                overridePendingTransition(R.anim.slide_left,R.anim.slide_right);
                //invoke the SecondActivity.

                finish();
                //the current activity will get finished.
            }
        }, SPLASH_SCREEN_TIME_OUT);
    }
}
