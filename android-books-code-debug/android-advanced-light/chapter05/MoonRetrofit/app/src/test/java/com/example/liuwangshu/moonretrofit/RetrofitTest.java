package com.example.liuwangshu.moonretrofit;

import android.util.Log;
import android.widget.Toast;

import com.example.liuwangshu.moonretrofit.model.Ip;
import com.example.liuwangshu.moonretrofit.model.IpModel;

import org.junit.Test;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 测试网络接口，没有请求结果，异常和正常结果都没有。可能retrofit调用android的api了，直接通过这种简单的单元测试
 * 无法实际执行android代码，比如请求相应主线程，应该通过Handler转化的。
 */
public class RetrofitTest {
    /**
     * 普通GET请求
     */
    @Test
    public void getIpInformationString() {
        System.out.println("getIpInformation");
//        String url = "http://ip.taobao.com/service/";//
//        String url = "http://opendata.baidu.com/api.php/";
        String url = "https://ip.taobao.com/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IpServiceString ipService = retrofit.create(IpServiceString.class);
        Call<String> call = ipService.getIpMsg();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println("country:" + response.body().toString());
//                Toast.makeText(getApplicationContext(), country, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("wangshu", "onFailure:" + t);
                System.out.println("onFailure:" + t);
            }
        });
    }
    /**
     * 普通GET请求
     */
    @Test
    public void getIpInformation() {
        System.out.println("getIpInformation");
//        String url = "http://ip.taobao.com/service/";//
//        String url = "http://opendata.baidu.com/api.php/";
        String url = "https://ip.taobao.com/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IpService ipService = retrofit.create(IpService.class);
        Call<IpModel> call = ipService.getIpMsg();
        call.enqueue(new Callback<IpModel>() {
            @Override
            public void onResponse(Call<IpModel> call, Response<IpModel> response) {
                String country = response.body().getData().get(0).getLocation();
                Log.i("wangshu", "country" + country);
                System.out.println("country:" + country);
//                Toast.makeText(getApplicationContext(), country, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<IpModel> call, Throwable t) {
                Log.e("wangshu", "onFailure:" + t);
                System.out.println("onFailure:" + t);
            }
        });
    }

    /**
     * @param path
     * @Path方式GET请求
     */
    private void getIpInformationForPath(String path) {
        String url = "http://ip.taobao.com/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IpServiceForPath ipService = retrofit.create(IpServiceForPath.class);
        Call<IpModel> call = ipService.getIpMsg(path);
        call.enqueue(new Callback<IpModel>() {
            @Override
            public void onResponse(Call<IpModel> call, Response<IpModel> response) {
                String country = response.body().getData().get(0).getLocation();
                Log.i("wangshu", "country" + country);
//                Toast.makeText(getApplicationContext(), country, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<IpModel> call, Throwable t) {

            }
        });
    }


    /**
     * @param ip
     * @Query方式GET请求
     */
    private void getIpInformationForQuery(String ip) {
        String url = "http://ip.taobao.com/service/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IpServiceForQuery ipService = retrofit.create(IpServiceForQuery.class);
        Call<IpModel> call = ipService.getIpMsg(ip);
        call.enqueue(new Callback<IpModel>() {
            @Override
            public void onResponse(Call<IpModel> call, Response<IpModel> response) {
                String country = response.body().getData().get(0).getLocation();
                Log.i("wangshu", "country" + country);
//                Toast.makeText(getApplicationContext(), country, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<IpModel> call, Throwable t) {

            }
        });
    }

    /**
     * 传输数据类型为键值对的POST请求
     *
     * @param ip
     */
    private void postIpInformation(String ip) {
        String url = "http://ip.taobao.com/service/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IpServiceForPost ipService = retrofit.create(IpServiceForPost.class);
        Call<IpModel> call = ipService.getIpMsg(ip);
        call.enqueue(new Callback<IpModel>() {
            @Override
            public void onResponse(Call<IpModel> call, Response<IpModel> response) {
                String country = response.body().getData().get(0).getLocation();
                Log.i("wangshu", "country" + country);
//                Toast.makeText(getApplicationContext(), country, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<IpModel> call, Throwable t) {

            }
        });
    }

    /**
     * 传输数据类型Json字符串的POST请求
     *
     * @param ip
     */
    private void postIpInformationForBody(String ip) {
        String url = "http://ip.taobao.com/service/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IpServiceForPostBody ipService = retrofit.create(IpServiceForPostBody.class);
        Call<IpModel> call = ipService.getIpMsg(new Ip(ip));
        call.enqueue(new Callback<IpModel>() {
            @Override
            public void onResponse(Call<IpModel> call, Response<IpModel> response) {
                String country = response.body().getData().get(0).getLocation();
                Log.i("wangshu", "country" + country);
//                Toast.makeText(getApplicationContext(), country, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<IpModel> call, Throwable t) {

            }
        });
    }

}
