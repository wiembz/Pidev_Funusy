package pidev.esprit.Services;


import pidev.esprit.Entities.Credit;
import pidev.esprit.Tools.MyConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CreditCrud implements ICrud <Credit> {
    Connection cnx2;
    public CreditCrud() {
        cnx2 = MyConnection.getInstance().getCnx();
    }

    @Override
    public void ajouterEntite(Credit p) {
     String req1 = "INSERT INTO credit (montant_credit, duree_credit, taux_credit,date_credit,id_user) VALUES ('"+p.getMontant_credit()+"', '"+p.getDuree_credit()+"', '"+p.getTaux_credit()+"',NOW(),'"+p.getId_user()+"')";
        try {
            Statement st = cnx2.createStatement();
            st.executeUpdate(req1);
            System.out.println("Credit ajouté");
            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                p.setId_credit(id);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void updateEntite(Credit p) {
        String req2 = "UPDATE credit SET montant_credit = '" + p.getMontant_credit() + "', duree_credit = '" + p.getDuree_credit() + "', taux_credit = '" + p.getTaux_credit() + "' WHERE Id_credit = '" + p.getId_credit() + "'";
        try {
            Statement st = cnx2.createStatement();
            st.executeUpdate(req2);
            System.out.println("Crédit modifié");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    }

    @Override
    public void deleteEntite(Credit p) {
        String req3 = "DELETE FROM credit WHERE Id_credit = '" + p.getId_credit() + "'";
        try {
            Statement st = cnx2.createStatement();
            st.executeUpdate(req3);
            System.out.println("Crédit supprimé");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }


    }

    @Override
    public List<Credit> afficherEntite() {
        List<Credit> list = new ArrayList<>();
        String req4 = "SELECT * FROM credit";
        try {
            Statement st = cnx2.createStatement();
            ResultSet rs = st.executeQuery(req4);
            while (rs.next()){
                Credit p = new Credit();
                p.setId_credit(rs.getInt("id_credit"));
                p.setMontant_credit(rs.getDouble("montant_credit"));
                p.setDuree_credit(rs.getInt("duree_credit"));
                p.setTaux_credit(rs.getDouble("taux_credit"));
                p.setDate_credit(rs.getDate("date_credit"));
                p.setId_user(rs.getInt("id_user"));
                list.add(p);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return list;
    }
}
