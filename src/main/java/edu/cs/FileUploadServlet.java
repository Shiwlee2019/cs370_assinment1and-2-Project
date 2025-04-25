package edu.cs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/FileUploadServlet")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 10,  // 10 MB
    maxFileSize = 1024 * 1024 * 50,        // 50 MB
    maxRequestSize = 1024 * 1024 * 100     // 100 MB
)
public class FileUploadServlet extends HttpServlet {

    private static final long serialVersionUID = 205242440643911308L;
    private static final String UPLOAD_DIR = "uploads";

    private static final String DB_URL = "jdbc:mysql://3.145.65.29:3306/Dbtest?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String DB_USER = "db_user";
    private static final String DB_PASS = "Ahmed2019";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String message = "";
        try {
            String applicationPath = request.getServletContext().getRealPath("");
            String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR;

            File fileSaveDir = new File(uploadFilePath);
            if (!fileSaveDir.exists()) {
                fileSaveDir.mkdirs();
            }

            String fileName = "";
            File uploadedFile = null;
            InputStream fileContent = null;

            for (Part part : request.getParts()) {
                fileName = getFileName(part);
                fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
                uploadedFile = new File(uploadFilePath + File.separator + fileName);
                part.write(uploadedFile.getAbsolutePath());
                fileContent = new FileInputStream(uploadedFile);
            }

            if (fileContent != null && uploadedFile.length() > 0) {
                message = saveFileToDatabase(fileName, fileContent);
            } else {
                message = "Error: File content is empty or null.";
            }

        } catch (Exception ex) {
            message = "Exception: " + ex.getMessage();
            ex.printStackTrace();
        }

        response.getWriter().write(message);
    }

    private String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] tokens = contentDisp.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length() - 1);
            }
        }
        return "";
    }

    private String saveFileToDatabase(String fileName, InputStream fileContent) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                 PreparedStatement pstmt = conn.prepareStatement(
                         "INSERT INTO uploaded_files (file_name, file_content) VALUES (?, ?)")) {

                pstmt.setString(1, fileName);
                pstmt.setBlob(2, fileContent);
                int rowsInserted = pstmt.executeUpdate();

                return (rowsInserted > 0)
                        ? "File uploaded and stored in database successfully!"
                        : "Error: File upload to database failed.";

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return "Database error: MySQL JDBC Driver not found.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Database error: " + e.getMessage();
        }
    }
}