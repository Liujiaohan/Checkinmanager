package com.liujiaohan.checkinmanager;

import cn.bmob.v3.BmobObject;

/**
 * Created by Liu jiaohan on 2017-07-04.
 */
public class Message extends BmobObject{

    private int id;

    private String numbering;

    private String location;

    private String forest;

    private String time;

    private String longitude;

    private String latitude;

    private String altitude;

    private String femaleCount;

    private String maleCount;

    private String total;

    public Message(String numbering, String location, String forest, String time, String longitude,
                   String latitude, String altitude, String femaleCount, String maleCount, String total) {
        this.numbering = numbering;
        this.location = location;
        this.forest = forest;
        this.time = time;
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
        this.femaleCount = femaleCount;
        this.maleCount = maleCount;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumbering() {
        return numbering;
    }

    public void setNumbering(String numbering) {
        this.numbering = numbering;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getForest() {
        return forest;
    }

    public void setForest(String forest) {
        this.forest = forest;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getFemaleCount() {
        return femaleCount;
    }

    public void setFemaleCount(String femaleCount) {
        this.femaleCount = femaleCount;
    }

    public String getMaleCount() {
        return maleCount;
    }

    public void setMaleCount(String maleCount) {
        this.maleCount = maleCount;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "编号为"+numbering+"地点为"+location+"时间为"+time+"天牛总数为"+total;
    }
}
