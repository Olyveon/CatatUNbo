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

    public void configureTableLayout() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        colEncargado.setCellValueFactory(new PropertyValueFactory<>("encargado"));

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

    public void configureRowSelectionBehaviour() {
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
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/adminAuditPanel.fxml"));
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

   
}