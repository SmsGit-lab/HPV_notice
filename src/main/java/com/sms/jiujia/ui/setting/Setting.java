package com.sms.jiujia.ui.setting;

import com.sms.jiujia.model.City;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.util.Properties;


/**
 * 全局变量设置
 *
 * @Author songmingsong
 * @Date 2023/4/28
 **/
public class Setting {
    /**
     * home窗体
     */
    public static Stage homeStage;
    /**
     * 测试推送按钮
     */
    private Button btnTest;
    /**
     * 开始按钮
     */
    private Button btnStart;
    /**
     * 输入pushDeerKey按钮
     */
    private Button setPushDeerKeyBtn;
    /**
     * 窗体标题
     */
    private Label title;
    /**
     * 状态提示
     */
    private Label statusLabel;

    /**
     * TextArea
     */
    private TextArea textArea;
    /**
     * pushDeerText
     */
    private TextField pushDeerText = new TextField();
    /**
     * 重新输入KEY
     */
    private Button reSetKeyBtn;

    /**
     * 通知公告
     */
    private Label notice;
    /**
     * 下拉框提示
     */
    private Text ChoiceBoxTitle;

    /**
     * pushDeerKey获取方式
     */
    private Button getKeyMethodBtn;
    /**
     * 停止
     */
    private Button stopBtn;
    /**
     * 清空日志
     */
    private Button clearLogBtn;
    /**
     * 停止
     */
    private Properties properties = new Properties();


    public Setting() {
        pushDeerText.setAlignment(Pos.CENTER_LEFT); // 设置单行输入框的对齐方式
        pushDeerText.setPrefColumnCount(16); // 设置单行输入框的推荐列数
        pushDeerText.setPromptText("粘贴 PushDeer Key 后回车保存");

        // 下拉框只显示城市名称，需要在设置选中项之前注册
        cityBox.setConverter(new StringConverter<City>() {
            @Override
            public String toString(City city) {
                return city == null ? "" : city.getName();
            }

            @Override
            public City fromString(String string) {
                return null;
            }
        });
    }

    /**
     * city下拉框
     */
    private ChoiceBox<City> cityBox = new ChoiceBox<City>();

    public static Stage getHomeStage() {
        return homeStage;
    }

    public static void setHomeStage(Stage homeStage) {
        Setting.homeStage = homeStage;
    }

    public Button getBtnTest() {
        return btnTest;
    }

    public void setBtnTest(Button btnTest) {
        this.btnTest = btnTest;
    }

    public Button getBtnStart() {
        return btnStart;
    }

    public void setBtnStart(Button btnStart) {
        this.btnStart = btnStart;
    }

    public Label getTitle() {
        return title;
    }

    public void setTitle(Label title) {
        this.title = title;
    }

    public Label getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(Label statusLabel) {
        this.statusLabel = statusLabel;
    }

    public TextArea getTextArea() {
        return textArea;
    }

    public void setTextArea(TextArea textArea) {
        this.textArea = textArea;
    }

    public ChoiceBox<City> getCityBox() {
        return cityBox;
    }

    public void setCityBox(ChoiceBox<City> cityBox) {
        this.cityBox = cityBox;
    }

    public Button getSetPushDeerKeyBtn() {
        return setPushDeerKeyBtn;
    }

    public void setSetPushDeerKeyBtn(Button setPushDeerKeyBtn) {
        this.setPushDeerKeyBtn = setPushDeerKeyBtn;
    }

    public TextField getPushDeerText() {
        return pushDeerText;
    }

    public void setPushDeerText(TextField pushDeerText) {
        this.pushDeerText = pushDeerText;
    }

    public Button getReSetKeyBtn() {
        return reSetKeyBtn;
    }

    public void setReSetKeyBtn(Button reSetKeyBtn) {
        this.reSetKeyBtn = reSetKeyBtn;
    }

    public Label getNotice() {
        return notice;
    }

    public void setNotice(Label notice) {
        this.notice = notice;
    }

    public Text getChoiceBoxTitle() {
        return ChoiceBoxTitle;
    }

    public void setChoiceBoxTitle(Text choiceBoxTitle) {
        ChoiceBoxTitle = choiceBoxTitle;
    }

    public Button getGetKeyMethodBtn() {
        return getKeyMethodBtn;
    }

    public void setGetKeyMethodBtn(Button getKeyMethodBtn) {
        this.getKeyMethodBtn = getKeyMethodBtn;
    }

    public Button getStopBtn() {
        return stopBtn;
    }

    public void setStopBtn(Button stopBtn) {
        this.stopBtn = stopBtn;
    }

    public Button getClearLogBtn() {
        return clearLogBtn;
    }

    public void setClearLogBtn(Button clearLogBtn) {
        this.clearLogBtn = clearLogBtn;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
