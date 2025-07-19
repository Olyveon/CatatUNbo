package org.catatunbo.spynet.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.catatunbo.spynet.AuditorCount;
import org.catatunbo.spynet.database.DatabaseConnection;

public class AuditorsDAO {
    public List<AuditorCount> getAllAuditors() {
        List<AuditorCount> auditorCountList = new ArrayList<>();
        String sql = "SELECT * FROM active_auditors_with_auditory_count"; // Adjust the SQL query as needed
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                auditorCountList.add(new AuditorCount(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getInt("total_auditories")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving auditors: " + e.getMessage());
            e.printStackTrace();
        }
        return auditorCountList;
    }
    public int getIdbyName(String name) {
        int id = -1; // Default value if not found
        String sql = "SELECT user_id FROM active_auditors_with_auditory_count WHERE username = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                id = rs.getInt("user_id");
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving auditor ID by name: " + e.getMessage());
            e.printStackTrace();
        }
        return id;
    }
}
