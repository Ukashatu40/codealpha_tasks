package com.ukasha.servlet;

import com.ukasha.dao.StudentDAO;
import com.ukasha.model.Student;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
// import jakarta.servlet.http.HttpSession;
// import java.util.List;

// import jakarta.servlet.annotation.WebServlet;

// @WebServlet("/students")
public class StudentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private StudentDAO studentDAO;

    public void init() {
        studentDAO = new StudentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
    
        if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            studentDAO.deleteStudent(id);
        }
    
        response.sendRedirect("students.jsp");
    }
    


    @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    request.setCharacterEncoding("UTF-8");

    String action = request.getParameter("action");

    if ("update".equals(action)) { // 🔥 Removed duplicate "update" action
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String department = request.getParameter("department");

        Student student = new Student(id, name, email, department);
        studentDAO.updateStudent(student);

        response.sendRedirect("students.jsp");
        return; // 🔥 Ensures no further execution after redirect
    }

    if ("delete".equals(action)) {
        int id = Integer.parseInt(request.getParameter("id"));
        studentDAO.deleteStudent(id);
        response.sendRedirect("students.jsp");
        return;
    }

    String name = request.getParameter("name");
    String email = request.getParameter("email");
    String department = request.getParameter("department");

    // Debugging: Print received values
    System.out.println("📩 Received Name: " + name);
    System.out.println("📩 Received Email: " + email);
    System.out.println("📩 Received Department: " + department);

    if (name == null || email == null || department == null) {
        System.out.println("❌ Error: One or more form values are NULL");
        response.sendRedirect("students.jsp?error=missing_data");
        return;
    }

    Student student = new Student(0, name, email, department);
    boolean success = studentDAO.addStudent(student);

    if (success) {
        System.out.println("✅ Student added successfully: " + name);
    } else {
        System.out.println("❌ Failed to add student: " + name);
    }

    response.sendRedirect("students.jsp");
}

    

}
