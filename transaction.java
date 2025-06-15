import java.sql.*;
import java.util.Scanner;

public class transaction {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/lenden";  // your database name
        String user = "root";
        String password = "";
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        try{
            Connection cnt = DriverManager.getConnection(url,user,password);
            cnt.setAutoCommit(false);
            String debit_query = "UPDATE accounts SET balance = balance - ? WHERE acccount_number = ?";
            String credit_query = "UPDATE accounts SET balance = balance + ? WHERE acccount_number = ?";
            PreparedStatement ps = cnt.prepareStatement(debit_query);
            PreparedStatement cs = cnt.prepareStatement(credit_query);
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter account number");
            int accnumber = sc.nextInt();
            System.out.println("Enter balance");
            double balance = sc.nextDouble();
            ps.setDouble(1, balance);
            ps.setInt(2, accnumber);
            cs.setDouble(1, balance);
            cs.setInt(2, 1299);
            ps.executeUpdate();
            cs.executeUpdate();
            if(isSufficient(cnt,accnumber,balance)){
                cnt.commit();
                System.out.println("Transaction Successful");
            }else{
                cnt.rollback();
                System.out.println("Transaction Failed");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    static boolean isSufficient(Connection cnt, int accnumber, double balance) throws SQLException {
        try{
            String sql = "select * from accounts where acccount_number = ?";
            PreparedStatement ps = cnt.prepareStatement(sql);
            ps.setInt(1, accnumber);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                double ctbalance = rs.getDouble("balance");
                if(ctbalance < balance){
                    return false;
                }else{
                    return true;
                }
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }
}
