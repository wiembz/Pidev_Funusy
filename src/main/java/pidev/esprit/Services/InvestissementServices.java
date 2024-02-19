package pidev.esprit.Services;

import pidev.esprit.Entities.Investissement;
import pidev.esprit.Tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvestissementServices implements ICrud<Investissement>{
    Connection cnx2;
    public InvestissementServices(){
        cnx2 = MyConnection.getInstance().getCnx();
    }
    @Override
    public void ajouterEntite(Investissement i) {
        String requet = "INSERT INTO investissement (idClient,montant,date_inv,periode,typeProjet) ";
        try {
            PreparedStatement pst = cnx2.prepareStatement(requet);
            pst.setInt(1,i.getId_user());
            pst.setFloat(2,i.getMontant());
            pst.setString(3, String.valueOf(i.getDate_inv()));
            pst.setFloat(4,i.getPeriode());
            pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public List<Investissement> afficherEntite() {
        List<Investissement> investissements = new ArrayList<>();
        String req3="SELECT * FROM investissement";
        try {
            Statement stm = cnx2.createStatement();
            ResultSet rs= stm.executeQuery(req3);
            while (rs.next())
            {
                Investissement inv = new Investissement();
                inv.setId(rs.getInt(1));
                inv.setId_user(rs.getInt(2));
                inv.setMontant(rs.getFloat(3));
                inv.setDate_inv(rs.getDate(4));
                inv.setPeriode(rs.getInt(5));
                investissements.add(inv);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return investissements;
    }

    @Override
    public void updateEntite(Investissement i) {
        String req = "UPDATE investissement SET idClient = ?, montant = ?, date_inv = ?, periode = ? WHERE id = ?";
        try {
            PreparedStatement pst = cnx2.prepareStatement(req);
            pst.setInt(1,i.getId_user());
            pst.setFloat(2,i.getMontant());
            pst.setDate(3, (Date) i.getDate_inv());
            pst.setFloat(4,i.getPeriode());
            pst.setInt(5,i.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void deleteEntite(int id) {
        String req = "DELETE FROM investissement WHERE id_investissement = ?";
        try {
            PreparedStatement pst = cnx2.prepareStatement(req);
            pst.setInt(1,id);
            pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
