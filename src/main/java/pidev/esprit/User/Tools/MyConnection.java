package pidev.esprit.User.Tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {
    public String url="jdbc:mysql://localhost:3306/funusy";
    public String login="root";
    public String pwd="";
    Connection cnx;
    public static MyConnection instance;

    private MyConnection() {
        try {
            cnx = DriverManager.getConnection(url,login,pwd);
            System.out.println("connexion etablie!");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }


    public Connection getCnx() {
        return cnx;
    }
    //3eme etape
    public static MyConnection getInstance(){
        if(instance == null){
            instance = new MyConnection();
        }
        return instance;
    }
}