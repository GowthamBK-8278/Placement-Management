package src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Handles database operations (CRUD) for Students.
 * Demonstrates JDBC connectivity, exception handling, and query execution.
 */
public class StudentManager {

    /**
     * Adds a new student record to the database.
     */
    public boolean addStudent(Student student) {
        String sql = "INSERT INTO students (student_id, name, department, cgpa, arrears, email, phone, placed_company_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, student.getStudentId());
            pstmt.setString(2, student.getName());
            pstmt.setString(3, student.getDepartment());
            pstmt.setDouble(4, student.getCgpa());
            pstmt.setInt(5, student.getArrears());
            pstmt.setString(6, student.getEmail());
            pstmt.setString(7, student.getPhone());
            
            if (student.getPlacedCompanyId() == null || student.getPlacedCompanyId() == 0) {
                pstmt.setNull(8, Types.INTEGER);
            } else {
                pstmt.setInt(8, student.getPlacedCompanyId());
            }

            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.err.println("Database Error adding student: " + e.getMessage());
            return false;
        }
    }

    /**
     * Displays all student records in a formatted table.
     */
    public void viewAllStudents() {
        String sql = "SELECT s.*, c.company_name FROM students s LEFT JOIN companies c ON s.placed_company_id = c.company_id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("\n-------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("%-10s | %-20s | %-18s | %-6s | %-7s | %-22s | %-12s | %-15s\n",
                    "ID", "Name", "Department", "CGPA", "Arrears", "Email", "Phone", "Placement Status");
            System.out.println("-------------------------------------------------------------------------------------------------------------------------");

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                int id = rs.getInt("student_id");
                String name = rs.getString("name");
                String dept = rs.getString("department");
                double cgpa = rs.getDouble("cgpa");
                int arrears = rs.getInt("arrears");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String companyName = rs.getString("company_name");

                String placement = (companyName == null) ? "Unplaced" : "Placed (" + companyName + ")";

                System.out.printf("%-10d | %-20s | %-18s | %-6.2f | %-7d | %-22s | %-12s | %-15s\n",
                        id, name, dept, cgpa, arrears, email, phone, placement);
            }

            if (!hasData) {
                System.out.println("No student records found.");
            }
            System.out.println("-------------------------------------------------------------------------------------------------------------------------");
        } catch (SQLException e) {
            System.err.println("Database Error fetching students: " + e.getMessage());
        }
    }

    /**
     * Updates an existing student record.
     */
    public boolean updateStudent(Student student) {
        String sql = "UPDATE students SET name = ?, department = ?, cgpa = ?, arrears = ?, email = ?, phone = ?, placed_company_id = ? WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getDepartment());
            pstmt.setDouble(3, student.getCgpa());
            pstmt.setInt(4, student.getArrears());
            pstmt.setString(5, student.getEmail());
            pstmt.setString(6, student.getPhone());
            
            if (student.getPlacedCompanyId() == null || student.getPlacedCompanyId() == 0) {
                pstmt.setNull(7, Types.INTEGER);
            } else {
                pstmt.setInt(7, student.getPlacedCompanyId());
            }
            
            pstmt.setInt(8, student.getStudentId());

            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Database Error updating student: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a student record by ID.
     */
    public boolean deleteStudent(int studentId) {
        String sql = "DELETE FROM students WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.err.println("Database Error deleting student: " + e.getMessage());
            return false;
        }
    }

    /**
     * Searches for a student by ID and prints details.
     */
    public Student searchStudent(int studentId) {
        String sql = "SELECT s.*, c.company_name FROM students s LEFT JOIN companies c ON s.placed_company_id = c.company_id WHERE s.student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Student student = new Student();
                    student.setStudentId(rs.getInt("student_id"));
                    student.setName(rs.getString("name"));
                    student.setDepartment(rs.getString("department"));
                    student.setCgpa(rs.getDouble("cgpa"));
                    student.setArrears(rs.getInt("arrears"));
                    student.setEmail(rs.getString("email"));
                    student.setPhone(rs.getString("phone"));
                    
                    int placedId = rs.getInt("placed_company_id");
                    if (rs.wasNull()) {
                        student.setPlacedCompanyId(null);
                    } else {
                        student.setPlacedCompanyId(placedId);
                    }
                    return student;
                }
            }
        } catch (SQLException e) {
            System.err.println("Database Error searching student: " + e.getMessage());
        }
        return null;
    }

    /**
     * Updates placement status directly for a student.
     */
    public boolean updatePlacementStatus(int studentId, Integer companyId) {
        String sql = "UPDATE students SET placed_company_id = ? WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (companyId == null || companyId == 0) {
                pstmt.setNull(1, Types.INTEGER);
            } else {
                pstmt.setInt(1, companyId);
            }
            pstmt.setInt(2, studentId);

            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Database Error updating placement status: " + e.getMessage());
            return false;
        }
    }
}
