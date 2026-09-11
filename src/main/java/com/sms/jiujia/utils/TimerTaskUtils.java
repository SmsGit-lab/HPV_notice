package com.sms.jiujia.utils;

import com.sms.jiujia.service.CurlTimer;
import com.sms.jiujia.ui.setting.Constant;

import java.util.Timer;

/**
 * @author songmingsong
 * @date 2023/5/11
 */
public class TimerTaskUtils {
    /**
     * 立即执行一次，之后每 1 分钟执行一次
     *
     * @param curlCmd curl命令
     */
    public static void execute(ProcessBuilder curlCmd) {
        // 设置为守护线程，避免影响程序退出
        Constant.timer = new Timer("jiujia-notice-monitor", true);
        Constant.timer.schedule(new CurlTimer(curlCmd), 1000L, 60000L);
    }
}
