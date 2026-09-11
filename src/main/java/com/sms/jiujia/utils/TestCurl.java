package com.sms.jiujia.utils;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;

import okhttp3.*;


public class TestCurl {
    // 城市代码，查看README
    static String citycode = "cd";
    // 检查间隔时间，单位分钟
    static int checktime = 10;
    // 钉钉WebHook地址
    static String DWebHook = "xxx";
    static String Dsecret = "xxx"; // 可选：创建机器人勾选“加签”选项时使用
    // PushDeer秘钥
    // https://github.com/easychen/pushdeer
    static String PushDeerKey = "PDU22208TT0ZJh8rFXRlH2Q59aUpV9PMqPBapgYmL";
    // ServerChan秘钥
    // https://sct.ftqq.com/
    static String ServerChanKey = "xxx";

    // 程序部分，非专业人士请勿更改
    static boolean FirstRun = true;
    static String b2 = "";

//    public static void main(String[] args) throws IOException {
//        ProcessBuilder pb = new ProcessBuilder("curl", "-X", "GET", "-H", "Content-Type: application/json", "https://wxapidg.bendibao.com/smartprogram/zhuanti.php?platform=wx&version=21.12.06&action=jiujia&citycode=cd");
//
//
//        execCurl2(pb);
//        //try {
//        //    //ProcessBuilder pb = new ProcessBuilder("curl", "-X", "GET", "-H", "Content-Type: application/json", "https://wxapidg.bendibao.com/smartprogram/zhuanti.php?platform=wx&version=21.12.06&action=jiujia&citycode=cd");
//        //    //ProcessBuilder pb = new ProcessBuilder("curl", "-X", "GET", "-H", "Host: wxapidg.bendibao.com", "-H", "content-type: application/json", "-H", "User-Agent: Mozilla/5.0 (iPhone; CPU iPhone OS 15_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/8.0.16(0x18001034) NetType/4G Language/zh_CN", "-H", "Referer: https://servicewechat.com/wx2efc0705600eb6db/130/page-frame.html", "--compressed", "https://wxapidg.bendibao.com/smartprogram/zhuanti.php?platform=wx&version=21.12.06&action=jiujia&citycode=cd");
//        //    pb.redirectErrorStream(true);
//        //    Process p = pb.start();
//        //    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
//        //    String line;
//        //    StringBuilder builder = new StringBuilder();
//        //    while ((line = reader.readLine()) != null) {
//        //        //System.out.println(line);
//        //        builder.append(line);
//        //        builder.append(System.getProperty("line.separator"));
//        //    }
//        //    System.out.println(builder.toString());
//        //    System.out.println("**********");
//        //    System.out.println(convertUnicode(builder.toString()));
//        //} catch (Exception e) {
//        //    e.printStackTrace();
//        //}
//
//
//        //Timer timer = new Timer();
//        //timer.schedule(new TimerTask() {
//        //    @Override
//        //    public void run() {
//        //        if (check_data(citycode) != true) {
//        //            System.out.println(
//        //                    "[-]---------------城市代码不能为空，请输入地区代码，详情查看README---------------" + System.currentTimeMillis());
//        //            System.exit(0);
//        //        }
//        //        try {
//        //            System.out.println("输入的地区代码为: " + citycode);
//        //        } catch (IndexOutOfBoundsException e) {
//        //            System.out.println("usage: java main.java cd");
//        //        }
//        //        try {
//        //            String command = "curl -H \"Host: wxapidg.bendibao.com\" -H \"content-type: application/json\" -H \"User-Agent: Mozilla/5.0 (iPhone; CPU iPhone OS 15_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/8.0.16(0x18001034) NetType/4G Language/zh_CN\" -H \"Referer: https://servicewechat.com/wx2efc0705600eb6db/130/page-frame.html\" --compressed \"https://wxapidg.bendibao.com/smartprogram/zhuanti.php?platform=wx&version=21.12.06&action=jiujia&citycode="
//        //                    + citycode + "\"";
//        //            System.out.println("执行curl命令为:\n" + command);
//        //            if (FirstRun == true) {
//        //                String b1 = "";
//        //                try {
//        //                    b1 = execCurl(command);
//        //                } catch (IOException e) {
//        //                    e.printStackTrace();
//        //                }
//        //                System.out.println("[+]---------------初始化请求完毕--------------[+]" + System.currentTimeMillis());
//        //                b2 = b1;
//        //            } else {
//        //                String b1 = b2;
//        //                System.out.println("[+]---------------再次请求完毕---------------[+]" + System.currentTimeMillis());
//        //                String a2 = "";
//        //                try {
//        //                    a2 = execCurl(command);
//        //                } catch (IOException e) {
//        //                    e.printStackTrace();
//        //                }
//        //                if (a2.equals(b1)) {
//        //                    System.out.println("[+]---------------数据未更新---------------[+]" + System.currentTimeMillis());
//        //                } else {
//        //                    System.out.println("[+]---------------数据已更新---------------[+]" + System.currentTimeMillis());
//        //                    b2 = a2;
//        //                    push_message(a2);
//        //                }
//        //            }
//        //            FirstRun = false;
//        //        } catch (Exception e) {
//        //            e.printStackTrace();
//        //        }
//        //    }
//        //}, 0, TimeUnit.MINUTES.toMillis(checktime));
//    }

    public static String execCurl2(ProcessBuilder pb) throws IOException {
        try {
            //ProcessBuilder pb = new ProcessBuilder("curl", "-X", "GET", "-H", "Content-Type: application/json", "https://wxapidg.bendibao.com/smartprogram/zhuanti.php?platform=wx&version=21.12.06&action=jiujia&citycode=cd");
            //ProcessBuilder pb = new ProcessBuilder("curl", "-X", "GET", "-H", "Host: wxapidg.bendibao.com", "-H", "content-type: application/json", "-H", "User-Agent: Mozilla/5.0 (iPhone; CPU iPhone OS 15_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/8.0.16(0x18001034) NetType/4G Language/zh_CN", "-H", "Referer: https://servicewechat.com/wx2efc0705600eb6db/130/page-frame.html", "--compressed", "https://wxapidg.bendibao.com/smartprogram/zhuanti.php?platform=wx&version=21.12.06&action=jiujia&citycode=cd");
            pb.redirectErrorStream(true);
            Process p = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                //System.out.println(line);
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }
            System.out.println(builder.toString());
            System.out.println("**********");
            System.out.println(convertUnicode(builder.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


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
        return sb.toString();
    }


    public static boolean check_data(String citycode) {
        if (citycode.equals("")) {
            return false;
        } else {
            return true;
        }
    }

    public static String execCurl(String command) throws IOException {
        Process process = Runtime.getRuntime().exec(command);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        StringBuilder builder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            builder.append(line);
            builder.append(System.getProperty("line.separator"));
        }
        return builder.toString();
    }

    public static void push_message(String data) {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        Request request = new Request.Builder()
                .url("https://push.deer.io/v1/push?apikey=" + PushDeerKey + "&title=%E6%95%B0%E6%8D%AE%E5%B7%B2%E6%9B%B4%E6%96%B0&text=%E6%95%B0%E6%8D%AE%E5%B7%B2%E6%9B%B4%E6%96%B0")
                .method("POST", RequestBody.create(mediaType, ""))
                .build();
        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String executeCurlCommand(String curlCommand) {
        StringBuilder output = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec(curlCommand);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            reader.close();
            process.waitFor(); // wait for the process to finish
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return output.toString();
    }


}

