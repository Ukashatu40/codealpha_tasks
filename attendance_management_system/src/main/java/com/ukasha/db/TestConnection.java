package com.ukasha.db;

import com.ukasha.dao.StudentDAO;
import com.ukasha.model.Student;
import java.sql.Connection;


public class TestConnection {
    public static void main(String[] args) {
        try {
            Student student = new Student(0,"Ukashat Ukashat", "ukashat@gmail.com","Computer Science");
            System.out.println(student.getId());
            System.out.println(student.getName());
            System.out.println(student.getEmail());
            System.out.println(student.getDepartment());
            StudentDAO studentDAO = new StudentDAO();
            boolean success = studentDAO.addStudent(student);
            if (success) {
                System.out.println("✅ Student added: " + student.getName());
            } else {
                System.out.println("❌ Insertion failed for: " + student.getName());
            }
            Connection connection = DBConnection.getConnection();
            System.out.println("Database connected successfully!");
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

