package com.example.liuwangshu.moonrxjava.net;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.widget.Toast;

import com.example.liuwangshu.moonrxjava.IpServiceForPost;
import com.example.liuwangshu.moonrxjava.IpServiceForPostWithHttpResult;
import com.example.liuwangshu.moonrxjava.IpServiceForPostWithResponseBody;
import com.example.liuwangshu.moonrxjava.R;
import com.example.liuwangshu.moonrxjava.model.HttpResult;
import com.example.liuwangshu.moonrxjava.model.HttpResult2;
import com.example.liuwangshu.moonrxjava.model.IpData;
import com.example.liuwangshu.moonrxjava.model.IpModel;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class RetrofitActivity extends AppCompatActivity {
    private static final String TAG="RxJava";
    private Subscription subscription;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
    }
    @Override
    protected void onResume() {
        super.onResume();
//        postIpInformation("59.108.54.37");
        postIpInformationHttpResult("59.108.54.37");
//        postIpInformationResponseBody("59.108.54.37");
    }
    @Override
    public void onStop() {
        super.onStop();
        if (subscription != null && subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
    private void postIpInformation(String ip) {
        String url = "http://opendata.baidu.com/api.php/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        IpServiceForPost ipService = retrofit.create(IpServiceForPost.class);
        subscription=ipService.getIpMsg(ip).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<IpModel>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(IpModel ipDataHttpResult) {
                Log.d(TAG, "onNext:" + ipDataHttpResult);
                IpData data= ipDataHttpResult.getData().get(0);
                Toast.makeText(getApplicationContext(), data.getLocation(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void postIpInformationHttpResult(String ip) {
//        String url = "http://ip.taobao.com/service/";
        String url = "http://opendata.baidu.com/api.php/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        IpServiceForPostWithHttpResult ipService = retrofit.create(IpServiceForPostWithHttpResult.class);
        subscription=ipService.getIpMsg(ip).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<HttpResult2<List<IpData>>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HttpResult2<List<IpData>> ipDataHttpResult) {
                        IpData data=ipDataHttpResult.getData().get(0);
                        Toast.makeText(getApplicationContext(), data.getLocation(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void postIpInformationResponseBody(String ip) {
        String url = "http://opendata.baidu.com/api.php/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
//                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        IpServiceForPostWithResponseBody ipService = retrofit.create(IpServiceForPostWithResponseBody.class);
        subscription=ipService.getIpMsg(ip).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseBody ipDataHttpResult) {
//                        IpData data=ipDataHttpResult.getData();
                        try {
                            Toast.makeText(getApplicationContext(), ipDataHttpResult.string(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
