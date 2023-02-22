package com.lionfish.GUI;

import com.lionfish.GUI.views.GameWizardView;
import com.lionfish.board.util.PieceColor;
import com.lionfish.network.NetworkInterface;
import com.lionfish.network.Ping;
import com.lionfish.network.Pong;
import com.lionfish.util.ErrorDialog;
import com.lionfish.util.PopUpDialog;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
public class GameWizard {
    private static final String PATH = "views/GameWizardView.fxml";

    private final GameWizardView gwv;
    public GameWizard() {
        this.gwv = getController();

        this.gwv.buttonStart.setOnAction(e -> {
            /* establish new ping */
            int port;
            try {
                port = Integer.parseInt(this.gwv.startPortField.getText());
            } catch (NumberFormatException nfe) {
                new ErrorDialog("Server error: invalid parameters");
                return;
            }
            try {
                PopUpDialog pu = new PopUpDialog("Server listening at address " + InetAddress.getLocalHost().getHostAddress() + ":" + port + "...");
                GameWizard.this.gwv.pane.getScene().getWindow().hide();
                Ping ping = new Ping(port);
                new Thread(ping).start();
                Task<Void> task = new Task<>() {
                        @Override
                        protected Void call() {
                            try {
                                ping.getObject();
                            } catch(Exception exception) {
                                cancel();
                            }
                            return null;
                        }

                        @Override
                        protected void succeeded() {
                            super.succeeded();
                            ChessboardPresenter cp = new ChessboardPresenter(ping, PieceColor.COLOR_WHITE);
                            Parent root = cp.getRoot();
                            Stage stage = new Stage();
                            stage.setTitle("LionFish");
                            stage.setScene(new Scene(root, 94 * 8, 94 * 8));
                            stage.setResizable(false);
                            stage.setOnCloseRequest(e -> ((Stage) GameWizard.this.gwv.pane.getScene().getWindow()).show());
                            stage.show();
                            pu.setResult(ButtonType.CLOSE);
                        }

                        @Override
                        protected void cancelled() {
                            super.cancelled();
                            ((Stage) GameWizard.this.gwv.pane.getScene().getWindow()).show();
                        }
                    };
                new Thread(task).start();
            } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    Platform.exit();
            }
        });

        this.gwv.buttonConnect.setOnAction(e -> {
            /* establish new sender */
            int port;
            String address;
            ChessboardPresenter cp;
            try {
                port = Integer.parseInt(this.gwv.connectPortField.getText());
                address = this.gwv.connectAddressField.getText();
            } catch (NumberFormatException nfe) {
                new ErrorDialog("Connection error: invalid parameters");
                return;
            }
            GameWizard.this.gwv.pane.getScene().getWindow().hide();
            NetworkInterface networkInterface = new Pong(address, port);
            new Thread(networkInterface).start();
            cp = new ChessboardPresenter(networkInterface, PieceColor.COLOR_BLACK);
            Parent root = cp.getRoot();
            Stage stage = new Stage();
            stage.setTitle("LionFish");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.setOnCloseRequest(ex -> ((Stage) GameWizard.this.gwv.pane.getScene().getWindow()).show());
            stage.show();
        });
    }

    public Parent getRoot() {
        return this.gwv.pane;
    }

    private GameWizardView getController() {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource(PATH));
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fxmlLoader.getController();
    }
}
