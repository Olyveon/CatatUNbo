package org.catatunbo.spynet.dao;

import org.catatunbo.spynet.database.DatabaseConnection;
import org.catatunbo.spynet.Auditory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuditoryDAO {
    /**
     * Retrieves all auditory records from the database.
     *
     * @return A list of `Auditory` objects representing the records in the database.
     *         Returns an empty list if no records are found or an error occurs.
     */
    public List<Auditory> getAllAuditories() {
        List<Auditory> list = new ArrayList<>();
        String sql = """
            SELECT 
                a.auditory_id, 
                a.auditory_name, 
                c.client_name, 
                u.username, 
                u.user_id,
                a.auditory_date_init, 
                a.auditory_date_limit, 
                a.auditory_state,
                c.client_id
            FROM auditory a
            JOIN client c ON c.client_id = a.auditory_client_id
            LEFT JOIN auditory_access aa ON a.auditory_id = aa.aud_access_auditory_id
            LEFT JOIN user u ON u.user_id = aa.aud_access_user_id
            ORDER BY a.auditory_id
        """;
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String username = rs.getString("username");
                // Si no hay auditor asignado, mostrar "Sin asignar"
                if (username == null || username.trim().isEmpty()) {
                    username = "Sin asignar";
                }
                
                Auditory auditory = new Auditory(
                    rs.getInt("auditory_id"),
                    rs.getString("auditory_name"),
                    rs.getString("client_name"),
                    username,
                    (rs.getDate("auditory_date_init") != null ? rs.getDate("auditory_date_init").toLocalDate() : null),
                    (rs.getDate("auditory_date_limit") != null ? rs.getDate("auditory_date_limit").toLocalDate() : null),
                    rs.getString("auditory_state")
                );
                // Añade el id del cliente
                auditory.setAuditoryClientId(rs.getInt("client_id"));
                list.add(auditory);
            }
        } catch (SQLException e) {
            System.err.println("Error en imported auditories: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public List<String> getObservationsByAuditoryId(int auditoryid){

        List<String> observations = new ArrayList<>();
        String sql = "CALL get_observations_by_auditory_id(?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection()){
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, auditoryid);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                String observation = "["+rs.getString("username")+"]" 
                            + " --> "+rs.getString("observation_title").toUpperCase() 
                            +":\n" +rs.getString("observation_description")
                            +"\n"+rs.getString("observation_datetime");
                observations.add(observation); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return observations;
    }


    public List<String> getFindingsByAuditoryId(int auditoryid){

        List<String> observations = new ArrayList<>();
        String sql = "CALL get_findings_by_auditory_id(?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection()){
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, auditoryid);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                String observation = "["+rs.getString("username")+"]" 
                            + " --> "+rs.getString("finding_title").toUpperCase() 
                            +"\n" +rs.getString("finding_description")
                            +"\n¡RIESGO!: "+rs.getString("finding_security_risk")
                            +"\n"+rs.getString("finding_datetime");
                            
                observations.add(observation); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return observations;
    }


    public boolean insertObservation(int auditoryId, int userId, String title, String description) {
        String sql = "INSERT INTO observation (observation_auditory_id, observation_user_id, observation_title, observation_description, observation_datetime) VALUES (?, ?, ?, ?, NOW())";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, auditoryId);
            stmt.setInt(2, userId);
            stmt.setString(3, title);
            stmt.setString(4, description);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertFinding(int auditoryId, int userId, String title, String description, String risk) {
        String sql = "INSERT INTO finding (finding_auditory_id, finding_user_id, finding_title, finding_description, finding_security_risk, finding_datetime) VALUES (?, ?, ?, ?, ?, NOW())";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, auditoryId);
            stmt.setInt(2, userId);
            stmt.setString(3, title);
            stmt.setString(4, description);
            stmt.setString(5, risk);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateAuditoryState(int auditoryId, String newState) {
        String sql = "UPDATE auditory SET auditory_state = ? WHERE auditory_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newState);
            stmt.setInt(2, auditoryId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("ERROR: Error al actualizar estado de auditoría: " + e.getMessage());
            return false;
        }
    }

    public boolean updateAuditoryAssignedUser(int auditoryId, int newAssignedUserID) {
        String sql = "UPDATE auditory_access SET aud_access_user_id = ? WHERE aud_access_auditory_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newAssignedUserID);
            stmt.setInt(2, auditoryId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public List<String> getIPsByClientId(int clientId) {
        List<String> ips = new ArrayList<>();
        String sql = "CALL get_ip_by_client_id(?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ips.add(rs.getString("ip_direction"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ips;
    }

    /**
     * Busca un cliente por nombre. Si no existe, lo crea.
     * @param clientName Nombre del cliente
     * @return ID del cliente (existente o recién creado)
     */
    public int findOrCreateClient(String clientName) {
        // Primero buscar si el cliente ya existe
        String selectSql = "SELECT client_id FROM client WHERE client_name = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(selectSql)) {
            stmt.setString(1, clientName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("client_id");
            }
        } catch (SQLException e) {
            System.err.println("Error buscando cliente: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Si no existe, crear el cliente
        String insertSql = "INSERT INTO client (client_name, client_number, client_email) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, clientName);
            stmt.setString(2, "Sin especificar"); // Número por defecto
            stmt.setString(3, "sin_email@cliente.com"); // Email por defecto
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creando cliente: " + e.getMessage());
            e.printStackTrace();
        }
        
        return -1; // Error
    }

    /**
     * Crea una nueva auditoría
     * @param auditoryName Nombre de la auditoría
     * @param clientId ID del cliente
     * @param dateLimit Fecha límite
     * @return ID de la auditoría creada, o -1 si hubo error
     */
    public int createAuditory(String auditoryName, int clientId, java.sql.Timestamp dateLimit) {
        String sql = "INSERT INTO auditory (auditory_name, auditory_client_id, auditory_state, auditory_date_init, auditory_date_limit) VALUES (?, ?, 'PENDIENTE', NOW(), ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, auditoryName);
            stmt.setInt(2, clientId);
            stmt.setTimestamp(3, dateLimit);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int auditoryId = generatedKeys.getInt(1);
                    
                    // Asignar automáticamente la auditoría al usuario "SinAsignar" (ID 14)
                    boolean accessAssigned = assignAuditoryAccess(auditoryId, 14);
                    if (accessAssigned) {
                        System.out.println("Auditoría " + auditoryId + " asignada automáticamente a SinAsignar");
                    } else {
                        System.err.println("ERROR: No se pudo asignar la auditoría " + auditoryId + " a SinAsignar");
                    }
                    
                    return auditoryId;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creando auditoría: " + e.getMessage());
            e.printStackTrace();
        }
        
        return -1; // Error
    }

    /**
     * Asigna acceso a una auditoría para un usuario (crear relación auditory_access)
     * @param auditoryId ID de la auditoría
     * @param userId ID del usuario
     * @return true si se asignó correctamente
     */
    public boolean assignAuditoryAccess(int auditoryId, int userId) {
        String sql = "INSERT INTO auditory_access (aud_access_auditory_id, aud_access_user_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, auditoryId);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error asignando acceso a auditoría: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves auditory records assigned to a specific auditor.
     *
     * @param auditorUsername The username of the auditor whose auditorías we want to retrieve
     * @return A list of `Auditory` objects representing the auditorías assigned to the auditor.
     *         Returns an empty list if no records are found or an error occurs.
     */
    public List<Auditory> getAuditoriesByAuditorUsername(String auditorUsername) {
        List<Auditory> list = new ArrayList<>();
        String sql = "SELECT auditory_id, auditory_name, client_name, username, auditory_date_init, auditory_date_limit, auditory_state, client_id " +
                     "FROM auditory " +
                     "JOIN auditory_access ON auditory_id = aud_access_auditory_id " +
                     "JOIN user ON user_id = aud_access_user_id " +
                     "JOIN client ON client_id = auditory_client_id " +
                     "WHERE username = ?";
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, auditorUsername);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String username = rs.getString("username");
                // Si no hay auditor asignado, mostrar "Sin asignar"
                if (username == null) {
                    username = "Sin asignar";
                }
                
                Auditory auditory = new Auditory(
                    rs.getInt("auditory_id"),
                    rs.getString("auditory_name"),
                    rs.getString("client_name"),
                    username,
                    (rs.getDate("auditory_date_init") != null ? rs.getDate("auditory_date_init").toLocalDate() : null),
                    (rs.getDate("auditory_date_limit") != null ? rs.getDate("auditory_date_limit").toLocalDate() : null),
                    rs.getString("auditory_state")
                );
                // Añade el id del cliente
                auditory.setAuditoryClientId(rs.getInt("client_id"));
                list.add(auditory);
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo auditorías del auditor " + auditorUsername + ": " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
}
