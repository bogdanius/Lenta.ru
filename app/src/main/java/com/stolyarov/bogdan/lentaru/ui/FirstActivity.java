package com.stolyarov.bogdan.lentaru.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.stolyarov.bogdan.lentaru.R;


public class FirstActivity extends Activity {

    private static int SPLASH_SCREEN_TIMEOUT = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_first);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(FirstActivity.this, MainActivity.class);
                startActivity(intent);
//                overridePendingTransition(R.anim.main_activity_start, R.anim.splash_stop);
                finish();
            }
        }, SPLASH_SCREEN_TIMEOUT);

    }
}
