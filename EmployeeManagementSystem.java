package com.excelr.assignment.jdbc;

import java.sql.*;
import java.util.Scanner;

public class EmployeeManagementSystem {
   static Connection conn = null;
    static Statement stmt = null;
    static ResultSet rs = null;

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            // Open a connection
            conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "srijith");

            while (true) {
                System.out.println("Employee Management System");
                System.out.println("1. Add new employee");
                System.out.println("2. Update employee details");
                System.out.println("3. Delete an employee");
                System.out.println("4. Display all employees");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        addEmployee();
                        break;
                    case 2:
                        updateEmployee();
                        break;
                    case 3:
                        deleteEmployee();
                        break;
                    case 4:
                        displayAllEmployees();
                        break;
                    case 5:
                        System.out.println("Exiting program...");
                        conn.close();
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void addEmployee() {
        try {
            System.out.print("Enter employee ID: ");
            int id = scanner.nextInt();
            scanner.nextLine(); 
            System.out.print("Enter employee name: ");
            String name = scanner.nextLine();
            System.out.print("Enter employee department: ");
            String department = scanner.nextLine();
            System.out.print("Enter employee salary: ");
            double salary = scanner.nextDouble();

            String query = "INSERT INTO employees (id, name, department, salary) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, department);
            pstmt.setDouble(4, salary);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Employee added successfully.");
            } else {
                System.out.println("Failed to add employee.");
            }
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void updateEmployee() {
        try {
            System.out.print("Enter employee ID to update: ");
            int id = scanner.nextInt();
            scanner.nextLine(); 

            System.out.println("Select field to update:");
            System.out.println("1. Name");
            System.out.println("2. Department");
            System.out.println("3. Salary");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            String column = "";
            String newValue = "";
            switch (choice) {
                case 1:
                    column = "name";
                    System.out.print("Enter new name: ");
                    newValue = scanner.nextLine();
                    break;
                case 2:
                    column = "department";
                    System.out.print("Enter new department: ");
                    newValue = scanner.nextLine();
                    break;
                case 3:
                    column = "salary";
                    System.out.print("Enter new salary: ");
                    newValue = String.valueOf(scanner.nextDouble());
                    break;
                default:
                    System.out.println("Invalid choice.");
                    return;
            }

            String query = "UPDATE employees SET " + column + " = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, newValue);
            pstmt.setInt(2, id);

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Employee details updated successfully.");
            } else {
                System.out.println("No employee found with the given ID.");
            }
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void deleteEmployee() {
        try {
            System.out.print("Enter employee ID to delete: ");
            int id = scanner.nextInt();

            String sql = "DELETE FROM employees WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Employee deleted successfully.");
            } else {
                System.out.println("No employee found with the given ID.");
            }
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void displayAllEmployees() {
        try {
            stmt = conn.createStatement();
            String query = "SELECT * FROM employees";
            rs = stmt.executeQuery(query);

            System.out.println("Employee ID | Name | Department | Salary");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String department = rs.getString("department");
                double salary = rs.getDouble("salary");
                System.out.println(id + " | " + name + " | " + department + " | " + salary);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

