module org.catatunbo.spynet {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.catatunbo.spynet to javafx.fxml;
    exports org.catatunbo.spynet;
}