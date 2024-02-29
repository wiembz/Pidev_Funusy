package pidev.esprit.Services;

import pidev.esprit.Entities.Investissement;
import pidev.esprit.Tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvestissementServices implements ICrud<Investissement> {
    Connection cnx2;

    public InvestissementServices() {
        cnx2 = MyConnection.getInstance().getCnx();
    }

    @Override
    public void ajouterEntite(Investissement i) {
        String requet = "INSERT INTO investissement (id_user, montant, date_inv, periode, id_projet) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement pst = cnx2.prepareStatement(requet);
            pst.setInt(1, i.getId_user());
            pst.setFloat(2, i.getMontant());
            pst.setDate(3, new java.sql.Date(i.getDate_investissement().getTime()));
            pst.setInt(4, i.getPeriode());
            pst.setInt(5, i.getId_projet());
            pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
    public List<Investissement> findInvestmentsByProjectId(int projectId) {
        List<Investissement> investments = new ArrayList<>();
        String query = "SELECT * FROM investissement WHERE id_projet = ?";
        try {
            PreparedStatement pst = cnx2.prepareStatement(query);
            pst.setInt(1, projectId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Investissement inv = new Investissement();
                inv.setId_investissement(rs.getInt("id_investissement"));
                inv.setId_user(rs.getInt("id_user"));
                inv.setMontant(rs.getFloat("montant"));
                inv.setDate_investissement(rs.getDate("date_inv"));
                inv.setPeriode(rs.getInt("periode"));
                inv.setId_projet(rs.getInt("id_projet"));
                investments.add(inv);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return investments;
    }
    public boolean EntiteExists(Investissement i) {
        String query = "SELECT COUNT(*) FROM investissement WHERE id_user = ? AND montant = ? AND date_inv = ? AND periode = ? AND id_projet = ?";
        try {
            PreparedStatement pst = cnx2.prepareStatement(query);
            pst.setInt(1, i.getId_user());
            pst.setFloat(2, i.getMontant());
            pst.setDate(3, new java.sql.Date(i.getDate_investissement().getTime()));
            pst.setInt(4, i.getPeriode());
            pst.setInt(5, i.getId_projet());
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
    public List<Investissement> afficherEntite() {
        List<Investissement> investissements = new ArrayList<>();
        String req3 = "SELECT * FROM investissement";
        try {
            Statement stm = cnx2.createStatement();
            ResultSet rs = stm.executeQuery(req3);
            while (rs.next()) {
                Investissement inv = new Investissement();
                inv.setId_investissement(rs.getInt("id_investissement"));
                inv.setId_user(rs.getInt("id_user"));
                inv.setMontant(rs.getFloat("montant"));
                inv.setDate_investissement(rs.getDate("date_inv"));
                inv.setPeriode(rs.getInt("periode"));
                inv.setId_projet(rs.getInt("id_projet"));
                investissements.add(inv);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return investissements;
    }

    @Override
    public void updateEntite(Investissement i) {
        String req = "UPDATE investissement SET id_user = ?, montant = ?, date_inv = ?, periode = ?, id_projet = ? WHERE id_investissement = ?";
        try {
            PreparedStatement pst = cnx2.prepareStatement(req);
            pst.setInt(1, i.getId_user());
            pst.setFloat(2, i.getMontant());
            pst.setDate(3, new java.sql.Date(i.getDate_investissement().getTime()));
            pst.setInt(4, i.getPeriode());
            pst.setInt(5, i.getId_investissement());
            pst.setInt(6, i.getId_projet());
            pst.executeUpdate();
            System.out.println("Update successful");
        } catch (SQLException e) {
            System.err.println("Error updating entity: " + e.getMessage());
        }
    }

    @Override
    public void deleteEntite(int id) {
        String req = "DELETE FROM investissement WHERE id_investissement = ?";
        try {
            PreparedStatement pst = cnx2.prepareStatement(req);
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}