package com.ukasha.servlet;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
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
import java.util.List;


@WebServlet("/ExportAttendanceServlet")
public class ExportAttendanceServlet extends HttpServlet {
    private AttendanceDAO attendanceDAO = new AttendanceDAO();
    private StudentDAO studentDAO = new StudentDAO();  // ✅ Add StudentDAO to fetch names

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String format = request.getParameter("format");

        if ("csv".equalsIgnoreCase(format)) {
            exportAsCSV(response);
        } else if ("pdf".equalsIgnoreCase(format)) {
            exportAsPDF(response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid format. Choose 'csv' or 'pdf'.");
        }
    }

    private void exportAsCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=attendance.csv");

        PrintWriter writer = response.getWriter();
        writer.println("ID,Student Name,Date,Status");

        List<Attendance> attendanceRecords = attendanceDAO.getAllAttendance();
        for (Attendance attendance : attendanceRecords) {
            // ✅ Fetch student name and set it
            String studentName = studentDAO.getStudentById(attendance.getStudentId()).getName();
            attendance.setStudentName(studentName);

            writer.println(attendance.getId() + "," +
                    attendance.getStudentName() + "," +
                    attendance.getDate() + "," +
                    attendance.getStatus());
        }
        writer.flush();
        writer.close();
    }

    private void exportAsPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=attendance.pdf");

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            document.add(new Paragraph("Attendance Records\n\n"));
            PdfPTable table = new PdfPTable(4);
            table.addCell("ID");
            table.addCell("Student Name");
            table.addCell("Date");
            table.addCell("Status");

            List<Attendance> attendanceRecords = attendanceDAO.getAllAttendance();
            for (Attendance attendance : attendanceRecords) {
                // ✅ Fetch student name and set it
                String studentName = studentDAO.getStudentById(attendance.getStudentId()).getName();
                attendance.setStudentName(studentName);

                table.addCell(String.valueOf(attendance.getId()));
                table.addCell(attendance.getStudentName());
                table.addCell(attendance.getDate().toString());
                table.addCell(attendance.getStatus());
            }

            document.add(table);
            document.close();
        } catch (DocumentException e) {
            throw new IOException("Error generating PDF", e);
        }
    }
}
