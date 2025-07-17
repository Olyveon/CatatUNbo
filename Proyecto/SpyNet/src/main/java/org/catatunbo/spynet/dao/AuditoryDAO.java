package org.catatunbo.spynet.dao;

import org.catatunbo.spynet.database.DatabaseConnection;
import org.catatunbo.spynet.Auditory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuditoryDAO {
    public List<Auditory> getAllAuditories() {
        List<Auditory> list = new ArrayList<>();
        String sql = "SELECT * FROM vista_auditorias_inspector_admin";
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Auditory(
                        rs.getInt("auditory_id"),
                        rs.getString("auditory_name"),
                        rs.getString("client_name"),
                        rs.getString("username"),
                        (rs.getDate("auditory_date_init") != null ? rs.getDate("auditory_date_init").toLocalDate() : null),
                        (rs.getDate("auditory_date_limit") != null ? rs.getDate("auditory_date_limit").toLocalDate() : null),
                        rs.getString("auditory_state")
                ));
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
                            +"\n"+rs.getString("observation_datetime")
                            +"\n\n";
                observations.add(observation); 
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return observations;

    }
}
