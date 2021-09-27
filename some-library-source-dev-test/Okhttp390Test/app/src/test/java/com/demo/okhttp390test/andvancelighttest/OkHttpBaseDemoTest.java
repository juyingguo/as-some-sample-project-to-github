package com.demo.okhttp390test.andvancelighttest;

import com.demo.okhttp390test.FileUtils;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 * ref:android-advanced-light\chapter_5\MoonOkHttp3
 */
public class OkHttpBaseDemoTest {
    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private OkHttpClient mOkHttpClient;
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    @Before
    public void init(){
        initOkHttpClient();
    }
    private void initOkHttpClient() {
//        File sdcache = getExternalCacheDir();
        File sDcache = new File("D:/DATA/okhttp3");
        FileUtils.createOrExistsDir(sDcache);
        int cacheSize = 10 * 1024 * 1024;
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .cache(new Cache(sDcache.getAbsoluteFile(), cacheSize));
        mOkHttpClient = builder.build();
    }
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    /**  get异步请求 */
    @Test
    public void getAsynHttpUseEnqueue() {
        Request.Builder requestBuilder = new Request.Builder().url("http://www.baidu.com");
        requestBuilder.method("GET", null);
        Request request = requestBuilder.build();
        Call mCall = mOkHttpClient.newCall(request);
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                System.out.println("onResponse");
            }
        });
        sleep(2000);
    }
    /**  get异步请求 */
    @Test
    public void getAsynHttpUseEnqueueTwice() {
        Request.Builder requestBuilder = new Request.Builder().url("http://www.baidu.com");
        requestBuilder.method("GET", null);
        Request request = requestBuilder.build();
        Call mCall = mOkHttpClient.newCall(request);
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                System.out.println("onResponse");
            }
        });

        try {
            mCall.enqueue(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sleep(2000);
    }
    @Test
    public void getAsynForEngine(){
        OkHttpEngine.getInstance().getAsynHttp("http://www.baidu.com", new ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {

            }
            @Override
            public void onResponse(String str) throws IOException{
                System.out.println("onResponse");
            }
        });
    }
    /**
     * post异步请求
     */
    @Test
    public void postAsynHttp() {
        RequestBody formBody = new FormBody.Builder()
                .add("ip", "59.108.54.37")
                .build();
        Request request = new Request.Builder()
                .url("https://ip.taobao.com/outGetIpInfo")
                .post(formBody)
                .build();
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("onFailure,call.isCanceled():" + call.isCanceled()  + " IOException:" + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                System.out.println(str);
            }

        });
        sleep(2000);
    }

    /**
     * 异步上传文件
     */
    @Test
    public void postAsynFile() {
        File sDcache = new File("D:/DATA/okhttp3");
        if (!FileUtils.isDir(sDcache)) FileUtils.createOrExistsDir(sDcache);

        String filepath = sDcache.getAbsolutePath();
        /*if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            filepath = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            filepath = getFilesDir().getAbsolutePath();
        }*/
        File file = new File(filepath, "wangshu.txt");
        Request request = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, file))
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.d(TAG, response.body().string());
                System.out.println(response.body().string());
            }
        });
        sleep(2000);
    }
    @Test
    public void postUseExecuteSyncUploadFile() {
        File sDcache = new File("D:/DATA/okhttp3");
        if (!FileUtils.isDir(sDcache)) FileUtils.createOrExistsDir(sDcache);

        String filepath = sDcache.getAbsolutePath();
        /*if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            filepath = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            filepath = getFilesDir().getAbsolutePath();
        }*/
        File file = new File(filepath, "wangshu.txt");
        Request request = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, file))
                .build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            System.out.println(response.body().string());
            if(response.body() != null)
                response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sleep(2000);
    }
    /**
     * 异步下载文件
     */
    @Test
    public void downAsynFileUseEnqueue() {
        String url = "https://img0.baidu.com/it/u=205282336,1640876785&fm=253&fmt=auto&app=120&f=JPEG?w=690&h=460";
        Request request = new Request.Builder().url(url).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) {
                InputStream inputStream = response.body().byteStream();
                FileOutputStream fileOutputStream = null;
                String filepath = "";
                try {
                    /*if (Environment.getExternalStorageState().equals(
                            Environment.MEDIA_MOUNTED)) {
                        filepath = Environment.getExternalStorageDirectory().getAbsolutePath();
                    } else {
                        filepath = getFilesDir().getAbsolutePath();
                    }*/
                    File sDcache = new File("D:/DATA/okhttp3");
                    if (!FileUtils.isDir(sDcache)) FileUtils.createOrExistsDir(sDcache);
                    File file = new File(sDcache.getAbsolutePath(), "wangshu.jpg");
                    if (null != file) {
                        fileOutputStream = new FileOutputStream(file);
                        byte[] buffer = new byte[2048];
                        int len = 0;
                        while ((len = inputStream.read(buffer)) != -1) {
                            fileOutputStream.write(buffer, 0, len);
                        }
                        fileOutputStream.flush();
                        /*runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "文件存储成功", Toast.LENGTH_SHORT).show();
                            }
                        });*/
                        System.out.println("onResponse download file success.");
                    } else {
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        sleep(2000);
    }
    /** server address to be done */
    @Test
    public void sendMultipartTest() {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title", "wangshu")
                .addFormDataPart("image", "wangshu.jpg",
                        RequestBody.create(MEDIA_TYPE_PNG, new File("/sdcard/wangshu.jpg")))
                .build();

        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + "...")
                .url("https://api.imgur.com/3/image")
                .post(requestBody)
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println(response.body().string());
            }
        });
    }
    @Test
    public void cancelTest() {
        final Request request = new Request.Builder()
                .url("http://www.baidu.com")
                .cacheControl(CacheControl.FORCE_NETWORK)
                .build();
        Call call = null;
        call = mOkHttpClient.newCall(request);
        final Call finalCall = call;
        //100毫秒后取消call
        executor.schedule(new Runnable() {
            @Override
            public void run() {
                finalCall.cancel();
            }
        }, 1, TimeUnit.MILLISECONDS);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("onFailure,call.isCanceled():" + call.isCanceled()  + " IOException:" + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null != response.cacheResponse()) {
                    String str = response.cacheResponse().toString();
                    System.out.println("cache---" + str);
                } else {
                    String str = response.networkResponse().toString();
                    System.out.println("network---" + str);
                }
            }
        });

        sleep(2000);
    }
    private void sleep(long millis){
        //should sleep;because net callback need time,after test method end,will not receive info any more.
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}