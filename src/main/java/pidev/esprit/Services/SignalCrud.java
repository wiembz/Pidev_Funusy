package pidev.esprit.Services;

import pidev.esprit.Entities.Signal;
import pidev.esprit.Tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SignalCrud implements ICrud<Signal> {

    Connection cnx2;

    public SignalCrud() {
        cnx2 = MyConnection.getInstance().getCnx();
    }

    @Override
    public void ajouterEntite(Signal s) {
        String req1 = "INSERT INTO signale (id_commentaire, description, date_signal) VALUES (?, ?, ?)";
        try {
            PreparedStatement pst = cnx2.prepareStatement(req1);
            pst.setInt(1, s.getId_commentaire());
            pst.setString(2, s.getDescription());

            // Convert LocalDate to java.sql.Date
            java.sql.Date sqlDate = java.sql.Date.valueOf(s.getDate_signal());
            pst.setDate(3, sqlDate);

            pst.executeUpdate();
            pst.close(); // Close the PreparedStatement
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }


    @Override
    public List<Signal> afficherEntite() {
        List<Signal> signals = new ArrayList<>();
        String req3 = "SELECT * FROM signale";
        try {
            Statement stm = cnx2.createStatement();
            ResultSet rs = stm.executeQuery(req3);
            while (rs.next()) {
                Signal signal = new Signal();
                signal.setId_signal(rs.getInt("id_signal"));
                signal.setId_commentaire(rs.getInt("id_commentaire"));
                signal.setDescription(rs.getString("description"));
                signal.setDate_signal(rs.getDate("date_signal").toLocalDate());
                signals.add(signal);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return signals;
    }

    @Override
    public boolean EntiteExists(Signal s) {
        return true;
    }

    @Override
    public void updateEntite(Signal s) {
        String req6 = "UPDATE signale SET id_commentaire = ?, description = ?, date_signal = ?,etat = ? WHERE id_signal = ?";
        try {
            PreparedStatement pst = cnx2.prepareStatement(req6);
            pst.setInt(1, s.getId_commentaire());
            pst.setString(2, s.getDescription());

            // Convert LocalDate to java.sql.Date
            java.sql.Date sqlDate = java.sql.Date.valueOf(s.getDate_signal());
            pst.setDate(3, sqlDate);

            pst.setInt(4, s.getId_signal()); // id_signal doit être le dernier paramètre

            pst.executeUpdate();
            System.out.println("Signal modifiée");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    }

    @Override
    public void deleteEntite(int id) {
        String req3 = "DELETE FROM signale WHERE id_signal = ?";
        try {
            PreparedStatement pst = cnx2.prepareStatement(req3);
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("Signal supprimée");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    }
}

