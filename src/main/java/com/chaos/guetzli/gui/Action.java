package com.chaos.guetzli.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zcfrank1st on 18/03/2017.
 */
public class Action {
    @FXML
    private Label result;
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
    private void transform() throws IOException {
        String pathString = path.getText();
        if (!pathString.isEmpty()) {
            File fileOrDir = new File(pathString);
            if (fileOrDir.isFile()) {
                transformOneFile(fileOrDir);
            } else {
                File[] files = fileOrDir.listFiles();
                if (files != null) {
                    for (File f : files) {
                        transformOneFile(f);
                    }
                } else {
                    makeErrorAlert("需要转换目录为空!");
                }
            }
        } else {
            makeErrorAlert("打开路劲为空!");
        }
    }

    private void makeErrorAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ERROR");
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait();
    }

    private void transformOneFile (File file) {
        ProcessBuilder builder = new ProcessBuilder();
        List<String> command = new ArrayList<>();
        command.add(System.getProperty("user.dir") + "/guetzli");
        command.add(file.getAbsolutePath());
        command.add(file.getAbsolutePath() + ".compress.jpg");
        builder.command(command);
        try {
            // TODO 展示修改
            result.setText("开始压缩文件，请耐心等待...");
            Process p = builder.start();
            int status = p.waitFor();
            if (0 == status) {
                result.setText("压缩文件成功!");
            }
        } catch (IOException | InterruptedException e) {
            result.setText("执行失败!");
        }


    }
}
