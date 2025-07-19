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
        String sql = "SELECT * FROM vista_auditorias_inspector_admin";
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Auditory auditory = new Auditory(
                    rs.getInt("auditory_id"),
                    rs.getString("auditory_name"),
                    rs.getString("client_name"),
                    rs.getString("username"),
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
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
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

}
