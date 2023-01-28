package com.example.liuwangshu.moonmvpsimple.model;

import java.util.List;

/**
 * Created by Administrator on 2016/12/25 0025.
 */

public class IpInfo {

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

    @Override
    public String toString() {
        return "IpInfo{" +
                "status='" + status + '\'' +
                ", t='" + t + '\'' +
                ", setCacheTime='" + setCacheTime + '\'' +
                ", data=" + data +
                '}';
    }
}