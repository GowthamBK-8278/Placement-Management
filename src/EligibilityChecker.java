package src;

/**
 * Handles the eligibility checks for students against specific companies.
 * Compares student CGPA and arrears to company requirements.
 */
public class EligibilityChecker {

    /**
     * Checks if a student is eligible for a company and displays a detailed breakdown.
     */
    public boolean checkEligibility(Student student, Company company, boolean printDetails) {
        if (student == null || company == null) {
            if (printDetails) {
                System.out.println("[!] Error: Invalid student or company data provided.");
            }
            return false;
        }

        boolean cgpaEligible = student.getCgpa() >= company.getMinCgpa();
        boolean arrearsEligible = student.getArrears() <= company.getMaxArrears();
        boolean isEligible = cgpaEligible && arrearsEligible;

        if (printDetails) {
            System.out.println("\n=============================================");
            System.out.println("        ELIGIBILITY CHECK SUMMARY            ");
            System.out.println("=============================================");
            System.out.printf("Student: %-15s (ID: %d)\n", student.getName(), student.getStudentId());
            System.out.printf("Company: %-15s (ID: %d)\n", company.getCompanyName(), company.getCompanyId());
            System.out.printf("Job Role: %s | Package: %.2f LPA\n", company.getJobRole(), company.getPackageOffered());
            System.out.println("---------------------------------------------");
            
            System.out.printf("CGPA Check:    Student CGPA = %.2f | Required >= %.2f [%s]\n",
                    student.getCgpa(), company.getMinCgpa(), cgpaEligible ? "PASS" : "FAIL");
            
            System.out.printf("Arrears Check: Student Arrears = %d | Allowed <= %d [%s]\n",
                    student.getArrears(), company.getMaxArrears(), arrearsEligible ? "PASS" : "FAIL");
            
            System.out.println("---------------------------------------------");
            if (isEligible) {
                System.out.println("STATUS: >>> ELIGIBLE <<<");
                System.out.println("The student meets all the requirements for this company.");
            } else {
                System.out.println("STATUS: >>> NOT ELIGIBLE <<<");
                System.out.print("Reason: ");
                if (!cgpaEligible && !arrearsEligible) {
                    System.out.println("Low CGPA and exceeds maximum allowed arrears.");
                } else if (!cgpaEligible) {
                    System.out.println("CGPA is below the minimum required CGPA.");
                } else {
                    System.out.println("Number of active arrears exceeds the maximum allowed.");
                }
            }
            System.out.println("=============================================");
        }

        return isEligible;
    }
}
