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
}
