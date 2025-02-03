package com.ukasha.servlet;

import com.google.gson.Gson;
import com.ukasha.dao.AttendanceDAO;
import com.ukasha.dao.StudentDAO;
import com.ukasha.model.Attendance;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@WebServlet("/attendance-stats")
public class AttendanceStatsServlet extends HttpServlet {
    private AttendanceDAO attendanceDAO = new AttendanceDAO();
    private StudentDAO studentDAO = new StudentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Get all attendance records
        List<Attendance> attendanceRecords = attendanceDAO.getAllAttendance();

        // Maps to store attendance data
        Map<String, Integer> dailyAttendance = new HashMap<>();
        Map<String, Integer> studentAttendance = new HashMap<>();

        // Date formatter (YYYY-MM-DD)
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Process attendance records
        for (Attendance attendance : attendanceRecords) {
            String date = dateFormat.format(attendance.getDate()); // Convert java.sql.Date to String
        
            // Fetch student name from StudentDAO
            String studentName = studentDAO.getStudentById(attendance.getStudentId()).getName(); 
        
            // Count daily attendance
            dailyAttendance.put(date, dailyAttendance.getOrDefault(date, 0) + 1);
        
            // Count per-student attendance
            studentAttendance.put(studentName, studentAttendance.getOrDefault(studentName, 0) + 1);
        }

        
        // Create a response JSON object
        Map<String, Object> stats = new HashMap<>();
        stats.put("dailyAttendance", dailyAttendance);
        stats.put("studentAttendance", studentAttendance);
        
        int presentCount = 0;
        int absentCount = 0;
        
        // Count present and absent records
        for (Attendance attendance : attendanceRecords) {
            if ("Present".equalsIgnoreCase(attendance.getStatus())) {
                presentCount++;
            } else if ("Absent".equalsIgnoreCase(attendance.getStatus())) {
                absentCount++;
            }
        }
        
        // Add Present vs Absent data to response
        stats.put("presentAbsent", Map.of("Present", presentCount, "Absent", absentCount));
        
        // Send JSON response
        PrintWriter out = response.getWriter();
        out.print(new Gson().toJson(stats));
        out.flush();
    }
}

