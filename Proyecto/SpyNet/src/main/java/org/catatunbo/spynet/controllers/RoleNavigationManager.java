package org.catatunbo.spynet.controllers;

import org.catatunbo.spynet.User;

/**
 * Clase encargada de manejar la navegación basada en roles de usuario.
 * Define las rutas FXML iniciales y disponibles para cada tipo de usuario.
 */
public class RoleNavigationManager {
    
    // Rutas iniciales para cada rol (siempre empiezan con "create")
    private static final String ADMIN_INITIAL_PATH = "/fxml/admin/adminCreatePanel.fxml";
    private static final String AUDITOR_INITIAL_PATH = "/fxml/auditor/auditCreatePanel.fxml";
    private static final String INSPECTOR_INITIAL_PATH = "/fxml/inspector/adminCreatePanel.fxml"; // Nota: inspector usa adminCreatePanel
    private static final String USER_INITIAL_PATH = "/fxml/user/userCreatePanel.fxml";
    
    // Rutas adicionales para cada rol
    private static final String ADMIN_MAIN_PATH = "/fxml/admin/adminCreatePanel.fxml";
    private static final String ADMIN_AUDIT_PATH = "/fxml/auditor/auditEditPanel.fxml";
    private static final String ADMIN_USER_PATH = "/fxml/admin/adminUserPanel.fxml";
    
    private static final String AUDITOR_LIST_PATH = "/fxml/auditor/auditListPanel.fxml";
    private static final String AUDITOR_EDIT_PATH = "/fxml/auditor/auditEditPanel.fxml";
    
    private static final String INSPECTOR_LIST_PATH = "/fxml/inspector/inspectorListPanel.fxml";
    private static final String INSPECTOR_EDIT_PATH = "/fxml/inspector/inspectorEditPanel.fxml";
    
    private static final String LOGIN_PATH = "/fxml/login.fxml";
    private static final String CREATE_USER_PATH = "/fxml/createUser.fxml";
    
    /**
     * Obtiene la ruta FXML inicial para un usuario basado en su rol.
     * Todos los roles empiezan con su respectivo panel de "create".
     * 
     * @param user El usuario para el cual obtener la ruta inicial
     * @return La ruta FXML inicial correspondiente al rol del usuario
     * @throws IllegalArgumentException Si el rol del usuario no es reconocido
     */
    public static String getInitialPathForUser(User user) {
        if (user == null || user.getUserRole() == null) {
            throw new IllegalArgumentException("Usuario o rol no puede ser nulo");
        }
        
        String role = user.getUserRole().toLowerCase();
        System.out.println("RoleNavigationManager: Obteniendo ruta inicial para rol: " + role);
        
        switch (role) {
            case "admin":
                System.out.println("RoleNavigationManager: Redirigiendo a panel de administrador: " + ADMIN_INITIAL_PATH);
                return ADMIN_INITIAL_PATH;
            case "auditor":
                System.out.println("RoleNavigationManager: Redirigiendo a panel de auditor: " + AUDITOR_INITIAL_PATH);
                return AUDITOR_INITIAL_PATH;
            case "inspector":
                System.out.println("RoleNavigationManager: Redirigiendo a panel de inspector: " + INSPECTOR_INITIAL_PATH);
                return INSPECTOR_INITIAL_PATH;
            case "cliente":
                System.out.println("RoleNavigationManager: Redirigiendo a panel de usuario: " + USER_INITIAL_PATH);
                return USER_INITIAL_PATH;
            default:
                System.err.println("RoleNavigationManager: Rol no reconocido: " + user.getUserRole());
                throw new IllegalArgumentException("Rol no reconocido: " + user.getUserRole());
        }
    }
    
    /**
     * Obtiene la ruta FXML inicial para un rol específico.
     * 
     * @param role El rol para el cual obtener la ruta inicial
     * @return La ruta FXML inicial correspondiente al rol
     */
    public static String getInitialPathForRole(String role) {
        if (role == null) {
            throw new IllegalArgumentException("El rol no puede ser nulo");
        }
        
        switch (role.toLowerCase()) {
            case "admin":
                return ADMIN_INITIAL_PATH;
            case "auditor":
                return AUDITOR_INITIAL_PATH;
            case "inspector":
                return INSPECTOR_INITIAL_PATH;
            case "user":
                return USER_INITIAL_PATH;
            default:
                throw new IllegalArgumentException("Rol no reconocido: " + role);
        }
    }
    
    /**
     * Verifica si un usuario tiene acceso a una ruta específica.
     * 
     * @param user El usuario a verificar
     * @param fxmlPath La ruta FXML a verificar
     * @return true si el usuario tiene acceso, false en caso contrario
     */
    public static boolean hasAccessToPath(User user, String fxmlPath) {
        if (user == null || fxmlPath == null) {
            return false;
        }
        
        String role = user.getUserRole().toLowerCase();
        
        // Acceso común para todos los roles
        if (fxmlPath.equals(LOGIN_PATH)) {
            return true;
        }
        
        // Acceso específico por rol
        switch (role) {
            case "admin":
                return fxmlPath.startsWith("/fxml/admin/") || 
                       fxmlPath.equals(CREATE_USER_PATH);
            case "auditor":
                return fxmlPath.startsWith("/fxml/auditor/");
            case "inspector":
                return fxmlPath.startsWith("/fxml/inspector/");
            case "user":
                return fxmlPath.startsWith("/fxml/user/");
            default:
                return false;
        }
    }
    
    /**
     * Obtiene todas las rutas disponibles para un rol específico.
     * 
     * @param role El rol para el cual obtener las rutas
     * @return Array de rutas FXML disponibles para el rol
     */
    public static String[] getAvailablePathsForRole(String role) {
        if (role == null) {
            return new String[0];
        }
        
        switch (role.toLowerCase()) {
            case "admin":
                return new String[]{
                    ADMIN_INITIAL_PATH,
                    ADMIN_MAIN_PATH,
                    ADMIN_AUDIT_PATH,
                    ADMIN_USER_PATH,
                    CREATE_USER_PATH,
                    LOGIN_PATH
                };
            case "auditor":
                return new String[]{
                    AUDITOR_INITIAL_PATH,
                    AUDITOR_LIST_PATH,
                    AUDITOR_EDIT_PATH,
                    LOGIN_PATH
                };
            case "inspector":
                return new String[]{
                    INSPECTOR_INITIAL_PATH,
                    INSPECTOR_LIST_PATH,
                    INSPECTOR_EDIT_PATH,
                    LOGIN_PATH
                };
            case "user":
                return new String[]{
                    USER_INITIAL_PATH,
                    LOGIN_PATH
                };
            default:
                return new String[]{LOGIN_PATH};
        }
    }
    
    /**
     * Obtiene la ruta del panel principal para un rol (después del create inicial).
     * 
     * @param role El rol para el cual obtener el panel principal
     * @return La ruta FXML del panel principal
     */
    public static String getMainPanelForRole(String role) {
        if (role == null) {
            return LOGIN_PATH;
        }
        
        switch (role.toLowerCase()) {
            case "admin":
                return ADMIN_MAIN_PATH;
            case "auditor":
                return AUDITOR_LIST_PATH;
            case "inspector":
                return INSPECTOR_LIST_PATH;
            case "user":
                return USER_INITIAL_PATH; // User solo tiene create panel
            default:
                return LOGIN_PATH;
        }
    }
    
    /**
     * Obtiene la ruta de login.
     * 
     * @return La ruta FXML del login
     */
    public static String getLoginPath() {
        return LOGIN_PATH;
    }
    
    /**
     * Obtiene la ruta de creación de usuarios.
     * 
     * @return La ruta FXML de creación de usuarios
     */
    public static String getCreateUserPath() {
        return CREATE_USER_PATH;
    }
}
