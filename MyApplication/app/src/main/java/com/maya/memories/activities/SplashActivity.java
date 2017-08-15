package com.maya.memories.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.maya.memories.R;
import com.maya.memories.constants.Constants;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences PREFS;
    SplashActivity splashActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        splashActivity=this;
        PREFS=getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE);
        setContentView(R.layout.activity_splash);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {

                if(PREFS.contains(Constants.PIN_FLAG))
                {
                    Intent intent=new Intent(splashActivity,MainActivity.class);
                    startActivity(intent);
                }
                else
                {
                    if(PREFS.contains(Constants.LOGIN_FLAG))
                    {
                        Intent intent=new Intent(splashActivity,PinActivity.class);
                        startActivity(intent);

                    }
                    else
                    {
                        Intent intent=new Intent(splashActivity,LoginActivity.class);
                        startActivity(intent);

                    }

                }
                finish();

            }
        },1500);

    }
}
