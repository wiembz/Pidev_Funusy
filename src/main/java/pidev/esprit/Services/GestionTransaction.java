package pidev.esprit.Services;

import pidev.esprit.Entities.Transaction;
import pidev.esprit.Tools.MyConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GestionTransaction implements IGestionTransaction<Transaction> {
    private Connection connection;

    public void ServiceTransaction(){
        connection = MyConnection.getInstance().getCnx();
    }
    @Override
    public void ajouter(Transaction T) throws SQLException {
        try {
            String sql = "INSERT INTO produit (nomProduit, prixProduit, tailleProduit, QauntiteProduit) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.executeUpdate();
            statement.close();

            System.out.println("Produit inséré avec succès.");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'insertion du produit : " + e.getMessage());
        }

    }

    @Override
    public void modifier(Transaction t) throws SQLException {
        String sql = "Update produit set nomProduit = ?, prixProduit= ? , tailleProduit= ? , QauntiteProduit= ? where IdProduit = ?";
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
    public List<Transaction> afficher() throws SQLException {
        List<Transaction> produits= new ArrayList<>();
        String sql = "select * from produit";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()){
            Transaction t = new Transaction();
            produits.add(t);
        }
        return produits;
    }
}
