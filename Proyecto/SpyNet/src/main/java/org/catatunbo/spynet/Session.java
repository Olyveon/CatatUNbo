package org.catatunbo.spynet;

public class Session { 
    private static Session instance;
    private User currentUser;
    
    private Session() {}
    
    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    public String getCurrentUserRole() {
        return currentUser != null ? currentUser.getUserRole() : null;
    }
    
    public String getCurrentUsername() {
        return currentUser != null ? currentUser.getUsername() : null;
    }
    
    public void logout() {
        this.currentUser = null;
    }
    
    public boolean hasRole(String role) {
        return currentUser != null && role.equals(currentUser.getUserRole());
    }
}
