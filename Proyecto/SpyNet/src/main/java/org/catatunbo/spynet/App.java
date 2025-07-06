package org.catatunbo.spynet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

import java.io.IOException;

public class App extends Application {
    private static Stage primaryStage;
    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        setRoot("/fxml/login.fxml");
        stage.setTitle("SpyNet");
        stage.show();
    }
    public static void setRoot(String fxmlPath) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource(fxmlPath));
            Parent root = loader.load();
            primaryStage.setScene(new Scene(root, 1280, 800));
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Scene Load Error");
            alert.setHeaderText("Unable to load the view.");
            alert.setContentText("There was a problem loading " + fxmlPath);
            alert.showAndWait();
        }
    }
    public static void main(String[] args) {
        launch();
    }
}