module org.catatunbo.spynet {
    // JavaFX modules
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    // Database
    requires transitive java.sql;

    // OpenAI Java SDK and HTTP client
    requires openai.java;               // funciona sin necesidad de esta linea, pero igual no quitarla
    requires openai.java.client.okhttp;
    requires openai.java.core;

    // Allow reflection for FXML controllers
    opens org.catatunbo.spynet to javafx.fxml;
    exports org.catatunbo.spynet;
    opens org.catatunbo.spynet.controllers to javafx.fxml;
    exports org.catatunbo.spynet.controllers;
    opens org.catatunbo.spynet.database to java.sql;
    exports org.catatunbo.spynet.database;
}
