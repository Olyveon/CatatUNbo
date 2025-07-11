package org.catatunbo.spynet.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordHasher {
    private static final int SALT_LENGTH = 16;
    private static final int ITERATIONS = 1000;
    private static final int KEY_LENGTH = 256;

    public static void main(String[] args) {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String password = readLine(in);
        try {
            System.out.println(hashPassword(password)); 
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.toString());
        } catch(InvalidKeySpecException e) {
            System.out.println(e.toString());
        }
               
    }

    public static String hashPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException{
        return hashPassword(password, generateSalt());
    }

    // NOTE: TMP 
    private static String readLine(BufferedReader in) {
        try {
            return in.readLine();
        } catch (IOException e) {
            System.out.println(e.toString());
            return null;
        }
    }

    private static byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        SecureRandom s = new SecureRandom();
        s.nextBytes(salt);
        return salt;
    }

    private static String encodeToString(byte[] arr) {
        return Base64.getEncoder().encodeToString(arr);
    }

    private static String hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // NOTE: CHATGPT
        // Encode password + salt
        PBEKeySpec keySpecification = new PBEKeySpec(password.toCharArray(),
                                                     salt,
                                                     ITERATIONS,
                                                     KEY_LENGTH); 
        // Hash algorithm
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");   
        // Hash the encoded info
        byte[] hash = skf.generateSecret(keySpecification).getEncoded();        
        return encodeToString(hash);
    }
}
