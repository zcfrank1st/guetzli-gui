package com.chaos.guetzli.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Created by zcfrank1st on 18/03/2017.
 */
public class Guetzli extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(ClassLoader.getSystemResource("menu.fxml"));

        primaryStage.setTitle("Guetzli Mac Gui");
        primaryStage.getIcons().add(new Image(ClassLoader.getSystemResource("sun.jpg").toExternalForm()));
        primaryStage.setScene(new Scene(root, 450, 200));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
