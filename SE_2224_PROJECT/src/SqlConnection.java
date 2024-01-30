import java.sql.Connection;
import java.sql.DriverManager;

public class SqlConnection {
    Connection SqlConnect(){
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/se_2224", "root", "admin");
            return connection;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
