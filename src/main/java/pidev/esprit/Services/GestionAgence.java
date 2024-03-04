package pidev.esprit.Services;

import pidev.esprit.Entities.Transaction;
import pidev.esprit.Tools.MyConnection;
import pidev.esprit.Entities.Agence;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GestionAgence implements IGestionAgence<Agence>{
    Connection connection = MyConnection.getInstance().getCnx();




    public void ServiceTransaction() throws SQLException {
        try(Connection connection = MyConnection.getInstance().getCnx()){
            this.connection = connection;
        }
    }

    @Override
    public void ajouter(Agence agence) throws SQLException {
        try {

            connection = MyConnection.getInstance().getCnx();


            String sql = "INSERT INTO agence (`code_agence`, `adresse`, `codepostal`) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, agence.getCodeAgence());
            statement.setString(2, agence.getAdresse());
            statement.setInt(3, agence.getCodepostal());


            statement.executeUpdate();
            statement.close();

            System.out.println("Agence insérée avec succès.");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'insertion de l'agence : " + e.getMessage());
        }
    }

    @Override
    public void modifier(Agence agence) throws SQLException {

    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "Delete from agence where code_agence = ? ";
        PreparedStatement preparedStatement= connection.prepareStatement(sql);
        preparedStatement.setInt(1,id);
        preparedStatement.executeUpdate();
    }

    @Override
    public List<Agence> afficher() throws SQLException {
        List<Agence> agences = new ArrayList<>();
        String req3 = "SELECT * FROM agence";
        try {
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery(req3);
            while (rs.next()) {
                Agence agence = new Agence();
                agence.setAdresse(rs.getString("adresse"));
                agence.setCodeAgence(rs.getInt("code_agence"));
                agence.setCodepostal(rs.getInt("codepostal"));


                agences.add(agence);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return agences;
    }
}
