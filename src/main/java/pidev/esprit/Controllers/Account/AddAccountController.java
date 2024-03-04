package pidev.esprit.Controllers.Account;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;
import pidev.esprit.entities.Compte;
import pidev.esprit.entities.User;
import pidev.esprit.services.CompteCrud;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static java.lang.Integer.parseInt;
import static javafx.scene.control.Alert.*;
import static javafx.scene.control.Alert.AlertType.*;
import static pidev.esprit.services.CompteCrud.retrieveUserByRIB;
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
    private BorderPane mainBorderPane;
    @FXML
    private ListView<Compte> accountListView;


    @FXML
    private void add_account() throws SQLException {
        int id_user = parseInt(tf_user.getText());
        try {

            try {
                id_user = parseInt(tf_user.getText());
            } catch (NumberFormatException e) {
                Alert alert = new Alert(ERROR, "INVALID ID USER ", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            // Check if the user exists
            if (!userExists(id_user)) {
                Alert alert = new Alert(ERROR, "Id user " + id_user + " doesn't exist :( !! ", ButtonType.OK);
                alert.showAndWait();
                return;
            }
            double solde = Double.parseDouble(tf_solde.getText());
            validateSolde(solde); // Validate solde range

            // Get selected account type
            String selectedType = getSelectedType();
            // Create a new Compte object

            Compte compte = new Compte(Compte.generateRib(), solde, selectedType, id_user);
            // Add the compte to the database
            CompteCrud compteCrud = new CompteCrud();
            tf_rib.setText(compte.getRib());
            CompteCrud.add_account(compte, id_user, selectedType);
            ObservableList<Compte> items = accountListView.getItems();
            items.add(compte);

            // Show success message
            Alert alert = new Alert(INFORMATION, "Account added :) !!", ButtonType.OK);
            alert.showAndWait();

            // Clear input fields    // Assuming this sets a default account type

        } catch (NumberFormatException e) {
            // Handle invalid solde input
            Alert alert = new Alert(ERROR, "Balance should be positive ! ", ButtonType.OK);
            alert.showAndWait();
        } catch (IllegalArgumentException e) {
            // Handle other validation errors
            Alert alert = new Alert(ERROR, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
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

        tf_user.textProperty().addListener((observableValue, s, t1) -> {
            try {
                int userId = parseInt(t1);
                // If valid, retrieve user information and populate fields
                User user = CompteCrud.retrieveUserById(userId);
                if (user != null) {
                    tf_nom.setText(user.getNom_user());
                    tf_prenom.setText(user.getPrenom_user());
                    tf_cin.setText(String.valueOf(user.getCIN()));
                    tf_tel.setText(String.valueOf(user.getTel()));
                    tf_adr.setText(user.getAdresse_user());

                } else {
                    Alert alert = new Alert(ERROR, "User not found!! The entered ID does not exist.", ButtonType.OK);
                    alert.showAndWait();
                }
            } catch (NumberFormatException e) {

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


    // Method to refresh the ListView

    private Optional<ButtonType> showAlert(AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        return alert.showAndWait();
    }


    @FXML
    private void refreshListView() {
        // Fetch accounts from the database using CompteCrud singleton instance
        List<Compte> accounts = CompteCrud.getInstance().displayCompte();

        // Populate the ListView with the fetched accounts
        accountListView.getItems().setAll(accounts);
    }

    @FXML
    private void handleDelete(KeyEvent event) throws SQLException {
        accountListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        if (event.getCode() == KeyCode.DELETE) {
            // Retrieve the selected item
            Compte selectedCompte = accountListView.getSelectionModel().getSelectedItem();

            // Check if an item is selected
            if (selectedCompte != null) {
                // Show confirmation dialog
                Alert alert = new Alert(CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText("Delete Confirmation");
                alert.setContentText("Are you sure you want to delete the selected item?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    accountListView.getItems().remove(selectedCompte);


                    CompteCrud.deleteCompte(selectedCompte.getRib());
                } else {

                }
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




//    @FXML
//    private void updateAccount(ActionEvent event) {
//        // Récupérer le RIB, le nouveau solde et le nouveau type des champs de texte
//        String rib = tf_rib.getText();
//        String nouveauSoldeText = tf_solde.getText();
//      getSelectedType();
//
//        // Vérifier si le RIB est vide
//        if (rib.isEmpty()) {
//            showAlert(Alert.AlertType.ERROR, "Erreur", "RIB manquant", "Veuillez entrer un RIB.");
//            return;
//        }
//
//        // Vérifier si le solde est vide
//        if (nouveauSoldeText.isEmpty()) {
//            showAlert(Alert.AlertType.ERROR, "Erreur", "Solde manquant", "Veuillez entrer un nouveau solde.");
//            return;
//        }
//
//        // Vérifier si le type est vide
//        if (getSelectedType().isEmpty()) {
//            showAlert(Alert.AlertType.ERROR, "Erreur", "Type manquant", "Veuillez entrer un nouveau type.");
//            return;
//        }
//
//        // Vérifier si le solde est un nombre valide
//        double nouveauSolde;
//        try {
//            nouveauSolde = Double.parseDouble(nouveauSoldeText);
//        } catch (NumberFormatException e) {
//            showAlert(Alert.AlertType.ERROR, "Erreur", "Solde invalide", "Veuillez entrer un solde valide.");
//            return;
//        }
//
//        // Vérifier si le compte existe
//        if (!compteCrud.accountExists(rib)) {
//            showAlert(Alert.AlertType.ERROR, "Erreur", "Compte inexistant", "Aucun compte trouvé avec ce RIB.");
//            return;
//        }
//
//        // Mettre à jour le compte dans la base de données
//        Compte compte = new Compte(rib, nouveauSolde, getSelectedType(),retrieveUserByRIB(rib).getId_user());
//       CompteCrud cc=new CompteCrud();
//       cc.updateCompte(compte);
//    }
//}

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
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "Account updated successfully !", ButtonType.OK);
            alert2.showAndWait();

            // Clear input fields
            tf_rib.setText("");
            tf_solde.setText("");
            epargne.setSelected(true);
            bloque.setSelected(false);
            courant.setSelected(false);
        } else {
            // If the account doesn't exist, display an error message
            Alert alert = new Alert(Alert.AlertType.ERROR, "RIB not found ", ButtonType.OK);
            alert.showAndWait();
        }
    }}




