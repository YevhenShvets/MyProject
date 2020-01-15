package com.yevhen.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final ImageView img1 = (ImageView) findViewById(R.id.splash_logo1);
        final ImageView img2 = (ImageView) findViewById(R.id.splash_logo2);
        final ImageView img3 = (ImageView) findViewById(R.id.splash_logo3);
        final ImageView img4 = (ImageView) findViewById(R.id.splash_logo4);
        img2.setVisibility(View.INVISIBLE);
        img3.setVisibility(View.INVISIBLE);
        img4.setVisibility(View.INVISIBLE);

        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                img1.setVisibility(View.INVISIBLE);
                img3.setVisibility(View.VISIBLE);
            }
        },10);

        Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                img3.setVisibility(View.INVISIBLE);
                img4.setVisibility(View.VISIBLE);
            }
        },24);

        Handler handler3 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                img4.setVisibility(View.INVISIBLE);
                img2.setVisibility(View.VISIBLE);
            }
        },36);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },50);
    }
}
