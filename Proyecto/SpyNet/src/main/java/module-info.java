module org.catatunbo.spynet {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires transitive java.sql;
    opens org.catatunbo.spynet to javafx.fxml;
    exports org.catatunbo.spynet;
    opens org.catatunbo.spynet.controllers to javafx.fxml;
    exports  org.catatunbo.spynet.controllers;
    opens org.catatunbo.spynet.database to java.sql;
}