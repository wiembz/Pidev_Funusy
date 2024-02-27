package pidev.esprit.Controllers.Transaction;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import pidev.esprit.Entities.Transaction;
import pidev.esprit.Services.GestionTransaction;

import javax.xml.crypto.dsig.TransformService;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class AddTransactionController {


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;


    @FXML
    private Button send_money;

    @FXML
    private TextField tf_destination;

    @FXML
    private TextField tf_amount;
    @FXML
    private TableView<Transaction> transactions_tablefx;

    @FXML
    private TableView<Transaction> table;
    @FXML
    private TableView<Transaction> transactionsTable;

    @FXML
    private TableColumn<Transaction, Integer> idColumn;

    @FXML
    private TableColumn<Transaction, Integer> sourceColumn;

    @FXML
    private TableColumn<Transaction, Double> amountColumn;

    @FXML
    private TableColumn<Transaction, Date> dateColumn;
    @FXML

    private GestionTransaction gestionTransaction;

    @FXML
    void save_transaction(ActionEvent event) throws SQLException {
        //sauvgarder de personne dans la BD
        Transaction p = new Transaction(Double.valueOf(tf_amount.getText()), tf_destination.getText(),"RIBTEST",4 );
        GestionTransaction transactionCrud = new GestionTransaction();
        transactionCrud.ajouter(p);
        Alert alert1 = new Alert(Alert.AlertType.INFORMATION, "SUCCES ! Personne ajouté", ButtonType.OK);
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Transaction saved successfully!", ButtonType.OK);
        alert.showAndWait();
    }



    private void populateTransactionsTable() {
        List<Transaction> transactions = gestionTransaction.afficher();
        ObservableList<Transaction> transactionsData = FXCollections.observableArrayList(transactions);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id")); // Assuming "id" is the ID field in Transaction
        sourceColumn.setCellValueFactory(new PropertyValueFactory<>("source")); // Assuming "source" exists in Transaction
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateTransaction"));

        transactionsTable.setItems(transactionsData);
    }



    @FXML
    void initialize() {

        populateTransactionsTable();
     }
}
