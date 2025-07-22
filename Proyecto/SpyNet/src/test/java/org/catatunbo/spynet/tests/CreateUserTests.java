package org.catatunbo.spynet.tests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;

import org.catatunbo.spynet.User;
import org.catatunbo.spynet.controllers.CreateUserController;
import org.catatunbo.spynet.dao.UserDAO;
import org.junit.jupiter.api.Test;

public class CreateUserTests {
    User testUser;

    public CreateUserTests() {
        this.testUser = new CreateUserController().createNewClient("testUser", "password");

    }

    @Test
    void testAddNewUserNotThrowException() {
        UserDAO userDAO = new UserDAO();

        assertDoesNotThrow(() -> {
            userDAO.addToDataBase(testUser);
        });
    }

    @Test 
    void testRemoveUser(){
        UserDAO userDAO = new UserDAO();
        assertDoesNotThrow(() -> {
            userDAO.removeUser(testUser);
        });
    }
}
