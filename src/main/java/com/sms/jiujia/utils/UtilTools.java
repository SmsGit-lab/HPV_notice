package com.sms.jiujia.utils;

import com.alibaba.fastjson2.JSONObject;
import com.sms.jiujia.model.City;
import com.sms.jiujia.model.MainData;
import com.sms.jiujia.ui.setting.Constant;
import javafx.scene.layout.Pane;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static com.sms.jiujia.ui.setting.Constant.cities;

/**
 * @Author songmingsong
 * @Date 2023/4/28
 **/
public class UtilTools {
    /**
     * 获取新Pane
     *
     * @return
     */
    public static Pane getNewPane() {
        return new Pane();
    }

    /**
     * Unicode转换
     *
     * @param jsonStr
     * @return
     */
    public static String convertUnicode(String jsonStr) {
        StringBuilder sb = new StringBuilder();
        int i = -1;
        int pos = 0;
        while ((i = jsonStr.indexOf("\\u", pos)) != -1) {
            sb.append(jsonStr.substring(pos, i));
            if (i + 5 < jsonStr.length()) {
                pos = i + 6;
                sb.append((char) Integer.parseInt(jsonStr.substring(i + 2, i + 6), 16));
            }
        }
        sb.append(jsonStr.substring(pos));
        return sb.substring(sb.indexOf("{"), sb.length());
    }

    /**
     * 执行Curl语句，返回结果值
     *
     * @param pb
     * @return
     * @throws IOException
     */
    public static String execCurl(ProcessBuilder pb) {
        pb.redirectErrorStream(true);
        Process p = null;
        StringBuilder builder = null;
        try {
            p = pb.start();
            // 固定按 UTF-8 读取，不依赖启动参数里的 file.encoding
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8));
            String line;
            builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convertUnicode(builder.toString());
    }


    /**
     * 将json字符串转为java类
     *
     * @param jsonData
     * @return
     * @throws IOException
     */
    public static MainData coverJson(String jsonData) {
        JSONObject jsonObject = (JSONObject) JSONObject.parse(jsonData);
        MainData mainData = jsonObject.toJavaObject(MainData.class);
        return mainData;
    }

    /**
     * 解析城市代码文件
     * @param filePath
     */
    public static void parseCitiesFromTextFile(String filePath) {

        try {
            // 从文件路径创建一个新的File对象。
            File file = new File(filePath);
            // 创建一个新的Scanner对象来读取文件。
            Scanner scanner = new Scanner(file);

            // 循环遍历文件中的每一行。
            while (scanner.hasNextLine()) {
                // 读取该行，并将其拆分为字符串数组。
                String[] line = scanner.nextLine().split(": ");

                if (line.length == 4) {
                    // 该行表示一个城市。
                    String cityName = line[1].split(" ")[0];
                    String cityCode = line[2].split(" ")[0];
                    String province = line[3];
                    // 创建一个新的City对象，并将其添加到列表中。
                    cities.add(new City(cityName, cityCode, province));
                }
            }
            // 关闭Scanner。
            scanner.close();
        } catch (FileNotFoundException e) {
            // 处理文件未找到的错误。
            e.printStackTrace();
        }
    }

    public static void parseCitiesFromTextFile2(InputStream file) throws IOException {
        InputStreamReader isr = new InputStreamReader(file,"UTF-8");
        BufferedReader reader = new BufferedReader(isr);

        String line = null;

        while ((line = reader.readLine()) != null) {

            String[] cityInfo = line.split(": ");

            if (cityInfo.length == 4) {

                String cityName = cityInfo[1].split(" ")[0];

                String cityCode = cityInfo[2].split(" ")[0];

                String province = cityInfo[3];

                cities.add(new City(cityName, cityCode, province));

            }

        }

        reader.close();

    }
}
