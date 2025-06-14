import java.sql.*;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/K23SA";  // your database name
        String user = "root";
        String password = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);

            Statement stmt = conn.createStatement();
            System.out.println("Connected to database");
            ResultSet rs = stmt.executeQuery("SELECT * FROM user");  // replace with your table

            while (rs.next()) {
                System.out.println("Row: " + rs.getString(1)); // prints first column, change index or use rs.getInt() etc.
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
