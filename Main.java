import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/students";  // your database name
        String user = "root";
        String password = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
//            Statement stmt = conn.createStatement();
//            Scanner sc = new Scanner(System.in);
//            while (true) {
//                System.out.print("Enter student name: ");
//                String name = sc.nextLine();
//
//                System.out.print("Enter student age: ");
//                int age = sc.nextInt();
//
//                System.out.print("Enter student marks: ");
//                double marks = sc.nextDouble();
//                sc.nextLine();
//
//                String query = String.format(
//                        "INSERT INTO student(name, age, marks) VALUES('%s', %d, %f)",
//                        name, age, marks
//                );
//                stmt.addBatch(query);
//
//                System.out.print("Enter more data? (Y/N): ");
//                String choice = sc.nextLine();
//                if (choice.equalsIgnoreCase("N")) {
//                    break;
//                }
//            }
//            int []arr = stmt.executeBatch();
//            for(int i = 0; i < arr.length; i++) {
//                if(arr[i]==0){
//                    System.out.println("Query " + i + " has not been added to the database");
//                }
//            }
            String query = "INSERT INTO student(name, age, marks) VALUES(?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);

            Scanner sc = new Scanner(System.in);

            while (true) {
                System.out.print("Enter student name: ");
                String name = sc.nextLine();

                System.out.print("Enter student age: ");
                int age = sc.nextInt();

                System.out.print("Enter student marks: ");
                double marks = sc.nextDouble();
                sc.nextLine();  // consume leftover newline

                pstmt.setString(1, name);
                pstmt.setInt(2, age);
                pstmt.setDouble(3, marks);
                pstmt.addBatch();

                System.out.print("Enter more data? (Y/N): ");
                String choice = sc.nextLine();
                if (choice.equalsIgnoreCase("N")) {
                    break;
                }
            }

            pstmt.executeBatch(); // Executes all batched inserts
            pstmt.close();
//            Statement stmt = conn.createStatement();
//            System.out.println("Connected to database");
//            ResultSet rs = stmt.executeQuery("SELECT * FROM users");  // replace with your table
//
//            while (rs.next()) {
//              System.out.println("username: " + rs.getString(1)); //
//                int id = rs.getInt("id");
//                String username = rs.getString("username");
//                String email = rs.getString("email");
//                String role = rs.getString("role");
//                System.out.println(id + " " + username + " " + email + " " + role);
//            }
//            conn.close();
//            PreparedStatement ps = conn.prepareStatement("select * from student");
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
////              System.out.println("username: " + rs.getString(1)); //
//                int id = rs.getInt("id");
//                String username = rs.getString("username");
//                String email = rs.getString("email");
//                String role = rs.getString("role");
//                System.out.println(id + " " + username + " " + email + " " + role);
//            }
//            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
