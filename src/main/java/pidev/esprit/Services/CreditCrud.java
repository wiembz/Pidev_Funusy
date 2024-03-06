package pidev.esprit.Services;


import pidev.esprit.Entities.Credit;
import pidev.esprit.Entities.Garantie;
import pidev.esprit.Entities.User;
import pidev.esprit.Tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static pidev.esprit.Controllers.Credit.SmsCredit.sendSMS;

public class CreditCrud implements ICrud<Credit> {
    static Connection cnx2;

    public CreditCrud() {
        cnx2 = MyConnection.getInstance().getCnx();
    }

    @Override
    public void ajouterEntite(Credit p) {
        String req1 = "INSERT INTO credit (montant_credit, duree_credit, taux_credit,date_credit,id_user) VALUES ('" + p.getMontant_credit() + "', '" + p.getDuree_credit() + "', '" + p.getTaux_credit() + "',NOW(),'" + p.getId_user() + "')";
        try {
            Statement st = cnx2.createStatement();
            st.executeUpdate(req1);
            System.out.println("Added credit");
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
    public void ajouterGarantie(Credit p, int id_credit) {

    }

    @Override
    public boolean EntiteExists(Credit p) {
        String query = "SELECT COUNT(*) FROM Credit WHERE montant_credit = ? AND duree_credit = ? AND taux_credit = ? AND id_user = ?";
        try {
            PreparedStatement pst = cnx2.prepareStatement(query);
            pst.setDouble(1, p.getMontant_credit());
            pst.setInt(2, p.getDuree_credit());
            pst.setDouble(3, p.getTaux_credit());
            pst.setInt(4, p.getId_user());
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
    public void updateEntite(Credit p) {
        String req2 = "UPDATE credit SET montant_credit = '" + p.getMontant_credit() + "', duree_credit = '" + p.getDuree_credit() + "', taux_credit = '" + p.getTaux_credit() + "' WHERE Id_credit = '" + p.getId_credit() + "'";
        try {
            Statement st = cnx2.createStatement();
            st.executeUpdate(req2);
            System.out.println("Added credit");
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
            System.out.println("Credit deleted");
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
            while (rs.next()) {
                Credit p = new Credit();
                p.setId_credit(rs.getInt("id_credit"));
                p.setMontant_credit(rs.getDouble("montant_credit"));
                p.setDuree_credit(rs.getInt("duree_credit"));
                p.setTaux_credit(rs.getDouble("taux_credit"));
                p.setDate_credit(rs.getDate("date_credit"));
                p.setId_user(rs.getInt("id_user"));
                p.setStatus(rs.getString("Status"));
                list.add(p);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return list;
    }


    public int getLastInsertedCreditId() {
        int lastInsertedId = 0;
        String query = "SELECT MAX(id_credit) AS last_id FROM credit"; // Get the last inserted id

        try (PreparedStatement statement = cnx2.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                lastInsertedId = resultSet.getInt("last_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lastInsertedId;
    }

    public double getLastInsertedMontantCredit() {
        double lastInsertedMontantCredit = 0;
        String query = "SELECT montant_credit FROM credit WHERE id_credit = (SELECT MAX(id_credit) FROM credit)";// Get the last inserted montant_credit

        try (PreparedStatement statement = cnx2.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                lastInsertedMontantCredit = resultSet.getDouble("montant_credit");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lastInsertedMontantCredit;
    }

    public List<Credit> getAllCredits() {
        List<Credit> credits = new ArrayList<>();
        String query = "SELECT * FROM credit";

        try (PreparedStatement statement = cnx2.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id_credit");
                double montant = resultSet.getDouble("montant_credit");
                int duree = resultSet.getInt("duree_credit");
                double taux = resultSet.getDouble("taux_credit");
                int id_user = resultSet.getInt("id_user");
                String Status = resultSet.getString("Status");
                Credit credit = new Credit(id, montant, duree, taux, id_user, Status);
                credits.add(credit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return credits;
    }

    public void updateCreditStatus(int idCredit, String accepted) {
        String query = "UPDATE credit SET Status = ? WHERE id_credit = ?";

        try (PreparedStatement statement = cnx2.prepareStatement(query)) {
            statement.setString(1, accepted);
            statement.setInt(2, idCredit);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Méthode pour récupérer le numéro de téléphone de l'utilisateur par son ID
    public String recupererNumeroTelephoneParId(int idUtilisateur) {
        String numeroTelephone = null;
        String query = "SELECT tel FROM user WHERE id_user = ?";
        try (PreparedStatement statement = cnx2.prepareStatement(query)) {
            statement.setInt(1, idUtilisateur);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String tel = resultSet.getString("tel");
                // Ajouter le code du pays au début du numéro de téléphone
                numeroTelephone = "+216" + tel; // Remplacez "216" par le code de votre pays si nécessaire
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return numeroTelephone;
    }




}
