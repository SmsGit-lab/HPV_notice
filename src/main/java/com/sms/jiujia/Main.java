package com.sms.jiujia;

import com.sms.jiujia.event.HomeEvent;
import com.sms.jiujia.ui.Home;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Home.homeUi(primaryStage);
        HomeEvent buttonController = new HomeEvent();
        buttonController.init();
        buttonController.globalShutdown(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
