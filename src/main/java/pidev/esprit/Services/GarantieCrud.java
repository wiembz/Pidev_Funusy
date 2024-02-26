package pidev.esprit.Services;

import pidev.esprit.Entities.Garantie;
import pidev.esprit.Tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GarantieCrud implements ICrud <Garantie>{

    Connection cnx2;
    public GarantieCrud() {
        cnx2 = MyConnection.getInstance().getCnx();
    }
    @Override
    public void ajouterEntite(Garantie G) {
        String req1 = "INSERT INTO garantie (nature_garantie, valeur_garantie, preuve) VALUES ('"+G.getNature()+"','"+G.getValeur()+"', '"+G.getData()+"')";
        try {
            Statement st = cnx2.createStatement();
            st.executeUpdate(req1);
            System.out.println("Garantie ajouté");
            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                G.setId_garantie(id);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void updateEntite(Garantie G) {
        String req2 = "UPDATE garantie SET nature_garantie = '" + G.getNature() + "', valeur_garantie = '" + G.getValeur() + "', preuve = '" + G.getData() + "' WHERE Id_garantie = '" + G.getId_garantie() + "'";
        try {
            Statement st = cnx2.createStatement();
            st.executeUpdate(req2);
            System.out.println("Garantie modifié");
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
            System.out.println("Garantie supprimé");
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
                G.setNature(Garantie.Nature.valueOf(rs.getString("nature_garantie")));
                G.setValeur(rs.getDouble("valeur_garantie"));
                G.setData(rs.getString("preuve"));
                list.add(G);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return list;
    }
}
