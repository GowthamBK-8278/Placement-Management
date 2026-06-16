package src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Generates and prints analytical placement reports.
 * Computes metrics such as totals, overall eligibility, placement percentage,
 * and company-wise performance.
 */
public class ReportManager {

    /**
     * Generates and displays the comprehensive Placement Report.
     */
    public void generatePlacementReport() {
        int totalStudents = 0;
        int totalCompanies = 0;
        int eligibleStudentsCount = 0;
        int placedStudentsCount = 0;
        double placementPercentage = 0.0;

        String queryTotalStudents = "SELECT COUNT(*) FROM students";
        String queryTotalCompanies = "SELECT COUNT(*) FROM companies";
        
        // Count of students eligible for at least one company
        String queryEligibleStudents = "SELECT COUNT(DISTINCT s.student_id) FROM students s " +
                "CROSS JOIN companies c " +
                "WHERE s.cgpa >= c.min_cgpa AND s.arrears <= c.max_arrears";
        
        // Count of students placed
        String queryPlacedStudents = "SELECT COUNT(*) FROM students WHERE placed_company_id IS NOT NULL";

        try (Connection conn = DatabaseConnection.getConnection()) {
            
            // Fetch total students
            try (PreparedStatement pstmt = conn.prepareStatement(queryTotalStudents);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    totalStudents = rs.getInt(1);
                }
            }

            // Fetch total companies
            try (PreparedStatement pstmt = conn.prepareStatement(queryTotalCompanies);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    totalCompanies = rs.getInt(1);
                }
            }

            // Fetch eligible students count
            try (PreparedStatement pstmt = conn.prepareStatement(queryEligibleStudents);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    eligibleStudentsCount = rs.getInt(1);
                }
            }

            // Fetch placed students count
            try (PreparedStatement pstmt = conn.prepareStatement(queryPlacedStudents);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    placedStudentsCount = rs.getInt(1);
                }
            }

            // Calculate percentage
            if (totalStudents > 0) {
                placementPercentage = ((double) placedStudentsCount / totalStudents) * 100;
            }

            // Display Report UI
            System.out.println("\n=================================================================");
            System.out.println("                   COLLEGE PLACEMENT REPORT                      ");
            System.out.println("=================================================================");
            System.out.printf("  Total Students Registered:   %-10d\n", totalStudents);
            System.out.printf("  Total Companies Visiting:     %-10d\n", totalCompanies);
            System.out.printf("  Students Eligible (>= 1 Co):  %-10d\n", eligibleStudentsCount);
            System.out.printf("  Students Selected / Placed:   %-10d\n", placedStudentsCount);
            System.out.printf("  Overall Placement Percentage: %-10.2f%%\n", placementPercentage);
            System.out.println("=================================================================");

            // Show company-wise eligibility and placement stats
            if (totalCompanies > 0) {
                displayCompanyWiseStats(conn);
            }

        } catch (SQLException e) {
            System.err.println("Database Error generating placement report: " + e.getMessage());
        }
    }

    /**
     * Queries and displays stats for each company (Eligible vs Placed count).
     */
    private void displayCompanyWiseStats(Connection conn) throws SQLException {
        String sql = "SELECT c.company_id, c.company_name, c.job_role, c.min_cgpa, c.max_arrears, " +
                "(SELECT COUNT(*) FROM students s WHERE s.cgpa >= c.min_cgpa AND s.arrears <= c.max_arrears) AS eligible_count, " +
                "(SELECT COUNT(*) FROM students s WHERE s.placed_company_id = c.company_id) AS placed_count " +
                "FROM companies c";

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("\n--- COMPANY-WISE PLACEMENT SUMMARY ---");
            System.out.printf("%-6s | %-18s | %-20s | %-12s | %-12s\n", 
                    "ID", "Company Name", "Job Role", "Eligible Qty", "Placed Qty");
            System.out.println("---------------------------------------------------------------------------------");
            
            while (rs.next()) {
                int id = rs.getInt("company_id");
                String name = rs.getString("company_name");
                String role = rs.getString("job_role");
                int eligibleCount = rs.getInt("eligible_count");
                int placedCount = rs.getInt("placed_count");

                System.out.printf("%-6d | %-18s | %-20s | %-12d | %-12d\n", 
                        id, name, role, eligibleCount, placedCount);
            }
            System.out.println("---------------------------------------------------------------------------------");
        }
    }
}
