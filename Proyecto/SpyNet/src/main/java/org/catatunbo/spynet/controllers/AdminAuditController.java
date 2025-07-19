package org.catatunbo.spynet.controllers;

import org.catatunbo.spynet.auditUtils.*;
import org.catatunbo.spynet.dao.AuditoryDAO;
import org.catatunbo.spynet.Auditory;
import java.util.List;


import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ComboBox;


public class AdminAuditController {

    @FXML private Button btnVolver;
    @FXML private Button btnAddObservacion;
    @FXML private Button btnAddHallazgo;
    @FXML private Button btnExeNmap;
    @FXML private Button btnAnalizarIA;
    @FXML private Button btnGuardarObservacion;
    @FXML private Button btnExportarPDF;

    @FXML private Label lblNombre;
    @FXML private Label lblEstado;
    @FXML private Label lblDatos;
    @FXML private Label lblAuditorEncargado;
    @FXML private Label lblAuditoria;
    @FXML private Label lblColorEstado;
    // @FXML private Label lblSpynetPrompt;

    @FXML private TextArea txtAreaAddObservation;
    @FXML private TextArea txtAreaObservaciones;

    @FXML private TextArea txtAreaAddHallazgo;
    @FXML private TextArea txtAreaHallazgos;

    @FXML private TextArea txtAreaTerminal;
   

    // @FXML private ComboBox<String> hola;
    @FXML private ComboBox<String> comboBoxNivelRiesgo;
    @FXML private ComboBox<String> comboBoxIP;
    @FXML private ComboBox<String> comboBoxArgNMAP;
    @FXML private ComboBox<String> comboBoxEstadoAuditoria;

    public String nmapOutput=null;
    public StringBuilder openaiOutput;

    private Task<String> nmapTask;
    private nmapCommand nmapComando;
    
    private Auditory currentAuditory;

    @FXML
    public void initialize() {
        // Inicialización de componentes si es necesario
        comboBoxEstadoAuditoria.getItems().addAll("PENDIENTE","EN PROCESO", "ARCHIVADO", "FINALIZADO");
        
        comboBoxNivelRiesgo.getItems().addAll("Bajo", "Medio", "Alto");
        // Seleccionar una opción por defecto
        comboBoxNivelRiesgo.setValue("Medio");

        comboBoxArgNMAP.getItems().addAll("-sV","-O","-sS");
        comboBoxArgNMAP.setValue("-sV");

        // IP DE EJEMPLO, ESTA SE DEBE CARGAR DESDE LA BASE DE DATOS
        comboBoxIP.getItems().addAll("scanme.nmap.org","TryHackMe.com");
        comboBoxIP.setValue("scanme.nmap.org");

        comboBoxEstadoAuditoria.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (currentAuditory != null && newVal != null && !newVal.equals(oldVal)) {
                // Actualiza en la base de datos
                AuditoryDAO dao = new AuditoryDAO();
                boolean success = dao.updateAuditoryState(currentAuditory.getAuditoryId(), newVal);
                if (success) {
                    currentAuditory.setEstado(newVal); // Actualiza el modelo local
                }
                // Cambia el color del label según el estado
                switch (newVal) {
                    case "PENDIENTE":
                        lblColorEstado.setStyle("-fx-background-color: gray;");
                        break;
                    case "EN PROCESO":
                        lblColorEstado.setStyle("-fx-background-color: orange;");
                        break;
                    case "ARCHIVADO":
                        lblColorEstado.setStyle("-fx-background-color: blue;");
                        break;
                    case "FINALIZADO":
                        lblColorEstado.setStyle("-fx-background-color: green;");
                        break;
                    default:
                        lblColorEstado.setStyle("-fx-background-color: transparent;");
                }
            }
        });
        // Deshabilita el botón al inicio
        btnGuardarObservacion.setDisable(true);
    }

    private void updateGuardarObservacionButtonState() {
        btnGuardarObservacion.setDisable(nmapOutput == null || openaiOutput == null);
    }


    public void clearOutputsAtributtes(){
        nmapOutput=null;
        openaiOutput=null;
        updateGuardarObservacionButtonState();
    }


    @FXML
    private void handleExecuteNMAP(){
        /*
         * Esta función controla el botón de Ejecutar Nmap.
         * Lanza un comando con parametros de nmap ajustables y una dirección ip disponible, ambas de las combobox en la interfaz.
         * Se crea un hilo muevo independiente de handleExecuteNMAP para manejar individualmente nmapCommand().executeNmap(arg, ip);
         * pues, si no fuera por el hilo independiente, el prompt y el "Running" aparecerían después de haberse ejecutado el comando y no antes.
         */
        btnAnalizarIA.setDisable(true);

        clearOutputsAtributtes();

        String arg = comboBoxArgNMAP.getValue().trim();
        String ip = comboBoxIP.getValue().trim();

        String comando = "spynet@auditor:~$ nmap " + arg + " " + ip + "\n";
        txtAreaTerminal.setText(comando + "Running...\n");
        
        // Crear tarea para ejecutar nmap en segundo plano
        nmapTask = new Task<>() {
            @Override
            protected String call() {
                return new nmapCommand().executeNmap(arg, ip);
            }

            @Override
            protected void succeeded() {
                String result = getValue();
                txtAreaTerminal.setText(comando + result);
                nmapOutput = txtAreaTerminal.getText();
                updateGuardarObservacionButtonState(); // <-- Actualiza el estado del botón
                btnAnalizarIA.setDisable(false);
            }

            @Override
            protected void failed() {
                txtAreaTerminal.setText(comando + "Error al ejecutar Nmap.");
                nmapOutput = txtAreaTerminal.getText();
                updateGuardarObservacionButtonState();
            }
        };

        // Ejecutar la tarea en un nuevo hilo
        new Thread(nmapTask).start();
        
    }


    @FXML
    private void handleExecuteIA() {
        // String apikey= OpenAIConfig.getApiKey();
        txtAreaTerminal.appendText("\n\nspynet@ia:~$ ");

        openaiOutput = new StringBuilder();
        updateGuardarObservacionButtonState(); // <-- Actualiza el estado del botón

        Task<Void> iaTask = new Task<>() {
            @Override
            protected Void call() {
                try {
                    OpenAI completions = new OpenAI(nmapOutput);
                    
                    completions.start(fragment -> {
                        openaiOutput.append(fragment);
                        // Actualiza el TextArea en el hilo de la UI
                        Platform.runLater(() -> txtAreaTerminal.appendText(fragment));
                    });

                } catch (Exception e){
                    Platform.runLater(()->{txtAreaTerminal.appendText("Error: parece que la API_KEY no está configurada. Para hacerlo, hecha un vistazo en:\nresources/openai_resources/README.txt");});
                }
                return null;
            }
        };

        new Thread(iaTask).start();
    }

    @FXML
    private void handleClearTerminal() {
        if (nmapTask != null && nmapTask.isRunning()) {
            txtAreaTerminal.appendText("\n[⚠ No se puede limpiar la terminal mientras Nmap esté ejecutándose.]");
            return;
        }

        clearOutputsAtributtes();
        txtAreaTerminal.setText("spynet@auditor:~$ ");
        
    }


    @FXML
    private void handleVolver() { // Botón para volver al panel anterior
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/admin/adminMainPanel.fxml"));
            javafx.scene.Parent root = loader.load();       
            javafx.stage.Stage stage = (javafx.stage.Stage) btnVolver.getScene().getWindow();
            javafx.scene.Scene scene = new javafx.scene.Scene(root, 1280, 800);
            stage.setScene(scene);
            stage.show();
                        
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getCurrentUserId() {
        return org.catatunbo.spynet.Session.getInstance().getCurrentUser().getUserId();
    }

    // Carga el método de abajo (Es una buena práctica :3)   
    public void setAuditoryData(Auditory auditory) {

        this.currentAuditory = auditory;
        loadAuditoryInfo();
    }
    
    // Métod que se encarga de cargar la información de la auditoría seleccionada
    private void loadAuditoryInfo() {
        if (currentAuditory != null) {
            currentAuditory.getId(); // id para identificar las observaciones y hallazgos correspondientes

            lblNombre.setText("Nombre: " + currentAuditory.getNombre());
            lblDatos.setText("Datos: " + currentAuditory.getCliente());
            lblAuditorEncargado.setText("Auditor encargado: "+ currentAuditory.getEncargado());
            lblAuditoria.setText("Auditoría ID: " + currentAuditory.getId());
            // Configurar el ComboBox de estado con el estado actual (sin lblEstado)
            if (comboBoxEstadoAuditoria != null && currentAuditory.getEstado() != null) {
                // Agregar el estado actual al ComboBox si no existe
                if (!comboBoxEstadoAuditoria.getItems().contains(currentAuditory.getEstado())) {
                    comboBoxEstadoAuditoria.getItems().add(currentAuditory.getEstado());
                }
                comboBoxEstadoAuditoria.setValue(currentAuditory.getEstado());
            } else {
                System.out.println("WARNING: comboBoxEstadoAuditoria es null o estado es null");
            }

            // se cargan las observaciones dado el usuario
            AuditoryDAO auditoryData = new AuditoryDAO();
            List<String> observations = auditoryData.getObservationsByAuditoryId(currentAuditory.getAuditoryId());
            for (int i = 0; i < observations.size(); i++) {
                txtAreaObservaciones.appendText(observations.get(i));
                if (i < observations.size() - 1) {
                    txtAreaObservaciones.appendText("\n\n");
                }
            }

            // se cargan los hallazgos dado el usuario
            List<String> findings = auditoryData.getFindingsByAuditoryId(currentAuditory.getAuditoryId());
            for (int i = 0; i < findings.size(); i++) {
                txtAreaHallazgos.appendText(findings.get(i));
                if (i < findings.size() - 1) {
                    txtAreaHallazgos.appendText("\n\n");
                }
            }

            // Cargar IPs asociadas al cliente de la auditoría
            int clientId = currentAuditory.getAuditoryClientId(); // Asegúrate de tener este getter en Auditory
            List<String> ips = auditoryData.getIPsByClientId(clientId);
            comboBoxIP.getItems().clear();
            comboBoxIP.getItems().addAll(ips);
            if (!ips.isEmpty()) {
                comboBoxIP.setValue(ips.get(0));
            }
        } else {
            System.out.println("WARNING: currentAuditory es null en loadAuditoryInfo");
        }
    }


    @FXML
    private void handleAddObservacion() {
        String input = txtAreaAddObservation.getText().trim();
        if (!input.contains(":")) {
            // Mostrar error de formato
            return;
        }
        String[] parts = input.split(":", 2);
        String title = parts[0].trim();
        String description = parts[1].trim();
        int userId = getCurrentUserId(); 

        AuditoryDAO dao = new AuditoryDAO();
        boolean success = dao.insertObservation(currentAuditory.getAuditoryId(), userId, title, description);
        if (success) {
            txtAreaAddObservation.clear();
            // Recargar observaciones
            txtAreaObservaciones.clear();
            List<String> observations = dao.getObservationsByAuditoryId(currentAuditory.getAuditoryId());
            for (int i = 0; i < observations.size(); i++) {
                txtAreaObservaciones.appendText(observations.get(i));
                if (i < observations.size() - 1) {
                    txtAreaObservaciones.appendText("\n\n");
                }
            }
        }
    }

    @FXML
    private void handleAddHallazgo() {
        String input = txtAreaAddHallazgo.getText().trim();
        if (!input.contains(":")) {
            // Mostrar error de formato
            return;
        }
        String[] parts = input.split(":", 2);
        String title = parts[0].trim();
        String description = parts[1].trim();
        String risk = comboBoxNivelRiesgo.getValue(); 
        int userId = getCurrentUserId(); 

        AuditoryDAO dao = new AuditoryDAO();
        boolean success = dao.insertFinding(currentAuditory.getAuditoryId(), userId, title, description, risk);
        if (success) {
            txtAreaAddHallazgo.clear();
            // Recargar hallazgos
            txtAreaHallazgos.clear();
            List<String> findings = dao.getFindingsByAuditoryId(currentAuditory.getAuditoryId());
            for (int i = 0; i < findings.size(); i++) {
                txtAreaHallazgos.appendText(findings.get(i));
                if (i < findings.size() - 1) {
                    txtAreaHallazgos.appendText("\n\n");
                }
            }
        }
    }


    @FXML 
    private void handleSaveObservation(){
        if (nmapOutput==null || openaiOutput==null){
            return;
        }

        int userId = getCurrentUserId(); 
        String title= "OPENAI OBSERVATION";
        String bitacoraGenerada = openaiOutput.toString();

        AuditoryDAO dao = new AuditoryDAO();
        boolean success = dao.insertObservation(currentAuditory.getAuditoryId(), userId, title, bitacoraGenerada);
        
        clearOutputsAtributtes();
        if (success) {
            txtAreaAddObservation.clear();
            // Recargar observaciones
            txtAreaObservaciones.clear();
            List<String> observations = dao.getObservationsByAuditoryId(currentAuditory.getAuditoryId());
            for (int i = 0; i < observations.size(); i++) {
                txtAreaObservaciones.appendText(observations.get(i));
                if (i < observations.size() - 1) {
                    txtAreaObservaciones.appendText("\n\n");
                }
            }
        } 
    }


    @FXML
    private void handleExportarPDF() {
        btnExportarPDF.setDisable(true); // Deshabilita el botón al iniciar

        String nombreAuditoria = lblAuditoria.getText();
        String auditorEncargado = lblAuditorEncargado.getText();
        String datosAuditoria = lblDatos.getText();
        String estadoAuditoria = comboBoxEstadoAuditoria.getValue();

        String clientInfo = "Auditoria: " + nombreAuditoria
                + "\nEncargado: " + auditorEncargado
                + "\nDatos: " + datosAuditoria
                + "\nEstado de la auditoria: " + estadoAuditoria;

        String findings = txtAreaHallazgos.getText();
        String observations = txtAreaObservaciones.getText();
        String totalContent = clientInfo + findings + observations;


        
        txtAreaTerminal.appendText(" \n\nGenerando PDF... \n(Esto puede tardar unos segundos por el análisis de OpenIA)\n");
        Task<String> iaTask = new Task<>() {
            @Override
            protected String call() {
                // Llama a la IA de forma bloqueante
                return new OpenAI(totalContent).generateBitacora();
            }

            @Override
            protected void succeeded() {
                String openaiBitacore = getValue();
                ReporteAuditoriaPDF pdf = new ReporteAuditoriaPDF();
                pdf.generarReporte(clientInfo, observations, findings, openaiBitacore);
                btnExportarPDF.setDisable(false); // Habilita el botón al terminar
                txtAreaTerminal.appendText(pdf.getUbicacion()+pdf.getFilename()+"\n");
            }

            @Override
            protected void failed() {
                btnExportarPDF.setDisable(false); // Habilita el botón si falla
                txtAreaTerminal.appendText("\nAlgo ha salido mal :( no se pudo exportar\n");
            }
        };

        new Thread(iaTask).start();
    }


}
