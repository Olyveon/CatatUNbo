package org.catatunbo.spynet.tests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.catatunbo.spynet.User;
import org.catatunbo.spynet.controllers.CreateUserController;
import org.catatunbo.spynet.dao.UserDAO;
import org.junit.jupiter.api.Test;

public class CreateUserTests {
    @Test
    void testAddNewUserNotThrowException() {
        CreateUserController createUserController = new CreateUserController();
        User newUser = createUserController.createNewClient("testUser", "password");
        UserDAO userDAO = new UserDAO();

        assertDoesNotThrow(() -> {
            userDAO.addToDataBase(newUser);
        });
    }
}
