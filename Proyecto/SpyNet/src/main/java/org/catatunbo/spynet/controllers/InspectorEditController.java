package org.catatunbo.spynet.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ComboBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.catatunbo.spynet.Auditory;
import org.catatunbo.spynet.dao.AuditoryDAO;
import org.catatunbo.spynet.dao.AuditorsDAO;
import org.catatunbo.spynet.AuditorCount;

import java.io.IOException;
import java.util.List;

public class InspectorEditController {

    // Botones
    @FXML private Button btnVolver;
    @FXML private Button btnExportarPDF;
    @FXML private Button btnGuardarAuditor;
    @FXML private Button btnGuardarEstado;

    // Labels para mostrar información
    @FXML private Label lblNombre;
    @FXML private Label lblDatos;
    @FXML private Label lblAuditorEncargado;
    @FXML private Label lblAuditoria;
    @FXML private Label lblColorEstado;

    // TextAreas
    @FXML private TextArea txtAreaObservaciones;
    @FXML private TextArea txtAreaHallazgos;

    // ComboBoxes
    @FXML private ComboBox<String> comboBoxAuditor;
    @FXML private ComboBox<String> comboBoxEstadoAuditoria;

    // Variable para almacenar la auditoría actual
    private Auditory currentAuditory;

    @FXML
    private void initialize() {
        // Inicializar ComboBox de estado con los valores posibles
        if (comboBoxEstadoAuditoria != null) {
            comboBoxEstadoAuditoria.getItems().addAll("PENDIENTE", "EN PROCESO", "ARCHIVADO", "FINALIZADO");
        }
        
        // Cargar lista de auditores en el ComboBox
        if (comboBoxAuditor != null) {
            loadAuditors();
        }
        
        // Los inspectores pueden editar auditor y estado, pero no observaciones/hallazgos
        if (txtAreaObservaciones != null) {
            txtAreaObservaciones.setEditable(false);
        }
        if (txtAreaHallazgos != null) {
            txtAreaHallazgos.setEditable(false);
        }
    }

    private void loadAuditors() {
        try {
            AuditorsDAO auditorsDAO = new AuditorsDAO();
            List<AuditorCount> auditorCounts = auditorsDAO.getAllAuditors();
            List<String> auditorNames = auditorCounts.stream()
                    .map(a -> a.getNombre() + " (" + a.getCuenta() + ")")
                    .toList();
            comboBoxAuditor.getItems().addAll(auditorNames);
        } catch (Exception e) {
            System.err.println("ERROR al cargar auditores: " + e.getMessage());
        }
    }

    private void updateAuditorAssignment(String selectedAuditor) {
        try {
            // Extraer solo el nombre del auditor (antes del paréntesis)
            String auditorName = selectedAuditor.split(" \\(")[0];
            
            AuditoryDAO dao = new AuditoryDAO();
            AuditorsDAO auditorsDAO = new AuditorsDAO();
            
            // Obtener el ID del auditor por su nombre
            int auditorId = auditorsDAO.getIdbyName(auditorName);
            
            // Actualizar la asignación en la base de datos
            boolean success = dao.updateAuditoryAssignedUser(currentAuditory.getAuditoryId(), auditorId);
            
            if (success) {
                // Actualizar el modelo local
                currentAuditory.setEncargado(auditorName);
                System.out.println("Auditor actualizado exitosamente a: " + auditorName);
            } else {
                System.err.println("ERROR: No se pudo actualizar el auditor asignado");
            }
        } catch (Exception e) {
            System.err.println("ERROR al actualizar auditor: " + e.getMessage());
        }
    }

    private void updateAuditoryState(String newState) {
        try {
            AuditoryDAO dao = new AuditoryDAO();
            boolean success = dao.updateAuditoryState(currentAuditory.getAuditoryId(), newState);
            
            if (success) {
                currentAuditory.setEstado(newState);
                refreshAuditoryFromDatabase();
            } else {
                System.err.println("ERROR: No se pudo actualizar el estado de la auditoría");
            }
        } catch (Exception e) {
            System.err.println("ERROR al actualizar estado: " + e.getMessage());
        }
    }
    
    /**
     * Refresca los datos de la auditoría desde la base de datos para verificar que los cambios se guardaron
     */
    private void refreshAuditoryFromDatabase() {
        try {
            AuditoryDAO dao = new AuditoryDAO();
            List<Auditory> allAuditories = dao.getAllAuditories();
            
            // Buscar la auditoría actual en la lista actualizada
            Auditory refreshedAuditory = allAuditories.stream()
                    .filter(a -> a.getAuditoryId() == currentAuditory.getAuditoryId())
                    .findFirst()
                    .orElse(null);
            
            if (refreshedAuditory != null) {
                // Actualizar el modelo local con los datos frescos
                currentAuditory.setEstado(refreshedAuditory.getEstado());
                currentAuditory.setEncargado(refreshedAuditory.getEncargado());
                
                // Actualizar la interfaz con los datos frescos
                updateInterfaceFromModel();
            } else {
                System.err.println("ERROR: No se pudo encontrar la auditoría actualizada en la BD");
            }
        } catch (Exception e) {
            System.err.println("ERROR al refrescar datos desde BD: " + e.getMessage());
        }
    }
    
    /**
     * Actualiza la interfaz con los datos del modelo actual
     */
    private void updateInterfaceFromModel() {
        if (currentAuditory != null) {
            // Actualizar ComboBox de estado
            if (comboBoxEstadoAuditoria != null && currentAuditory.getEstado() != null) {
                String estadoActual = currentAuditory.getEstado();
                comboBoxEstadoAuditoria.setValue(estadoActual);
            }
            
            // Actualizar ComboBox de auditor
            if (comboBoxAuditor != null && currentAuditory.getEncargado() != null) {
                String encargado = currentAuditory.getEncargado();
                String selectedItem = comboBoxAuditor.getItems().stream()
                        .filter(item -> item.startsWith(encargado + " ("))
                        .findFirst()
                        .orElse(null);
                if (selectedItem != null) {
                    comboBoxAuditor.setValue(selectedItem);
                }
            }
        }
    }

    /**
     * Establece los datos de la auditoría a mostrar
     */
    public void setAuditoryData(Auditory auditory) {
        this.currentAuditory = auditory;
        
        if (auditory != null) {
            // Cargar información básica
            if (lblAuditoria != null) {
                lblAuditoria.setText("Auditoria ID: " + auditory.getAuditoryId());
            }
            if (lblNombre != null) {
                lblNombre.setText("Nombre: " + auditory.getNombre());
            }
            if (lblDatos != null) {
                lblDatos.setText("Datos: " + auditory.getCliente());
            }
            if (lblAuditorEncargado != null) {
                lblAuditorEncargado.setText("Auditor encargado:");
            }
            
            // Asegurar que los auditores estén cargados antes de preseleccionar
            if (comboBoxAuditor != null && comboBoxAuditor.getItems().isEmpty()) {
                loadAuditors();
            }
            
            // Preseleccionar el auditor en el ComboBox
            if (comboBoxAuditor != null && auditory.getEncargado() != null) {
                String encargado = auditory.getEncargado();
                String selectedItem = comboBoxAuditor.getItems().stream()
                        .filter(item -> item.startsWith(encargado + " ("))
                        .findFirst()
                        .orElse(null);
                if (selectedItem != null) {
                    comboBoxAuditor.setValue(selectedItem);
                } else {
                    // Si no se encuentra el auditor, dejarlo como prompt text
                    comboBoxAuditor.setValue(null);
                }
            }
            
            // Preseleccionar el estado en el ComboBox
            if (comboBoxEstadoAuditoria != null && auditory.getEstado() != null) {
                String estadoActual = auditory.getEstado();
                System.out.println("DEBUG: Estado actual de la auditoría: '" + estadoActual + "'");
                System.out.println("DEBUG: Estados disponibles en ComboBox: " + comboBoxEstadoAuditoria.getItems());
                
                // Buscar el estado exacto o similar
                String estadoEncontrado = comboBoxEstadoAuditoria.getItems().stream()
                        .filter(item -> item.equalsIgnoreCase(estadoActual))
                        .findFirst()
                        .orElse(null);
                
                if (estadoEncontrado != null) {
                    comboBoxEstadoAuditoria.setValue(estadoEncontrado);
                    System.out.println("DEBUG: Estado preseleccionado: " + estadoEncontrado);
                } else {
                    System.out.println("DEBUG: Estado '" + estadoActual + "' no encontrado en las opciones disponibles");
                    comboBoxEstadoAuditoria.setValue("PENDIENTE"); // Valor por defecto
                }
            } else {
                System.out.println("DEBUG: No hay estado para preseleccionar");
                if (comboBoxEstadoAuditoria != null) {
                    comboBoxEstadoAuditoria.setValue("PENDIENTE");
                }
            }
            
            // Cargar observaciones desde la base de datos
            loadObservationsAndFindings();
        }
    }

    private void loadObservationsAndFindings() {
        if (currentAuditory != null) {
            try {
                AuditoryDAO dao = new AuditoryDAO();
                
                // Cargar observaciones
                List<String> observations = dao.getObservationsByAuditoryId(currentAuditory.getAuditoryId());
                if (txtAreaObservaciones != null) {
                    txtAreaObservaciones.clear();
                    if (observations != null && !observations.isEmpty()) {
                        for (int i = 0; i < observations.size(); i++) {
                            txtAreaObservaciones.appendText(observations.get(i));
                            if (i < observations.size() - 1) {
                                txtAreaObservaciones.appendText("\n\n");
                            }
                        }
                    } else {
                        txtAreaObservaciones.setText("No hay observaciones registradas");
                    }
                }
                
                // Cargar hallazgos
                List<String> findings = dao.getFindingsByAuditoryId(currentAuditory.getAuditoryId());
                if (txtAreaHallazgos != null) {
                    txtAreaHallazgos.clear();
                    if (findings != null && !findings.isEmpty()) {
                        for (int i = 0; i < findings.size(); i++) {
                            txtAreaHallazgos.appendText(findings.get(i));
                            if (i < findings.size() - 1) {
                                txtAreaHallazgos.appendText("\n\n");
                            }
                        }
                    } else {
                        txtAreaHallazgos.setText("No hay hallazgos registrados");
                    }
                }
                
            } catch (Exception e) {
                System.err.println("ERROR al cargar observaciones y hallazgos: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleGuardarAuditor(ActionEvent event) {
        if (currentAuditory != null && comboBoxAuditor != null && comboBoxAuditor.getValue() != null) {
            String selectedAuditor = comboBoxAuditor.getValue();
            updateAuditorAssignment(selectedAuditor);
            
            // Mostrar confirmación visual
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle("Guardado exitoso");
            alert.setHeaderText(null);
            alert.setContentText("El auditor encargado ha sido actualizado correctamente.");
            alert.showAndWait();
        } else {
            // Mostrar error si no hay auditor seleccionado
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText(null);
            alert.setContentText("Por favor selecciona un auditor encargado antes de guardar.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleGuardarEstado(ActionEvent event) {
        if (currentAuditory == null) {
            System.err.println("ERROR: currentAuditory es null");
            return;
        }
        
        if (comboBoxEstadoAuditoria == null) {
            System.err.println("ERROR: comboBoxEstadoAuditoria es null");
            return;
        }
        
        String selectedState = comboBoxEstadoAuditoria.getValue();
        
        if (selectedState != null && !selectedState.trim().isEmpty()) {
            updateAuditoryState(selectedState);
            
            // Mostrar confirmación visual
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle("Guardado exitoso");
            alert.setHeaderText(null);
            alert.setContentText("El estado de la auditoría ha sido actualizado a: " + selectedState);
            alert.showAndWait();
        } else {
            // Mostrar error si no hay estado seleccionado
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText(null);
            alert.setContentText("Por favor selecciona un estado antes de guardar.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleVolver(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/inspector/inspectorListPanel.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1280, 800);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERROR: No se pudo cargar el panel de lista del inspector");
        }
    }

    @FXML
    private void handleRealizarAuditoria(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/inspector/adminCreatePanel.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1280, 800);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERROR: No se pudo cargar el panel de crear auditoría");
        }
    }

    @FXML
    private void handleListaAuditorias(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/inspector/inspectorListPanel.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1280, 800);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERROR: No se pudo cargar el panel de lista del inspector");
        }
    }

    @FXML
    private void handleExportarPDF(ActionEvent event) {
        if (currentAuditory != null) {
            // Aquí se implementaría la lógica de exportación a PDF
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle("Exportar PDF");
            alert.setHeaderText(null);
            alert.setContentText("Funcionalidad de exportar PDF en desarrollo");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleExit(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1280, 800);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERROR: No se pudo cargar el login");
        }
    }
}
