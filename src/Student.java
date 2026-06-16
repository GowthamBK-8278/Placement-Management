package src;

/**
 * Model class representing a Student.
 * Demonstrates Encapsulation using private fields and public getters/setters.
 */
public class Student {
    private int studentId;
    private String name;
    private String department;
    private double cgpa;
    private int arrears;
    private String email;
    private String phone;
    private Integer placedCompanyId; // Nullable to indicate placement status

    // Default Constructor
    public Student() {}

    // Parameterized Constructor
    public Student(int studentId, String name, String department, double cgpa, int arrears, String email, String phone, Integer placedCompanyId) {
        this.studentId = studentId;
        this.name = name;
        this.department = department;
        this.cgpa = cgpa;
        this.arrears = arrears;
        this.email = email;
        this.phone = phone;
        this.placedCompanyId = placedCompanyId;
    }

    // Getters and Setters
    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public double getCgpa() {
        return cgpa;
    }

    public void setCgpa(double cgpa) {
        this.cgpa = cgpa;
    }

    public int getArrears() {
        return arrears;
    }

    public void setArrears(int arrears) {
        this.arrears = arrears;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getPlacedCompanyId() {
        return placedCompanyId;
    }

    public void setPlacedCompanyId(Integer placedCompanyId) {
        this.placedCompanyId = placedCompanyId;
    }

    @Override
    public String toString() {
        return String.format(
            "Student ID: %d | Name: %s | Dept: %s | CGPA: %.2f | Arrears: %d | Email: %s | Phone: %s | Placed Co. ID: %s",
            studentId, name, department, cgpa, arrears, email, phone, 
            (placedCompanyId == null || placedCompanyId == 0) ? "Unplaced" : String.valueOf(placedCompanyId)
        );
    }
}
