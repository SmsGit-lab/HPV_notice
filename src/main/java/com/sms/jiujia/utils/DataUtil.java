package com.sms.jiujia.utils;

import javafx.scene.control.TextField;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author songmingsong
 * @date 2023/5/12
 */
public class DataUtil {
    /**
     * 存放数据
     *
     * @param properties 使用Properties类将数据持久化到内存中
     * @param textField
     */
    public static void setMData(Properties properties, TextField textField) {
        // 获取TextField中的数据
        String data = textField.getText();
        // 将数据存入Properties对象中
        properties.setProperty("data", data);
        // 将Properties对象写入内存中
        try {
            String filePath = DataUtil.class.getClassLoader().getResource("data.properties").getFile();
            FileOutputStream outputStream = new FileOutputStream(filePath);
            properties.store(outputStream, "TextField数据持久化");
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取数据
     *
     * @param properties
     * @param textField
     */
    public static void getMData(Properties properties, TextField textField) {
        // 从内存中读取数据并设置到TextField中
        try {
            //FileInputStream inputStream = new FileInputStream("resources/data.properties");
            String filePath = DataUtil.class.getClassLoader().getResource("data.properties").getFile();
            FileInputStream inputStream = new FileInputStream(filePath);
            properties.load(inputStream);
            String savedData = properties.getProperty("data");
            textField.setText(savedData);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
