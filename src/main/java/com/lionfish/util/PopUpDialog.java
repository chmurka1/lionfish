package com.lionfish.util;

import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class PopUpDialog extends Dialog<ButtonType> {
    public PopUpDialog(String string) {
        super();
        this.setContentText(string);
        this.setOnCloseRequest(e-> ((Stage)this.getDialogPane().getScene().getWindow()).close());
        this.show();
    }


}