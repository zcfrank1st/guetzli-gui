package com.chaos.guetzli.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.TextFlow;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * Created by zcfrank1st on 18/03/2017.
 */
public class Action {
    @FXML
    private TextFlow flow;
    @FXML
    private Label path;

    @FXML
    private void chooseDir() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File directory = directoryChooser.showDialog(null);

        if (directory != null) {
            path.setText(directory.getAbsolutePath());
        }
    }

    @FXML
    private void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            path.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void transform() {
        String pathString = path.getText();
        if (!pathString.isEmpty()) {
            File fileOrDir = new File(pathString);
            if (fileOrDir.isFile()) {
                // TODO 文件转换
                System.getProperty("user.dir");
            } else {
                File[] files = fileOrDir.listFiles();
                if (files != null) {
                    for (File f : files) {
                        // TODO 文件转换
                        System.out.println(System.getProperty("user.dir"));
                    }
                } else {
                    // TODO 弹窗转换目录为空
                }
            }
        } else {
            // TODO 弹窗路径为空
        }
    }

    private void notify (String message) {}

    private void transformOneFile (String path) {}
}
