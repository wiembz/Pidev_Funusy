package pidev.esprit.Services;

import pidev.esprit.Entities.Echeance;
import pidev.esprit.Tools.MyConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SimulateurCrud implements ISim<Echeance> {
    Connection cnx2;

    public SimulateurCrud() {
        cnx2 = MyConnection.getInstance().getCnx();
    }

    @Override
    public void ajouterSim(Echeance p) {
        String req1 = "INSERT INTO echeance (numero, principal, valeurResiduelle, interets, mensualite) VALUES ('" + p.getNumero() + "', '" + p.getPrincipal() + "', '" + p.getValeurResiduelle() + "', '" + p.getInterets() + "', '" + p.getMensualite() + "')";
        try {
            Statement st = cnx2.createStatement();
            st.executeUpdate(req1);
            System.out.println("Simulation added");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void afficherSim(Echeance p) {
        String req = "SELECT * FROM echeance";
        try {
            Statement st = cnx2.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                System.out.println("Echeance: " + rs.getInt(1) + " Principal: " + rs.getDouble(2) + " Valeur residuelle: " + rs.getDouble(3) + " Interets: " + rs.getDouble(4) + " Mensualite: " + rs.getDouble(5));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    }
}
