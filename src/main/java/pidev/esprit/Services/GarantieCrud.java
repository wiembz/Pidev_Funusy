package pidev.esprit.Services;

import pidev.esprit.Entities.Garantie;
import pidev.esprit.Entities.Nature;
import pidev.esprit.Tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GarantieCrud implements ICrudCredit<Garantie> {

    Connection cnx2;

    public GarantieCrud() {
        cnx2 = MyConnection.getInstance().getCnx();
    }

    @Override
    public void ajouterEntite(Garantie p) {

    }

    @Override
    public void ajouterGarantie(Garantie G, int id_credit) {
        String req1 = "INSERT INTO garantie (id_credit, nature_garantie, valeur_garantie, preuve) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pst = cnx2.prepareStatement(req1, Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, id_credit);
            pst.setString(2, G.getNature_garantie().toString());
            pst.setDouble(3, G.getValeur_Garantie());
            pst.setString(4, G.getPreuve());

            pst.executeUpdate();

            System.out.println("Added warranty");

            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                G.setId_garantie(id);
            }
        } catch (SQLException e) {
            System.err.println("Error adding guarantee: " + e.getMessage());
        }
    }

    @Override
    public boolean EntiteExists(Garantie p) {
        String query = "SELECT COUNT(*) FROM Garantie WHERE id_credit = ? AND nature_garantie = ? AND Valeur_Garantie  = ? AND preuve = ?";
        try {
            PreparedStatement pst = cnx2.prepareStatement(query);
            pst.setInt(1, p.getId_credit());
            pst.setString(2, p.getNature_garantie());
            pst.setDouble(3, p.getValeur_Garantie());
            pst.setString(4, p.getPreuve());
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
    public void updateEntite(Garantie G) {
        String req2 = "UPDATE garantie SET nature_garantie = '" + G.getNature_garantie() + "', valeur_garantie = '" + G.getValeur_Garantie() + "', preuve = '" + G.getPreuve() + "' WHERE Id_garantie = '" + G.getId_garantie() + "'";
        try {
            Statement st = cnx2.createStatement();
            st.executeUpdate(req2);
            System.out.println("Warranty modified");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }


    }

    @Override
    public void deleteEntite(Garantie G) {
        String req3 = "DELETE FROM garantie WHERE Id_garantie = '" + G.getId_garantie() + "'";
        try {
            Statement st = cnx2.createStatement();
            st.executeUpdate(req3);
            System.out.println("Warranty removed");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    }

    @Override
    public List<Garantie> afficherEntite() {
        List<Garantie> list = new ArrayList<>();
        String req4 = "SELECT * FROM garantie";
        try {
            Statement st = cnx2.createStatement();
            ResultSet rs = st.executeQuery(req4);
            while (rs.next()) {
                Garantie G = new Garantie();
                G.setId_garantie(rs.getInt("Id_garantie"));
                G.setId_credit(rs.getInt("id_credit"));
                // Convertir la valeur de la colonne nature_garantie en objet Garantie.Nature
                G.setNature_garantie(rs.getString("nature_garantie"));
                G.setValeur_Garantie(rs.getDouble("valeur_garantie"));
                G.setPreuve(rs.getString("preuve"));
                list.add(G);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return list;
    }

    public void deleteGarantiesByCreditId(int idCredit) {
        String req3 = "DELETE FROM garantie WHERE id_credit = '" + idCredit + "'";
        try {
            Statement st = cnx2.createStatement();
            st.executeUpdate(req3);
            System.out.println("Warranty removed");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public List<Garantie> getGarantiesByCreditId(int idCredit) {
        List<Garantie> list = new ArrayList<>();
        String req4 = "SELECT * FROM garantie WHERE id_credit = '" + idCredit + "'";
        try {
            Statement st = cnx2.createStatement();
            ResultSet rs = st.executeQuery(req4);
            while (rs.next()) {
                Garantie G = new Garantie();
                G.setId_garantie(rs.getInt("Id_garantie"));
                G.setId_credit(rs.getInt("id_credit"));
                // Convertir la valeur de la colonne nature_garantie en objet Garantie.Nature
                G.setNature_garantie(rs.getString("nature_garantie"));
                G.setValeur_Garantie(rs.getDouble("valeur_garantie"));
                G.setPreuve(rs.getString("preuve"));
                list.add(G);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return list;
    }
}
