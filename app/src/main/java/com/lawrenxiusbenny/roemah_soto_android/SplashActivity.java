package com.lawrenxiusbenny.roemah_soto_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;


public class SplashActivity extends AppCompatActivity {

    private ImageView splash_logo;

    private SharedPreferences sPreferences;
    public static final String KEY_ID = "id_customer";
    private int id_customer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        id_customer = 0;

        sPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        id_customer = sPreferences.getInt(KEY_ID,Context.MODE_PRIVATE);

        splash_logo = findViewById(R.id.splash_logo);

        splash_logo.animate().translationY(-5000).setDuration(1000).setStartDelay(2500);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i;
//                if(id_customer != 0){
//                    i = new Intent(SplashActivity.this,MainActivity.class);
//                }else{
                    i = new Intent(SplashActivity.this,LoginActivity.class);
//                }

                startActivity(i);
                finish();
            }
        },3500);
    }
}