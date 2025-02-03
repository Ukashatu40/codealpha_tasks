package com.ukasha.dao;

import com.ukasha.db.DBConnection;
import com.ukasha.model.Attendance;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttendanceDAO {

    // Add an attendance record
    public boolean addAttendance(Attendance attendance) {
        String sql = "INSERT INTO attendance (student_id, date, status) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, attendance.getStudentId());
            stmt.setDate(2, attendance.getDate());
            stmt.setString(3, attendance.getStatus());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get attendance by student ID
    public List<Attendance> getAttendanceByStudentId(int studentId) {
        List<Attendance> attendanceList = new ArrayList<>();
        String sql = "SELECT * FROM attendance WHERE student_id = ?";
    
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                Attendance attendance = new Attendance(
                    rs.getInt("id"),
                    rs.getInt("student_id"),
                    rs.getDate("date"),
                    rs.getString("status")
                );
                attendanceList.add(attendance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attendanceList;
    }
    

    // Get all attendance records
    public List<Attendance> getAllAttendance() {
        List<Attendance> attendanceList = new ArrayList<>();
        String sql = "SELECT * FROM attendance";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                attendanceList.add(new Attendance(
                        rs.getInt("id"),
                        rs.getInt("student_id"),
                        rs.getDate("date"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attendanceList;
    }

    public Map<String, Integer> getAttendanceStats() {
        Map<String, Integer> stats = new HashMap<>();
        String sql = "SELECT status, COUNT(*) as count FROM attendance GROUP BY status";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                stats.put(rs.getString("status"), rs.getInt("count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stats;
    }

    public List<Attendance> getAttendanceByDateRange(String startDate, String endDate) {
        List<Attendance> attendanceList = new ArrayList<>();
        String sql = "SELECT * FROM attendance WHERE date BETWEEN ? AND ?";
    
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));
    
            System.out.println("📌 SQL Query: " + stmt.toString()); // Debugging
    
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                Attendance attendance = new Attendance(
                    rs.getInt("id"),
                    rs.getInt("student_id"),
                    rs.getDate("date"),
                    rs.getString("status")
                );
                attendanceList.add(attendance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attendanceList;
    }
    

    public List<Attendance> getAttendanceByDateRangeAndStudentId(String startDate, String endDate, int studentId) {
        List<Attendance> attendanceList = new ArrayList<>();
        String sql = "SELECT * FROM attendance WHERE date BETWEEN ? AND ? AND student_id = ?";
    
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));
            stmt.setInt(3, studentId);
    
            System.out.println("📌 Executing SQL: " + stmt.toString()); // Debugging
    
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                Attendance attendance = new Attendance(
                    rs.getInt("id"),
                    rs.getInt("student_id"),
                    rs.getDate("date"),
                    rs.getString("status")
                );
                attendanceList.add(attendance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attendanceList;
    }
    

public Map<String, Integer> getAttendanceSummary() {
    Map<String, Integer> stats = new HashMap<>();
    String sql = "SELECT status, COUNT(*) AS count FROM attendance GROUP BY status";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            stats.put(rs.getString("status"), rs.getInt("count"));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return stats;
}

public Attendance getAttendanceById(int id) {
    Attendance attendance = null;
    String sql = "SELECT * FROM attendance WHERE id = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            int studentId = rs.getInt("student_id");
            Date date = rs.getDate("date");
            String status = rs.getString("status");
            attendance = new Attendance(id, studentId, date, status);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return attendance;
}

public void updateAttendance(Attendance attendance) {
    String sql = "UPDATE attendance SET status = ? WHERE id = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, attendance.getStatus());
        stmt.setInt(2, attendance.getId());
        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


public boolean deleteAttendance(int id) {
    String sql = "DELETE FROM attendance WHERE id=?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, id);
        int rowsDeleted = stmt.executeUpdate();
        return rowsDeleted > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

    
}
