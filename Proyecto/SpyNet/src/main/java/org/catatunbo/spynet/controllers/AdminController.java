package org.catatunbo.spynet.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import org.catatunbo.spynet.dao.AuditoryDAO;
import org.catatunbo.spynet.Auditory;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class AdminController {

    @FXML
    private TableView<Auditory> tableAuditories;
    @FXML
    private TableColumn<Auditory, String> colNombre;
    @FXML
    private TableColumn<Auditory, String> colCliente;
    @FXML
    private TableColumn<Auditory, String> colEncargado;
    @FXML
    private TableColumn<Auditory, String> colFechaInicio;
    @FXML
    private TableColumn<Auditory, String> colFechaFin;
    @FXML
    private TableColumn<Auditory, String> colEstado;

    @FXML
    private void initialize() {
        configureTableLayout();

        AuditoryDAO dao = new AuditoryDAO();
        tableAuditories.setItems(FXCollections.observableArrayList(dao.getAllAuditories()));
        
       configureRowSelectionBehaviour();
    }

    private void configureTableLayout() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        
        // Configurar la columna de encargado con manejo mejorado de valores nulos
        colEncargado.setCellValueFactory(cellData -> {
            String encargado = cellData.getValue().getEncargado();
            return new javafx.beans.property.SimpleStringProperty(
                encargado != null && !encargado.trim().isEmpty() ? encargado : "Sin asignar"
            );
        });

        // Formatear fechas como String para la tabla
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        colFechaInicio.setCellValueFactory(cellData -> {
            var fecha = cellData.getValue().getFechaInicio();
            return javafx.beans.property.SimpleStringProperty.stringExpression(
                javafx.beans.binding.Bindings.createStringBinding(
                    () -> fecha != null ? fecha.format(formatter) : ""
                )
            );
        });
        colFechaFin.setCellValueFactory(cellData -> {
            var fecha = cellData.getValue().getFechaFin();
            return javafx.beans.property.SimpleStringProperty.stringExpression(
                javafx.beans.binding.Bindings.createStringBinding(
                    () -> fecha != null ? fecha.format(formatter) : ""
                )
            );
        });

        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
    }

    private void configureRowSelectionBehaviour() {
        tableAuditories.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { 
                Auditory selectedAuditory = tableAuditories.getSelectionModel().getSelectedItem();
                if (selectedAuditory != null) {
                    openAuditPanel(selectedAuditory);
                }
            }
        });
    }

    // Abre el panel de auditoría con los datos de la auditoría seleccionada
    private void openAuditPanel(Auditory selectedAuditory) {
        try {
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/auditor/auditEditPanel.fxml"));
            Parent root = loader.load();
            
            
            // Obtener el controlador y pasarle los datos
            AdminAuditController auditController = loader.getController();
            
            if (auditController != null) {
                auditController.setAuditoryData(selectedAuditory);
            }
            
            // Cambiar a la nueva escena
            Stage stage = (Stage) tableAuditories.getScene().getWindow();
            Scene scene = new Scene(root, 1280, 800);
            stage.setScene(scene);
            stage.show();
            
            
        } catch (IOException e) {
            System.err.println("ERROR: Error al abrir panel de auditoría: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("ERROR: Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
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
    private void handleUser(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/adminUserPanel.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1280, 800);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleRealizarAuditoria(ActionEvent event) throws IOException {
        // Navegar al panel de crear auditoría para auditores
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/auditor/auditCreatePanel.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1280, 800);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleAuditoriasARealizar(ActionEvent event) throws IOException {
        // Ya estamos en el panel de lista de auditorías
        // Solo mostramos un mensaje informativo o refrescamos la tabla
        loadAuditories(); // Recargar auditorías con filtros apropiados
        System.out.println("Lista de auditorías actualizada");
    }

    /**
     * Carga las auditorías basándose en el rol del usuario actual.
     * Los auditores solo ven sus auditorías asignadas, los admins ven todas.
     */
    private void loadAuditories() {
        AuditoryDAO dao = new AuditoryDAO();
        
        // Obtener el usuario actual de la sesión
        org.catatunbo.spynet.User currentUser = org.catatunbo.spynet.Session.getInstance().getCurrentUser();
        
        if (currentUser != null && "auditor".equalsIgnoreCase(currentUser.getUserRole())) {
            // Si es auditor, solo mostrar sus auditorías asignadas
            String auditorUsername = currentUser.getUsername();
            tableAuditories.setItems(FXCollections.observableArrayList(dao.getAuditoriesByAuditorUsername(auditorUsername)));
            System.out.println("Cargando auditorías para auditor: " + auditorUsername);
        } else {
            // Si es admin o inspector, mostrar todas las auditorías
            tableAuditories.setItems(FXCollections.observableArrayList(dao.getAllAuditories()));
            System.out.println("Cargando todas las auditorías (usuario admin/inspector)");
        }
    }
}