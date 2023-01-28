package com.example.liuwangshu.moonrxjava.model;


import java.util.List;

public class HttpResult2<T> {
    private String status;
    private String t;
    private String setCacheTime;
    private T data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getSetCacheTime() {
        return setCacheTime;
    }

    public void setSetCacheTime(String setCacheTime) {
        this.setCacheTime = setCacheTime;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
