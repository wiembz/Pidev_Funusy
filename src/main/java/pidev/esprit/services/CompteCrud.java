package pidev.esprit.services;
import pidev.esprit.entities.Compte;

import pidev.esprit.Tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import pidev.esprit.entities.User;

public class CompteCrud implements CRUD<Compte> {
    public static Connection cnx2;

    public CompteCrud() {
        cnx2 = MyConnection.getInstance().getCnx();
    }


//    public static void add_account(Compte c, int id_user, String selectedType) {
//        // First, verify if the user exists
//        if (!userExists(id_user)) {
//            System.err.println("Erreur : L'utilisateur avec l'ID " + id_user + " n'existe pas.");
//            return; // Exit the method if the user does not exist
//        }
//        String rib = Compte.generateRib();
//        if (userExists(id_user)) {
//            // If the user exists, proceed to add the account
//            String sql = "INSERT INTO compte (rib, solde, date_ouverture, type_compte, id_user) VALUES (?, ?, ?, ?, ?)";
//
//            try {
//                PreparedStatement pstmt = cnx2.prepareStatement(sql);
//                pstmt.setString(1, c.getRib());
//                pstmt.setDouble(2, c.getSolde());
//                pstmt.setDate(3, java.sql.Date.valueOf(c.getDate_ouverture()));
//                pstmt.setString(4, selectedType); // Use the selected type
//                pstmt.setInt(5, id_user); // Set the id_user
//
//                pstmt.executeUpdate();
//                System.out.println("Compte ajouté avec succès !");
//            } catch (SQLException e) {
//                System.err.println("Erreur lors de l'ajout du compte : " + e.getMessage());
//            }
//        }
//    }

    public static void add_account(Compte c, int id_user, String selectedType) {
        // First, verify if the user exists


       // Assuming generateRib() method is properly implemented

        // If the user exists, proceed to add the account
        String sql = "INSERT INTO compte (rib, solde, date_ouverture, type_compte, id_user) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement pstmt = cnx2.prepareStatement(sql);
            pstmt.setString(1, c.getRib());
            pstmt.setDouble(2, c.getSolde());
            pstmt.setDate(3, java.sql.Date.valueOf(c.getDate_ouverture()));
            pstmt.setString(4, selectedType); // Use the selected type
            pstmt.setInt(5, id_user); // Set the id_user

            pstmt.executeUpdate();
            System.out.println("Compte ajouté avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du compte : " + e.getMessage());
        }
    }


    // Method to check if a user exists
    public static boolean userExists(int id_user) {
        String sql = "SELECT COUNT(*) FROM user WHERE id_user = ?";
        try (PreparedStatement pstmt = cnx2.prepareStatement(sql)) {
            pstmt.setInt(1, id_user);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking if user exists: " + e.getMessage());
        }
        return false; // Return false in case of exceptions or if the query fails
    }


    @Override
    public void addCompte(Compte c) {

    }

    @Override
    public List<Compte> displayCompte() {

        List <Compte> comptes=new ArrayList<>();
        String req2= "SELECT * FROM compte";
        try {
            Statement st2= cnx2.createStatement();
            ResultSet rs= st2.executeQuery(req2);
            while(rs.next()){
                Compte c=new Compte();
                c.setRib(rs.getString("rib"));
                c.setSolde(rs.getDouble("Solde"));
                c.setDate_ouverture(rs.getDate("date_ouverture").toLocalDate());
                c.setType_compte(rs.getString("type_compte"));
                c.setId_user(rs.getInt("id_user"));
                comptes.add(c);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return comptes;
    }


    public void updateCompte(Compte c) {
        String sql = "UPDATE compte SET solde = ?, date_ouverture = ?, type_compte = ? WHERE rib = ?";

        try {
            PreparedStatement pstmt = cnx2.prepareStatement(sql);
            pstmt.setDouble(1, c.getSolde());
            java.sql.Date sqlDate = java.sql.Date.valueOf(c.getDate_ouverture());
            pstmt.setDate(2, sqlDate);
            pstmt.setString(3, c.getType_compte());
            pstmt.setString(4, c.getRib());

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Compte with RIB " + c.getRib() + " updated successfully.");
            } else {
                System.out.println("No compte found with RIB " + c.getRib() + ".");
            }
        } catch (SQLException e) {
            System.err.println("Error updating compte: " + e.getMessage());
        }
    }



    public static void deleteCompte(String rib) {
        String sql = "DELETE FROM compte WHERE rib = ?";

        try {
            PreparedStatement pstmt = cnx2.prepareStatement(sql);
            pstmt.setString(1, rib);
            pstmt.executeUpdate();
            System.out.println("Compte with RIB " + rib + " deleted successfully.");
        } catch (SQLException e) {
            System.err.println("Error deleting compte: " + e.getMessage());
        }
    }


    public  boolean accountExists(String rib) {
        String sql = "SELECT COUNT(*) FROM compte WHERE rib = ?";
        try (PreparedStatement pstmt = cnx2.prepareStatement(sql)) {
            pstmt.setString(1, rib);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking if account exists: " + e.getMessage());
        }
        return false; // Return false in case of exceptions or if the query fails
    }

    private static final CompteCrud instance = new CompteCrud();

    // Method to access the single instance
    public static CompteCrud getInstance() {
        return instance;
    }

    public static User retrieveUserById(int id_user) {
        String sql = "SELECT nom_user, prenom_user, tel, adresse_user, CIN FROM user WHERE id_user = ?";

        ResultSet rs = null;
        PreparedStatement pstmt = null;
        User user = null;
        try {
            // Establish database connection
            pstmt = cnx2.prepareStatement(sql);

            // Prepare SQL statement to select user information based on ID


            pstmt.setInt(1, id_user);
            rs = pstmt.executeQuery();

            // Execute the query
            rs = pstmt.executeQuery();

            // If a user with the given ID is found, populate the User object
            if (rs.next()) {
                user = new User();
                user.setNom_user(rs.getString("nom_user"));
                user.setPrenom_user(rs.getString("prenom_user"));
                user.setTel(rs.getInt("tel"));
                user.setAdresse_user(rs.getString("adresse_user"));
                user.setCIN(rs.getInt("CIN"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database-related errors
        } finally {
            // Close resources in reverse order of their creation to avoid resource leaks
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return user;
    }

    public static User retrieveUserByRIB(String rib) {
        String query = "SELECT * FROM user WHERE id_user IN (SELECT id_user FROM compte WHERE rib = ?)";
        User user = null;

        try (PreparedStatement pstmt = cnx2.prepareStatement(query)) {
            pstmt.setString(1, rib);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId_user(rs.getInt("id_user"));
                user.setNom_user(rs.getString("nom_user"));
                user.setPrenom_user(rs.getString("prenom_user"));
                user.setTel(rs.getInt("tel"));
                // Populate other user fields as needed
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }
}









