package com.ymss.tinyshop;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ymss.config.CommonVar;
import com.ymss.config.Constants;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        new Handler().postDelayed(new Runnable() {
            public void run() {
            /* Create an Intent that will start the Main WordPress Activity. */
                if (isShowGuidActivity() ==true) {
                    Intent mainIntent = new Intent(SplashActivity.this, GuidActivity.class);
                    SplashActivity.this.startActivity(mainIntent);
                }else{
                    Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                    SplashActivity.this.startActivity(mainIntent);
                }
                SplashActivity.this.finish();
            }
        }, 1000); //2000 for release

    }

    public boolean isShowGuidActivity(){
        String key = CommonVar.getGuidPreferencesKey(this);
        if (key!=null) {
            SharedPreferences sp= getSharedPreferences(Constants.SharedPreferencesName, Activity.MODE_PRIVATE);
            return !sp.getBoolean(key,false);
        }
        return true;
    }
}
