package org.catatunbo.spynet.controllers;


import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.catatunbo.spynet.PasswordObject;

public class PasswordHasher {
    private static final int ITERATIONS = 1000;
    private static final int KEY_LENGTH = 256;

    public String hashPassword(PasswordObject passwordObj) throws NoSuchAlgorithmException, InvalidKeySpecException{
        return hashPassword(passwordObj.getPassword(), passwordObj.getSalt());
    }

    private static String hashPassword(String password, String saltStr) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // NOTE: CHATGPT
        byte[] salt = Base64.getDecoder().decode(saltStr);
        // Encode password + salt
        PBEKeySpec keySpecification = new PBEKeySpec(password.toCharArray(),
                                                     salt,
                                                     ITERATIONS,
                                                     KEY_LENGTH); 
        // Hash algorithm
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");   
        // Hash the encoded info
        byte[] hash = skf.generateSecret(keySpecification).getEncoded();        
        return Base64.getEncoder().encodeToString(hash);
    }
}
