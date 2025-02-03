<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page import="jakarta.servlet.http.HttpSession, java.util.List, com.ukasha.model.Attendance, com.ukasha.model.Student, com.ukasha.dao.AttendanceDAO, com.ukasha.dao.StudentDAO" %>
<%
    HttpSession sessionObj = request.getSession(false);
    if (sessionObj == null || sessionObj.getAttribute("admin") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    AttendanceDAO attendanceDAO = new AttendanceDAO();
    StudentDAO studentDAO = new StudentDAO();
    List<Attendance> attendanceList = attendanceDAO.getAllAttendance();
    List<Student> students = studentDAO.getAllStudents();
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Manage Attendance</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="styles.css">
</head>

<body>
    <jsp:include page="header.jsp" />

    <div class="container mt-4">
        <h2>Manage Attendance</h2>

        <!-- Mark Attendance Form -->
        <form action="AttendanceServlet" method="post" class="mb-4">
            <div class="row">
                <div class="col-md-4">
                    <select name="student_id" class="form-control" required>
                        <option value="">Select Student</option>
                        <% for (Student student : students) { %>
                            <option value="<%= student.getId() %>"><%= student.getName() %></option>
                        <% } %>
                    </select>
                </div>
                <div class="col-md-3">
                    <input type="date" name="date" class="form-control" required>
                </div>
                <div class="col-md-3">
                    <select name="status" class="form-control" required>
                        <option value="Present">Present</option>
                        <option value="Absent">Absent</option>
                    </select>
                </div>
                <div class="col-md-2">
                    <button type="submit" class="btn btn-success">Mark Attendance</button>
                </div>
            </div>
        </form>
        <div class="mb-3">
            <a href="ExportAttendanceServlet?format=csv" class="btn btn-success">Export as CSV</a>
            <a href="ExportAttendanceServlet?format=pdf" class="btn btn-danger">Export as PDF</a>
        </div>

        <form method="get" action="AttendanceServlet">
            <label for="startDate">Start Date:</label>
            <input type="date" id="startDate" name="startDate" >
        
            <label for="endDate">End Date:</label>
            <input type="date" id="endDate" name="endDate" >
        
            <button type="submit">Filter</button>
        </form>

        

        <c:if test="${empty attendanceRecords}">
             <p>No attendance records found.</p>
        </c:if>

        <!-- Attendance Table -->
        <table class="table table-bordered table-striped">
            <thead class="table-dark">
                <tr>
                    <th>ID</th>
                    <th>Student Name</th>
                    <th>Date</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="attendance" items="${attendanceRecords}">
                    <tr>
                        <td>${attendance.id}</td>
                        <td>${attendance.studentName}</td>
                        <td>${attendance.date}</td>
                        <td>${attendance.status}</td>
                        <td>
                            <a href="editAttendance.jsp?id=${attendance.id}" class="btn btn-primary btn-sm">Edit</a>
                            <form action="AttendanceServlet" method="post" style="display:inline;">
                                <input type="hidden" name="id" value="${attendance.id}">
                                <input type="hidden" name="action" value="delete">
                                <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Are you sure?');">Delete</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>            
        </table>

        <!-- Load Chart.js -->
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<h3>Daily Attendance Trends</h3>
<canvas id="attendanceChart"></canvas>

<h3>Student Attendance Records</h3>
<canvas id="studentAttendanceChart"></canvas>

<h3>Present vs Absent Students</h3>
<canvas id="presentAbsentChart"></canvas>

<script>
    window.onload = function() {
        // ✅ Prevent infinite reload: Check if "autoLoad=true" is already in the URL
        const params = new URLSearchParams(window.location.search);
        if (!params.has("startDate") && !params.has("filterStudentId") && !params.has("autoLoad")) {
            console.log("📌 Automatically loading all attendance records...");
            window.location.href = "AttendanceServlet?autoLoad=true"; // Reloads page only ONCE
            return; // ✅ Stop further execution to avoid conflicts
        }

    // Fetch attendance statistics
    fetch('attendance-stats')
    .then(response => response.json())
    .then(data => {
        const labels = Object.keys(data.dailyAttendance);
        const values = Object.values(data.dailyAttendance);

        // Bar Chart (Daily Attendance)
        new Chart(document.getElementById('attendanceChart'), {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Daily Attendance Count',
                    data: values,
                    backgroundColor: '#4CAF50'
                }]
            }
        });

        // Pie Chart (Present vs Absent)
        const presentAbsentLabels = Object.keys(data.presentAbsent);
        const presentAbsentValues = Object.values(data.presentAbsent);

        new Chart(document.getElementById('presentAbsentChart'), {
            type: 'pie',
            data: {
                labels: presentAbsentLabels,
                datasets: [{
                    data: presentAbsentValues,
                    backgroundColor: ['#4CAF50', '#F44336']
                }]
            }
        });

        // Per-Student Bar Chart
        const studentLabels = Object.keys(data.studentAttendance);
        const studentValues = Object.values(data.studentAttendance);

        new Chart(document.getElementById('studentAttendanceChart'), {
            type: 'bar',
            data: {
                labels: studentLabels,
                datasets: [{
                    label: 'Attendance per Student',
                    data: studentValues,
                    backgroundColor: '#2196F3'
                }]
            }
        });
    })
    .catch(error => console.error('Error fetching stats:', error));

    };
</script>


    </div>

    <jsp:include page="footer.jsp" />
</body>
</html>
