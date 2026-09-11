package com.sms.jiujia.service;


import com.sms.jiujia.utils.DateUtils;
import com.sms.jiujia.utils.TextAreaPrint;
import org.apache.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author songmingsong
 * @date 2023/5/5
 */
public class LayoutConsoleLog {
    private Logger logger = LoggerFactory.getLogger(LayoutConsoleLog.class);
    private TextAreaPrint ps;
    private Integer maxRow;
    private volatile boolean runing = true;
    private LinkedBlockingQueue<String> queue;
    /**
     * 是否同时输出到日志文件
     */
    private boolean outputToLog;

    /**
     * @param ps
     * @param maxRow 最大日志显示多少行。到达这个行数就清空一次
     */
    public LayoutConsoleLog(TextAreaPrint ps, Integer maxRow) {
        this.ps = ps;
        this.maxRow = maxRow;
        queue = new LinkedBlockingQueue(maxRow);
        run();
    }

    public LayoutConsoleLog(TextAreaPrint ps) {
        this(ps, 5000);
    }

    /**
     * 停止打印，停止后就不能再打印了
     */
    public void stop() {
        this.runing = false;
    }

    private void run() {
        new Thread(() -> {
            int lastRow = 0;
            try {
                String templog = null;
                String log = null;
                while (runing) {
                    log = queue.poll(500, TimeUnit.MILLISECONDS);
                    if (log == null) {
                        continue;
                    }
                    if (outputToLog) {
                        templog = log.substring(log.indexOf("|") + 2);
                        if (log.startsWith(Level.ERROR.toString())) {
                            logger.error(templog);
                        } else if (log.startsWith(Level.WARN.toString())) {
                            logger.error(templog);
                        } else {
                            logger.info(templog);
                        }
                    }
                    ps.println(log);
                    lastRow++;
                    if (lastRow >= maxRow) {
                        lastRow = 0;
                        ps.clear();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void log(String msg) {
        try {
            queue.offer(msg, 200, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 给日志消息增加前缀时间
     *
     * @param msg
     */
    public void logTime(String msg) {
        this.log(buildLog(Level.INFO, msg, true));
    }

    public void logTimeError(String msg) {
        this.log(buildLog(Level.ERROR, msg, true));
    }

    public void logTimeWarn(String msg) {
        this.log(buildLog(Level.WARN, msg, true));
    }

    /**
     * 构建日志信息
     *
     * @param level 日志级别
     * @param msg   信息
     * @param time  是否在信息前增加时间
     */
    private String buildLog(Level level, String msg, boolean time) {
        final StringBuffer buffer = new StringBuffer();
        buffer.append(level.toString());
        if (time) {
            buffer.append(" " + DateUtils.nowDate());
        }
        buffer.append(" | ");
        buffer.append(msg);
        return buffer.toString();
    }

    public boolean isOutputToLog() {
        return outputToLog;
    }

    public void setOutputToLog(boolean outputToLog) {
        this.outputToLog = outputToLog;
    }
}
