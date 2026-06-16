-- Create Database
CREATE DATABASE IF NOT EXISTS placement_db;
USE placement_db;

-- Drop Tables if they exist (ordered to respect foreign keys)
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS companies;

-- Create Companies Table
CREATE TABLE companies (
    company_id INT PRIMARY KEY,
    company_name VARCHAR(100) NOT NULL,
    min_cgpa DOUBLE NOT NULL,
    max_arrears INT NOT NULL,
    package_offered DOUBLE NOT NULL,
    job_role VARCHAR(100) NOT NULL
);

-- Create Students Table
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

-- Insert Sample Companies
INSERT INTO companies (company_id, company_name, min_cgpa, max_arrears, package_offered, job_role) VALUES
(101, 'Google', 8.5, 0, 35.5, 'Software Engineer'),
(102, 'Microsoft', 8.0, 0, 28.0, 'Program Manager'),
(103, 'Accenture', 6.5, 2, 4.5, 'Associate Software Engineer'),
(104, 'TCS', 6.0, 3, 3.6, 'Systems Engineer'),
(105, 'Amazon', 8.2, 1, 18.5, 'Cloud Support Associate');

-- Insert Sample Students
INSERT INTO students (student_id, name, department, cgpa, arrears, email, phone, placed_company_id) VALUES
(1, 'Alice Smith', 'Computer Science', 8.7, 0, 'alice@example.com', '9876543210', 101),
(2, 'Bob Johnson', 'Information Technology', 7.8, 0, 'bob@example.com', '9876543211', NULL),
(3, 'Charlie Brown', 'Electronics', 6.2, 2, 'charlie@example.com', '9876543212', 103),
(4, 'Diana Prince', 'Computer Science', 8.3, 1, 'diana@example.com', '9876543213', NULL),
(5, 'Evan Wright', 'Mechanical', 5.8, 4, 'evan@example.com', '9876543214', NULL),
(6, 'Fiona Gallagher', 'Computer Science', 9.1, 0, 'fiona@example.com', '9876543215', 102);
