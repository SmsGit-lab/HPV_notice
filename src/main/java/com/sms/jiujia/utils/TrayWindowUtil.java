package com.sms.jiujia.utils;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Objects;

import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * @author songmingsong
 * @date 2023/5/5
 * 托盘窗口工具类
 */
public class TrayWindowUtil {

    private static final String APP_NAME = "九价疫苗信息推送";

    /**
     * 托盘菜单是 AWT 原生菜单，文字由 JVM 按平台编码转换：
     * 当 file.encoding 与系统编码不一致（例如启动参数里加了 -Dfile.encoding=UTF-8）时，
     * 中文会显示成方框。这种情况下菜单退回英文，保证始终可读。
     */
    private static final boolean NATIVE_CHINESE_OK = nativeChineseSupported();

    private static TrayWindowUtil instance;
    private static MenuItem showItem;
    private static MenuItem exitItem;
    private static TrayIcon trayIcon;
    private static ActionListener showListener;
    private static ActionListener exitListener;
    private static MouseAdapter mouseListener;

    static {
        //执行stage.close()方法,窗口不直接退出
        // 默认情况下，所有窗口都被关闭了，那么程序就结束了
        Platform.setImplicitExit(false);
        //菜单项(打开)
        showItem = new MenuItem(NATIVE_CHINESE_OK ? "打开主界面" : "Open");
        //菜单项(退出)
        exitItem = new MenuItem(NATIVE_CHINESE_OK ? "退出程序" : "Exit");
        //此处不能选择ico格式的图片,要使用16*16的png格式的图片
        URL url = TrayWindowUtil.class.getResource("/ui/OIP.png");
        Image image = Toolkit.getDefaultToolkit().getImage(url);
        //系统托盘图标
        trayIcon = new TrayIcon(image);
        //初始化监听事件(空)
        showListener = e -> Platform.runLater(() -> {
        });
        exitListener = e -> {
        };
        mouseListener = new MouseAdapter() {
        };
        if (!NATIVE_CHINESE_OK) {
            LogUtils.getLayoutConsoleLogInstance().log("[warn]->检测到启动编码与系统编码不一致，托盘菜单已切换为英文；"
                    + "去掉 -Dfile.encoding 启动参数即可恢复中文菜单");
        }
    }

    /**
     * 判断原生菜单能否正确显示中文
     */
    private static boolean nativeChineseSupported() {
        String fileEncoding = System.getProperty("file.encoding");
        String platformEncoding = System.getProperty("sun.jnu.encoding");
        if (Objects.isNull(fileEncoding) || Objects.isNull(platformEncoding)
                || !platformEncoding.equalsIgnoreCase(fileEncoding)) {
            return false;
        }
        try {
            return Charset.forName(platformEncoding).newEncoder().canEncode(APP_NAME);
        } catch (Exception e) {
            return false;
        }
    }


    public static TrayWindowUtil getInstance() {
        if (instance == null) {
            instance = new TrayWindowUtil();
        }
        return instance;
    }

    private TrayWindowUtil() {
        try {
            //检查系统是否支持托盘
            if (!SystemTray.isSupported()) {
                //系统托盘不支持
                System.out.println(Thread.currentThread().getStackTrace()[1].getClassName() + ":系统托盘不支持");
                return;
            }
            //设置图标尺寸自动适应
            trayIcon.setImageAutoSize(true);
            //系统托盘
            SystemTray tray = SystemTray.getSystemTray();
            //弹出式菜单组件
            final PopupMenu popup = new PopupMenu();
            popup.add(new MenuItem(NATIVE_CHINESE_OK ? APP_NAME + " v1.0" : "Jiujia Notice v1.0"));
            popup.add(showItem);
            popup.add(exitItem);
            trayIcon.setPopupMenu(popup);
            //鼠标移到系统托盘,会显示提示文本
            trayIcon.setToolTip(NATIVE_CHINESE_OK ? APP_NAME + "服务" : "Jiujia Notice");
            tray.add(trayIcon);
        } catch (Exception e) {
            //系统托盘添加失败
            System.out.println(Thread.currentThread().getStackTrace()[1].getClassName() + ":系统添加失败");
        }
    }

    /**
     * 更改系统托盘所监听的Stage
     */
    public void listen(Stage stage) {
        //防止报空指针异常
        if (showListener == null || exitListener == null || mouseListener == null || showItem == null || exitItem == null || trayIcon == null) {
            return;
        }
        //移除原来的事件
        showItem.removeActionListener(showListener);
        exitItem.removeActionListener(exitListener);
        trayIcon.removeMouseListener(mouseListener);
        //行为事件: 点击"打开"按钮,显示窗口
        showListener = e -> Platform.runLater(() -> showStage(stage));
        //行为事件: 点击"退出"按钮, 就退出系统
        exitListener = e -> {
            System.exit(0);
        };
        //鼠标行为事件: 单机显示stage
        mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //鼠标左键
                if (e.getButton() == MouseEvent.BUTTON1) {
                    showStage(stage);
                }
            }
        };
        //给菜单项添加事件
        showItem.addActionListener(showListener);
        exitItem.addActionListener(exitListener);
        //给系统托盘添加鼠标响应事件
        trayIcon.addMouseListener(mouseListener);
    }

    /**
     * 关闭窗口
     */
    public void hide(Stage stage) {
        Platform.runLater(() -> {
            //如果支持系统托盘,就隐藏到托盘,不支持就直接退出
            if (SystemTray.isSupported()) {
                //stage.hide()与stage.close()等价
                stage.hide();
            } else {
                System.exit(0);
            }
        });
    }

    /**
     * 点击系统托盘,显示界面(并且显示在最前面,将最小化的状态设为false)
     */
    private void showStage(Stage stage) {
        //点击系统托盘,
        Platform.runLater(() -> {
            if (stage.isIconified()) {
                stage.setIconified(false);
            }
            if (!stage.isShowing()) {
                stage.show();
            }
            // 将窗口置于前景中：如果被其他窗口覆盖，可以显示在最前面来
            stage.toFront();
        });
    }
}
