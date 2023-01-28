package com.example.liuwangshu.moonmvpsimple.model;

/**
 * Created by Administrator on 2016/12/25 0025.
 */

public class IpData {
    /*接口及返回json::
       https://ip.useragentinfo.com/json?ip=117.136.12.79
       {"country": "中国", "short_name": "CN", "province": "广东省", "city": "", "area": "",
       "isp": "移动", "net": "移动网络", "ip": "117.136.12.79", "code": 200, "desc": "success"}
       */
    private String country;
    private String country_id;
    private String province;
    private String area;
    private String area_id;
    private String region;
    private String region_id;
    private String city;
    private String city_id;
    private String county;
    private String county_id;
    private String isp;
    private String isp_id;
    private String net;
    private String ip;
    private String short_name;
    private String desc;
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getNet() {
        return net;
    }

    public void setNet(String net) {
        this.net = net;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegion_id() {
        return region_id;
    }

    public void setRegion_id(String region_id) {
        this.region_id = region_id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCounty_id() {
        return county_id;
    }

    public void setCounty_id(String county_id) {
        this.county_id = county_id;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public String getIsp_id() {
        return isp_id;
    }

    public void setIsp_id(String isp_id) {
        this.isp_id = isp_id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "IpData{" +
                "country='" + country + '\'' +
                ", country_id='" + country_id + '\'' +
                ", province='" + province + '\'' +
                ", area='" + area + '\'' +
                ", area_id='" + area_id + '\'' +
                ", region='" + region + '\'' +
                ", region_id='" + region_id + '\'' +
                ", city='" + city + '\'' +
                ", city_id='" + city_id + '\'' +
                ", county='" + county + '\'' +
                ", county_id='" + county_id + '\'' +
                ", isp='" + isp + '\'' +
                ", isp_id='" + isp_id + '\'' +
                ", net='" + net + '\'' +
                ", ip='" + ip + '\'' +
                ", short_name='" + short_name + '\'' +
                ", desc='" + desc + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}