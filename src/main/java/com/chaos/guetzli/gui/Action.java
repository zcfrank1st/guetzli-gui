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
                    makeAlert("ERROR", "需要转换目录为空!");
                }
            }
        } else {
            makeAlert("ERROR", "打开路劲为空!");
        }
    }

    private void makeAlert (String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait();
    }

    private void transformOneFile (File file) throws IOException {
        ProcessBuilder builder = new ProcessBuilder();
        List<String> command = new ArrayList<>();
        command.add(System.getProperty("user.dir") + "/guetzli");
        command.add(file.getAbsolutePath());
        command.add(file.getAbsolutePath() + ".compress.jpg");
        builder.command(command);
        builder.redirectErrorStream(true);
        Process p = builder.start();

        output(p.getInputStream());

        // TODO 优化转化展示
    }

    private void output(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append(System.getProperty("line.separator"));
                result.setText(sb.toString());
            }
        }
    }
}
