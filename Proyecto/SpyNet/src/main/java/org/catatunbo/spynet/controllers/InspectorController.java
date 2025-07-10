package org.catatunbo.spynet.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

public class InspectorController {
    @FXML
    private TextField auditoriaIdField;
    @FXML
    private TextField auditorIdField;
    @FXML
    private TableView<?> tablaSinEncargado;
    @FXML
    private TableColumn<?, ?> colIdAuditoria;
    @FXML
    private TableColumn<?, ?> colDescripcion;
    @FXML
    private TableView<?> tablaAuditores;
    @FXML
    private TableColumn<?, ?> colIdAuditor;
    @FXML
    private TableColumn<?, ?> colNombreAuditor;
    @FXML
    private TableColumn<?, ?> colCantidadAuditorias;

    @FXML
    public void handleAsignarAuditoria(ActionEvent event) {
        // Lógica para asignar auditoría
        System.out.println("Asignar auditoría: " + auditoriaIdField.getText() + " a auditor: " + auditorIdField.getText());
    }

    @FXML
    private void handleExit(ActionEvent event) {
        // Puedes implementar la lógica de salir aquí si lo necesitas
    }
}
