import java.sql.*;
import java.util.Scanner;

public class StudentManager {
    static final String DB_URL = "jdbc:mysql://localhost:3306/school";
    static final String USER = "root";
    static final String PASS = "";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            while (true) {
                System.out.println("\n1. Add Student\n2. View Students\n3. Update Marks\n4. Delete Student\n5. Exit");
                System.out.print("Choose an option: ");
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1:
                        System.out.print("Enter name: ");
                        String name = sc.nextLine();
                        System.out.print("Enter age: ");
                        int age = sc.nextInt();
                        System.out.print("Enter marks: ");
                        double marks = sc.nextDouble();

                        String insertSQL = "INSERT INTO students (name, age, marks) VALUES (?, ?, ?)";
                        try (PreparedStatement ps = conn.prepareStatement(insertSQL)) {
                            ps.setString(1, name);
                            ps.setInt(2, age);
                            ps.setDouble(3, marks);
                            ps.executeUpdate();
                            System.out.println("Student added successfully.");
                        }
                        break;

                    case 2:
                        String selectSQL = "SELECT * FROM students";
                        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(selectSQL)) {
                            System.out.println("ID | Name | Age | Marks");
                            while (rs.next()) {
                                System.out.printf("%d | %s | %d | %.2f\n", rs.getInt("id"), rs.getString("name"),
                                        rs.getInt("age"), rs.getDouble("marks"));
                            }
                        }
                        break;

                    case 3:
                        System.out.print("Enter student ID to update marks: ");
                        int updateId = sc.nextInt();
                        System.out.print("Enter new marks: ");
                        double newMarks = sc.nextDouble();

                        String updateSQL = "UPDATE students SET marks = ? WHERE id = ?";
                        try (PreparedStatement ps = conn.prepareStatement(updateSQL)) {
                            ps.setDouble(1, newMarks);
                            ps.setInt(2, updateId);
                            int rows = ps.executeUpdate();
                            System.out.println(rows > 0 ? "Marks updated." : "Student not found.");
                        }
                        break;

                    case 4:
                        System.out.print("Enter student ID to delete: ");
                        int deleteId = sc.nextInt();

                        String deleteSQL = "DELETE FROM students WHERE id = ?";
                        try (PreparedStatement ps = conn.prepareStatement(deleteSQL)) {
                            ps.setInt(1, deleteId);
                            int rows = ps.executeUpdate();
                            System.out.println(rows > 0 ? "Student deleted." : "Student not found.");
                        }
                        break;

                    case 5:
                        System.out.println("Exiting...");
                        return;

                    default:
                        System.out.println("Invalid choice!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
