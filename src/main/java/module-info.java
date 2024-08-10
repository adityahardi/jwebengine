module com.community.javafx.jwebengine {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;


    opens com.community.javafx.jwebengine to javafx.fxml;
    exports com.community.javafx.jwebengine;
}