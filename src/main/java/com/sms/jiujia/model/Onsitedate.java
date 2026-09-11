package com.sms.jiujia.model;

import java.util.List;

/**
 * @Author songmingsong
 * @Date 2023/4/28
 **/
public class Onsitedate {
    private List<Place> place;

    public List<Place> getPlace() {
        return place;
    }

    public void setPlace(List<Place> place) {
        this.place = place;
    }

    @Override
    public String toString() {
        return "Onsitedate{" +
                "place=" + place +
                '}';
    }
}
