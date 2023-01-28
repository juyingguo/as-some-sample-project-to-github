package com.example.liuwangshu.moonrxjava.model;


import java.util.List;

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

    @Override
    public String toString() {
        return "IpModel{" +
                "status='" + status + '\'' +
                ", t='" + t + '\'' +
                ", setCacheTime='" + setCacheTime + '\'' +
                ", data=" + data +
                '}';
    }
}
