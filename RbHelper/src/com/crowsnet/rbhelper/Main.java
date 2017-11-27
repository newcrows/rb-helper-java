package com.crowsnet.rbhelper;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import com.crowsnet.rbhelper.v2.Chain;
import com.crowsnet.rbhelper.v2.Tasks;

public class Main extends Application {

    private static Pane root;
    private static VBox vbox;
    private static TextField user;
    private static TextField pass;
    private static TextField character;
    private static Button button;
    private static TextField hintLabel;
    private static WebEngine webEngine;

    @Override
    public void start(Stage primaryStage) throws Exception{
        root = new Pane();

        Label accountL = new Label("Username");
        Label passL = new Label("Password");
        Label nameL = new Label("Character");

        user = new TextField();
        pass = new PasswordField();
        character = new TextField();

        button = new Button("Activate");
        button.setDefaultButton(true);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onButtonClick();
            }
        });

        hintLabel = new TextField();

        vbox = new VBox();
        vbox.getChildren().addAll(accountL, user,
                passL,pass,
                nameL, character,
                button, hintLabel);
        vbox.setSpacing(0);
        vbox.setPrefWidth(300);

        root.getChildren().add(vbox);
        primaryStage.setTitle("RbHelper v0.1");
        primaryStage.setScene(new Scene(root, 300, 180));
        primaryStage.show();

        WebView webView = new WebView();
        webView.setVisible(false);

        webEngine = webView.getEngine();
        webEngine.load("http://silkroad.ws");

        vbox.getChildren().add(webView);
    }

    private static void onButtonClick() {
        button.setDisable(true);    //prevent multi-launch

        Tasks.init(webEngine, user.getText(), pass.getText(), character.getText(), hintLabel);

        Chain chain = new Chain(20, 200, true);
        chain.add(new Tasks.Wait110Task());
        chain.add(new Tasks.LogoutTask());
        chain.add(new Tasks.LoginTask());
        chain.add(new Tasks.SelectionTask());
        chain.add(new Tasks.RebirthTask());

        chain.start();
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }
}
