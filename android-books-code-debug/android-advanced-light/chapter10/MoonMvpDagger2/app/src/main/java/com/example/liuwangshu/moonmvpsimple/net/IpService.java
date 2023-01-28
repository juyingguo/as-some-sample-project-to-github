package com.example.liuwangshu.moonmvpsimple.net;

import com.example.liuwangshu.moonmvpsimple.model.IpInfo;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Administrator on 2016/12/30 0030.
 */

public interface IpService {
    @FormUrlEncoded
    @POST("api.php?co=&resource_id=6006&oe=utf8")
    Observable<IpInfo> getIpInfo(@Field("query") String ip);
}
