package org.catatunbo.spynet.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminCreateAuditController {

    @FXML
    private TextField txtNombreEmpresa;
    
    @FXML
    private TextField txtFechaLimite;
    
    @FXML
    private TextArea txtDescripcion;
    
    @FXML
    private Button btnEnviar;

    @FXML
    private void initialize() {
        // Inicialización del controlador si es necesario
        System.out.println("AdminCreateAuditController inicializado");
    }

    @FXML
    private void handleEnviar(ActionEvent event) {
        String nombreEmpresa = txtNombreEmpresa.getText();
        String fechaLimite = txtFechaLimite.getText();
        String descripcion = txtDescripcion.getText();
        
        System.out.println("Datos del formulario:");
        System.out.println("Empresa: " + nombreEmpresa);
        System.out.println("Fecha límite: " + fechaLimite);
        System.out.println("Descripción: " + descripcion);
        
    }

    @FXML
    private void handleExit(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1280, 800);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleConsultarAuditorias(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/adminMainPanel.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1280, 800);
        stage.setScene(scene);
        stage.show();
    }
}
