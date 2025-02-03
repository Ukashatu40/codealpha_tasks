<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="jakarta.servlet.http.HttpSession, java.util.List, com.ukasha.model.Student, com.ukasha.model.Admin, com.ukasha.dao.StudentDAO" %>
<%
    HttpSession sessionObj = request.getSession(false);
    if (sessionObj == null || sessionObj.getAttribute("admin") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    Admin admin = (Admin) sessionObj.getAttribute("admin");
    StudentDAO studentDAO = new StudentDAO();
    List<Student> students = studentDAO.getAllStudents();
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Manage Students</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <jsp:include page="header.jsp" />

    <div class="container mt-4">
        <h2>Manage Students</h2>

        <!-- Add Student Form -->
        <form action="StudentServlet" method="post" class="mb-4">
            <div class="row">
                <div class="col-md-3">
                    <input type="text" name="name" class="form-control" placeholder="Student Name" required>
                </div>
                <div class="col-md-3">
                    <input type="email" name="email" class="form-control" placeholder="Email" required>
                </div>
                <div class="col-md-3">
                    <input type="text" name="department" class="form-control" placeholder="Department" required>
                </div>
                <div class="col-md-3">
                    <button type="submit" class="btn btn-success">Add Student</button>
                </div>
            </div>
        </form>

        <!-- Student Table -->
        <table class="table table-bordered table-striped">
            <thead class="table-dark">
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Department</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <% for (Student student : students) { %>
                    <tr>
                        <td><%= student.getId() %></td>
                        <td><%= student.getName() %></td>
                        <td><%= student.getEmail() %></td>
                        <td><%= student.getDepartment() %></td>
                        <td>
                            <a href="editStudent.jsp?id=<%= student.getId() %>" class="btn btn-primary btn-sm">Edit</a>
                            <form action="StudentServlet" method="post" style="display:inline;">
                                <input type="hidden" name="id" value="<%= student.getId() %>">
                                <input type="hidden" name="action" value="delete">
                                <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Are you sure?');">Delete</button>
                            </form>                            
                        </td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    </div>

    <jsp:include page="footer.jsp" />
</body>
</html>
