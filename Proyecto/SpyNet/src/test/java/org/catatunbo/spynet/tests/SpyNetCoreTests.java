package org.catatunbo.spynet.tests;

import org.catatunbo.spynet.auditUtils.ReporteAuditoriaPDF;
import org.catatunbo.spynet.auditUtils.nmapCommand;
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

}