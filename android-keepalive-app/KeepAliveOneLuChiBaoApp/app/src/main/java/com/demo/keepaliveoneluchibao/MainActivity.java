package com.demo.keepaliveoneluchibao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //启动保活后台服务
        Intent intent = new Intent(MainActivity.this,LcbAliveService.class);
        startService(intent);

    }
}
