package com.sms.jiujia.model;

/**
 * @Author songmingsong
 * @Date 2023/4/28
 **/
public class Place {
    private String id;
    private String cityname;
    private String name;
    private String addr;
    private String minge;
    private String condition;
    private String tel;
    private String method;
    private String orderid;
    private String jd;
    private String wd;
    private String platform;
    private String yy_time;
    private String url;
    private String course;
    private String citycode;
    private UrlArr url_arr;
    private CourseArr course_arr;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getMinge() {
        return minge;
    }

    public void setMinge(String minge) {
        this.minge = minge;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getJd() {
        return jd;
    }

    public void setJd(String jd) {
        this.jd = jd;
    }

    public String getWd() {
        return wd;
    }

    public void setWd(String wd) {
        this.wd = wd;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getYy_time() {
        return yy_time;
    }

    public void setYy_time(String yy_time) {
        this.yy_time = yy_time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
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

    public CourseArr getCourse_arr() {
        return course_arr;
    }

    public void setCourse_arr(CourseArr course_arr) {
        this.course_arr = course_arr;
    }

    @Override
    public String toString() {
        return "Place{" +
                "id='" + id + '\'' +
                ", cityname='" + cityname + '\'' +
                ", name='" + name + '\'' +
                ", addr='" + addr + '\'' +
                ", minge='" + minge + '\'' +
                ", condition='" + condition + '\'' +
                ", tel='" + tel + '\'' +
                ", method='" + method + '\'' +
                ", orderid='" + orderid + '\'' +
                ", jd='" + jd + '\'' +
                ", wd='" + wd + '\'' +
                ", platform='" + platform + '\'' +
                ", yy_time='" + yy_time + '\'' +
                ", url='" + url + '\'' +
                ", course='" + course + '\'' +
                ", citycode='" + citycode + '\'' +
                ", url_arr=" + url_arr +
                ", course_arr=" + course_arr +
                '}';
    }
}
