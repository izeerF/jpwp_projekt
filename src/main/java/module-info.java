module jpwp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    opens jpwp to javafx.fxml;
    exports jpwp;
}