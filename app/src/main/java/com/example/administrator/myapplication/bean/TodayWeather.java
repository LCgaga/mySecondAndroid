package com.example.administrator.myapplication.bean;

/**
 * Created by Administrator on 2016/10/11.
 */
public class TodayWeather {
    private String city;

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWendu() {
        return wendu;
    }

    public void setWendu(String wendu) {
        this.wendu = wendu;
    }

    public String getShidu() {
        return shidu;
    }

    public void setShidu(String shidu) {
        this.shidu = shidu;
    }

    public String getPm25() {
        return pm25;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getFengxiang() {
        return fengxiang;
    }

    public void setFengxiang(String fengxiang) {
        this.fengxiang = fengxiang;
    }

    public String getFengli() {
        return fengli;
    }

    public void setFengli(String fengli) {
        this.fengli = fengli;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getB1_day() {
        return b1_day;
    }

    public void setB1_day(String b1_day) {
        this.b1_day = b1_day;
    }

    public String getA3_day() {
        return a3_day;
    }

    public void setA3_day(String a3_day) {
        this.a3_day = a3_day;
    }

    public String getB1_type() {
        return b1_type;
    }

    public void setB1_type(String b1_type) {
        this.b1_type = b1_type;
    }

    public String getA1_type() {
        return a1_type;
    }

    public void setA1_type(String a1_type) {
        this.a1_type = a1_type;
    }

    public String getA1_day() {
        return a1_day;
    }

    public void setA1_day(String a1_day) {
        this.a1_day = a1_day;
    }

    public String getA2_type() {
        return a2_type;
    }

    public void setA2_type(String a2_type) {
        this.a2_type = a2_type;
    }

    public String getA2_day() {
        return a2_day;
    }

    public void setA2_day(String a2_day) {
        this.a2_day = a2_day;
    }

    public String getA3_type() {
        return a3_type;
    }

    public void setA3_type(String a3_type) {
        this.a3_type = a3_type;
    }

    public String getA4_type() {
        return a4_type;
    }

    public void setA4_type(String a4_type) {
        this.a4_type = a4_type;
    }

    public String getA4_day() {
        return a4_day;
    }

    public void setA4_day(String a4_day) {
        this.a4_day = a4_day;
    }


    private String date;

    @Override
    public String toString() {
        return "TodayWeather{" +
                "city='" + city + '\'' +
                ", date='" + date + '\'' +
                ", high='" + high + '\'' +
                ", low='" + low + '\'' +
                ", type='" + type + '\'' +
                ", updatetime='" + updatetime + '\'' +
                ", wendu='" + wendu + '\'' +
                ", shidu='" + shidu + '\'' +
                ", pm25='" + pm25 + '\'' +
                ", quality='" + quality + '\'' +
                ", fengxiang='" + fengxiang + '\'' +
                ", fengli='" + fengli + '\'' +
                ", b1_type='" + b1_type + '\'' +
                ", b1_day='" + b1_day + '\'' +
                ", a1_type='" + a1_type + '\'' +
                ", a1_day='" + a1_day + '\'' +
                ", a2_type='" + a2_type + '\'' +
                ", a2_day='" + a2_day + '\'' +
                ", a3_type='" + a3_type + '\'' +
                ", a3_day='" + a3_day + '\'' +
                ", a4_type='" + a4_type + '\'' +
                ", a4_day='" + a4_day + '\'' +
                '}';
    }

    private String high;
    private String low;
    private String type;
    private String updatetime;
    private String wendu;
    private String shidu;
    private String pm25;
    private String quality;
    private String fengxiang;
    private String fengli;
    private String b1_type;
    private String b1_day;
    private String a1_type;
    private String a1_day;
    private String a2_type;
    private String a2_day;
    private String a3_type;
    private String a3_day;
    private String a4_type;
    private String a4_day;
}
