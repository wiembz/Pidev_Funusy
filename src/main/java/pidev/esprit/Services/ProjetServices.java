package pidev.esprit.Services;

import pidev.esprit.Entities.Investissement;
import pidev.esprit.Entities.Projet;
import pidev.esprit.Tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjetServices implements ICrud<Projet> {
    Connection cnx2;

    public ProjetServices() {
        cnx2 = MyConnection.getInstance().getCnx();
    }


    @Override
    public void ajouterEntite(Projet p) {
        String requet = "INSERT INTO projet (id_user,nom_projet,montant_req,longitude,laltitude,id_type_projet) ";
        try {
            PreparedStatement pst = cnx2.prepareStatement(requet);
            pst.setInt(1, p.getId_user());
            pst.setString(2, p.getNom_projet());
            pst.setFloat(3, p.getMontant_req());
            pst.setString(4, p.getLongitude());
            pst.setString(5, p.getLatitude());
            pst.setString(6, p.getId_type_projet());
            pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

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
                projet.setId_projet(rs.getInt(1));
                projet.setId_user(rs.getInt(2));
                projet.setNom_projet(rs.getString(3));
                projet.setMontant_req(rs.getFloat(4));
                projet.setLongitude(rs.getString(5));
                projet.setLatitude(rs.getString(6));
                projet.setId_type_projet(rs.getString(7));
                projets.add(projet);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return projets;
    }

    @Override
    public void updateEntite(Projet p) {
        String req = "UPDATE projet SET id_user = ?, nom_projet = ?, montant_req = ?, longitude = ?, laltitude = ?, id_type_projet = ? WHERE id_projet = ?";
        try {
            PreparedStatement pst = cnx2.prepareStatement(req);
            pst.setInt(1, p.getId_user());
            pst.setString(2, p.getNom_projet());
            pst.setFloat(3, p.getMontant_req());
            pst.setString(4, p.getLongitude());
            pst.setString(5, p.getLatitude());
            pst.setString(6, p.getId_type_projet());
            pst.setInt(7, p.getId_projet());
            pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    }

    @Override
    public void deleteEntite(int id) {
        String req = "DELETE FROM projet WHERE id_projet = ?";
        try {
            PreparedStatement pst = cnx2.prepareStatement(req);
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());

        }
    }
}
