module com.catatunbo.spynet {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.catatunbo.spynet to javafx.fxml;
    exports com.catatunbo.spynet;
}