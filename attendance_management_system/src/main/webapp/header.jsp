<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="jakarta.servlet.http.HttpSession, com.ukasha.model.Admin" %>
<%
    HttpSession sessionObj = request.getSession(false);
    if (sessionObj == null || sessionObj.getAttribute("admin") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    Admin admin = (Admin) sessionObj.getAttribute("admin");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Attendance Management</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <div class="wrapper d-flex">
        <!-- Sidebar -->
        <nav id="sidebar" class="bg-dark text-white">
            <div class="sidebar-header text-center py-3">
                <h3>Attendance System</h3>
            </div>
            <ul class="list-unstyled components px-3">
                <li><a href="dashboard.jsp" class="text-white">Dashboard</a></li>
                <li><a href="students.jsp" class="text-white">Manage Students</a></li>
                <li><a href="attendance.jsp" class="text-white">Manage Attendance</a></li>
                <li><a href="LogoutServlet" class="text-danger">Logout</a></li>
            </ul>
        </nav>

        <!-- Page Content -->
        <div id="content" class="flex-grow-1">
            <nav class="navbar navbar-expand-lg navbar-light bg-light">
                <button type="button" id="sidebar-toggle" class="btn btn-dark">
                    ☰ Toggle Sidebar
                </button>
                <h4 class="ms-3">Welcome, <%= admin.getUsername() %>!</h4>
            </nav>
            <script>
                document.getElementById("sidebar-toggle").addEventListener("click", function () {
                    const sidebar = document.getElementById("sidebar");
                    const content = document.getElementById("content");
            
                    // Toggle the 'collapsed' class for both sidebar and content
                    sidebar.classList.toggle("collapsed");
                    content.classList.toggle("collapsed");
            
                    // Optional: Log to debug
                    console.log("Sidebar classes:", sidebar.className);
                    console.log("Content classes:", content.className);
                });
            </script>