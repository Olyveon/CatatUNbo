module org.catatunbo.spynet {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive java.sql;
    opens org.catatunbo.spynet to javafx.fxml;
    exports org.catatunbo.spynet;
    opens org.catatunbo.spynet.controllers to javafx.fxml;
    exports  org.catatunbo.spynet.controllers;
    exports org.catatunbo.spynet.controllers.Admin;
    opens org.catatunbo.spynet.controllers.Admin to javafx.fxml;
}