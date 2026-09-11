package com.sms.jiujia.event;

import com.sms.jiujia.model.City;
import com.sms.jiujia.model.MainData;
import com.sms.jiujia.ui.setting.Constant;
import com.sms.jiujia.ui.setting.Setting;
import com.sms.jiujia.utils.DataUtil;
import com.sms.jiujia.utils.DateUtils;
import com.sms.jiujia.utils.LogUtils;
import com.sms.jiujia.utils.PushMsgUtils;
import com.sms.jiujia.utils.TimerTaskUtils;
import com.sms.jiujia.utils.TrayWindowUtil;
import com.sms.jiujia.utils.UtilTools;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.net.URL;
import java.util.Date;
import java.util.Objects;

/**
 * 按钮控制类
 *
 * @Author songmingsong
 * @Date 2023/4/28
 **/
public class HomeEvent {

    private static final String KEY_METHOD_URL = "https://gitee.com/easychen/pushdeer/raw/main/doc/image/clipcode.png";

    /**
     * 申请方法弹窗中二维码的显示尺寸
     */
    private static final int QR_CODE_SIZE = 180;

    /**
     * 城市按钮
     */
    private Button btnTest;
    /**
     * 开始按钮
     */
    private Button btnStart;
    /**
     * 重新输入key按钮
     */
    private Button reSetKeyBtn;
    /**
     * 输入key按钮
     */
    private Button pushDeerBtn;
    /**
     * pushDeerText
     */
    private TextField pushDeerText;
    /**
     * 下拉框
     */
    private ChoiceBox<City> cityBox;
    /**
     * pushDeerBtn
     */
    private Button getKeyMethodBtn;
    /**
     * 停止按钮
     */
    private Button stopBtn;
    /**
     * 清空日志按钮
     */
    private Button clearLogBtn;

    /**
     * 监控任务是否正在运行
     */
    private boolean monitoring = false;
    /**
     * 是否正在输入key
     */
    private boolean keyEditing = false;
    /**
     * 上一次保存的key，避免重复写文件
     */
    private String savedKey = "";

    /**
     * 按钮初始化
     */
    public HomeEvent() {
        Setting setting = Constant.setting;
        btnTest = setting.getBtnTest();
        btnStart = setting.getBtnStart();
        cityBox = setting.getCityBox();
        pushDeerBtn = setting.getSetPushDeerKeyBtn();
        pushDeerText = setting.getPushDeerText();
        reSetKeyBtn = setting.getReSetKeyBtn();
        getKeyMethodBtn = setting.getGetKeyMethodBtn();
        stopBtn = setting.getStopBtn();
        clearLogBtn = setting.getClearLogBtn();
    }

    public void init() {
        refreshKeyArea();

        btnStart.setOnMouseClicked(e -> {
            if (!check()) {
                showKeyInput();
                alterError("请先输入 PushDeer Key");
                log("[error]->请先输入 PushDeer Key");
                return;
            }
            if (monitoring) {
                log("[warn]->监控任务已经在运行中，无需重复开始");
                return;
            }
            TimerTaskUtils.execute(getCurlCmd());
            monitoring = true;
            setStatus("监控中 · " + currentCityName(), "status-running");
            log("[info]->开始监控「" + currentCityName() + "」，每 1 分钟查询一次放号信息");
        });

        btnTest.setOnMouseClicked(e -> {
            log("[info]->" + "测试推送-->");
            if (!check()) {
                showKeyInput();
                alterError("请先输入 PushDeer Key");
                log("[error]->请先输入 PushDeer Key");
                return;
            }
            String execCurlJson = UtilTools.execCurl(getCurlCmd());
            MainData mainData = UtilTools.coverJson(execCurlJson);
            PushMsgUtils.PushResult result = PushMsgUtils.pushDeer(mainData, true);
            if (result.isSuccess()) {
                alterSuccessful("推送成功！");
                log("[info]->" + DateUtils.changeDate(new Date()) + "推送成功！\n" + mainData);
            } else {
                alterError("推送失败：" + result.getMessage());
                log("[error]->" + DateUtils.changeDate(new Date()) + "推送失败：" + result.getMessage());
            }
        });

        /**
         * 城市下拉框事件
         */
        cityBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (Objects.isNull(newValue)) {
                return;
            }
            Constant.CITY = newValue.getCode();
            log("[info]->" + "选择了：" + newValue.getName() + "（" + Constant.CITY + "）");
            if (monitoring) {
                setStatus("监控中 · " + newValue.getName(), "status-running");
            }
        });

        /**
         * 输入pushDeer按钮事件
         */
        pushDeerBtn.setOnAction(e -> showKeyInput());

        /**
         * pushDeer文本，回车保存
         */
        pushDeerText.setOnAction(e -> saveKey());

        /**
         * pushDeer文本，失去焦点时保存
         */
        pushDeerText.setOnMouseExited(e -> saveKey());

        /**
         * 重置key
         */
        reSetKeyBtn.setOnMouseClicked(e -> {
            log("[info]->" + "重置Key");
            Constant.PUSH_DEER_KEY = null;
            Constant.has_PUSH_DEER_KEY = false;
            keyEditing = false;
            savedKey = "";
            pushDeerText.setText("");
            DataUtil.setMData(Constant.setting.getProperties(), pushDeerText);
            refreshKeyArea();
        });

        /**
         * 查看按钮
         */
        getKeyMethodBtn.setOnMouseClicked(e -> {
            log("[info]->" + "点击了查看按钮");
            showKeyMethodDialog();
        });

        /**
         * 清空日志
         */
        clearLogBtn.setOnMouseClicked(e -> Constant.setting.getTextArea().clear());

        /**
         * 停止
         */
        stopBtn.setOnMouseClicked(e -> {
            if (!monitoring || Objects.isNull(Constant.timer)) {
                log("[warn]->当前没有正在运行的监控任务");
                return;
            }
            Constant.timer.cancel();
            Constant.timer = null;
            monitoring = false;
            setStatus("已停止", "status-stopped");
            log("[info]->" + "停止监控");
        });
    }

    /**
     * 窗口右上角关闭事件监听
     *
     * @param primaryStage
     */
    public void globalShutdown(Stage primaryStage) {
        TrayWindowUtil.getInstance().listen(primaryStage);
        TrayWindowUtil.getInstance().hide(primaryStage);
    }

    /**
     * 必要参数校验
     */
    private boolean check() {
        return !(Objects.isNull(Constant.PUSH_DEER_KEY) || "".equals(Constant.PUSH_DEER_KEY.trim()));
    }

    /**
     * 展示key输入框，并载入已保存的key
     */
    private void showKeyInput() {
        Setting setting = Constant.setting;
        DataUtil.getMData(setting.getProperties(), setting.getPushDeerText());
        keyEditing = true;
        refreshKeyArea();
        setting.getPushDeerText().requestFocus();
        setting.getPushDeerText().positionCaret(setting.getPushDeerText().getText() == null
                ? 0 : setting.getPushDeerText().getText().length());
    }

    /**
     * 保存key
     */
    private void saveKey() {
        Setting setting = Constant.setting;
        String key = pushDeerText.getText() == null ? "" : pushDeerText.getText().trim();
        if (key.equals(savedKey)) {
            return;
        }
        savedKey = key;
        if (key.isEmpty()) {
            Constant.PUSH_DEER_KEY = null;
            Constant.has_PUSH_DEER_KEY = false;
        } else {
            Constant.PUSH_DEER_KEY = key;
            Constant.has_PUSH_DEER_KEY = true;
            DataUtil.setMData(setting.getProperties(), pushDeerText);
            log("[info]->PushDeer Key 已保存");
        }
        refreshKeyArea();
    }

    /**
     * 根据key的状态切换输入框和按钮的显示
     */
    private void refreshKeyArea() {
        boolean hasKey = Boolean.TRUE.equals(Constant.has_PUSH_DEER_KEY);
        boolean showField = hasKey || keyEditing;
        setVisible(pushDeerText, showField);
        setVisible(reSetKeyBtn, showField);
        setVisible(pushDeerBtn, !showField);
    }

    private void setVisible(Node node, boolean visible) {
        node.setVisible(visible);
        node.setManaged(visible);
    }

    private void setStatus(String text, String styleClass) {
        Label statusLabel = Constant.setting.getStatusLabel();
        statusLabel.setText(text);
        statusLabel.getStyleClass().removeAll("status-idle", "status-running", "status-stopped");
        statusLabel.getStyleClass().add(styleClass);
    }

    private String currentCityName() {
        City city = cityBox.getValue();
        return Objects.isNull(city) ? Constant.CITY : city.getName();
    }

    private void log(String msg) {
        LogUtils.getLayoutConsoleLogInstance().log(msg);
    }

    private void alterError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(msg);
        applyDialogStyle(alert);
        alert.showAndWait();
    }

    private void alterSuccessful(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(msg);
        applyDialogStyle(alert);
        alert.showAndWait();
    }

    /**
     * pushDeerKey申请方法
     */
    private void showKeyMethodDialog() {
        String msg = "申请方法：\n" +
                "1、使用手机原相机扫描下方二维码，安装 PushDeer 客户端\n" +
                "   （iOS / Android 也可以在应用商店直接搜索 PushDeer 安装）\n" +
                "2、打开 App，点击底部导航栏中名为 key 的钥匙图标\n" +
                "3、点击右上角的 “+” 号，即可看到自动生成的 Key\n" +
                "4、把该 Key 复制到程序的【PushDeer Key】输入框中，回车保存即可\n\n" +
                "V 1.0 版\n版权所有：songmingsong";

        Label content = new Label(msg);
        content.setWrapText(true);
        content.setMaxWidth(Double.MAX_VALUE);
        // 说明文字不允许被压缩，否则会被二维码挤掉
        content.setMinHeight(Region.USE_PREF_SIZE);

        VBox box = new VBox(12, content, buildQrCodeView());
        box.setAlignment(Pos.CENTER);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("PushDeer Key 申请方法");
        alert.setHeaderText("如何获取 PushDeer Key");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setContent(box);
        dialogPane.setPrefWidth(420);
        // 弹窗高度跟随内容自适应，保证文字与二维码都能完整显示
        dialogPane.setMinHeight(Region.USE_PREF_SIZE);
        applyDialogStyle(alert);
        alert.showAndWait();
    }

    /**
     * 二维码图片。先按固定尺寸占位，图片异步加载完成后不会再改变弹窗布局；
     * 加载失败时整块隐藏，不影响文案展示
     */
    private StackPane buildQrCodeView() {
        ImageView imageView = new ImageView();
        // 宽高都要限制，否则图片会溢出预留区域盖住文字
        imageView.setFitWidth(QR_CODE_SIZE);
        imageView.setFitHeight(QR_CODE_SIZE);
        imageView.setPreserveRatio(true);

        StackPane holder = new StackPane(imageView);
        holder.setMinSize(QR_CODE_SIZE, QR_CODE_SIZE);
        holder.setPrefSize(QR_CODE_SIZE, QR_CODE_SIZE);
        holder.setMaxSize(QR_CODE_SIZE, QR_CODE_SIZE);
        holder.setAlignment(Pos.CENTER);

        Image image = new Image(KEY_METHOD_URL, QR_CODE_SIZE, 0, true, true, true);
        imageView.setImage(image);
        image.errorProperty().addListener((observable, oldValue, newValue) -> {
            if (Boolean.TRUE.equals(newValue)) {
                holder.setVisible(false);
                holder.setManaged(false);
            }
        });
        return holder;
    }

    private void applyDialogStyle(Alert alert) {
        URL stylesheet = HomeEvent.class.getResource("/ui/app.css");
        if (stylesheet != null) {
            alert.getDialogPane().getStylesheets().add(stylesheet.toExternalForm());
        }
    }

    private ProcessBuilder getCurlCmd() {
        return new ProcessBuilder("curl", "-X", "GET", "-H", "Content-Type: application/json", "https://wxapidg.bendibao.com/smartprogram/zhuanti.php?platform=wx&version=21.12.06&action=jiujia&citycode=" + Constant.CITY);
    }

}
