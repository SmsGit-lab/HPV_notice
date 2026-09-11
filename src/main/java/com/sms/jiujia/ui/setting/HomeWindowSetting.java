package com.sms.jiujia.ui.setting;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * home窗体
 *
 * @Author songmingsong
 * @Date 2023/4/28
 **/
public class HomeWindowSetting {

    private static final Integer HEIGHT = 720;
    private static final Integer WIDTH = 660;
    private static final Integer MIN_HEIGHT = 660;
    private static final Integer MIN_WIDTH = 580;
    private static final Boolean IS_REGULAR = true;

    public static void set(Stage primaryStage) {
        // 屏幕分辨率或缩放开销较大时，避免窗口超出屏幕
        Rectangle2D screen = Screen.getPrimary().getVisualBounds();
        double width = Math.min(WIDTH, screen.getWidth() - 40);
        double height = Math.min(HEIGHT, screen.getHeight() - 40);

        primaryStage.setWidth(width);
        primaryStage.setHeight(height);
        primaryStage.setMinWidth(Math.min(MIN_WIDTH, width));
        primaryStage.setMinHeight(Math.min(MIN_HEIGHT, height));
        primaryStage.setResizable(IS_REGULAR);
        primaryStage.centerOnScreen();
    }
}
