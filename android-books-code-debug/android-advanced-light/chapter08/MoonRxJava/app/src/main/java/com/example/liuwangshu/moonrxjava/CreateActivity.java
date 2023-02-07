package com.example.liuwangshu.moonrxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * 创建操作符
 */
public class CreateActivity extends AppCompatActivity {
 private static final String TAG="RxJava";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        interval();
//        range();
//        repeat();
    }

    private void create() {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(1);
                subscriber.onCompleted();
            }
        }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "create:" +integer.intValue());
            }
        });
    }
    private void map() {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(1);
                subscriber.onCompleted();
            }
        }).map(new Func1<Integer, String>() {
            @Override
            public String call(Integer integer) {
                return "a" + integer.intValue();
            }
        }).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String integer) {
                Log.d(TAG, "create:" +integer);
            }
        });
    }
    private void repeat() {
        Observable.range(0, 3)
                .repeat(2)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.d(TAG, "repeat:" +integer.intValue());
                    }
                });
    }

    private void range() {
        Observable.range(0, 5)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.d(TAG, "range:" + integer.intValue());
                    }
                });
    }

    private void interval() {
        Observable.interval(3, TimeUnit.SECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long mlong) {
                        Log.d(TAG, "interval:" + mlong.intValue());
                    }
                });
    }


}
