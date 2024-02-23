package pidev.esprit.Services;

import pidev.esprit.Entities.Projet;
import pidev.esprit.Tools.MyConnection;
import pidev.esprit.Entities.ProjectType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjetServices implements ICrud<Projet> {
    Connection cnx2;

    public ProjetServices() {
        cnx2 = MyConnection.getInstance().getCnx();
    }

    public void ajouterEntite(Projet p) {
        String requet = "INSERT INTO projet (id_user, nom_projet, montant_req, longitude, latitude, type_projet) VALUES (?,?,?,?,?,?)";
        try {
            PreparedStatement pst = cnx2.prepareStatement(requet);
            pst.setInt(1, p.getId_user());
            pst.setString(2, p.getNom_projet());
            pst.setFloat(3, p.getMontant_req());
            pst.setString(4, p.getLongitude());
            pst.setString(5, p.getLatitude());
            pst.setString(6, p.getType_projet()); // Use the type_projet field directly
            pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private int getIdTypeProjet(ProjectType projectType) {
        // Implement a method to get the ID of the ProjectType enum
        // This method will return the corresponding ID based on the ProjectType enum
        // You can implement this method by iterating through the enum values and matching the given projectType
        switch (projectType) {
            case AGRICULTURE:
                return 1; // Assuming 1 is the ID for Agriculture in your database
            case TECHNOLOGIQUE:
                return 2; // Assuming 2 is the ID for Technologique in your database
            case BOURSE:
                return 3; // Assuming 3 is the ID for Bourse in your database
            case IMMOBILIER:
                return 4; // Assuming 4 is the ID for Immobilier in your database
            default:
                throw new IllegalArgumentException("Unknown ProjectType: " + projectType);
        }
    }


    @Override
    public boolean EntiteExists(Projet p) {
        String query = "SELECT COUNT(*) FROM projet WHERE id_user = ? AND nom_projet = ? AND montant_req = ? AND longitude = ? AND latitude = ? AND type_projet = ?";
        try {
            PreparedStatement pst = cnx2.prepareStatement(query);
            pst.setInt(1, p.getId_user());
            pst.setString(2, p.getNom_projet());
            pst.setFloat(3, p.getMontant_req());
            pst.setString(4, p.getLongitude());
            pst.setString(5, p.getLatitude());
            pst.setString(6, p.getType_projet()); // Use a method to get the ID of the ProjectType enum
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    @Override
    public List<Projet> afficherEntite() {
        List<Projet> projets = new ArrayList<>();
        String req3 = "SELECT * FROM projet";
        try {
            Statement stm = cnx2.createStatement();
            ResultSet rs = stm.executeQuery(req3);
            while (rs.next()) {
                Projet projet = new Projet();
                projet.setId_projet(rs.getInt("id_projet"));
                projet.setId_user(rs.getInt("id_user"));
                projet.setNom_projet(rs.getString("nom_projet"));
                projet.setMontant_req(rs.getFloat("montant_req"));
                projet.setLongitude(rs.getString("longitude"));
                projet.setLatitude(rs.getString("latitude"));
                projet.setType_projet(rs.getString("type_projet")); // Use the type_projet field directly
                projets.add(projet);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return projets;
    }

    @Override
    public void updateEntite(Projet p) {
        String req = "UPDATE projet SET id_user=?, nom_projet=?, montant_req=?, longitude=?, latitude=?, type_projet=? WHERE id_projet=?";
        try {
            PreparedStatement pst = cnx2.prepareStatement(req);
            pst.setInt(1, p.getId_user());
            pst.setString(2, p.getNom_projet());
            pst.setFloat(3, p.getMontant_req());
            pst.setString(4, p.getLongitude());
            pst.setString(5, p.getLatitude());
            pst.setString(6, p.getType_projet());
            pst.setInt(7, p.getId_projet());
            pst.executeUpdate();
           // pst.close(); // Close PreparedStatement after execution
            System.out.println("Update successful");
        } catch (SQLException e) {
            System.err.println("Error updating entity: " + e.getMessage());
        }
    }

    @Override
    public void deleteEntite(int id) {
        String req = "DELETE FROM projet WHERE id_projet=?";
        try {
            PreparedStatement pst = cnx2.prepareStatement(req);
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
