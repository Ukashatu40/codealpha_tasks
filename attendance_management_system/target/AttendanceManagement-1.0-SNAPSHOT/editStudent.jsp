<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="jakarta.servlet.http.HttpSession, com.ukasha.model.Student, com.ukasha.dao.StudentDAO" %>

<%
    HttpSession sessionObj = request.getSession(false);
    if (sessionObj == null || sessionObj.getAttribute("admin") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    int id = Integer.parseInt(request.getParameter("id"));
    StudentDAO studentDAO = new StudentDAO();
    Student student = studentDAO.getStudentById(id);
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Edit Student</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <jsp:include page="header.jsp" />

    <div class="container mt-4">
        <h2>Edit Student</h2>
        <form action="StudentServlet" method="post">
            <input type="hidden" name="id" value="<%= student.getId() %>">
            <div class="mb-3">
                <label>Name:</label>
                <input type="text" name="name" class="form-control" value="<%= student.getName() %>" required>
            </div>
            <div class="mb-3">
                <label>Email:</label>
                <input type="email" name="email" class="form-control" value="<%= student.getEmail() %>" required>
            </div>
            <div class="mb-3">
                <label>Department:</label>
                <input type="text" name="department" class="form-control" value="<%= student.getDepartment() %>" required>
            </div>
            <button type="submit" name="action" value="update" class="btn btn-primary">Update</button>
        </form>
    </div>

    <jsp:include page="footer.jsp" />
</body>
</html>
