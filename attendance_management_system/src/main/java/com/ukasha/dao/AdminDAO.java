package com.ukasha.dao;

import com.ukasha.db.DBConnection;
import com.ukasha.model.Admin;
import java.sql.*;

public class AdminDAO {
    public Admin validateAdmin(String username, String password) {
        String sql = "SELECT * FROM admin WHERE username = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setString(1, username);
            stmt.setString(2, password);
            System.out.println("Executing Query: " + stmt.toString());  // Debugging Line

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Login Success!"); // Debugging Line
                return new Admin(rs.getInt("id"), rs.getString("username"), rs.getString("password"));
            } else {
                System.out.println("Invalid Credentials!"); // Debugging Line
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
