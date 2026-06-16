package src;

import java.util.Scanner;

/**
 * Main entry point of the College Placement Management System.
 * Implements a menu-driven interface with robust console inputs and navigation.
 */
public class Main {
    private static final StudentManager studentManager = new StudentManager();
    private static final CompanyManager companyManager = new CompanyManager();
    private static final EligibilityChecker eligibilityChecker = new EligibilityChecker();
    private static final ReportManager reportManager = new ReportManager();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=================================================================");
        System.out.println("            COLLEGE PLACEMENT MANAGEMENT SYSTEM                  ");
        System.out.println("=================================================================");

        // Test database connection and retry/reconfigure if necessary
        DatabaseConnection.initializeConnectionWithRetry(scanner);

        boolean running = true;
        while (running) {
            System.out.println("\n===== MAIN MENU =====");
            System.out.println("1. Student Management");
            System.out.println("2. Company Management");
            System.out.println("3. Check Placement Eligibility");
            System.out.println("4. Generate Placement Report");
            System.out.println("5. Exit");
            
            int choice = readInt(scanner, "Enter your choice (1-5): ");
            
            switch (choice) {
                case 1:
                    studentMenu(scanner);
                    break;
                case 2:
                    companyMenu(scanner);
                    break;
                case 3:
                    runEligibilityCheck(scanner);
                    break;
                case 4:
                    reportManager.generatePlacementReport();
                    break;
                case 5:
                    System.out.println("\nThank you for using College Placement Management System!");
                    running = false;
                    break;
                default:
                    System.out.println("[!] Invalid choice. Please select between 1 and 5.");
            }
        }
        scanner.close();
    }

    // ==========================================
    // STUDENT MANAGEMENT SUB-MENU
    // ==========================================
    private static void studentMenu(Scanner scanner) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Student Management Sub-Menu ---");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Update Student Details");
            System.out.println("4. Delete Student Record");
            System.out.println("5. Search Student by ID");
            System.out.println("6. Mark/Update Placement Status");
            System.out.println("7. Back to Main Menu");

            int choice = readInt(scanner, "Enter your choice (1-7): ");
            switch (choice) {
                case 1:
                    addStudentFlow(scanner);
                    break;
                case 2:
                    studentManager.viewAllStudents();
                    break;
                case 3:
                    updateStudentFlow(scanner);
                    break;
                case 4:
                    deleteStudentFlow(scanner);
                    break;
                case 5:
                    searchStudentFlow(scanner);
                    break;
                case 6:
                    markPlacementFlow(scanner);
                    break;
                case 7:
                    back = true;
                    break;
                default:
                    System.out.println("[!] Invalid choice. Select 1 to 7.");
            }
        }
    }

    private static void addStudentFlow(Scanner scanner) {
        System.out.println("\n--- Add New Student ---");
        int id = readInt(scanner, "Enter Student ID (Number): ");
        if (studentManager.searchStudent(id) != null) {
            System.out.println("[!] A student with ID " + id + " already exists.");
            return;
        }

        System.out.print("Enter Student Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter Department: ");
        String dept = scanner.nextLine().trim();
        double cgpa = readDouble(scanner, "Enter CGPA: ");
        int arrears = readInt(scanner, "Enter Number of Arrears: ");
        System.out.print("Enter Email Address: ");
        String email = scanner.nextLine().trim();
        System.out.print("Enter Phone Number: ");
        String phone = scanner.nextLine().trim();

        Student student = new Student(id, name, dept, cgpa, arrears, email, phone, null);
        if (studentManager.addStudent(student)) {
            System.out.println("[+] Student added successfully!");
        } else {
            System.out.println("[!] Failed to add student. Please try again.");
        }
    }

    private static void updateStudentFlow(Scanner scanner) {
        System.out.println("\n--- Update Student ---");
        int id = readInt(scanner, "Enter Student ID to update: ");
        Student existing = studentManager.searchStudent(id);
        if (existing == null) {
            System.out.println("[!] Student record not found.");
            return;
        }

        System.out.println("Existing Details: " + existing);
        System.out.println("Press Enter to keep existing values.");

        System.out.print("Enter New Name [" + existing.getName() + "]: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) name = existing.getName();

        System.out.print("Enter New Department [" + existing.getDepartment() + "]: ");
        String dept = scanner.nextLine().trim();
        if (dept.isEmpty()) dept = existing.getDepartment();

        System.out.print("Enter New CGPA [" + existing.getCgpa() + "]: ");
        String cgpaStr = scanner.nextLine().trim();
        double cgpa = cgpaStr.isEmpty() ? existing.getCgpa() : Double.parseDouble(cgpaStr);

        System.out.print("Enter New Arrears [" + existing.getArrears() + "]: ");
        String arrearsStr = scanner.nextLine().trim();
        int arrears = arrearsStr.isEmpty() ? existing.getArrears() : Integer.parseInt(arrearsStr);

        System.out.print("Enter New Email [" + existing.getEmail() + "]: ");
        String email = scanner.nextLine().trim();
        if (email.isEmpty()) email = existing.getEmail();

        System.out.print("Enter New Phone [" + existing.getPhone() + "]: ");
        String phone = scanner.nextLine().trim();
        if (phone.isEmpty()) phone = existing.getPhone();

        Student updated = new Student(id, name, dept, cgpa, arrears, email, phone, existing.getPlacedCompanyId());
        if (studentManager.updateStudent(updated)) {
            System.out.println("[+] Student updated successfully!");
        } else {
            System.out.println("[!] Failed to update student.");
        }
    }

    private static void deleteStudentFlow(Scanner scanner) {
        System.out.println("\n--- Delete Student ---");
        int id = readInt(scanner, "Enter Student ID to delete: ");
        Student existing = studentManager.searchStudent(id);
        if (existing == null) {
            System.out.println("[!] Student record not found.");
            return;
        }

        System.out.print("Are you sure you want to delete student " + existing.getName() + "? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        if (confirm.equals("y") || confirm.equals("yes")) {
            if (studentManager.deleteStudent(id)) {
                System.out.println("[+] Student deleted successfully!");
            } else {
                System.out.println("[!] Failed to delete student.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private static void searchStudentFlow(Scanner scanner) {
        System.out.println("\n--- Search Student ---");
        int id = readInt(scanner, "Enter Student ID to search: ");
        Student student = studentManager.searchStudent(id);
        if (student != null) {
            System.out.println("\nStudent Record Found:");
            System.out.println("----------------------------------------");
            System.out.println("ID:          " + student.getStudentId());
            System.out.println("Name:        " + student.getName());
            System.out.println("Department:  " + student.getDepartment());
            System.out.println("CGPA:        " + student.getCgpa());
            System.out.println("Arrears:     " + student.getArrears());
            System.out.println("Email:       " + student.getEmail());
            System.out.println("Phone:       " + student.getPhone());
            String placedText = "Unplaced";
            if (student.getPlacedCompanyId() != null && student.getPlacedCompanyId() > 0) {
                Company c = companyManager.searchCompany(student.getPlacedCompanyId());
                placedText = (c != null) ? "Placed at " + c.getCompanyName() + " (ID: " + c.getCompanyId() + ")" : "Placed (Company ID: " + student.getPlacedCompanyId() + ")";
            }
            System.out.println("Placed:      " + placedText);
            System.out.println("----------------------------------------");
        } else {
            System.out.println("[!] Student not found with ID: " + id);
        }
    }

    private static void markPlacementFlow(Scanner scanner) {
        System.out.println("\n--- Mark/Update Placement Status ---");
        int studentId = readInt(scanner, "Enter Student ID: ");
        Student s = studentManager.searchStudent(studentId);
        if (s == null) {
            System.out.println("[!] Student not found.");
            return;
        }

        System.out.println("Current status: " + (s.getPlacedCompanyId() == null ? "Unplaced" : "Placed in Company ID: " + s.getPlacedCompanyId()));
        System.out.print("Enter Company ID to place the student in (Enter 0 to mark as Unplaced): ");
        int companyId = readInt(scanner, "");

        if (companyId == 0) {
            if (studentManager.updatePlacementStatus(studentId, null)) {
                System.out.println("[+] Student status updated to Unplaced.");
            } else {
                System.out.println("[!] Failed to update status.");
            }
        } else {
            Company c = companyManager.searchCompany(companyId);
            if (c == null) {
                System.out.println("[!] Company with ID " + companyId + " does not exist.");
                return;
            }

            // Optional: check eligibility before placing!
            if (!eligibilityChecker.checkEligibility(s, c, false)) {
                System.out.println("[WARNING] Student is NOT technically eligible for this company based on criteria!");
                System.out.print("Do you still want to force place them? (y/n): ");
                String force = scanner.nextLine().trim().toLowerCase();
                if (!force.equals("y") && !force.equals("yes")) {
                    System.out.println("Placement operation cancelled.");
                    return;
                }
            }

            if (studentManager.updatePlacementStatus(studentId, companyId)) {
                System.out.println("[+] Student marked as placed at " + c.getCompanyName() + " successfully!");
            } else {
                System.out.println("[!] Failed to update placement.");
            }
        }
    }

    // ==========================================
    // COMPANY MANAGEMENT SUB-MENU
    // ==========================================
    private static void companyMenu(Scanner scanner) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Company Management Sub-Menu ---");
            System.out.println("1. Add Company");
            System.out.println("2. View All Companies");
            System.out.println("3. Update Company Details");
            System.out.println("4. Delete Company Record");
            System.out.println("5. Back to Main Menu");

            int choice = readInt(scanner, "Enter your choice (1-5): ");
            switch (choice) {
                case 1:
                    addCompanyFlow(scanner);
                    break;
                case 2:
                    companyManager.viewAllCompanies();
                    break;
                case 3:
                    updateCompanyFlow(scanner);
                    break;
                case 4:
                    deleteCompanyFlow(scanner);
                    break;
                case 5:
                    back = true;
                    break;
                default:
                    System.out.println("[!] Invalid choice. Select 1 to 5.");
            }
        }
    }

    private static void addCompanyFlow(Scanner scanner) {
        System.out.println("\n--- Add New Company ---");
        int id = readInt(scanner, "Enter Company ID (Number): ");
        if (companyManager.searchCompany(id) != null) {
            System.out.println("[!] A company with ID " + id + " already exists.");
            return;
        }

        System.out.print("Enter Company Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter Job Role: ");
        String role = scanner.nextLine().trim();
        double cgpa = readDouble(scanner, "Enter Minimum CGPA Required: ");
        int arrears = readInt(scanner, "Enter Maximum Arrears Allowed: ");
        double pkg = readDouble(scanner, "Enter Package Offered (LPA): ");

        Company company = new Company(id, name, cgpa, arrears, pkg, role);
        if (companyManager.addCompany(company)) {
            System.out.println("[+] Company added successfully!");
        } else {
            System.out.println("[!] Failed to add company.");
        }
    }

    private static void updateCompanyFlow(Scanner scanner) {
        System.out.println("\n--- Update Company ---");
        int id = readInt(scanner, "Enter Company ID to update: ");
        Company existing = companyManager.searchCompany(id);
        if (existing == null) {
            System.out.println("[!] Company record not found.");
            return;
        }

        System.out.println("Existing Details: " + existing);
        System.out.println("Press Enter to keep existing values.");

        System.out.print("Enter New Name [" + existing.getCompanyName() + "]: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) name = existing.getCompanyName();

        System.out.print("Enter New Job Role [" + existing.getJobRole() + "]: ");
        String role = scanner.nextLine().trim();
        if (role.isEmpty()) role = existing.getJobRole();

        System.out.print("Enter New Min CGPA [" + existing.getMinCgpa() + "]: ");
        String cgpaStr = scanner.nextLine().trim();
        double cgpa = cgpaStr.isEmpty() ? existing.getMinCgpa() : Double.parseDouble(cgpaStr);

        System.out.print("Enter New Max Arrears [" + existing.getMaxArrears() + "]: ");
        String arrearsStr = scanner.nextLine().trim();
        int arrears = arrearsStr.isEmpty() ? existing.getMaxArrears() : Integer.parseInt(arrearsStr);

        System.out.print("Enter New Package [" + existing.getPackageOffered() + "]: ");
        String pkgStr = scanner.nextLine().trim();
        double pkg = pkgStr.isEmpty() ? existing.getPackageOffered() : Double.parseDouble(pkgStr);

        Company updated = new Company(id, name, cgpa, arrears, pkg, role);
        if (companyManager.updateCompany(updated)) {
            System.out.println("[+] Company updated successfully!");
        } else {
            System.out.println("[!] Failed to update company.");
        }
    }

    private static void deleteCompanyFlow(Scanner scanner) {
        System.out.println("\n--- Delete Company ---");
        int id = readInt(scanner, "Enter Company ID to delete: ");
        Company existing = companyManager.searchCompany(id);
        if (existing == null) {
            System.out.println("[!] Company record not found.");
            return;
        }

        System.out.print("Are you sure you want to delete company " + existing.getCompanyName() + "? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        if (confirm.equals("y") || confirm.equals("yes")) {
            if (companyManager.deleteCompany(id)) {
                System.out.println("[+] Company deleted successfully!");
            } else {
                System.out.println("[!] Failed to delete company.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    // ==========================================
    // ELIGIBILITY CHECK FLOW
    // ==========================================
    private static void runEligibilityCheck(Scanner scanner) {
        System.out.println("\n--- Eligibility Checker ---");
        int studentId = readInt(scanner, "Enter Student ID: ");
        Student s = studentManager.searchStudent(studentId);
        if (s == null) {
            System.out.println("[!] Student not found.");
            return;
        }

        int companyId = readInt(scanner, "Enter Company ID: ");
        Company c = companyManager.searchCompany(companyId);
        if (c == null) {
            System.out.println("[!] Company not found.");
            return;
        }

        eligibilityChecker.checkEligibility(s, c, true);
    }

    // ==========================================
    // UTILITY METHODS FOR SAFE INPUT
    // ==========================================
    private static int readInt(Scanner scanner, String prompt) {
        while (true) {
            if (!prompt.isEmpty()) {
                System.out.print(prompt);
            }
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("[!] Invalid number. Please try again.");
            }
        }
    }

    private static double readDouble(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("[!] Invalid decimal number. Please try again.");
            }
        }
    }
}
