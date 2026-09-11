package com.sms.jiujia.service;

import com.sms.jiujia.model.MainData;
import com.sms.jiujia.ui.setting.Constant;
import com.sms.jiujia.utils.DateUtils;
import com.sms.jiujia.utils.LogUtils;
import com.sms.jiujia.utils.PushMsgUtils;
import com.sms.jiujia.utils.UtilTools;

import java.util.Date;
import java.util.TimerTask;

/**
 * @author songmingsong
 * @date 2023/5/11
 */
public class CurlTimer extends TimerTask {
    private ProcessBuilder curlCmd = null;


    public CurlTimer(ProcessBuilder curlCmd) {
        this.curlCmd = curlCmd;
    }

    @Override
    public void run() {
        try {
            query();
        } catch (Exception e) {
            // 单次查询失败不能让定时任务中断，否则监控会静默停止
            LogUtils.getLayoutConsoleLogInstance().log("[error]->" + DateUtils.changeDate(new Date())
                    + "查询失败：" + e.getMessage());
        }
    }

    private void query() {
        LogUtils.getLayoutConsoleLogInstance().log("[info]->" + new Date() + " : 任务「" + Constant.CITY + "」被执行。");
        String execCurlJson = UtilTools.execCurl(curlCmd);
        MainData mainData = UtilTools.coverJson(execCurlJson);
        LogUtils.getLayoutConsoleLogInstance().log(mainData.toString());
        PushMsgUtils.PushResult result = PushMsgUtils.pushDeer(mainData, false);
        if (result.isSkipped()) {
            LogUtils.getLayoutConsoleLogInstance().log("[info]->" + DateUtils.changeDate(new Date()) + "没有新的放号信息，不推送");
        } else if (result.isSuccess()) {
            LogUtils.getLayoutConsoleLogInstance().log("[info]->" + DateUtils.changeDate(new Date()) + "推送成功！\n" + mainData);
        } else {
            LogUtils.getLayoutConsoleLogInstance().log("[error]->" + DateUtils.changeDate(new Date()) + "推送失败：" + result.getMessage());
        }
    }


}
