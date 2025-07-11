package org.catatunbo.spynet.controllers;

import org.catatunbo.spynet.auditUtils.*;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;

public class AdminAuditController {

    @FXML private Button btnVolver;
    @FXML private Button btnAddObservacion;
    @FXML private Button btnAddHallazgo;
    @FXML private Button btnExeNmap;
    @FXML private Button btnAnalizarIA;
    @FXML private Button btnGuardarObservacion;

    @FXML private Label lblNombre;
    @FXML private Label lblEstado;
    @FXML private Label lblDatos;
    @FXML private Label lblAuditorEncargado;
    @FXML private Label lblAuditoria;
    // @FXML private Label lblSpynetPrompt;

    @FXML private TextArea txtAreaAddObservation;
    @FXML private TextArea txtAreaAddHallazgo;
    @FXML private TextArea txtAreaTerminal;

    // @FXML private ComboBox<String> hola;
    @FXML private ComboBox<String> comboBoxNivelRiesgo;
    @FXML private ComboBox<String> comboBoxIP;
    @FXML private ComboBox<String> comboBoxArgNMAP;

    // @FXML private ScrollPane scrollPaneTerminal;
    @FXML private ScrollPane scrollPaneHistorial;
    @FXML private ScrollPane scrollPaneHallazgos;

    @FXML
    public void initialize() {
        // Inicialización de componentes si es necesario
        
        comboBoxNivelRiesgo.getItems().addAll("Bajo", "Medio", "Alto");
        // Seleccionar una opción por defecto
        comboBoxNivelRiesgo.setValue("Medio");

        comboBoxArgNMAP.getItems().addAll("-sV","-O","-sS");
        comboBoxArgNMAP.setValue("-sV");

        // IP DE EJEMPLO, ESTA SE DEBE CARGAR DESDE LA BASE DE DATOS
        comboBoxIP.getItems().addAll("scanme.nmap.org","TryHackMe");
        comboBoxIP.setValue("scanme.nmap.org");

    }

    @FXML
    private void handleExecuteNMAP(){
        /*
         * Esta función controla el botón de Ejecutar Nmap.
         * Lanza un comando con parametros de nmap ajustables y una dirección ip disponible, ambas de las combobox en la interfaz.
         * Se crea un hilo muevo independiente de handleExecuteNMAP para manejar individualmente nmapCommand().executeNmap(arg, ip);
         * pues, si no fuera por el hilo independiente, el prompt y el "Running" aparecerían después de haberse ejecutado el comando y no antes.
         */
        String arg = comboBoxArgNMAP.getValue().trim();
        String ip = comboBoxIP.getValue().trim();

        String comando = "spynet@auditor:~$ nmap " + arg + " " + ip + "\n\n";
        txtAreaTerminal.setText(comando + "Running...\n");

        // Crear tarea para ejecutar nmap en segundo plano
        Task<String> nmapTask = new Task<>() {
            @Override
            protected String call() {
                return new nmapCommand().executeNmap(arg, ip);
            }

            @Override
            protected void succeeded() {
                String result = getValue();
                txtAreaTerminal.setText(comando + result);
            }

            @Override
            protected void failed() {
                txtAreaTerminal.setText(comando + "Error al ejecutar Nmap.");
            }
        };

        // Ejecutar la tarea en un nuevo hilo
        new Thread(nmapTask).start();


    }

    @FXML
    private void handleVolver() {
        // Lógica para volver al panel anterior
    }

    // Agrega aquí más métodos para manejar eventos de los botones, etc.
}