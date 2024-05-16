package pidev.esprit.Tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {
    private String url = "jdbc:mysql://localhost:3306/funusy";
    private String login = "root";
    private String password = "";
    private Connection cnx;
    private int userId; // Variable to store the logged-in user's ID
    private static MyConnection instance;

    private MyConnection() {
        try {
            cnx = DriverManager.getConnection(url, login, password);
            System.out.println("Connexion établie!");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public Connection getCnx() {
        return cnx;
    }

    public static MyConnection getInstance() {
        if (instance == null) {
            instance = new MyConnection();
        }
        return instance;
    }

    // Method to set the logged-in user's ID
    public void setLoggedInUserId(int userId) {
        this.userId = userId;
    }

    // Method to retrieve the logged-in user's ID
    public int getLoggedInUserId() {
        return userId;
    }
}
