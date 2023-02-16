package com.lionfish;


import com.lionfish.GUI.ChessboardPresenter;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Program extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        ChessboardPresenter cp = new ChessboardPresenter();
        Parent root = cp.getRoot();
        primaryStage.setTitle("LionFish");
        primaryStage.setScene(new Scene(root, 84*8, 84*8));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}