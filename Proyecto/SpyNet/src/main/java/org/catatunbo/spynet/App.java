package org.catatunbo.spynet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fxml/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 800);
        stage.setTitle("SpyNet");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        createDataBase(); 
        launch();
    }

    /**
     * Runs the script for the database creation,
     * with MySQL command, from the native terminal.
     * <p>
     * Requires MySQL root user and it's password.
     */
    private static void createDataBase() {
    
        final String mysqlPath = "mysql"; 
        final String user = "root";
        final String password = "\"" + "" + "\"";
        final String scriptPath = "src\\main\\resources\\database_resources\\spynetdb_script_creation_V2.sql";

        String fullScriptPath = "\"source " + new File(scriptPath).getAbsolutePath() + "\"";

        List<String> command = Arrays.asList(
                mysqlPath,
                "--default-character-set=utf8mb4",
                "-u", user,
                "-p" + password,
                "-h", "localhost",
                "-P", "3306",
                "-e", fullScriptPath
        );

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);

        try {
            Process process = processBuilder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Script executed successfully.");
            } else {
                System.err.println("MySQL script failed. Exit code: " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}