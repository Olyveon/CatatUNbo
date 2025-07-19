package org.catatunbo.spynet.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.catatunbo.spynet.Auditory;
import org.catatunbo.spynet.dao.AuditoryDAO;
import org.catatunbo.spynet.dao.AuditorsDAO;
import org.catatunbo.spynet.AuditorCount;

import java.util.List;

public class InspectorAuditController {
    @FXML private Button btnVolver;
    @FXML private Button btnExportarPDF;

    @FXML private Label lblAuditoria;
    @FXML private Label lblNombre;
    @FXML private Label lblDatos;
    @FXML private Label lblEstado;

    @FXML private TextArea txtAreaHallazgos;
    @FXML private TextArea txtAreaObservaciones;

    @FXML private ComboBox<String> comboBoxAuditor;

    private Auditory currentAuditory;

    private List<String> getNameList() {
        AuditorsDAO auditorDao = new AuditorsDAO();
        List<AuditorCount> auditorCounts = auditorDao.getAllAuditors();
        return auditorCounts.stream()
                .map(a -> a.getNombre() + " (" + a.getCuenta() + ")")
                .toList();
    }

    @FXML
    private void initialize() {
        comboBoxAuditor.getItems().addAll(getNameList());
        comboBoxAuditor.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (currentAuditory != null && newValue != null && !newValue.equals(oldValue)) {
                // Extrae solo el username antes de actualizar
                String username = newValue.split(" \\(")[0];
                AuditoryDAO dao = new AuditoryDAO();
                AuditorsDAO auditorDao = new AuditorsDAO();
                boolean success = dao.updateAuditoryAssignedUser(currentAuditory.getAuditoryId(), auditorDao.getIdbyName(username));
                if (success) {
                    currentAuditory.setEncargado(username); // Actualiza el modelo local
                }
            }
        });
    }

    @FXML
    private void handleVolver() { // Botón para volver al panel anterior
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/inspector/inspectorMainPanel.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = (javafx.stage.Stage) btnVolver.getScene().getWindow();
            javafx.scene.Scene scene = new javafx.scene.Scene(root, 1280, 800);
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExportarPDF(ActionEvent event) {
        // Lógica para exportar la auditoría a PDF
        // Aquí puedes implementar la funcionalidad de exportación a PDF
        System.out.println("Exportando auditoría a PDF...");
        // Implementa la lógica de exportación aquí
    }

    public void setAuditoryData(Auditory auditory) {

        this.currentAuditory = auditory;
        loadAuditoryInfo();
    }

    private void loadAuditoryInfo() {

        if (currentAuditory != null) {
            currentAuditory.getId(); // id para identificar las observaciones y hallazgos correspondientes


            lblNombre.setText("Nombre: " + currentAuditory.getNombre());
            lblDatos.setText("Datos: " + currentAuditory.getCliente());
            lblEstado.setText("Estado: " + currentAuditory.getEstado());
            lblAuditoria.setText("Auditoría ID: " + currentAuditory.getId());
            // Selecciona el elemento con el formato correcto en el ComboBox
            if (comboBoxAuditor != null && currentAuditory.getEncargado() != null) {
                String encargado = currentAuditory.getEncargado();
                String selectedItem = comboBoxAuditor.getItems().stream()
                        .filter(item -> item.startsWith(encargado + " ("))
                        .findFirst()
                        .orElse(null);
                if (selectedItem != null) {
                    comboBoxAuditor.setValue(selectedItem);
                } else {
                    comboBoxAuditor.setValue(null);
                }
            } else {
                System.out.println("WARNING: comboBoxAuditor es null o encargado es null");
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
        } else {
            System.out.println("WARNING: currentAuditory es null en loadAuditoryInfo");
        }
    }
}
