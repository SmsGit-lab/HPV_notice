package com.sms.jiujia.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.sms.jiujia.model.MainData;
import com.sms.jiujia.model.Place;
import com.sms.jiujia.model.Xiaoxi;
import com.sms.jiujia.ui.setting.Constant;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author songmingsong
 * @date 2023/5/6
 */
public class PushMsgUtils {
    /**
     * 上一次推送的内容，和本次相同就不再重复推送
     */
    private static String lastPushContent = "";

    /**
     * 推送正文的字段，按顺序拼成 markdown 列表
     */
    private static final String LABEL_TIME = "预约时间";
    private static final String LABEL_ADDR = "地址";
    private static final String LABEL_NUMBER = "数量";
    private static final String LABEL_METHOD = "方式";
    private static final String LABEL_TYPE = "途径";

    /**
     * pushDeer 消息推送
     *
     * @param mainData 消息体
     * @param isTest   是否测试推送（测试推送不过滤已过期的信息）
     * @return 推送结果；没有新的放号信息时返回 {@link PushResult#skipped()}
     */
    public static PushResult pushDeer(MainData mainData, Boolean isTest) {
        List<Place> placeList = new ArrayList<>();
        placeList.addAll(nullToEmpty(mainData.getData().getWebsite().getPlace()));
        placeList.addAll(nullToEmpty(mainData.getData().getOnsitedate().getPlace()));

        List<String> blocks = new ArrayList<>();
        for (Place place : placeList) {
            if (!isTest && !compareTimeToNow(place.getYy_time())) {
                continue;
            }
            blocks.add(buildPlaceBlock(place));
        }

        // 没有新的放号信息就不用推送了
        if (!isTest && blocks.isEmpty()) {
            return PushResult.skipped();
        }

        String title = cityName(mainData) + "疫苗消息推送";
        String content = buildContent(blocks, isTest);
        if (!isTest) {
            String pushContent = title + "\n" + content;
            if (pushContent.equals(lastPushContent)) {
                return PushResult.skipped();
            }
            lastPushContent = pushContent;
        }
        return pushMessage(title, content);
    }

    /**
     * 拼装推送正文：每条放号信息一段，段与段之间空一行
     */
    private static String buildContent(List<String> blocks, Boolean isTest) {
        StringBuilder content = new StringBuilder();
        if (blocks.isEmpty()) {
            content.append("当前没有查询到放号信息。");
        } else {
            for (String block : blocks) {
                if (content.length() > 0) {
                    content.append("\n\n");
                }
                content.append(block);
            }
        }
        if (isTest) {
            content.append("\n\n（测试推送，未过滤已过期的信息）");
        }
        return content.toString();
    }

    /**
     * 拼装一家接种点的信息：
     * <p>
     * **医院名称**
     * <p>
     * - 预约时间：xxx
     * - 地址：xxx
     * <p>
     * 医院名称与列表之间空一行，保证各种 markdown 渲染器都能正确显示成列表；
     * 值为空的字段整行不显示，避免出现「地址：」后面什么都没有
     */
    private static String buildPlaceBlock(Place place) {
        List<String> lines = new ArrayList<>();
        appendLine(lines, LABEL_TIME, place.getYy_time());
        appendLine(lines, LABEL_ADDR, place.getAddr());
        appendLine(lines, LABEL_NUMBER, place.getMinge());
        appendLine(lines, LABEL_METHOD, place.getMethod());
        appendLine(lines, LABEL_TYPE, place.getPlatform());

        StringBuilder block = new StringBuilder("**")
                .append(oneLine(place.getName(), "未知接种点"))
                .append("**");
        if (!lines.isEmpty()) {
            block.append("\n\n").append(String.join("\n", lines));
        }
        return block.toString();
    }

    private static void appendLine(List<String> lines, String label, String value) {
        String text = oneLine(value, "");
        if (!text.isEmpty()) {
            lines.add("- " + label + "：" + text);
        }
    }

    /**
     * 把内容压成一行：去掉换行和多余空格，避免把 markdown 的结构撑乱
     */
    private static String oneLine(String value, String defaultValue) {
        if (Objects.isNull(value)) {
            return defaultValue;
        }
        String text = value.replaceAll("\\s+", " ").trim();
        return text.isEmpty() ? defaultValue : text;
    }

    private static List<Place> nullToEmpty(List<Place> places) {
        return Objects.isNull(places) ? new ArrayList<Place>() : places;
    }

    private static String cityName(MainData mainData) {
        List<Xiaoxi> xiaoxiList = mainData.getData().getXiaoxi();
        if (Objects.isNull(xiaoxiList) || xiaoxiList.isEmpty()) {
            return "";
        }
        return oneLine(xiaoxiList.get(0).getCityname(), "");
    }

    /**
     * 比较预约时间
     *
     * @param placeTime 预约时间
     * @return true 可以推送
     */
    private static Boolean compareTimeToNow(String placeTime) {
        try {
            return DateUtils.changeDate(placeTime).after(new Date());
        } catch (Exception e) {
            // 时间格式异常时按可推送处理，避免整条定时任务中断
            return true;
        }
    }

    /**
     * pushDeer 消息推送请求
     * <p>
     * 参数用表单方式提交（自动做 URL 编码）。原来把内容拼在 URL 里，
     * 中文、空格、& 等特殊字符会导致内容被截断或错乱，内容较长时还会超出 URL 长度限制。
     *
     * @param title   推送标题
     * @param content 推送正文（markdown）
     */
    private static PushResult pushMessage(String title, String content) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        RequestBody body = new FormBody.Builder(StandardCharsets.UTF_8)
                .add("pushkey", Objects.toString(Constant.PUSH_DEER_KEY, ""))
                .add("text", title)
                .add("desp", content)
                .add("type", "markdown")
                .build();
        Request request = new Request.Builder()
                .url(Constant.PUSH_DEER_URL)
                .post(body)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String result = Objects.isNull(response.body()) ? "" : response.body().string();
            return parseResult(result);
        } catch (IOException e) {
            return PushResult.failure("网络异常：" + e.getMessage());
        }
    }

    /**
     * 解析 pushDeer 的返回。
     * 注意：key 错误时接口同样返回 HTTP 200，只能看响应体里的 code，
     * 成功为 {"code":0,...}，失败为 {"code":80501,"error":"错误的Key"}
     */
    private static PushResult parseResult(String body) {
        try {
            JSONObject json = JSON.parseObject(body);
            if (Objects.nonNull(json) && Objects.equals(0, json.getInteger("code"))) {
                return PushResult.success();
            }
            if (Objects.nonNull(json) && Objects.nonNull(json.getString("error"))) {
                return PushResult.failure(json.getString("error"));
            }
        } catch (Exception e) {
            // 落到下面统一按失败处理
        }
        return PushResult.failure("接口返回内容无法识别：" + oneLine(body, "空"));
    }

    /**
     * 推送结果
     */
    public static class PushResult {
        private final boolean skipped;
        private final boolean success;
        private final String message;

        private PushResult(boolean skipped, boolean success, String message) {
            this.skipped = skipped;
            this.success = success;
            this.message = message;
        }

        /**
         * 没有新的放号信息，未推送
         */
        public static PushResult skipped() {
            return new PushResult(true, false, "没有新的放号信息");
        }

        public static PushResult success() {
            return new PushResult(false, true, "推送成功");
        }

        public static PushResult failure(String message) {
            return new PushResult(false, false, message);
        }

        public boolean isSkipped() {
            return skipped;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
    }
}
