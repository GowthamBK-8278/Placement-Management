package src;

/**
 * Model class representing a Company.
 * Demonstrates Encapsulation using private fields and public getters/setters.
 */
public class Company {
    private int companyId;
    private String companyName;
    private double minCgpa;
    private int maxArrears;
    private double packageOffered;
    private String jobRole;

    // Default Constructor
    public Company() {}

    // Parameterized Constructor
    public Company(int companyId, String companyName, double minCgpa, int maxArrears, double packageOffered, String jobRole) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.minCgpa = minCgpa;
        this.maxArrears = maxArrears;
        this.packageOffered = packageOffered;
        this.jobRole = jobRole;
    }

    // Getters and Setters
    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public double getMinCgpa() {
        return minCgpa;
    }

    public void setMinCgpa(double minCgpa) {
        this.minCgpa = minCgpa;
    }

    public int getMaxArrears() {
        return maxArrears;
    }

    public void setMaxArrears(int maxArrears) {
        this.maxArrears = maxArrears;
    }

    public double getPackageOffered() {
        return packageOffered;
    }

    public void setPackageOffered(double packageOffered) {
        this.packageOffered = packageOffered;
    }

    public String getJobRole() {
        return jobRole;
    }

    public void setJobRole(String jobRole) {
        this.jobRole = jobRole;
    }

    @Override
    public String toString() {
        return String.format(
            "Company ID: %d | Name: %s | Role: %s | Min CGPA: %.2f | Max Arrears: %d | Package: %.2f LPA",
            companyId, companyName, jobRole, minCgpa, maxArrears, packageOffered
        );
    }
}
