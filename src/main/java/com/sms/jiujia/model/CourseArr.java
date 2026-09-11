package com.sms.jiujia.model;

/**
 * @Author songmingsong
 * @Date 2023/4/28
 **/
public class CourseArr {
    private String id;
    private String type;
    private String citycode;
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "CourseArr{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", citycode='" + citycode + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
