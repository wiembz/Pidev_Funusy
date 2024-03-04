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

import javax.sql.rowset.serial.SerialStruct;
import javax.xml.crypto.dsig.TransformService;
import java.io.IOException;
import java.net.URL;
import java.security.SecureRandom;
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

    public AddTransactionController() {
        gestionTransaction = new GestionTransaction();
    }


    private void showErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    void save_transaction(ActionEvent event) throws SQLException {
        if (tf_destination.getText().isEmpty() || !tf_destination.getText().matches("[A-Za-z ]+")) {
            showErrorDialog("Invalid destination", "Please enter a valid destination (letters and spaces only).");
            return;
        }

        try {
            double amount = Double.parseDouble(tf_amount.getText());
        } catch (NumberFormatException e) {
            showErrorDialog("Invalid amount", "Please enter a valid number for the amount.");
            return;
        }


        //sauvgarder de personne dans la BD
        Transaction p = new Transaction(Double.valueOf(tf_amount.getText()), "RIBTEST",tf_destination.getText(),4 );
        GestionTransaction transactionCrud = new GestionTransaction();
        transactionCrud.ajouter(p);
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Transaction saved successfully!", ButtonType.OK);
        alert.showAndWait();
        populateTransactionsTable();
    }




    private void populateTransactionsTable() throws Error{


        try {
            List<Transaction> transactions = gestionTransaction.afficher();
            ObservableList<Transaction> transactionsData = FXCollections.observableArrayList(transactions);

            idColumn.setCellValueFactory(new PropertyValueFactory<>("id")); // Assuming "id" is the ID field in Transaction
            sourceColumn.setCellValueFactory(new PropertyValueFactory<>("Source")); // Assuming "source" exists in Transaction
            amountColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("Destination"));

            transactionsTable.setItems(transactionsData);
        }catch (Error e){
            System.out.println(e);
        }
    }



    @FXML
    void initialize() {

        populateTransactionsTable();
     }
}
