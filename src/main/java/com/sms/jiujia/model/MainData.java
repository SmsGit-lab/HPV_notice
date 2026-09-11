package com.sms.jiujia.model;

/**
 * @Author songmingsong
 * @Date 2023/4/28
 **/
public class MainData {
    private int state;
    private String message;
    private Data data;
    private String title;
    private String desc;
    private boolean daofen;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isDaofen() {
        return daofen;
    }

    public void setDaofen(boolean daofen) {
        this.daofen = daofen;
    }

    @Override
    public String toString() {
        return "MainData{" +
                "state=" + state +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", daofen=" + daofen +
                '}';
    }
}
