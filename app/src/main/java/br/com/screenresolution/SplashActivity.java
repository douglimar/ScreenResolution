package br.com.screenresolution;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;


public class SplashActivity extends AppCompatActivity implements Runnable{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        Handler h = new Handler();

        h.postDelayed(this,2000);

    }

    @Override
    public void run() {

        Intent intent = new Intent(getApplicationContext(), Main2Activity.class);

        startActivity(intent);

        finish();

    }
}
