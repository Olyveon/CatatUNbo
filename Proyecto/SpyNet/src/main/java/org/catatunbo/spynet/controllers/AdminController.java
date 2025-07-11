package org.catatunbo.spynet.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminController {


    
    @FXML
    private void handleExit(ActionEvent event) throws IOException {
        // Load main.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Parent root = loader.load();

        // Get the current stage
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Set the new scene
        Scene scene = new Scene(root, 1280, 800);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleConsultarAuditorias(ActionEvent event) throws IOException {
        /*
         * jdrozo: no estoy seguro de si este metodo deba ir acá. No entiendo casi nada la estructura del proyecto.
         * 
         * Por otro lado, lo que hace este metodo es cargar la escena de Admin Audit Panel al seleccionar Consultar Auditorías.
         * Este metodo es temporal, lo pongo aqui unicamente para tener con qué testearlo en tiempo de ejecución en lo que están las demás escenas
         */
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/adminAuditPanel.fxml"));
        Scene scene = new Scene(loader.load(), 1280, 800);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

}