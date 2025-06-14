import java.sql.*;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/sport_sync";  // your database name
        String user = "root";
        String password = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);

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
            PreparedStatement ps = conn.prepareStatement("select * from users");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
//              System.out.println("username: " + rs.getString(1)); //
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String role = rs.getString("role");
                System.out.println(id + " " + username + " " + email + " " + role);
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
