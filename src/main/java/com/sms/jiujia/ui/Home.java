package com.sms.jiujia.ui;

import com.sms.jiujia.model.City;
import com.sms.jiujia.ui.setting.Constant;
import com.sms.jiujia.ui.setting.HomeWindowSetting;
import com.sms.jiujia.ui.setting.Setting;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static com.sms.jiujia.utils.UtilTools.parseCitiesFromTextFile2;

/**
 * @Author songmingsong
 * @Date 2023/4/28
 **/
public class Home {

    /**
     * 使用说明
     */
    private static final String NOTICE_TEXT =
            "1、首次启动程序会缩小到系统托盘，双击托盘图标可以重新打开主界面；\n" +
                    "2、使用前需要申请 PushDeer 的 Key，点击【查看申请方法】可以查看申请步骤；\n" +
                    "3、请在【城市】下拉框中选择需要监控的城市，未选择时默认监控成都；\n" +
                    "4、点击【开始监控】后每隔 1 分钟查询一次放号信息，发现新的放号信息才会推送到手机；\n" +
                    "5、点击窗口关闭按钮不会退出程序，请在托盘图标上点击右键选择【退出程序】结束运行。";

    public static Pane homeUi(Stage primaryStage) throws IOException {
        primaryStage.setTitle("九价疫苗信息推送 · Jiujia Notice");

        /**
         * 初始化组件
         */
        Setting setting = new Setting();
        setting.setTitle(new Label("九价疫苗信息推送"));
        setting.setBtnTest(button("测试推送", "btn-secondary"));
        setting.setBtnStart(button("开始监控", "btn-primary"));
        setting.setStopBtn(button("停止", "btn-danger"));
        setting.setSetPushDeerKeyBtn(button("输入 Key", "btn-secondary"));
        setting.setReSetKeyBtn(button("重新输入", "btn-ghost"));
        setting.setGetKeyMethodBtn(button("查看申请方法", "btn-ghost"));
        setting.setClearLogBtn(button("清空日志", "btn-ghost"));
        setting.setChoiceBoxTitle(new Text("城市"));
        setting.setStatusLabel(new Label("未开始"));
        setting.getStatusLabel().getStyleClass().add("status-idle");
        setting.setTextArea(new TextArea("开始输出日志...\n"));
        setting.setNotice(new Label(NOTICE_TEXT));

        /**
         * 初始化城市代码
         */
        InputStream resourceAsStream = Home.class.getClassLoader().getResourceAsStream("city.txt");
        parseCitiesFromTextFile2(resourceAsStream);
        setting.getCityBox().getItems().addAll(Constant.cities);
        selectDefaultCity(setting);

        /**
         * 组装界面
         */
        BorderPane root = new BorderPane();
        root.getStyleClass().add("app-root");
        root.setPadding(new Insets(16, 18, 16, 18));

        VBox header = buildHeader(setting);
        VBox controlCard = buildControlCard(setting);
        VBox logCard = buildLogCard(setting);
        VBox keyCard = buildKeyCard(setting);
        VBox noticeCard = buildNoticeCard(setting);
        VBox.setVgrow(logCard, Priority.ALWAYS);

        VBox center = new VBox(12, controlCard, logCard, keyCard);
        center.setFillWidth(true);
        root.setTop(header);
        root.setCenter(center);
        root.setBottom(noticeCard);
        BorderPane.setMargin(header, new Insets(0, 0, 14, 0));
        BorderPane.setMargin(noticeCard, new Insets(14, 0, 0, 0));

        Scene scene = new Scene(root);
        applyStylesheet(scene);
        primaryStage.setScene(scene);
        /**
         * 初始化窗体
         */
        HomeWindowSetting.set(primaryStage);
        primaryStage.show();

        // 将窗体赋值给全局变量
        Setting.homeStage = primaryStage;
        Constant.setting = setting;

        return root;
    }

    /**
     * 顶部标题栏
     */
    private static VBox buildHeader(Setting setting) {
        Label logo = new Label("HPV");
        logo.getStyleClass().add("app-logo");

        Label title = setting.getTitle();
        title.getStyleClass().add("app-title");

        Label badge = new Label("v1.0");
        badge.getStyleClass().add("app-badge");

        HBox titleRow = new HBox(10, logo, title, spacer(), badge);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        Label subtitle = new Label("自动查询目标城市九价疫苗放号信息，并通过 PushDeer 推送到手机");
        subtitle.getStyleClass().add("app-subtitle");

        VBox header = new VBox(5, titleRow, subtitle);
        header.getStyleClass().add("app-header");
        return header;
    }

    /**
     * 监控设置卡片
     */
    private static VBox buildControlCard(Setting setting) {
        Label cardTitle = label("监控设置", "card-title");
        HBox head = new HBox(8, cardTitle, spacer(), setting.getStatusLabel());
        head.setAlignment(Pos.CENTER_LEFT);

        Text cityTitle = setting.getChoiceBoxTitle();
        cityTitle.getStyleClass().add("field-label");

        ChoiceBox<City> cityBox = setting.getCityBox();
        cityBox.setPrefWidth(170);
        // 城市列表很长，默认最小宽度会按最长条目计算，窗口缩小时会把卡片撑破
        cityBox.setMinWidth(130);
        cityBox.setMinHeight(Control.USE_COMPUTED_SIZE);
        cityBox.setMaxHeight(200);
        cityBox.setTooltip(new Tooltip("选择需要监控的城市"));

        HBox toolbar = new HBox(10, cityTitle, cityBox, spacer(),
                setting.getBtnTest(), setting.getBtnStart(), setting.getStopBtn());
        toolbar.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(12, head, toolbar);
        card.getStyleClass().add("card");
        return card;
    }

    /**
     * 运行日志卡片
     */
    private static VBox buildLogCard(Setting setting) {
        HBox head = new HBox(8, label("运行日志", "card-title"), spacer(), setting.getClearLogBtn());
        head.setAlignment(Pos.CENTER_LEFT);

        TextArea logArea = setting.getTextArea();
        logArea.getStyleClass().add("log-area");
        logArea.setEditable(false);
        logArea.setWrapText(true);
        logArea.setFocusTraversable(false);
        // 窗口变小时优先压缩日志区，否则卡片会超出窗口
        logArea.setMinWidth(240);
        logArea.setMinHeight(60);
        VBox.setVgrow(logArea, Priority.ALWAYS);

        VBox card = new VBox(10, head, logArea);
        card.getStyleClass().add("card");
        return card;
    }

    /**
     * pushDeerKey 设置卡片
     */
    private static VBox buildKeyCard(Setting setting) {
        Label cardTitle = label("PushDeer Key", "card-title");
        Label hint = label("用于接收推送消息，输入后程序会记住该 Key", "field-hint");
        HBox head = new HBox(8, cardTitle, hint);
        head.setAlignment(Pos.BASELINE_LEFT);

        HBox.setHgrow(setting.getPushDeerText(), Priority.ALWAYS);
        setting.getPushDeerText().setMinWidth(120);
        setting.getPushDeerText().setMaxWidth(Double.MAX_VALUE);

        HBox keyArea = new HBox(8, setting.getPushDeerText(),
                setting.getSetPushDeerKeyBtn(), setting.getReSetKeyBtn(), spacer(),
                setting.getGetKeyMethodBtn());
        keyArea.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(10, head, keyArea);
        card.getStyleClass().add("card");
        return card;
    }

    /**
     * 使用说明卡片
     */
    private static VBox buildNoticeCard(Setting setting) {
        Label notice = setting.getNotice();
        notice.getStyleClass().add("notice-text");
        // 允许自动换行，最小宽度设为0，窗口变小时说明文字跟着换行而不是把卡片撑宽
        notice.setWrapText(true);
        notice.setMinWidth(0);
        notice.setMaxWidth(Double.MAX_VALUE);

        VBox card = new VBox(8, label("使用说明", "card-title"), notice);
        card.getStyleClass().add("card");
        return card;
    }

    /**
     * 默认选中全局变量中配置的城市（默认成都），保证界面显示与实际查询的城市一致
     */
    private static void selectDefaultCity(Setting setting) {
        ChoiceBox<City> cityBox = setting.getCityBox();
        City selected = null;
        for (City city : cityBox.getItems()) {
            if (city.getCode().equalsIgnoreCase(Constant.CITY)) {
                selected = city;
                break;
            }
        }
        if (selected == null && !cityBox.getItems().isEmpty()) {
            selected = cityBox.getItems().get(0);
        }
        if (selected != null) {
            cityBox.getSelectionModel().select(selected);
            Constant.CITY = selected.getCode();
        }
    }

    private static Button button(String text, String styleClass) {
        Button button = new Button(text);
        button.getStyleClass().add(styleClass);
        return button;
    }

    private static Label label(String text, String styleClass) {
        Label label = new Label(text);
        label.getStyleClass().add(styleClass);
        return label;
    }

    private static Region spacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    private static void applyStylesheet(Scene scene) {
        URL stylesheet = Home.class.getResource("/ui/app.css");
        if (stylesheet != null) {
            scene.getStylesheets().add(stylesheet.toExternalForm());
        }
    }
}
