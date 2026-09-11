package com.sms.jiujia.ui.setting;

import com.sms.jiujia.model.City;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * @Author songmingsong
 * @Date 2023/4/28
 **/
public class Constant {
    /**
     * 城市,默认为成都
     */
    public static Setting setting = null;

    /**
     * 城市,默认为成都
     */
    public static String CITY = "cd";

    /**
     * 城市,代码集合
     */
    public static List<City> cities = new ArrayList<>();

    /**
     * 定时器
     */
    public static Timer timer = null;


    /**
     * pushDeer消息推送接口地址，参数以表单（POST）方式提交，
     * 具体消息格式见 PushMsgUtils#buildPlaceBlock
     */
    public static final String PUSH_DEER_URL = "https://api2.pushdeer.com/message/push";


    /**
     * pushDeer消息推送用户的key
     */
    public static String PUSH_DEER_KEY;

    /**
     * 用户是否输入过pushDeerKey
     */
    public static Boolean has_PUSH_DEER_KEY = false;
}
