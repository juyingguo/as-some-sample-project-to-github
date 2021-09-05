package com.wd.room.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.wd.room.R;
import com.wd.room.bean.StaticDataBean;
import com.wd.room.viewmodel.MyViewModel;

import java.util.Timer;
import java.util.TimerTask;

public class RotationTestActivity extends AppCompatActivity {
    private static final String TAG = "RotationTestActivity";
    TextView textView;
    TextView textViewLiveData;
    TextView textView2;
    MyViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotation_test);
        Log.i(TAG,"onCreate().");

        textView = findViewById(R.id.textView);
        textViewLiveData = findViewById(R.id.textViewLiveData);
        textView2 = findViewById(R.id.textView2);
        //viewmodel中不要传入context，如果必须要使用，换成AndroidViewModel里的Application
        viewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(MyViewModel.class);
        textView.setText(String.valueOf(viewModel.num));
        textView2.setText(String.valueOf(StaticDataBean.count));

        viewModel.getCurrentSecond().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                textViewLiveData.setText(String.valueOf(integer));
            }
        });
        startTime();
    }

    //保存瞬态数据，屏幕旋转后数据还在
    public void AddNum(View view) {
        textView.setText(String.valueOf(++viewModel.num));
        textView2.setText(String.valueOf(++StaticDataBean.count));
    }
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            //非UI线程，用postValue
            //UI线程，用setValue
            viewModel.getCurrentSecond().postValue(viewModel.getCurrentSecond().getValue() + 1);
        }
    };
    Timer mTimer = new Timer();
    private void startTime(){
        mTimer.schedule(task,1000,1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy().");
        task.cancel();
//        mTimer.cancel();
//        mTimer.purge();
//        mTimer = null;
    }
}
