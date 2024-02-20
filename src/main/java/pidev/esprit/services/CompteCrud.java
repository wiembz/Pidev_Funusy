package pidev.esprit.services;
import pidev.esprit.entities.Compte;
import pidev.esprit.entities.TypeCompte;
import pidev.esprit.Tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CompteCrud implements CRUD<Compte> {
    Connection cnx2;

    public CompteCrud() {
        cnx2 = MyConnection.getInstance().getCnx();
    }

    public void addCompte(Compte c) {
        String sql = "INSERT INTO compte (rib, solde, date_ouverture, id_type_compte) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement pstmt = cnx2.prepareStatement(sql);
            pstmt.setString(1, c.getRib());
            pstmt.setDouble(2, c.getSolde());
            pstmt.setDate(3, java.sql.Date.valueOf(c.getDate_ouverture()));
            pstmt.setInt(4, c.getTP().getId_type_compte());

            pstmt.executeUpdate();
            System.out.println("Compte Ajoute");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
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
                comptes.add(c);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return comptes;
    }

    @Override
    public void updateCompte(Compte c) {
        String sql = "UPDATE compte SET solde = ?, date_ouverture = ?, id_type_compte = ? WHERE rib = ?";

        try {
            PreparedStatement pstmt = cnx2.prepareStatement(sql);
            pstmt.setDouble(1, c.getSolde());
            pstmt.setDate(2, java.sql.Date.valueOf(c.getDate_ouverture()));
            pstmt.setInt(3, c.getTP().getId_type_compte());
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


    @Override
    public void deleteCompte(String rib) {
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
    }

