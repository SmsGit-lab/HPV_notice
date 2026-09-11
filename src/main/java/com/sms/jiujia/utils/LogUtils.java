package com.sms.jiujia.utils;

import com.sms.jiujia.service.LayoutConsoleLog;
import com.sms.jiujia.ui.setting.Constant;

/**
 * @author songmingsong
 * @date 2023/5/6
 */
public class LogUtils {
    /**
     * 全局唯一的日志打印实例，避免每次打印都新建一个打印线程
     */
    private static volatile LayoutConsoleLog instance;

    /**
     * 获取全局日志打印实例
     *
     * @return LayoutConsoleLog
     */
    public static LayoutConsoleLog getLayoutConsoleLogInstance() {
        if (instance == null) {
            synchronized (LogUtils.class) {
                if (instance == null) {
                    instance = new LayoutConsoleLog(new TextAreaPrint(Constant.setting.getTextArea()));
                }
            }
        }
        return instance;
    }
}
