package com.example.liuwangshu.moonretrofit.model;

import com.example.liuwangshu.moonretrofit.model.IpData;

import java.util.List;

/**
 * Created by Administrator on 2016/9/8 0008.
 */
public class IpModel {

    private String status;
    private String t;
    private String setCacheTime;
    private List<IpData> data;


    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }


    public void setT(String t) {
        this.t = t;
    }
    public String getT() {
        return t;
    }


    public void setSetCacheTime(String setCacheTime) {
        this.setCacheTime = setCacheTime;
    }
    public String getSetCacheTime() {
        return setCacheTime;
    }


    public void setData(List<IpData> data) {
        this.data = data;
    }
    public List<IpData> getData() {
        return data;
    }
}
