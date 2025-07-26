package org.catatunbo.spynet.tests;

import org.catatunbo.spynet.*;
import org.catatunbo.spynet.auditUtils.OpenAIConfig;
import org.catatunbo.spynet.auditUtils.ReporteAuditoriaPDF;
import org.catatunbo.spynet.auditUtils.nmapCommand;
import org.catatunbo.spynet.controllers.PasswordHasher;
import org.catatunbo.spynet.dao.AuditoryDAO;
import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

class SpyNetCoreTests {

    
    @Test
    void testPDFGenerationCreatesFile() {
        /**
         * Verifica que se genere un archivo PDF correctamente.
         * 
         * Crea un PDF con datos ficticios de auditoría y verifica que:
         * - El archivo exista
         * - No esté vacío
         * 
         * Esto garantiza que la funcionalidad de generación de reportes está activa.
         */
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
        /**
         * Ejecuta un escaneo de Nmap contra 127.0.0.1 y verifica que la salida sea válida.
         * 
         * Esto comprueba que la herramienta Nmap esté correctamente integrada y operativa dentro del sistema.
         */
        nmapCommand nmap = new nmapCommand();
        // Simula un comando seguro (puedes cambiar la IP por una de prueba)
        String output = nmap.executeNmap("-sn", "127.0.0.1");
        assertNotNull(output, "La salida de Nmap no debe ser nula");
        assertTrue(output.toLowerCase().contains("nmap"), "La salida debe contener la palabra 'nmap'");
    }

    @Test
    void testDatabaseConnection() {
        /**
        *  Valida la conexión con la base de datos MySQL.
        * 
        * Verifica que:
        * - La conexión no sea nula
        * - La conexión esté abierta
        * 
        * Esto garantiza que las credenciales y la URL de la base de datos estén bien configuradas.
        */
        try {
            // Ajusta la URL, usuario y contraseña según tu configuración real
            String url = "jdbc:mysql://localhost:3306/spynetdb";
            String user = "root";
            String password = App.mysqlPassword; //  tu contraseña real

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
    void testIfDataBaseExists() {
        final String dbName = "spynetdb";
        final String mysqlPath = "mysql"; 
        final String user = "root";
        final String password = "\"" + App.mysqlPassword + "\"";

        String sql = String.format("""
                SELECT SCHEMA_NAME
                FROM INFORMATION_SCHEMA.SCHEMATA
                WHERE SCHEMA_NAME = '{0}'
                """, dbName);

        List<String> command = Arrays.asList(
                mysqlPath,
                "-u", user,
                "-p" + password,
                "-h", "localhost",
                "-P", "3306",
                "-e", sql
        );

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);

        ArrayList<String> lines = new ArrayList<>();
        try {
            Process process = processBuilder.start();
            String line;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    lines.add(line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Script executed successfully.");
            } else {
                System.err.println("MySQL script failed. Exit code: " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            assertFalse(lines.isEmpty());
        }
    }
    @Test
    void testSessionSingletonAndRoleValidation() {
        /**
         *  Valida el patrón Singleton en la clase Session y la lógica de roles.
         * 
         * - Verifica que se retorne siempre la misma instancia
         * - Comprueba si los roles son correctamente asignados y verificados
         */
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
        /**
         * Prueba la consistencia del algoritmo de hash de contraseñas.
         * 
         * - Mismo password + salt → mismo hash
         * - Distinto salt → distinto hash
         * - El hash nunca debe ser igual a la contraseña original
         * 
         * Esto garantiza una implementación segura de almacenamiento de contraseñas.
         */
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
        /**
         * Verifica los estados del usuario y la lógica de negocio relacionada.
         * 
         * - Compara estados "ACTIVE" e "INACTIVE"
         * - Evalúa el método toString()
         * - Valida fechas de sesión distintas
         */
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

    @Test
    void getAllAuditories() {
        /**
         * Recupera todas las auditorías desde la base de datos.
         * 
         * Verifica que:
         * - La lista de auditorías no sea nula
         * - Contenga al menos un elemento
         */
        AuditoryDAO dao = new AuditoryDAO();
        List<Auditory> auditories = dao.getAllAuditories();
        assertNotNull(auditories, "La lista no debe ser null");
        assertFalse(auditories.isEmpty(), "La lista no debe estar vacía");
    }

    @Test
    void insertFinding() {
        /**
         * Inserta un hallazgo de auditoría en la base de datos.
         * 
         * Comprueba que la operación devuelva true, validando la inserción correcta.
         */
        AuditoryDAO dao = new AuditoryDAO();
        // Usa IDs válidos según tu base de datos de pruebas
        int auditoryId = 1;
        int userId = 1;
        String title = "Test Finding";
        String description = "Descripción de prueba";
        String risk = "ALTO";
        boolean result = dao.insertFinding(auditoryId, userId, title, description, risk);
        assertTrue(result, "La inserción del hallazgo debe ser exitosa");
    }

    @Test
    void updateAuditoryAssignedUser() {
        /**
         * Actualiza el auditor asignado a una auditoría existente.
         * 
         * Este test asegura que el cambio de asignación se registre exitosamente.
         */
        AuditoryDAO dao = new AuditoryDAO();
        int auditoryId = 1; // ID válido
        int newUserId = 2;  // ID válido
        boolean result = dao.updateAuditoryAssignedUser(auditoryId, newUserId);
        assertTrue(result, "La actualización del usuario asignado debe ser exitosa");
    }

    @Test
    void createAuditory() {
        /**
         * Crea una nueva auditoría asociada a un cliente específico.
         * 
         * Verifica que se retorne un ID válido (mayor que cero).
         */
        AuditoryDAO dao = new AuditoryDAO();
        String name = "Auditoría de prueba";
        int clientId = 1; // ID válido
        Timestamp dateLimit = Timestamp.valueOf(LocalDateTime.now().plusDays(7));
        int newAuditoryId = dao.createAuditory(name, clientId, dateLimit);
        assertTrue(newAuditoryId > 0, "El ID de la nueva auditoría debe ser mayor a 0");
    }

    @Test
    void testOpenAIApiKeyIsPresent() {
        /**
         * Valida que la API Key de OpenAI esté configurada en los archivos de propiedades.
         * 
         * Este test previene errores de conexión futuros con la API.
         */
        String apiKey = OpenAIConfig.getApiKey();
        assertNotNull(apiKey, "La API key de OpenAI no debe ser nula");
        assertFalse(apiKey.isBlank(), "La API key de OpenAI no debe estar vacía");
    }

}