package org.catatunbo.spynet.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class CreateUserController {

    @FXML
    private Button userCreationButton;

    @FXML
    void handleUserCreation(ActionEvent event) {
        System.out.println("Funciona =D");
    }

}

