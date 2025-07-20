package org.catatunbo.spynet.tests;

import org.catatunbo.spynet.auditUtils.ReporteAuditoriaPDF;
import org.catatunbo.spynet.auditUtils.nmapCommand;
import org.catatunbo.spynet.User;
import org.catatunbo.spynet.AuditorCount;
import org.catatunbo.spynet.PasswordObject;
import org.catatunbo.spynet.Session;
import org.catatunbo.spynet.controllers.PasswordHasher;
import org.junit.jupiter.api.*;

import java.io.File;



import static org.junit.jupiter.api.Assertions.*;

class SpyNetCoreTests {

    
    @Test
    void testPDFGenerationCreatesFile() {
        ReporteAuditoriaPDF pdf = new ReporteAuditoriaPDF();
        pdf.generarReporte(
                "Cliente de Prueba\nAuditor JUnit\nDatos\nPENDIENTE",
                "Hallazgo de prueba generado por JUnit",
                "Observación de prueba generada por JUnit",
                "Bitácora IA de prueba generada por JUnit"
        );
        String path = System.getProperty("user.home") + "/SpynetReports/" + pdf.getFilename();
        File file = new File(path);
        assertTrue(file.exists() && file.length() > 0, "El PDF debe generarse y no estar vacío");
        // Limpieza opcional
        file.delete();
    }

    @Test
    void testNmapCommandReturnsOutput() {
        nmapCommand nmap = new nmapCommand();
        // Simula un comando seguro (puedes cambiar la IP por una de prueba)
        String output = nmap.executeNmap("-sn", "127.0.0.1");
        assertNotNull(output, "La salida de Nmap no debe ser nula");
        assertTrue(output.toLowerCase().contains("nmap"), "La salida debe contener la palabra 'nmap'");
    }

    @Test
    void testDatabaseConnection() {
        try {
            // Ajusta la URL, usuario y contraseña según tu configuración real
            String url = "jdbc:mysql://localhost:3306/spynetdb";
            String user = "root";
            String password = "sphynx4"; //  tu contraseña real

            // Intenta abrir la conexión
            try (java.sql.Connection conn = java.sql.DriverManager.getConnection(url, user, password)) {
                assertNotNull(conn, "La conexión no debe ser nula");
                assertFalse(conn.isClosed(), "La conexión debe estar abierta");
            }
        } catch (Exception e) {
            fail("No se pudo establecer conexión a la base de datos: " + e.getMessage());
        }
    }

    @Test
    void testSessionSingletonAndRoleValidation() {
        Session session1 = Session.getInstance();
        Session session2 = Session.getInstance();
        
        // Verifica patrón Singleton
        assertSame(session1, session2, "Session debe implementar patrón Singleton correctamente");
        
        // Verifica lógica de roles
        User adminUser = new User(1, "admin", "Admin", "2025-07-20", "ACTIVE");
        session1.setCurrentUser(adminUser);
        
        assertTrue(session1.hasRole("Admin"), "Debe validar correctamente el rol Admin");
        assertFalse(session1.hasRole("Inspector"), "Debe rechazar roles incorrectos");
        assertEquals("admin", session1.getCurrentUsername(), "Debe retornar el username correcto");
    }

    @Test
    void testPasswordHashingConsistencyAndSecurity() throws Exception {
        PasswordHasher hasher = new PasswordHasher();
        String password = "securePassword123";
        String salt = "dGVzdFNhbHQ="; // Base64 encoded "testSalt"
        
        PasswordObject passwordObj1 = new PasswordObject(password, salt);
        PasswordObject passwordObj2 = new PasswordObject(password, salt);
        
        String hash1 = hasher.hashPassword(passwordObj1);
        String hash2 = hasher.hashPassword(passwordObj2);
        
        // Verifica consistencia
        assertEquals(hash1, hash2, "El mismo password y salt deben producir el mismo hash");
        
        // Verifica que el hash no es igual al password original (seguridad básica)
        assertNotEquals(password, hash1, "El hash debe ser diferente al password original");
        
        // Verifica que diferentes salts producen diferentes hashes
        PasswordObject differentSaltObj = new PasswordObject(password, "ZGlmZmVyZW50U2FsdA==");
        String differentHash = hasher.hashPassword(differentSaltObj);
        assertNotEquals(hash1, differentHash, "Diferentes salts deben producir diferentes hashes");
    }

    @Test
    void testUserStateValidationAndBusinessLogic() {
        User activeUser = new User(1, "activeUser", "Inspector", "2025-07-20", "ACTIVE");
        User inactiveUser = new User(2, "inactiveUser", "Admin", "2025-07-19", "INACTIVE");
        
        // Verifica que toString incluye información relevante
        String activeToString = activeUser.toString();
        assertTrue(activeToString.contains("ACTIVE") && activeToString.contains("Inspector"), 
                   "toString debe incluir estado y rol para usuario activo");
        
        String inactiveToString = inactiveUser.toString();
        assertTrue(inactiveToString.contains("INACTIVE") && inactiveToString.contains("Admin"), 
                   "toString debe incluir estado y rol para usuario inactivo");
        
        // Verifica lógica de negocio: usuarios con diferentes estados
        assertNotEquals(activeUser.getUserState(), inactiveUser.getUserState(), 
                       "Estados de usuarios deben ser diferentes");
        
        // Verifica que la fecha de último acceso es diferente
        assertNotEquals(activeUser.getLastSession(), inactiveUser.getLastSession(),
                       "Fechas de última sesión deben ser diferentes");
    }

}