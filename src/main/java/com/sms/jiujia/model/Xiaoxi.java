package com.sms.jiujia.model;

/**
 * @Author songmingsong
 * @Date 2023/4/28
 **/
public class Xiaoxi {
    private String id;
    private String cityname;
    private String title;
    private String url;
    private String orderid;
    private String citycode;
    private UrlArr url_arr;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public UrlArr getUrl_arr() {
        return url_arr;
    }

    public void setUrl_arr(UrlArr url_arr) {
        this.url_arr = url_arr;
    }

    @Override
    public String toString() {
        return "Xiaoxi{" +
                "id='" + id + '\'' +
                ", cityname='" + cityname + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", orderid='" + orderid + '\'' +
                ", citycode='" + citycode + '\'' +
                ", url_arr=" + url_arr +
                '}';
    }
}
