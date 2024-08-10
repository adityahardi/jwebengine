package com.community.javafx.jwebengine;

import javafx.application.Application;
import javafx.concurrent.Worker.State;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class MainWebViewApplication extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        MainWebViewApplication.primaryStage = primaryStage;
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        // JavaScript function to request camera access
//        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue == State.SUCCEEDED) {
//                webEngine.executeScript(
//                        "navigator.mediaDevices.getUserMedia({ video: true })" +
//                                ".then(stream => console.log('Camera access granted'))" +
//                                ".catch(error => console.error('Camera access denied: ', error));"
//                );
//            }
//        });

        // Menu bar
        MenuBar menuBar = new MenuBar();

        Menu urlMenu = new Menu("File");
        Menu navigationMenu = new Menu("Navigate");
        Menu fullscreenMode = new Menu("View");

        MenuItem openUrlMenuItem = getOpenUrlMenuItem(webEngine);
        MenuItem backMenuItem = getBackMenuItem(webEngine);
        MenuItem forwardMenuItem = getForwardMenuItem(webEngine);
        MenuItem fullscreenMenuItem = getFullscreenMenuItem(webEngine);
        MenuItem exitAppMenuItem = getExitAppMenuItem(webEngine);


        urlMenu.getItems().addAll(openUrlMenuItem, exitAppMenuItem);
        navigationMenu.getItems().addAll(backMenuItem, forwardMenuItem);
        fullscreenMode.getItems().add(fullscreenMenuItem);
        menuBar.getMenus().addAll(urlMenu, navigationMenu, fullscreenMode);

        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(webView);

        Scene scene = new Scene(root, 800, 600);

        scene.getAccelerators().put(KeyCombination.keyCombination("F11"), () -> {
            primaryStage.setFullScreen(!primaryStage.isFullScreen());
        });

        primaryStage.fullScreenProperty().addListener((obs, wasFullScreen, isFullScreen) -> {
            menuBar.setVisible(!isFullScreen);
        });


        primaryStage.setTitle("Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static MenuItem getOpenUrlMenuItem(WebEngine webEngine) {
        MenuItem openUrlMenuItem = new MenuItem("Open URL");

        openUrlMenuItem.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog("https://www.example.com");
            dialog.setTitle("Open URL");
            dialog.setHeaderText("Enter the URL to visit:");
            dialog.setContentText("URL:");

            String url = dialog.showAndWait().orElse(null);

            if (url != null && !url.trim().isEmpty()) {
                webEngine.load(url);
            }
        });

        return openUrlMenuItem;
    }

    private static MenuItem getBackMenuItem(WebEngine webEngine) {
        MenuItem backMenuItem = new MenuItem("Back");

        backMenuItem.setOnAction(e -> {
            if (webEngine.getHistory().getCurrentIndex() > 0) {
                webEngine.getHistory().go(-1);
            }
        });

        return backMenuItem;
    }

    private static MenuItem getForwardMenuItem(WebEngine webEngine) {
        MenuItem forwardMenuItem = new MenuItem("Forward");

        forwardMenuItem.setOnAction(e -> {
            if (webEngine.getHistory().getCurrentIndex() < webEngine.getHistory().getEntries().size() - 1) {
                webEngine.getHistory().go(1);
            }
        });

        return forwardMenuItem;
    }

    private static MenuItem getFullscreenMenuItem(WebEngine webEngine) {
        MenuItem fullscreenMenuItem = new MenuItem("Fullscreen");

        fullscreenMenuItem.setOnAction(e -> {
            primaryStage.setFullScreen(!primaryStage.isFullScreen());
        });

        return fullscreenMenuItem;
    }


    private static MenuItem getExitAppMenuItem(WebEngine webEngine) {
        MenuItem exitMenuItem = new MenuItem("Exit");

        exitMenuItem.setOnAction(e -> {
            primaryStage.close();
        });

        return exitMenuItem;
    }


    public static void main(String[] args) {
        launch();
    }
}