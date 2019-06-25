package com.niehaus;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;
import java.sql.DriverManager;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            loadDriver();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        URL url = getClass().getResource("graphics/startWindow.fxml");
        Parent root = FXMLLoader.load(url);
        primaryStage.setTitle("Bank Statement Processor");
        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.show();
    }

    private void loadDriver() throws Exception {
        URL u = new URL("jar:file:/home/stefan/workplace/lib/SQLite/sqlite-jdbc-3.27.2.1.jar!/");
        String classname = "org.sqlite.JDBC";
        URLClassLoader ucl = new URLClassLoader(new URL[] { u });
        Driver d = (Driver)Class.forName(classname, true, ucl).getDeclaredConstructor().newInstance();
        DriverManager.registerDriver(new DriverShim(d));
    }


    public static void main(String[] args) {
        launch(args);
    }
}


