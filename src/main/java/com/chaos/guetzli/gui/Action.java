package com.chaos.guetzli.gui;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by zcfrank1st on 18/03/2017.
 */
public class Action {
    @FXML
    private Label start;
    @FXML
    private Label finish;
    @FXML
    private Label path;
    @FXML
    private Button transformButton;

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
    private void transform() throws IOException, ExecutionException, InterruptedException {
        start.setText("开始压缩文件，请耐心等待...");
        transformButton.setDisable(true);

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
            makeErrorAlert("打开路径为空!");
        }
    }

    private void makeErrorAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ERROR");
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait();
    }

    private void transformOneFile (File file) throws ExecutionException, InterruptedException {
        Task compressTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ProcessBuilder builder = new ProcessBuilder();
                List<String> command = new ArrayList<>();
                command.add(System.getProperty("user.dir") + "/guetzli");
                command.add(file.getAbsolutePath());
                command.add(file.getAbsolutePath() + ".compress.jpg");
                builder.command(command);

                Process p = builder.start();
                p.waitFor();
                return null;
            }
        };
        compressTask.setOnFailed((stat) -> {
            finish.setText("压缩转换执行失败!");
            transformButton.setDisable(false);
        });
        compressTask.setOnSucceeded((stat) -> {
            finish.setText("压缩转换执行成功!");
            transformButton.setDisable(false);
        });

        new Thread(compressTask).start();
    }
}
