package com.example.liuwangshu.moonmvpsimple.model;

/**
 * Created by Administrator on 2016/12/25 0025.
 */

public class IpData {

    private String extendedlocation;
    private String originquery;
    private String appinfo;
    private int dispType;
    private String fetchkey;
    private String location;
    private String origip;
    private String origipquery;
    private String resourceid;
    private int roleId;
    private int shareimage;
    private int showlikeshare;
    private String showlamp;
    private String titlecont;
    private String tplt;


    public void setExtendedlocation(String extendedlocation) {
        this.extendedlocation = extendedlocation;
    }
    public String getExtendedlocation() {
        return extendedlocation;
    }


    public void setOriginquery(String originquery) {
        this.originquery = originquery;
    }
    public String getOriginquery() {
        return originquery;
    }


    public void setAppinfo(String appinfo) {
        this.appinfo = appinfo;
    }
    public String getAppinfo() {
        return appinfo;
    }


    public void setDispType(int dispType) {
        this.dispType = dispType;
    }
    public int getDispType() {
        return dispType;
    }


    public void setFetchkey(String fetchkey) {
        this.fetchkey = fetchkey;
    }
    public String getFetchkey() {
        return fetchkey;
    }


    public void setLocation(String location) {
        this.location = location;
    }
    public String getLocation() {
        return location;
    }


    public void setOrigip(String origip) {
        this.origip = origip;
    }
    public String getOrigip() {
        return origip;
    }


    public void setOrigipquery(String origipquery) {
        this.origipquery = origipquery;
    }
    public String getOrigipquery() {
        return origipquery;
    }


    public void setResourceid(String resourceid) {
        this.resourceid = resourceid;
    }
    public String getResourceid() {
        return resourceid;
    }


    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
    public int getRoleId() {
        return roleId;
    }


    public void setShareimage(int shareimage) {
        this.shareimage = shareimage;
    }
    public int getShareimage() {
        return shareimage;
    }


    public void setShowlikeshare(int showlikeshare) {
        this.showlikeshare = showlikeshare;
    }
    public int getShowlikeshare() {
        return showlikeshare;
    }


    public void setShowlamp(String showlamp) {
        this.showlamp = showlamp;
    }
    public String getShowlamp() {
        return showlamp;
    }


    public void setTitlecont(String titlecont) {
        this.titlecont = titlecont;
    }
    public String getTitlecont() {
        return titlecont;
    }


    public void setTplt(String tplt) {
        this.tplt = tplt;
    }
    public String getTplt() {
        return tplt;
    }

    @Override
    public String toString() {
        return "IpData{" +
                "extendedlocation='" + extendedlocation + '\'' +
                ", originquery='" + originquery + '\'' +
                ", appinfo='" + appinfo + '\'' +
                ", dispType=" + dispType +
                ", fetchkey='" + fetchkey + '\'' +
                ", location='" + location + '\'' +
                ", origip='" + origip + '\'' +
                ", origipquery='" + origipquery + '\'' +
                ", resourceid='" + resourceid + '\'' +
                ", roleId=" + roleId +
                ", shareimage=" + shareimage +
                ", showlikeshare=" + showlikeshare +
                ", showlamp='" + showlamp + '\'' +
                ", titlecont='" + titlecont + '\'' +
                ", tplt='" + tplt + '\'' +
                '}';
    }
}