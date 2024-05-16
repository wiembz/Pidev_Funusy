package pidev.esprit.Controllers.Account;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import pidev.esprit.Entities.Compte;
import pidev.esprit.Entities.User;
import pidev.esprit.Services.CompteCrud;
import pidev.esprit.Tools.MyConnection;

import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;
import static javafx.scene.control.Alert.AlertType;
import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.Alert.AlertType.INFORMATION;
import static pidev.esprit.Services.CompteCrud.retrieveUserByRIB;
import static pidev.esprit.Services.CompteCrud.userExists;


public class AddAccountController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Compte> accountTable;

    @FXML
    private RadioButton bloque;

    @FXML
    private RadioButton courant;

    @FXML
    private TableColumn<Compte, Date> date_ouverture;

    @FXML
    private RadioButton epargne;

    @FXML
    private TableColumn<Compte, Integer> id_user;

    @FXML
    private TableColumn<Compte, String> rib;

    @FXML
    private TableColumn<Compte, Double> solde;

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
    private TableColumn<Compte, String> type_compte;

    @FXML
    private Button update_account;


    private CompteCrud comptecrud = new CompteCrud();
    private ObservableList<Compte> compteList = FXCollections.observableArrayList();

    public void ajouterCompte(Compte newCompte) {
        compteList.add(newCompte);
    }

    @FXML
    private void add_account(ActionEvent event) throws SQLException {
        Compte c = new Compte();
        int loggedInUserId = MyConnection.getInstance().getLoggedInUserId();
        c.setId_user(loggedInUserId);


        try {

//            try {
//                id_user = parseInt(tf_user.getText());
//            } catch (NumberFormatException e) {
//                Alert alert = new Alert(ERROR, "INVALID ID USER ", ButtonType.OK);
//                alert.showAndWait();
//                return;
//            }
//
//            // Check if the user exists
//            if (!userExists(id_user)) {
//                Alert alert = new Alert(ERROR, "Id user " + id_user + " doesn't exist 🙁 !! ", ButtonType.OK);
//                alert.showAndWait();
//                return;
//            }

//            if (CompteCrud.userExists(id_user)) {
//                Alert alert = new Alert(ERROR, "User with ID " + id_user + " already has an account!", ButtonType.OK);
//                alert.showAndWait();
//                return;
//            }
            double solde = Double.parseDouble(tf_solde.getText());
            validateSolde(solde); // Validate solde range

            // Get selected account type
            String selectedType = getSelectedType();
            // Create a new Compte object


            Compte compte = new Compte(Compte.generateRib(), solde, selectedType, loggedInUserId);
            String generatedRib = compte.getRib(); // Get the generated RIB
            tf_rib.setText(generatedRib);
            CompteCrud.add_account(compte, loggedInUserId, selectedType);

            c.setSolde(Double.parseDouble(tf_solde.getText()));
            c.setType_compte(getSelectedType());
            CompteCrud cc = new CompteCrud();
            cc.addCompte(c);
            ajouterCompte(c);
            initialize();
            accountTable.refresh();
            Alert alert = new Alert(INFORMATION, "Account added 🙂 !!", ButtonType.OK);
            alert.showAndWait();

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
        ObservableList<Compte> items = accountTable.getItems();

        // Clear previous selection
        accountTable.getSelectionModel().clearSelection();

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

        accountTable.getItems().setAll(filteredItems);
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
        accountTable.setEditable(true);
        int loggedInUserId = MyConnection.getInstance().getLoggedInUserId();

        User user = CompteCrud.retrieveUserById(loggedInUserId);
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



        id_user.setCellValueFactory(new PropertyValueFactory<Compte, Integer>("id_user"));
        rib.setCellValueFactory(new PropertyValueFactory<Compte, String>("rib"));
        solde.setCellValueFactory(new PropertyValueFactory<Compte, Double>("solde"));
        type_compte.setCellValueFactory(new PropertyValueFactory<Compte, String>("type_compte"));
        date_ouverture.setCellValueFactory(new PropertyValueFactory<Compte, Date>("date_ouverture"));
        CompteCrud cc = new CompteCrud();
        compteList.clear();
        compteList.addAll(cc.displayCompte());

        tf_search.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                refreshListView();

            } else {
                // Otherwise, perform the search
                searchListView(newValue);
            }
        });

        try {
            handleDelete(new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.DELETE, false, false, false, false));
        } catch (SQLException e) {
            // Handle SQL exception if necessary
            e.printStackTrace();
        }
        accountTable.setRowFactory(tv -> {
            TableRow<Compte> row = new TableRow<>();
            row.setOnMouseClicked(event2 -> {
                if (!row.isEmpty() && event2.getButton() == MouseButton.PRIMARY && event2.getClickCount() == 1) {
                    Compte selectedItem = row.getItem();
                    // Activer le bouton de suppression si une ligne est sélectionnée
                }
            });
            return row;
        });
        EditCells();
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

        List<Compte> accounts = CompteCrud.getInstance().displayCompte();


        accountTable.getItems().setAll(accounts);
    }

    @FXML
    private void handleDelete(KeyEvent event) throws SQLException {
        accountTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        if (event.getCode() == KeyCode.DELETE) {
            // Retrieve the selected item
            Compte selectedCompte = accountTable.getSelectionModel().getSelectedItem();

            // Check if an item is selected
            if (selectedCompte != null) {
                // Show confirmation dialog
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText("Delete Confirmation");
                alert.setContentText("Are you sure you want to delete the selected item?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    // Remove the selected item from the TableView
                    accountTable.getItems().remove(selectedCompte);

                    // Remove the selected item from the data source
                    CompteCrud.deleteCompte(selectedCompte.getRib());
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


    //
    @FXML
    private void updateAccount(ActionEvent event) {
        // Récupérer le RIB, le nouveau solde et le nouveau type des champs de texte
        String rib = tf_rib.getText();
        String nouveauSoldeText = tf_solde.getText();
        getSelectedType();

        // Vérifier si le RIB est vide
        if (rib.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "RIB manquant", "Veuillez entrer un RIB.");
            return;
        }

        // Vérifier si le solde est vide
        if (nouveauSoldeText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Solde manquant", "Veuillez entrer un nouveau solde.");
            return;
        }

        // Vérifier si le type est vide
        if (getSelectedType().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Type manquant", "Veuillez entrer un nouveau type.");
            return;
        }

        // Vérifier si le solde est un nombre valide
        double nouveauSolde;
        try {
            nouveauSolde = Double.parseDouble(nouveauSoldeText);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Solde invalide", "Veuillez entrer un solde valide.");
            return;
        }

        // Vérifier si le compte existe
        if (!compteCrud.accountExists(rib)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Compte inexistant", "Aucun compte trouvé avec ce RIB.");
            return;
        }

        // Mettre à jour le compte dans la base de données
        Compte compte = new Compte(rib, nouveauSolde, getSelectedType(), retrieveUserByRIB(rib).getId_user());
        CompteCrud cc = new CompteCrud();
        cc.updateCompte(compte);
    }

    private void EditCells() {
        // Type_compte
        solde.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));// Créez une cellule éditable pour les montants de crédit
        solde.setOnEditCommit(event -> {
            Double newValue = event.getNewValue();
            Compte selectedCompte = event.getTableView().getItems().get(event.getTablePosition().getRow());

            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation");
            confirmationAlert.setHeaderText("Change credit");
            confirmationAlert.setContentText("Are you sure you want to change this credit ?");
            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    selectedCompte.setSolde(newValue);
                    accountTable.refresh(); // Rafraîchir la TableView pour refléter les modifications
                    comptecrud.updateCompte(selectedCompte); // Enregistrer les modifications dans la base de données
                } else {
                    // Annuler la modification, ne rien faire
                }
            });
        });

        // Duree_credit
        type_compte.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<String>() {
            @Override
            public String toString(String object) {
                return object;
            }

            @Override
            public String fromString(String string) {
                return string;
            }
        }));

// Set up the event handler for edit commit action
        type_compte.setOnEditCommit(event -> {
            String newValue = event.getNewValue();
            Compte selectedCompte = event.getTableView().getItems().get(event.getTablePosition().getRow());

            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation");
            confirmationAlert.setHeaderText("Change type_compte");
            confirmationAlert.setContentText("Are you sure you want to change this type_compte?");

            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    selectedCompte.setType_compte(newValue);
                    accountTable.refresh(); // Refresh the TableView to reflect the modifications
                    comptecrud.updateCompte(selectedCompte); // Save the modifications to the database
                } else {
                    // Cancel the modification, do nothing
                    accountTable.refresh(); // Refresh the TableView to revert any changes made during editing
                }
            });
        });
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
            Compte updatedCompte = new Compte(rib, solde, selectedType, id_user);

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
    }


}