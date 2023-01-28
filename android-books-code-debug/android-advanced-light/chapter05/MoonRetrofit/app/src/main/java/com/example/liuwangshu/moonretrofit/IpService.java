package com.example.liuwangshu.moonretrofit;

import com.example.liuwangshu.moonretrofit.model.IpModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface IpService {
    @Headers({
            "Accept-Encoding: application/json",
            "User-Agent: MoonRetrofit"
    })
//    @GET("getIpInfo.php?ip=117.136.12.79")
    @GET("?query=117.136.12.79&co=&resource_id=6006&oe=utf8")
    Call<IpModel> getIpMsg();
}
