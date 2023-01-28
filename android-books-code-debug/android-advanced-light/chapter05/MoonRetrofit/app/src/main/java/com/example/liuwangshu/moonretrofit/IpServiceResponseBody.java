package com.example.liuwangshu.moonretrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface IpServiceResponseBody {
//    @Headers({
//            "Accept-Encoding: application/json",
//            "User-Agent: MoonRetrofit"
//    })
//    @GET("outGetIpInfo?ip=39.155.184.147")
    @GET("?query=117.136.12.79&co=&resource_id=6006&oe=utf8")
    Call<ResponseBody> getIpMsg();
}
