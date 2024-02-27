package pidev.esprit.Services;

import pidev.esprit.Entities.Transaction;
import pidev.esprit.Tools.MyConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GestionTransaction implements IGestionTransaction<Transaction> {
    Connection             connection = MyConnection.getInstance().getCnx();




    public void ServiceTransaction() throws SQLException{
        try(Connection connection = MyConnection.getInstance().getCnx()){
            this.connection = connection;
        }
    }
    @Override
    public void ajouter(Transaction T) throws SQLException {
        try {

            connection = MyConnection.getInstance().getCnx();


            String sql = "INSERT INTO transaction (`id_transaction`, `rib`, `destination`, `montant_transaction`, `type_transaction`, `date_transaction`) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, T.getId());
            statement.setString(2, T.getSource());
            statement.setString(3, T.getDestination());
            statement.setDouble(4, T.getMontant());
            statement.setString(5,"VIREMENT");
            statement.setDate(6,new Date(java.util.Calendar.getInstance().getTimeInMillis()));

            statement.executeUpdate();
            statement.close();

            System.out.println("Transaction insérée avec succès.");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'insertion de la transaction : " + e.getMessage());
        }
    }


    @Override
    public void modifier(Transaction t) throws SQLException {
        String sql = "";
        PreparedStatement preparedStatement= connection.prepareStatement(sql);
        preparedStatement.executeUpdate();

    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "Delete from produit where IdProduit = ? ";
        PreparedStatement preparedStatement= connection.prepareStatement(sql);
        preparedStatement.setInt(1,id);
        preparedStatement.executeUpdate();

    }


    @Override
    public List<Transaction> afficher() {
        List<Transaction> transactions = new ArrayList<>();
        String req3 = "SELECT * FROM transaction";
        try {
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery(req3);
            while (rs.next()) {
                Transaction tran = new Transaction();
                tran.setId(rs.getInt("id_transaction"));
                tran.setSource(rs.getString("rib"));
                tran.setDestination(rs.getString("destination"));
                tran.setMontant(rs.getDouble("montant_transaction"));

                 transactions.add(tran);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return transactions;
    }
}
