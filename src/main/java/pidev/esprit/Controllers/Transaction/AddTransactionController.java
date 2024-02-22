package pidev.esprit.Controllers.Transaction;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import pidev.esprit.Entities.Transaction;
import pidev.esprit.Services.GestionTransaction;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddTransactionController {


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;


    @FXML
    private Button submit_transaction;

    @FXML
    private TextField tf_destination;

    @FXML
    private TextField tf_amount;

    @FXML
    private TextField tf_source;

    @FXML
    void save_transaction(ActionEvent event) {
//sauvgarder de personne dans la BD
     //   Transaction p = new Transaction(tf_amount.getText(), tf_destination.getText(),"48949849849","fdfdf");
        GestionTransaction transactionCrud = new GestionTransaction();
     //   transactionCrud.ajouter(p);
        Alert alert1 = new Alert(Alert.AlertType.INFORMATION, "SUCCES ! Personne ajouté", ButtonType.OK);

    }

    @FXML
    void initialize() {

    }
}
