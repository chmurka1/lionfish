package com.lionfish.util;

import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;

public class ErrorDialog {
    public ErrorDialog(String string) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setContentText(string);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
    }
}
