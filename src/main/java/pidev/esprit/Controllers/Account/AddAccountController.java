package pidev.esprit.Controllers.Account;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;
import pidev.esprit.entities.Compte;
import pidev.esprit.services.CompteCrud;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import static pidev.esprit.services.CompteCrud.deleteCompte;

public class AddAccountController {

    @FXML
    private RadioButton bloque;

    @FXML
    private RadioButton courant;

    @FXML
    private RadioButton epargne;

    @FXML
    private TextField tf_rib;


    @FXML
    private TextField tf_solde;

    @FXML
    private Button update_account;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private ListView<Compte> accountListView;


    @FXML
    private void add_account() throws SQLException {
        try {
            // Parse input values and perform validation
            String rib = tf_rib.getText();
            validateRIB(rib); // Validate RIB length and format

            double solde = Double.parseDouble(tf_solde.getText());
            validateSolde(solde); // Validate solde range

            // Get selected account type
            String selectedType = getSelectedType();

            // Create a new Compte object
            Compte compte = new Compte(rib, solde, selectedType);


            // Add the compte to the database
            CompteCrud compteCrud = new CompteCrud();
            CompteCrud.add_account(compte, selectedType);
            ObservableList<Compte> items = accountListView.getItems();
            items.add(compte);
            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Compte ajouté avec succès !", ButtonType.OK);
            alert.showAndWait();

            // Clear input fields
            tf_rib.setText("");
            tf_solde.setText("");
            epargne.setSelected(true); // Assuming this sets a default account type

        } catch (NumberFormatException e) {
            // Handle invalid solde input
            Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez saisir un solde valide (ex: 123.45).", ButtonType.OK);
            alert.showAndWait();
        } catch (IllegalArgumentException e) {
            // Handle other validation errors
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }


    private void validateRIB(String rib) {
        if (rib.length() != 20 || !rib.matches("^[0-9]+$")) {
            throw new IllegalArgumentException("RIB must be 20 digits long and contains only numbers.");
        }
    }

    private void validateSolde(double solde) {
        if (solde < 0) {
            throw new IllegalArgumentException("Solde must be non-negative.");
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
        String selectedType;
        if (epargne.isSelected()) {
            selectedType = "epargne";
        } else if (courant.isSelected()) {
            selectedType = "courant";
        } else if (bloque.isSelected()) {
            selectedType = "bloque";
        }
    }




    @FXML
    private void initialize() throws SQLException {
        epargne.setSelected(true);

        refreshListView();
        accountListView.setEditable(true);
        accountListView.setCellFactory(TextFieldListCell.forListView(new StringConverter<Compte>() {
            @Override
            public String toString(Compte compte) {
                return compte.toString();
            }

            @Override
            public Compte fromString(String string) {
                // Not needed for ListView
                return null;
            }
        }));

        // Handle edit commit events
        accountListView.setOnEditCommit(event -> {
            Compte editedCompte = event.getNewValue();
            try {
                CompteCrud.getInstance().updateCompte(editedCompte);
                // Optionally, refresh the ListView to reflect the changes
                refreshListView();
            } catch (SQLException e) {
                // Handle database update error
                e.printStackTrace();
                // Optionally, display an error message to the user
            }
        });
    }



    private CompteCrud compteCrud = new CompteCrud();
    @FXML
    private void displayAccounts() throws SQLException {
        // Fetch accounts from CompteCrud
        List<Compte> accounts = compteCrud.displayCompte(); // Assuming this method exists

        // Clear existing items
        accountListView.getItems().clear();

        // Create a cell factory to customize how each cell is displayed
        accountListView.setCellFactory(param -> new ListCell<Compte>() {
            @Override
            protected void updateItem(Compte item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    // Customize how each account is displayed here
                    setText("Account Details: " + item.toString());
                }
            }
        });

        // Add accounts to the ListView
        accountListView.getItems().addAll(accounts);
    }

    // Other methods...






    @FXML
    private void handleEditCommit(ListView.EditEvent<Compte> event) throws SQLException {
        Compte editedCompte = event.getNewValue();

        compteCrud.updateCompte(editedCompte);
        System.out.println("Compte updated successfully!");

        ObservableList<Compte> accounts = FXCollections.observableArrayList(compteCrud.displayCompte());
        accountListView.setItems(accounts);
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
            try {
                validateSolde(solde); // Assuming validateSolde throws an exception on invalid input
            } catch (IllegalArgumentException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alert.showAndWait();
                return; // Prevent update if validation fails
            }

            String selectedType = getSelectedType(); // Assuming you have a method to get the selected account type

            // Create a Compte object with the updated values
            Compte updatedCompte = new Compte(rib, solde, selectedType);

            // Call the updateCompte method in your service class (e.g., CompteCrud) to update the account
            compteCrud.updateCompte(updatedCompte);

            // Show a confirmation message
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "Compte mis à jour avec succès !", ButtonType.OK);
            alert2.showAndWait();

            // Clear input fields
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


    @FXML
    public void Delete_account(ActionEvent event) {
        String rib = tf_rib.getText();

        if (rib.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", null, "Veuillez saisir un RIB pour supprimer le compte.");
            return;
        }

        CompteCrud compteCrud = new CompteCrud();
        if (!compteCrud.accountExists(rib)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", null, "Aucun compte trouvé avec le RIB " + rib + ".");
            return;
        }

        Optional<ButtonType> result = showAlert(Alert.AlertType.CONFIRMATION, "Confirmation", "Confirmation",
                "Êtes-vous sûr de vouloir supprimer le compte avec le RIB " + rib + "?");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            deleteCompte(rib);
            showAlert(Alert.AlertType.INFORMATION, "Succès", null, "Compte supprimé avec succès !");
            tf_rib.clear();
        } else {
            showAlert(Alert.AlertType.INFORMATION, null, null, "Suppression annulée.");
        }

    }

    private Optional<ButtonType> showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        return alert.showAndWait();
    }




    @FXML
    private void deleteAccount() {
        // Get the selected account from the ListView

        Compte selectedAccount = accountListView.getSelectionModel().getSelectedItem();
        if (selectedAccount != null) {
            // Perform delete operation using CompteCrud
            try {
                CompteCrud.getInstance().deleteCompte(selectedAccount.getRib());
                refreshListView(); // Refresh ListView after deletion
            } catch (SQLException e) {
                e.printStackTrace(); // Handle the exception appropriately
            }
        }
    }

//    @FXML
//    private void refreshListView() throws SQLException {
//        // Fetch accounts from the database using CompteCrud singleton instance
//        List<Compte> accounts = CompteCrud.getInstance().displayCompte();
//
//        // Clear existing items in the ListView
//        accountListView.getItems().clear();
//
//        // Populate the ListView with the fetched accounts
//        accountListView.getItems().addAll(accounts);
//    }
@FXML private void refreshListView() throws SQLException {
    // Fetch accounts from the database using CompteCrud singleton instance
    List<Compte> accounts = CompteCrud.getInstance().displayCompte();

    // Populate the ListView with the fetched accounts
    accountListView.getItems().setAll(accounts);}

    @FXML
    private void handleDelete(KeyEvent event) throws SQLException {
        accountListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        if (event.getCode() == KeyCode.DELETE) {
            // Retrieve the selected item
            Compte selectedCompte = accountListView.getSelectionModel().getSelectedItem();

            // Check if an item is selected
            if (selectedCompte != null) {
                // Remove the selected item from the ListView
                accountListView.getItems().remove(selectedCompte);

                // Update your data source (e.g., database) to reflect the deletion
                compteCrud.deleteCompte(selectedCompte.getRib());
            }
        }
    }
        @FXML
        private void handleKeyPressed(KeyEvent event) {
            if (event.getCode() == KeyCode.DELETE) {
                try {
                    handleDelete(event);
                } catch (SQLException e) {
                    e.printStackTrace(); // Handle the exception appropriately
                }
            }


    }




}




