package com.example.liuwangshu.moonretrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.liuwangshu.moonretrofit.model.IpModel;
import com.example.liuwangshu.moonretrofit.model.IpQuery;

import java.io.IOException;
import java.lang.*;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 1.retrofit简单的使用可以在单元测试中进行。先在模拟器中进行了。需要在模拟器中进行的在做。
 * 2.测试名称为RetrofitTest
 */
public class MainActivity extends AppCompatActivity {
    private Button bt_request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt_request = (Button) findViewById(R.id.bt_request);
        bt_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  getIpInformation();
//                getIpInformationForQuery("59.108.54.37");
//                getIpInformationForPath("service");
//                postIpInformationByIp("59.108.54.37");
//                postIpInformationForBody("59.108.54.37");

            }
        });
    }

    public void clickGetIpInformationForQuery(View view) {
        getIpInformationForQuery("59.108.54.37");
    }

    public void clickGetIpInformationForQueryMap(View view) {
        getIpInformationForQueryMap("59.108.54.37");
    }

    public void clickGetIpInformationForPath(View view) {
        getIpInformationForPath("api.php");
    }

    public void clickPostIpInformationByIp(View view) {
        postIpInformationByIp("59.108.54.37");
    }

    public void clickPostIpInformationForBody(View view) {
        postIpInformationForBody("59.108.54.37");
    }
    /**
     * 普通GET请求
     */
    private void getIpInformation() {
//        String url = "http://ip.taobao.com/service/";
        String url = "http://opendata.baidu.com/api.php/";
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
                Log.i("wangshu", "country:" + country);
                Toast.makeText(getApplicationContext(), country, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<IpModel> call, Throwable t) {

            }
        });
    }
    /**
     * 普通GET请求，响应json数据不转换为javabean。
     * 1.不添加GsonConverterFactory，使用IpServiceString 泛型为String,也不起作用。
     * 2.IpServiceResponseBody,泛型为String，也不起作用
     * 3.IpServiceResponseBody,泛型为ResponseBody，ok
     */
    public void clickGetIpInformationForJson(View view) {
        String url = "http://opendata.baidu.com/api.php/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
//                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IpServiceResponseBody ipService = retrofit.create(IpServiceResponseBody.class);
        Call<ResponseBody> call = ipService.getIpMsg();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i("wangshu", "response.message():" + response.message());
                try {
                    Log.i("wangshu", "response.body():" + response.body().string().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.i("wangshu", "response.raw():" + response.raw().body().toString());

//                String country = response.body().getData().get(0).getLocation();
//                Log.i("wangshu", "country:" + country);
//                Toast.makeText(getApplicationContext(), country, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    /**
     * @param ip
     * Query方式GET请求
     */
    private void getIpInformationForQuery(String ip) {
//        String url = "http://ip.taobao.com/service/";
        String url = "http://opendata.baidu.com/api.php/";
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
                Log.i("wangshu", "country:" + country);
                Toast.makeText(getApplicationContext(), country, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<IpModel> call, Throwable t) {

            }
        });
    }
    /**
     * @param ip
     * QueryMap方式GET请求
     */
    private void getIpInformationForQueryMap(String ip) {
//        String url = "http://ip.taobao.com/service/";
        String url = "http://opendata.baidu.com/api.php/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IpServiceForQueryMap ipService = retrofit.create(IpServiceForQueryMap.class);
        //query=117.136.12.79&co=&resource_id=6006&oe=utf8
        Map<String,String> map = new HashMap<>();
        map.put("query",ip);
        map.put("resource_id","6006");
        map.put("oe","utf8");
        Call<IpModel> call = ipService.getIpMsg(map);
        call.enqueue(new Callback<IpModel>() {
            @Override
            public void onResponse(Call<IpModel> call, Response<IpModel> response) {
                String country = response.body().getData().get(0).getLocation();
                Log.i("wangshu", "country:" + country);
                Toast.makeText(getApplicationContext(), country, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<IpModel> call, Throwable t) {

            }
        });
    }
    /**
     * @param path
     * Path方式GET请求
     */
    private void getIpInformationForPath(String path) {
//        String url = "http://ip.taobao.com/";
        String url = "http://opendata.baidu.com/";
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
                Log.i("wangshu", "country:" + country);
                Toast.makeText(getApplicationContext(), country, Toast.LENGTH_SHORT).show();
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
    private void postIpInformationByIp(String ip) {
//        String url = "http://ip.taobao.com/service/";
        String url = "http://opendata.baidu.com/api.php/";
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
                Log.i("wangshu", "country:" + country);
                Toast.makeText(getApplicationContext(), country, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<IpModel> call, Throwable t) {

            }
        });
    }

    /**
     * 传输数据类型Json字符串的POST请求
     * 需要接口服务器支持。
     * @param ip
     */
    private void postIpInformationForBody(String ip) {
//        String url = "http://ip.taobao.com/service/";
        String url = "http://opendata.baidu.com/api.php/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IpServiceForPostBody ipService = retrofit.create(IpServiceForPostBody.class);
//        Call<IpModel> call = ipService.getIpMsg(new Ip(ip));
        Call<IpModel> call = ipService.getIpMsg(new IpQuery(ip));
        call.enqueue(new Callback<IpModel>() {
            @Override
            public void onResponse(Call<IpModel> call, Response<IpModel> response) {
                String country = response.body().getData().get(0).getLocation();
                Log.i("wangshu", "country:" + country);
                Toast.makeText(getApplicationContext(), country, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<IpModel> call, Throwable t) {

            }
        });
    }
    /**
     *
     */
    private void uploadSingleFile(String ip) {
////        String url = "http://ip.taobao.com/service/";
//        String url = "http://opendata.baidu.com/api.php/";
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(url)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        UploadFileForPart ipService = retrofit.create(UploadFileForPart.class);
//        Call<IpModel> call = ipService.uploadFile(ip));
//        call.enqueue(new Callback<IpModel>() {
//            @Override
//            public void onResponse(Call<IpModel> call, Response<IpModel> response) {
//                String country = response.body().getData().get(0).getLocation();
//                Log.i("wangshu", "country:" + country);
//                Toast.makeText(getApplicationContext(), country, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(Call<IpModel> call, Throwable t) {
//
//            }
//        });
    }
}
