package com.example.liuwangshu.moonretrofit;

import com.example.liuwangshu.moonretrofit.model.IpModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2016/10/31 0031.
 */

public interface IpServiceForPath {
//    @GET("{path}/getIpInfo.php?ip=59.108.54.37")
    @GET("{path}/?query=117.136.12.79&co=&resource_id=6006&oe=utf8")
    Call<IpModel> getIpMsg(@Path("path") String path);
}
