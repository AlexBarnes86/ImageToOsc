package com.toastedbits.kadenze.mlartmusic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main extends Application {
    //Ensure Slf4j/logging activates on startup regardless of if we use the logger here or not
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Main.class.getResource("/ImageToOsc.fxml"));
        primaryStage.setTitle("Image to OSC");
        primaryStage.setScene(new Scene(root));
        primaryStage.sizeToScene();

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
