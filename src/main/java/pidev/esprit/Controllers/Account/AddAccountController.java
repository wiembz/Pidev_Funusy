package pidev.esprit.Controllers.Account;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import pidev.esprit.entities.User;
import pidev.esprit.services.CompteCrud;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static pidev.esprit.services.CompteCrud.deleteCompte;
import static pidev.esprit.services.CompteCrud.userExists;
import javafx.scene.control.DatePicker;


public class AddAccountController {

    @FXML
    private RadioButton bloque;

    @FXML
    private RadioButton courant;

    @FXML
    private RadioButton epargne;

    @FXML
    private TextField tf_adr;
    @FXML
    private TextField tf_search;

    @FXML
    private TextField tf_cin;

    @FXML
    private TextField tf_nom;

    @FXML
    private TextField tf_prenom;

    @FXML
    private TextField tf_rib;

    @FXML
    private TextField tf_solde;

    @FXML
    private TextField tf_tel;

    @FXML
    private TextField tf_user;
    @FXML
    private DatePicker tf_date;


    @FXML
    private Button update_account;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private ListView<Compte> accountListView;



    @FXML
    private void add_account() throws SQLException {
        int id_user = Integer.parseInt(tf_user.getText());
        try {

            try {
                id_user = Integer.parseInt(tf_user.getText());
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez saisir un ID utilisateur valide.", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            // Check if the user exists
            if (!userExists(id_user)) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Id user " + id_user + " doesn't exist :( !! ", ButtonType.OK);
                alert.showAndWait();
                return;
            }
            double solde = Double.parseDouble(tf_solde.getText());
            validateSolde(solde); // Validate solde range

            // Get selected account type
            String selectedType = getSelectedType();
            // Create a new Compte object

            Compte compte = new Compte(Compte.generateRib(), solde, selectedType,id_user);
            // Add the compte to the database
            CompteCrud compteCrud = new CompteCrud();
            tf_rib.setText(compte.getRib());
            CompteCrud.add_account(compte,id_user, selectedType);
            ObservableList<Compte> items = accountListView.getItems();
            items.add(compte);

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Account added :) !!", ButtonType.OK);
            alert.showAndWait();

            // Clear input fields    // Assuming this sets a default account type

        } catch (NumberFormatException e) {
            // Handle invalid solde input
            Alert alert = new Alert(Alert.AlertType.ERROR, "Balance should be positive ! ", ButtonType.OK);
            alert.showAndWait();
        } catch (IllegalArgumentException e) {
            // Handle other validation errors
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

//    @FXML
//    private void add_account() throws SQLException {
//        try {
//            // Create a new instance of Compte
//            Compte compte = new Compte();
//
//            // Generate RIB using the instance method from Compte class
//            String rib = compte.generateRib();
//
//            // Parse input values and perform validation
//            double solde = Double.parseDouble(tf_solde.getText());
//            validateSolde(solde); // Validate solde range
//            tf_rib.setText(rib);
//
//            // Make the TextField visible
//
//            // Parse id_user from the text field
//            int id_user = Integer.parseInt(tf_user.getText());
//
//            // Get selected account type
//            String selectedType = getSelectedType();
//
//            // Initialize the Compte object with the generated RIB and other values
//            compte.setRib(rib);
//            compte.setSolde(solde);
//            compte.setType_compte(selectedType);
//            compte.setId_user(id_user);  // Assuming setId_user method exists in Compte class
//
//            // Add the compte to the database
//            CompteCrud compteCrud = new CompteCrud();
//            compteCrud.addCompte(compte);
//
//            ObservableList<Compte> items = accountListView.getItems();
//            items.add(compte);
//
//            // Show success message
//            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Compte ajouté avec succès !", ButtonType.OK);
//            alert.showAndWait();
//
//            // Clear input fields
//            tf_solde.setText("");
//            tf_user.setText("");
//            epargne.setSelected(true); // Assuming this sets a default account type
//
//        } catch (NumberFormatException e) {
//            // Handle invalid input (either solde or id_user)
//            Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez saisir un nombre valide.", ButtonType.OK);
//            alert.showAndWait();
//        } catch (IllegalArgumentException e) {
//            // Handle other validation errors
//            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
//            alert.showAndWait();
//        }
//    }





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


    private void searchListView(String query) {
        ObservableList<Compte> items = accountListView.getItems();

        // Clear previous selection
        accountListView.getSelectionModel().clearSelection();

        // Create a new list to hold the filtered items
        ObservableList<Compte> filteredItems = FXCollections.observableArrayList();

        // Iterate through all accounts to find matches
        for (Compte item : items) {
            // Perform search based on the query
            if (item.getRib().startsWith(query)) {
                // Add matching items to the filtered list
                filteredItems.add(item);
            }
        }

        // Clear the ListView and add the filtered items
        accountListView.getItems().setAll(filteredItems);
    }

    private void handleSearchInput(KeyEvent event) {
        // Get the search query from the text field or other input
        String query = tf_search.getText(); // Adjust this to match your actual text field

        // Call the search method with the current query
        searchListView(query);
    }

    @FXML
    private void initialize() throws SQLException {

        epargne.setSelected(true);
        handleRadioButtonAction();
        refreshListView();
        // Assuming your ListView is named accountListView and your data model is a list of Compte objects
        accountListView.setEditable(true); // Enable editing for the ListView

// Set a custom cell factory to allow editing of the ListView cells
        accountListView.setCellFactory(TextFieldListCell.forListView(new StringConverter<Compte>() {
            @Override
            public String toString(Compte compte) {
                return compte.toString(); // Convert Compte object to string representation
            }

            @Override
            public Compte fromString(String string) {
                // Implement conversion from string to Compte if needed (not necessary for basic editing)
                return null;
            }
        }));

// Handle the edit commit event to update the edited Compte object
        // Handle the edit commit event to update the edited Compte object
        accountListView.setOnEditCommit(event -> {
            // Get the edited item
            Compte editedItem = event.getNewValue();

            // Get the index of the edited item
            int editedIndex = event.getIndex();

            // Update the item in the ListView
            accountListView.getItems().set(editedIndex, editedItem);

            // Update the item in the database

        });

// Handle the edit commit event to update the edited Compte object



        tf_user.textProperty().addListener((observableValue, s, t1) -> {
            try {
                int userId = Integer.parseInt(t1);
                // If valid, retrieve user information and populate fields
                User user = CompteCrud.retrieveUserById(userId);
                if (user != null) {
                    tf_nom.setText(user.getNom_user());
                    tf_prenom.setText(user.getPrenom_user());
                    tf_cin.setText(String.valueOf(user.getCIN()));
                    tf_tel.setText(String.valueOf(user.getTel()));
                    tf_adr.setText(user.getAdresse_user());

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "User not found!! The entered ID does not exist.", ButtonType.OK);
                    alert.showAndWait();
                }
            } catch (NumberFormatException e) {
                // Clear fields if invalid input

            }
        });

        tf_date.setValue(LocalDate.parse(LocalDate.now().toString()));

        tf_search.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                // If the search field is empty, refresh the list
                refreshListView();
            } else {
                // Otherwise, perform the search
                searchListView(newValue);
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
            int id_user = Integer.parseInt(tf_user.getText());
            // Create a Compte object with the updated values
            Compte updatedCompte = new Compte( rib,solde, selectedType,id_user);

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
            refreshListView();
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

                CompteCrud.getInstance().deleteCompte(selectedAccount.getRib());
                refreshListView(); // Refresh ListView after deletion

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
@FXML private void refreshListView() {
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
                CompteCrud.deleteCompte(selectedCompte.getRib());

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




