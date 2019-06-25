package com.niehaus;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class StarterController {

    @FXML private Text loadactiontarget;

    @FXML private Button startButton;

    private static final String classifierFXML = "graphics/classifyWindow.fxml";

    private String bankStatementPath;

    @FXML
    public void initialize() {
        startButton.setDisable(true);
    }

    @FXML protected void handleStartButtonAction(final ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(classifierFXML));
        Parent root = loader.load();
        ClassifierController CC = loader.getController();
        CC.loadBankStatement(bankStatementPath);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
    }

    @FXML protected void handleLoadButtonAction(final ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open CSV File");
        File bankStatement = fileChooser.showOpenDialog(stage);
        if (bankStatement != null) {
            setBankStatementPath(bankStatement.getAbsolutePath());
            loadactiontarget.setText("Loaded: " + bankStatement.getName());
            startButton.setDisable(false);
        }
    }

    private void setBankStatementPath(String bankStatementPath) {
        this.bankStatementPath = bankStatementPath;
    }
}
