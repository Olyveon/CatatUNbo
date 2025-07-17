package org.catatunbo.spynet;

import java.security.SecureRandom;
import java.util.Base64;

public class PasswordObject {
    private String password;
    private String salt;
    private static final int SALT_LENGTH = 16;

    public PasswordObject(String password) {
        this.password = password;
        this.salt = generateSalt();
    }
    
    public PasswordObject(String password, String saltStr) {
        this.password = password;
        this.salt = saltStr;
    }

    public String getPassword(){
        return this.password;
    }

    public String getSalt() {
        return this.salt;
    }

    private static String generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        SecureRandom s = new SecureRandom();
        s.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
}
