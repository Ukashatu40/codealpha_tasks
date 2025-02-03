package com.ukasha.servlet;

import com.ukasha.dao.AttendanceDAO;
import com.ukasha.dao.StudentDAO;
import com.ukasha.model.Attendance;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
// import jakarta.servlet.annotation.WebServlet;

// @WebServlet("/attendance")
public class AttendanceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AttendanceDAO attendanceDAO;
    private StudentDAO studentDAO;

    public void init() {
        attendanceDAO = new AttendanceDAO();
        studentDAO = new StudentDAO();
    }

@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    
    request.setCharacterEncoding("UTF-8");

    String action = request.getParameter("action");

    if ("update".equals(action)) {
        int id = Integer.parseInt(request.getParameter("id"));
        String status = request.getParameter("status");

        Attendance attendance = attendanceDAO.getAttendanceById(id);
        if (attendance != null) {
            attendance.setStatus(status);
            attendanceDAO.updateAttendance(attendance);
        }

        response.sendRedirect("attendance.jsp");
        return; // 🔹 Prevents further execution
    }

    if ("delete".equals(action)) {
        int id = Integer.parseInt(request.getParameter("id"));
        attendanceDAO.deleteAttendance(id);
        response.sendRedirect("attendance.jsp");
        return;
    }

    try {
        int studentId = Integer.parseInt(request.getParameter("student_id"));
        String dateString = request.getParameter("date");
        String status = request.getParameter("status");

        // Convert String to java.sql.Date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date utilDate = sdf.parse(dateString);
        Date sqlDate = new Date(utilDate.getTime()); // Convert to java.sql.Date

        // Create Attendance object with correct Date type
        Attendance attendance = new Attendance(0, studentId, sqlDate, status);
        attendanceDAO.addAttendance(attendance);

        response.sendRedirect("attendance.jsp");

    } catch (NumberFormatException | ParseException e) {
        e.printStackTrace();
        response.sendRedirect("attendance.jsp?error=invalidDate");
    }
}

@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String startDate = request.getParameter("startDate");
    String endDate = request.getParameter("endDate");
    String filterStudentId = request.getParameter("filterStudentId");

    System.out.println("📌 Received startDate: " + startDate);
    System.out.println("📌 Received endDate: " + endDate);


    List<Attendance> attendanceRecords;

    try {
        if (startDate != null && endDate != null && !startDate.isEmpty() && !endDate.isEmpty()) {
            System.out.println("📌 Filtering by date range: " + startDate + " to " + endDate);
            attendanceRecords = attendanceDAO.getAttendanceByDateRange(startDate, endDate);
        } else if (filterStudentId != null && !filterStudentId.isEmpty()) {
            System.out.println("📌 Filtering by student ID: " + filterStudentId);
            int studentId = Integer.parseInt(filterStudentId);
            attendanceRecords = attendanceDAO.getAttendanceByStudentId(studentId);
        } else {
            System.out.println("📌 No filters applied. Fetching all records.");
            attendanceRecords = attendanceDAO.getAllAttendance();
        }
        // Preload student names
        for (Attendance attendance : attendanceRecords) {
            String studentName = studentDAO.getStudentById(attendance.getStudentId()).getName();
            attendance.setStudentName(studentName);
            System.out.println("📌 Preloaded student name: " + studentName + " for student ID: " + attendance.getStudentId());
        }
        
    } catch (IllegalArgumentException e) {
        attendanceRecords = attendanceDAO.getAllAttendance();
    }

    request.setAttribute("attendanceRecords", attendanceRecords);
    request.getRequestDispatcher("attendance.jsp").forward(request, response);

}


}
