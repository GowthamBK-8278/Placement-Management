package src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Handles database operations (CRUD) for Companies.
 * Demonstrates JDBC connectivity, exception handling, and query execution.
 */
public class CompanyManager {

    /**
     * Adds a new company record to the database.
     */
    public boolean addCompany(Company company) {
        String sql = "INSERT INTO companies (company_id, company_name, min_cgpa, max_arrears, package_offered, job_role) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, company.getCompanyId());
            pstmt.setString(2, company.getCompanyName());
            pstmt.setDouble(3, company.getMinCgpa());
            pstmt.setInt(4, company.getMaxArrears());
            pstmt.setDouble(5, company.getPackageOffered());
            pstmt.setString(6, company.getJobRole());

            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.err.println("Database Error adding company: " + e.getMessage());
            return false;
        }
    }

    /**
     * Displays all company records in a formatted table.
     */
    public void viewAllCompanies() {
        String sql = "SELECT * FROM companies";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("\n------------------------------------------------------------------------------------------------------");
            System.out.printf("%-10s | %-22s | %-25s | %-10s | %-12s | %-12s\n",
                    "ID", "Company Name", "Job Role", "Min CGPA", "Max Arrears", "Package (LPA)");
            System.out.println("------------------------------------------------------------------------------------------------------");

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                int id = rs.getInt("company_id");
                String name = rs.getString("company_name");
                String role = rs.getString("job_role");
                double minCgpa = rs.getDouble("min_cgpa");
                int maxArrears = rs.getInt("max_arrears");
                double pkg = rs.getDouble("package_offered");

                System.out.printf("%-10d | %-22s | %-25s | %-10.2f | %-12d | %-12.2f\n",
                        id, name, role, minCgpa, maxArrears, pkg);
            }

            if (!hasData) {
                System.out.println("No company records found.");
            }
            System.out.println("------------------------------------------------------------------------------------------------------");
        } catch (SQLException e) {
            System.err.println("Database Error fetching companies: " + e.getMessage());
        }
    }

    /**
     * Updates an existing company record.
     */
    public boolean updateCompany(Company company) {
        String sql = "UPDATE companies SET company_name = ?, min_cgpa = ?, max_arrears = ?, package_offered = ?, job_role = ? WHERE company_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, company.getCompanyName());
            pstmt.setDouble(2, company.getMinCgpa());
            pstmt.setInt(3, company.getMaxArrears());
            pstmt.setDouble(4, company.getPackageOffered());
            pstmt.setString(5, company.getJobRole());
            pstmt.setInt(6, company.getCompanyId());

            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Database Error updating company: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a company record by ID.
     */
    public boolean deleteCompany(int companyId) {
        String sql = "DELETE FROM companies WHERE company_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, companyId);
            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.err.println("Database Error deleting company: " + e.getMessage());
            return false;
        }
    }

    /**
     * Searches for a company by ID and returns a Company object.
     */
    public Company searchCompany(int companyId) {
        String sql = "SELECT * FROM companies WHERE company_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, companyId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Company company = new Company();
                    company.setCompanyId(rs.getInt("company_id"));
                    company.setCompanyName(rs.getString("company_name"));
                    company.setMinCgpa(rs.getDouble("min_cgpa"));
                    company.setMaxArrears(rs.getInt("max_arrears"));
                    company.setPackageOffered(rs.getDouble("package_offered"));
                    company.setJobRole(rs.getString("job_role"));
                    return company;
                }
            }
        } catch (SQLException e) {
            System.err.println("Database Error searching company: " + e.getMessage());
        }
        return null;
    }
}
