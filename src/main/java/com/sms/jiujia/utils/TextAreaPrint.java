package com.sms.jiujia.utils;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * @author songmingsong
 * @date 2023/5/5
 */
public class TextAreaPrint extends PrintStream {
    private TextArea console;

    public TextAreaPrint(TextArea console) {
        super(new ByteArrayOutputStream());
        this.console = console;
    }

    @Override
    public void write(byte[] buf, int off, int len) {
        print(new String(buf, off, len));
    }

    @Override
    public void print(String s) {
        Platform.runLater(() -> {
            console.appendText(s);
            // 始终滚动到最新一行
            console.setScrollTop(Double.MAX_VALUE);
        });
    }

    public void clear() {
        Platform.runLater(() -> console.clear());
    }
}
