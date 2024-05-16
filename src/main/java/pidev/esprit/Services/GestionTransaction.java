package pidev.esprit.Services;

import pidev.esprit.Entities.Transaction;
import pidev.esprit.Tools.MyConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GestionTransaction implements IGestionTransaction<Transaction> {
    Connection connection = MyConnection.getInstance().getCnx();




    public void ServiceTransaction() throws SQLException{
        try(Connection connection = MyConnection.getInstance().getCnx()){
            this.connection = connection;
        }
    }
    @Override
    public void ajouter(Transaction T) throws SQLException {
        try {
            connection = MyConnection.getInstance().getCnx();

            String sqlInsert = "INSERT INTO `transaction` (`id_transaction`, `rib`, `destination`, `montant_transaction`, `type_transaction`, `date_transaction`) VALUES (?, ?, ?, ?, ? ,?) ON DUPLICATE KEY UPDATE `date_transaction` = VALUES(`date_transaction`), `type_transaction` = VALUES(`type_transaction`), `montant_transaction` = VALUES(`montant_transaction`), `rib` = VALUES(`rib`);";
            PreparedStatement statementInsert = connection.prepareStatement(sqlInsert);

            statementInsert.setInt(1, T.getId());
            statementInsert.setString(2, T.getSource());
            statementInsert.setString(3, T.getDestination());
            statementInsert.setDouble(4, T.getMontant());
            statementInsert.setString(5, "VIREMENT");
            statementInsert.setDate(6, new Date(java.util.Calendar.getInstance().getTimeInMillis()));

            statementInsert.executeUpdate();
            statementInsert.close();

            String sqlUpdate1 = "UPDATE `compte` SET `solde` = (`solde` + ?) WHERE `rib` = ?;";
            PreparedStatement statementUpdate1 = connection.prepareStatement(sqlUpdate1);

            statementUpdate1.setDouble(1, T.getMontant());
            statementUpdate1.setString(2, T.getDestination());

            statementUpdate1.executeUpdate();

            statementUpdate1.close();String sqlUpdate2 = "UPDATE `compte` SET `solde` = (`solde` - ?) WHERE `rib` = ?;";
            PreparedStatement statementUpdate2 = connection.prepareStatement(sqlUpdate2);

            statementUpdate2.setDouble(1, T.getMontant());
            statementUpdate2.setString(2, T.getSource());

            statementUpdate2.executeUpdate();
            statementUpdate2.close();

            System.out.println("Transaction insérée avec succès.");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'insertion de la transaction : " + e.getMessage());
        }
    }


    @Override
    public void modifier(Transaction t) throws SQLException {
        String sql = "UPDATE transaction SET montant_transaction = ?, destination = ?, rib = ? WHERE id_transaction = ?";
        PreparedStatement preparedStatement= connection.prepareStatement(sql);
        preparedStatement.setDouble(1,t.getMontant());
        preparedStatement.setString(2,t.getDestination());
        preparedStatement.setString(3, t.getSource());
        preparedStatement.setInt(4,t.getId());
        preparedStatement.executeUpdate();

    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "Delete from transaction where id_transaction = ? ";
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
    public boolean EntiteExists(Transaction t) {
        String query = "SELECT COUNT(*) FROM transaction WHERE id_transaction = ? ";
        try {
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, t.getId());
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
}
