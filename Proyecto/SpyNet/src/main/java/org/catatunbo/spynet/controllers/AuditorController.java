package org.catatunbo.spynet.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class AuditorController {
     @FXML
    private TableColumn<?, ?> colCliente;

    @FXML
    private TableColumn<?, ?> colEncargado;

    @FXML
    private TableColumn<?, ?> colEstado;

    @FXML
    private TableColumn<?, ?> colFechaFin;

    @FXML
    private TableColumn<?, ?> colFechaInicio;

    @FXML
    private TableColumn<?, ?> colNombre;

    @FXML
    private TableView<?> tableAuditories;


    @FXML
    void handleExit(ActionEvent event) {
        System.out.println("\n\n\n EXIT \n\n\n");
    }
}
