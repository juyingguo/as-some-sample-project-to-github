package com.example.liuwangshu.moonretrofit;

import com.example.liuwangshu.moonretrofit.model.IpModel;
import com.example.liuwangshu.moonretrofit.model.IpQuery;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Administrator on 2016/11/1 0001.
 */

public interface UploadFileForPart {
    @Multipart
    @POST("user/photo")
    Call<ResponseBody> uploadFile(@Part MultipartBody.Part photo, @Part("description")RequestBody description);
}
