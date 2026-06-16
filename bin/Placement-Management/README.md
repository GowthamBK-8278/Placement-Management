# College Placement Management System

A menu-driven **Java Console Application** with **MySQL Database Integration** via **JDBC**. This application manages student records and company visiting details, checks student eligibility based on academic criteria, and generates placement statistics and analytical reports. Designed as a college mini-project, it demonstrates clean object-oriented design and robust exception handling.

---

## Table of Contents
1. [Project Structure](#project-structure)
2. [Database Schema](#database-schema)
3. [Key Object-Oriented Programming (OOP) Concepts](#key-object-oriented-programming-oop-concepts)
4. [Installation & Setup](#installation--setup)
5. [How to Compile and Run](#how-to-compile-and-run)
6. [Sample Output Walkthrough](#sample-output-walkthrough)
7. [Project Explanation](#project-explanation)
8. [Viva Questions & Answers](#viva-questions--answers)

---

## Project Structure

```
College Placement Management System/
│
├── lib/
│   └── mysql-connector-j-9.0.0.jar        # MySQL JDBC Connector (Driver)
│
├── src/
│   ├── Main.java                          # CLI View and Menu Controller
│   ├── DatabaseConnection.java            # JDBC connection manager
│   ├── Student.java                       # Student Model (Encapsulated POJO)
│   ├── Company.java                       # Company Model (Encapsulated POJO)
│   ├── StudentManager.java                # Student DAO (CRUD Operations)
│   ├── CompanyManager.java                # Company DAO (CRUD Operations)
│   ├── EligibilityChecker.java            # Eligibility checking module
│   └── ReportManager.java                 # Placement report metrics generator
│
├── schema.sql                             # Database tables and sample inserts
├── db.properties                          # Configurable JDBC settings
├── compile_run.ps1                        # PowerShell build and run script
└── README.md                              # Project documentation and Viva QA
```

---

## Database Schema

The system integrates with a MySQL database named `placement_db`.

### 1. `companies` Table
Stores details of visiting recruitment companies.
```sql
CREATE TABLE companies (
    company_id INT PRIMARY KEY,
    company_name VARCHAR(100) NOT NULL,
    min_cgpa DOUBLE NOT NULL,
    max_arrears INT NOT NULL,
    package_offered DOUBLE NOT NULL,
    job_role VARCHAR(100) NOT NULL
);
```

### 2. `students` Table
Stores student academic records and tracks placement status via foreign key reference `placed_company_id`.
```sql
CREATE TABLE students (
    student_id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(50) NOT NULL,
    cgpa DOUBLE NOT NULL,
    arrears INT NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    placed_company_id INT NULL,
    FOREIGN KEY (placed_company_id) REFERENCES companies(company_id) ON DELETE SET NULL
);
```

---

## Key Object-Oriented Programming (OOP) Concepts

1. **Encapsulation**: Implemented in model classes [Student.java](file:///d:/Java%20Mini%20project/src/Student.java) and [Company.java](file:///d:/Java%20Mini%20project/src/Company.java) by making fields `private` and exposing access via public getters and setters.
2. **Abstractions & DAOs**: Separated the data access logic from menu flows. Classes like `StudentManager` and `CompanyManager` abstract SQL queries behind clean Java method calls.
3. **Constructors**: Parameterized constructors are used to instantiate objects with all parameters quickly. Default constructors are provided for object mapping flexibility.
4. **Robust Exception Handling**: Prevents SQL exceptions from crashing the application. Transactions are closed cleanly using Java’s try-with-resources syntax.

---

## Installation & Setup

1. **Prerequisites**:
   - Install Java JDK 8 or higher (tested on Java JDK 26).
   - Install MySQL Server (tested on MySQL Server 9.7).
   - Ensure the MySQL service is running on your machine.

2. **Database Initialization**:
   - Open your MySQL Command Line or any client (e.g. WorkBench) and execute the SQL script [schema.sql](file:///d:/Java%20Mini%20project/schema.sql):
     ```sql
     source d:/Java Mini project/schema.sql;
     ```
   - Alternatively, you can use our helper script [compile_run.ps1](file:///d:/Java%20Mini%20project/compile_run.ps1) to import the database automatically.

3. **Configure Connection**:
   - Open [db.properties](file:///d:/Java%20Mini%20project/db.properties).
   - Set the credentials according to your local MySQL server configurations:
     ```properties
     db.url=jdbc:mysql://localhost:3306/placement_db
     db.user=your_username
     db.password=your_password
     ```
   - If the application starts and connection fails, the console will interactively prompt you for credentials and update this file automatically!

---

## How to Compile and Run

### Option A: Using PowerShell Script (Recommended)
1. Open terminal in the project directory `d:\Java Mini project`.
2. Execute:
   ```powershell
   .\compile_run.ps1
   ```
3. Select Option `1` to initialize the database (requires MySQL CLI installed).
4. Select Option `2` to build and run the application.

### Option B: Manual Command-Line Commands
If you want to compile and execute manually:
1. **Compilation**:
   ```cmd
   mkdir bin
   javac -d bin -cp "lib/*" src/*.java
   ```
2. **Execution**:
   ```cmd
   java -cp "bin;lib/*" src.Main
   ```

---

## Sample Output Walkthrough

### 1. Main Menu
```
=================================================================
            COLLEGE PLACEMENT MANAGEMENT SYSTEM                  
=================================================================
Connecting to database at: jdbc:mysql://localhost:3306/placement_db...
Successfully connected to MySQL database!

===== MAIN MENU =====
1. Student Management
2. Company Management
3. Check Placement Eligibility
4. Generate Placement Report
5. Exit
Enter your choice (1-5):
```

### 2. View All Students
```
-------------------------------------------------------------------------------------------------------------------------
ID         | Name                 | Department         | CGPA   | Arrears | Email                  | Phone        | Placement Status
-------------------------------------------------------------------------------------------------------------------------
1          | Alice Smith          | Computer Science   | 8.70   | 0       | alice@example.com      | 9876543210   | Placed (Google)
2          | Bob Johnson          | Information Tech   | 7.80   | 0       | bob@example.com        | 9876543211   | Unplaced
3          | Charlie Brown        | Electronics        | 6.20   | 2       | charlie@example.com    | 9876543212   | Placed (Accenture)
4          | Diana Prince         | Computer Science   | 8.30   | 1       | diana@example.com      | 9876543213   | Unplaced
5          | Evan Wright          | Mechanical         | 5.80   | 4       | evan@example.com       | 9876543214   | Unplaced
6          | Fiona Gallagher      | Computer Science   | 9.10   | 0       | fiona@example.com      | 9876543215   | Placed (Microsoft)
-------------------------------------------------------------------------------------------------------------------------
```

### 3. Eligibility Checker Demo
```
Enter Student ID: 4
Enter Company ID: 105

=============================================
        ELIGIBILITY CHECK SUMMARY            
=============================================
Student: Diana Prince    (ID: 4)
Company: Amazon          (ID: 105)
Job Role: Cloud Support Associate | Package: 18.50 LPA
---------------------------------------------
CGPA Check:    Student CGPA = 8.30 | Required >= 8.20 [PASS]
Arrears Check: Student Arrears = 1 | Allowed <= 1 [PASS]
---------------------------------------------
STATUS: >>> ELIGIBLE <<<
The student meets all the requirements for this company.
=============================================
```

### 4. Placement Report Demo
```
=================================================================
                   COLLEGE PLACEMENT REPORT                      
=================================================================
  Total Students Registered:   6         
  Total Companies Visiting:     5         
  Students Eligible (>= 1 Co):  5         
  Students Selected / Placed:   3         
  Overall Placement Percentage: 50.00%    
=================================================================

--- COMPANY-WISE PLACEMENT SUMMARY ---
ID     | Company Name       | Job Role             | Eligible Qty | Placed Qty  
---------------------------------------------------------------------------------
101    | Google             | Software Engineer    | 2            | 1           
102    | Microsoft          | Program Manager      | 2            | 1           
103    | Accenture          | Associate Software En| 5            | 1           
104    | TCS                | Systems Engineer     | 5            | 0           
105    | Amazon             | Cloud Support Associa| 3            | 0           
---------------------------------------------------------------------------------
```

---

## Project Explanation

The application splits functionality into three main tiers:
1. **Presentation Layer (`Main.java`)**: Manages CLI menus, handles routing logic, and sanitizes user input (e.g. handles `NumberFormatException` when numeric entries are expected).
2. **Business Logic Layer (`EligibilityChecker.java`, `ReportManager.java`)**: Calculates metrics and cross-references values (e.g. matching CGPA requirements against student statistics).
3. **Data Access Layer (`StudentManager.java`, `CompanyManager.java`, `DatabaseConnection.java`)**: Interacts directly with MySQL database via JDBC API. All connections, prepared statements, and result sets are processed within try-with-resources statements to guarantee instant resource teardown.

---

## Viva Questions & Answers

#### Q1. What is JDBC and what is its main purpose?
**Ans:** JDBC (Java Database Connectivity) is an API from Java SE that defines how a Java application can access tabular databases. It provides methods to query, update, and manage relational databases, translating Java method calls into SQL commands for the database server.

#### Q2. Why did you use `PreparedStatement` instead of `Statement` in this project?
**Ans:** We used `PreparedStatement` because:
- **Security**: It prevents SQL injection attacks by pre-compiling the SQL statement and sanitizing parameters.
- **Performance**: Precompiled statements execute faster when run repeatedly.
- **Convenience**: Setting parameters (e.g., `pstmt.setInt()`, `pstmt.setString()`) is cleaner than manually building SQL queries with string concatenation.

#### Q3. What is the role of `Class.forName("com.mysql.cj.jdbc.Driver")`?
**Ans:** It explicitly loads and registers the MySQL JDBC Driver class into memory. In modern JDBC (JDBC 4.0+), drivers placed in the classpath are loaded automatically via the Service Provider mechanism. However, including it explicitly guarantees compatibility across different JVM configurations.

#### Q4. Explain the database schema design. How did you model the placement status?
**Ans:** We designed two tables: `students` and `companies`. We added a nullable foreign key `placed_company_id` in the `students` table pointing to `company_id` in `companies`. If a student is not placed, this field is `NULL`. If they are placed, it stores the ID of the company.

#### Q5. How is the overall "Placement Percentage" calculated?
**Ans:** It is calculated as:
$$\text{Placement Percentage} = \left( \frac{\text{Placed Students}}{\text{Total Students}} \right) \times 100$$
Our `ReportManager` executes aggregate queries to fetch these counts and handles the potential division-by-zero exception (when no students are in the database).

#### Q6. What is "try-with-resources" and why is it used in JDBC?
**Ans:** Try-with-resources is a Java feature (introduced in Java 7) that automatically closes resources (like `Connection`, `PreparedStatement`, and `ResultSet`) when the block exits. These classes implement the `AutoCloseable` interface. Using it prevents resource leaks (e.g., hanging database connections) without writing verbose `finally` blocks.

#### Q7. How do you handle database credentials if they differ across systems?
**Ans:** Credentials are stored in `db.properties`. In [DatabaseConnection.java](file:///d:/Java%20Mini%20project/src/DatabaseConnection.java), we implement a fallback: if JDBC fails to connect, the application prompts the user in the console to enter their MySQL hostname, port, username, and password, tests the connection, and writes the working settings back to `db.properties` automatically.

#### Q8. What is the difference between `executeQuery()` and `executeUpdate()`?
**Ans:** 
- `executeQuery()`: Used for SQL `SELECT` queries. It returns a `ResultSet` object containing the requested data.
- `executeUpdate()`: Used for queries that modify data (`INSERT`, `UPDATE`, `DELETE`, or DDL statements). It returns an `int` indicating the number of rows affected.

#### Q9. How did you implement encapsulation in your models?
**Ans:** In both [Student.java](file:///d:/Java%20Mini%20project/src/Student.java) and [Company.java](file:///d:/Java%20Mini%20project/src/Company.java), all member variables are declared `private` (e.g., `private double cgpa;`). Access to read or modify these values is strictly restricted to `public` getter and setter methods.

#### Q10. What happens in the database when a company is deleted? How does it affect students?
**Ans:** In our `students` table definition, the foreign key references `companies(company_id)` with `ON DELETE SET NULL`. If a company is deleted, any student who was placed in that company will automatically have their `placed_company_id` set to `NULL` (meaning they become "Unplaced"), preserving database integrity without failing the deletion query.

#### Q11. How do you check if a student is eligible for a company?
**Ans:** A student is eligible if:
- Their CGPA is greater than or equal to the company's minimum CGPA requirement (`student.cgpa >= company.min_cgpa`).
- Their active arrears count is less than or equal to the company's maximum allowed arrears (`student.arrears <= company.max_arrears`).

#### Q12. What SQL query did you use to count unique eligible students?
**Ans:** We used a cross join between `students` and `companies` to identify any student meeting the criteria of *at least one* company:
```sql
SELECT COUNT(DISTINCT s.student_id) FROM students s 
CROSS JOIN companies c 
WHERE s.cgpa >= c.min_cgpa AND s.arrears <= c.max_arrears;
```

#### Q13. How did you prevent the console application from crashing when a user enters non-numeric text in menu inputs?
**Ans:** In `Main.java`, we wrote utility functions `readInt` and `readDouble`. They read inputs as strings and attempt to parse them using wrapper methods (`Integer.parseInt` / `Double.parseDouble`). If a `NumberFormatException` occurs, the exception is caught, a user-friendly error message is displayed, and the loop repeats, requesting input until valid data is entered.

#### Q14. What driver class is used for MySQL JDBC connection?
**Ans:** The class name is `com.mysql.cj.jdbc.Driver`, which is located in the downloaded `mysql-connector-j-9.0.0.jar` archive.

#### Q15. Can we run this project on Linux or macOS?
**Ans:** Yes! Since the source code is written in pure Java, it is platform-independent. You can run it on macOS or Linux by substituting the path separators in the classpath command:
```bash
javac -d bin -cp "lib/*" src/*.java
java -cp "bin:lib/*" src.Main
```
(Windows uses `;` as classpath separator, while UNIX-based systems use `:`).
