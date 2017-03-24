package com.chaos.guetzli.gui;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
    private CheckBox jpg;
    @FXML
    private CheckBox png;

    private int type = 0; // 0:jpg  1:png

    @FXML
    private void chooseDir() {
        start.setText("");
        finish.setText("");
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File directory = directoryChooser.showDialog(null);

        if (directory != null) {
            path.setText(directory.getAbsolutePath());
        }
    }

    @FXML
    private void chooseFile() {
        start.setText("");
        finish.setText("");
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            path.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void jpgCheck() {
        if (ifChecked()) {
            transformButton.setDisable(false);
            type = 0;
            png.setSelected(false);
        } else {
            transformButton.setDisable(true);
        }
    }

    @FXML
    private void pngCheck() {
        if (ifChecked()) {
            transformButton.setDisable(false);
            type = 1;
            jpg.setSelected(false);
        } else {
            transformButton.setDisable(true);
        }
    }

    private boolean ifChecked() {
        return jpg.isSelected() || png.isSelected();
    }

    @FXML
    private void transform() throws IOException, ExecutionException, InterruptedException {
        String pathString = path.getText();
        if (!pathString.isEmpty()) {
            start.setText("开始压缩文件，请耐心等待...");
            transformButton.setDisable(true);
            File fileOrDir = new File(pathString);
            if (fileOrDir.isFile()) {
                transformOneFile(fileOrDir, type);
            } else {
                File[] files = fileOrDir.listFiles();
                if (files != null) {
                    for (File f : files) {
                        transformOneFile(f, type);
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

    private void transformOneFile (File file, int type) throws ExecutionException, InterruptedException {
        Task compressJpgTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ProcessBuilder builder = new ProcessBuilder();
                List<String> command = new ArrayList<>();

                if (0 == type) {
                    command.add(System.getProperty("user.dir") + File.separator + "guetzli");
                    command.add(file.getAbsolutePath());
                    command.add(file.getAbsolutePath() + ".guetzli.jpg");
                } else {
                    command.add(System.getProperty("user.dir") + File.separator + "pngquant");
                    command.add(file.getAbsolutePath());
                }
                builder.command(command);

                Process p = builder.start();
                p.waitFor();
                return null;
            }
        };
        compressJpgTask.setOnFailed((stat) -> {
            finish.setText("压缩转换执行失败!");
            transformButton.setDisable(false);
        });
        compressJpgTask.setOnSucceeded((stat) -> {
            finish.setText("压缩转换执行成功!");
            transformButton.setDisable(false);
        });

        new Thread(compressJpgTask).start();
    }
}
