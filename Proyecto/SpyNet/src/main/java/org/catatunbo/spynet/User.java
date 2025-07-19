package org.catatunbo.spynet;

public class User {
    private int userId;
    private String username;
    private String passwordHash;
    private String passwordSalt;
    private String userRole;
    private String userState;
    private String dateRegister;
    private String lastSession;

    // Default constructor
    public User() {}
    
    // Constructor 
    public User(int userId, String username, String passwordHash, String passwordSalt, String userRole, String userState, String dateRegister, String lastSession) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.passwordSalt = passwordSalt;
        this.userRole = userRole;
        this.userState = userState;
        this.dateRegister = dateRegister;
        this.lastSession = lastSession;
    }

    // Constructor without password fields for asigning roles
    public User(int userId, String username, String userRole, String lastSession, String userState) {
        this.userId = userId;
        this.username = username;
        this.userRole = userRole;
        this.lastSession = lastSession;
        this.userState = userState;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPasswordHash() {
        return passwordHash;
    }
    
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
    public String getPasswordSalt() {
        return this.passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }
    public String getUserRole() {
        return userRole;
    }
    
    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
    
    public String getUserState() {
        return userState;
    }
    
    public void setUserState(String userState) {
        this.userState = userState;
    }
    
    public String getDateRegister() {
        return dateRegister;
    }
    
    public void setDateRegister(String dateRegister) {
        this.dateRegister = dateRegister;
    }
    
    public String getLastSession() {
        return lastSession;
    }
    
    public void setLastSession(String lastSession) {
        this.lastSession = lastSession;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", userRole='" + userRole + '\'' +
                ", userState='" + userState + '\'' +
                '}';
    }
}
