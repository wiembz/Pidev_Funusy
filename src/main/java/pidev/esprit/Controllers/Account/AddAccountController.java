package pidev.esprit.Controllers.Account;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import pidev.esprit.entities.Compte;
import pidev.esprit.services.CompteCrud;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AddAccountController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private RadioButton epargne;

    @FXML
    private TextField tf_rib;

    @FXML
    private TextField tf_solde;

    @FXML
    private RadioButton bloque;

    @FXML
    private RadioButton courant;

    @FXML
    private BorderPane mainBorderPane;

    private String selectedType; // Variable to store selected type

    @FXML
    private void add_account() {
        try {
            // Parse input values
            double solde = Double.parseDouble(tf_solde.getText());
            String rib = tf_rib.getText();

            // Get selected account type
            String selectedType = getSelectedType();

            // Create a new Compte object with the parsed values and selected account type
            Compte compte = new Compte(rib, solde, selectedType);

            // Add the compte to the database
            CompteCrud compteCrud = new CompteCrud(); // Create an instance of CompteCrud
            compteCrud.add_account(compte, selectedType);

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Compte ajouté avec succès !", ButtonType.OK);
            alert.showAndWait();
            tf_rib.setText("");
            tf_solde.setText("");
            epargne.setSelected(true);
        } catch (NumberFormatException e) {
            // Handle invalid input
            Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez saisir un solde valide.", ButtonType.OK);
            alert.showAndWait();

        }
    }

    private String getSelectedType() {
        if (epargne.isSelected()) {
            return "epargne";
        } else if (courant.isSelected()) {
            return "courant";
        } else if (bloque.isSelected()) {
            return "bloque";
        }
        // Default to epargne if no selection
        return "epargne";
    }

    @FXML
    private void handleRadioButtonAction() {
        if (epargne.isSelected()) {
            selectedType = "epargne";
        } else if (courant.isSelected()) {
            selectedType = "courant";
        } else if (bloque.isSelected()) {
            selectedType = "bloque";
        }
    }

    @FXML
    private void initialize() {
        // Add event handlers to each radio button
        epargne.setSelected(true);
    }

    @FXML
    private void handleRadioButtonAction(ActionEvent event) {
        RadioButton selectedRadioButton = (RadioButton) event.getSource();

        // Deselect all radio buttons except the selected one
        if (selectedRadioButton == epargne) {
            courant.setSelected(false);
            bloque.setSelected(false);
        } else if (selectedRadioButton == courant) {
            epargne.setSelected(false);
            bloque.setSelected(false);
        } else if (selectedRadioButton == bloque) {
            epargne.setSelected(false);
            courant.setSelected(false);
        }
    }

    @FXML
    private void update_account(ActionEvent event) {
        // Retrieve the RIB from the form field
        String rib = tf_rib.getText();

        // Create an instance of CompteCrud
        CompteCrud compteCrud = new CompteCrud();

        // Check if the account exists in the database
        if (compteCrud.accountExists(rib)) {
            // If the account exists, proceed with the update process

            // Retrieve the updated values from the form fields
            double solde = Double.parseDouble(tf_solde.getText());
            String selectedType = getSelectedType(); // Assuming you have a method to get the selected account type

            // Create a Compte object with the updated values
            Compte updatedCompte = new Compte(rib, solde, selectedType);

            // Call the updateCompte method in your service class (e.g., CompteCrud) to update the account
            compteCrud.updateCompte(updatedCompte);

            // Show a confirmation message
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Compte mis à jour avec succès !", ButtonType.OK);
            alert.showAndWait();
            alert.showAndWait();
            tf_rib.setText("");
            tf_solde.setText("");
            epargne.setSelected(true);
            bloque.setSelected(false);
            courant.setSelected(false);
        } else {
            // If the account doesn't exist, display an error message
            Alert alert = new Alert(Alert.AlertType.ERROR, "Le compte avec le RIB spécifié n'existe pas.", ButtonType.OK);
            alert.showAndWait();
        }
    }



    public void handleAccountsButtonClick(javafx.event.ActionEvent actionEvent) {

        try {
            Parent GestionAccount = FXMLLoader.load(getClass().getResource("/AddAccounts.fxml"));

            mainBorderPane.setCenter(GestionAccount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void loadAccount(javafx.event.ActionEvent actionEvent) {

        try {
            Parent AddAccounts = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/DeleteAccount.fxml")));

            mainBorderPane.setCenter(AddAccounts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
