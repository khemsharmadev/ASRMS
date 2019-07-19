package com.khemsharma.rms;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;

    TextView logoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        logoText = findViewById(R.id.logoText);

        final Animation slide_down = AnimationUtils.loadAnimation(getBaseContext(), R.anim.slide_down);



        final Animation slide_up=AnimationUtils.loadAnimation(getBaseContext(), R.anim.slide_up);

        logoText.startAnimation(slide_up);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (AppUtils.isUserLogin(getApplicationContext())){

                    Intent intent = new Intent(SplashScreen.this,MainActivity.class);
                    startActivity(intent);
                    finish();

                }
                else{
                    Intent intent = new Intent(SplashScreen.this,LoginActivity.class);
                    finish();
                    startActivity(intent);
                }

            }
        }, SPLASH_TIME_OUT);

    }
}
