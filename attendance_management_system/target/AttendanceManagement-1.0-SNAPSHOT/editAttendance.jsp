<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="jakarta.servlet.http.HttpSession, com.ukasha.model.Attendance, com.ukasha.dao.AttendanceDAO" %>

<%
    HttpSession sessionObj = request.getSession(false);
    if (sessionObj == null || sessionObj.getAttribute("admin") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    int id = Integer.parseInt(request.getParameter("id"));
    AttendanceDAO attendanceDAO = new AttendanceDAO();
    Attendance attendance = attendanceDAO.getAttendanceById(id);
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Edit Attendance</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <jsp:include page="header.jsp" />

    <div class="container mt-4">
        <h2>Edit Attendance</h2>
        <form action="AttendanceServlet" method="post">
            <input type="hidden" name="id" value="<%= attendance.getId() %>">
            <div class="mb-3">
                <label>Status:</label>
                <select name="status" class="form-control">
                    <option value="Present" <%= attendance.getStatus().equals("Present") ? "selected" : "" %>>Present</option>
                    <option value="Absent" <%= attendance.getStatus().equals("Absent") ? "selected" : "" %>>Absent</option>
                </select>
            </div>
            <button type="submit" name="action" value="update" class="btn btn-primary">Update Attendance</button>
        </form>
    </div>

    <jsp:include page="footer.jsp" />
</body>
</html>
