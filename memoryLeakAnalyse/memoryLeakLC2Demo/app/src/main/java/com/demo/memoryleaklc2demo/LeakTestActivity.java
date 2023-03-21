package com.demo.memoryleaklc2demo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class LeakTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leak_test);
        LeakThread leakThread = new LeakThread();
        leakThread.start();
    }
    class LeakThread extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(6 * 60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
