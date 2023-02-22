package com.lionfish;


import com.lionfish.GUI.GameWizard;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Program extends Application {
    @Override
    public void start(Stage primaryStage) {
        GameWizard gw = new GameWizard();
        Parent root = gw.getRoot();
        primaryStage.setTitle("LionFish");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}