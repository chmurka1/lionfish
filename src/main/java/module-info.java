module com.lionfish {
    requires javafx.controls;
    requires javafx.fxml;

    exports com.lionfish;
    opens com.lionfish to javafx.fxml;
    exports com.lionfish.GUI.views;
    opens com.lionfish.GUI.views to javafx.fxml;
}