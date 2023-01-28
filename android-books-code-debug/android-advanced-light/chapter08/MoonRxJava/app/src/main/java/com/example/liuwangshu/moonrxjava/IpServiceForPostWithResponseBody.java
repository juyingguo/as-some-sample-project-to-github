package com.example.liuwangshu.moonrxjava;


import com.example.liuwangshu.moonrxjava.model.HttpResult;
import com.example.liuwangshu.moonrxjava.model.IpData;

import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2016/10/31 0031.
 */

public interface IpServiceForPostWithResponseBody {
    @FormUrlEncoded
    @POST("?resource_id=6006&oe=utf8")
    Observable<ResponseBody> getIpMsg(@Field("query") String first);
}
