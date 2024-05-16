package pidev.esprit.Controllers.Account;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import pidev.esprit.Entities.CarteBancaire;
import pidev.esprit.Entities.Compte;
import pidev.esprit.Entities.User;
import pidev.esprit.Services.CardCrud;
import pidev.esprit.Services.CompteCrud;
import pidev.esprit.Tools.MyConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class AddCardsController {

    @FXML
    private Button btndelete;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<CarteBancaire> card;

    @FXML
    private TableColumn<CarteBancaire, String> rib;

    @FXML
    private TableColumn<CarteBancaire, String> cardNumber;

    @FXML
    private TableColumn<CarteBancaire, Integer> code;

    @FXML
    private TableColumn<CarteBancaire, Integer> cvv2;

    @FXML
    private DatePicker date;

    @FXML
    private RadioButton tf_3y;

    @FXML
    private RadioButton tf_5y;

    @FXML
    private TextField tf_CVV2;

    @FXML
    private TextField tf_card;

    @FXML
    private TextField tf_code;

    @FXML
    private TextField tf_rib;

    @FXML
    private TextField tf_tel;
    @FXML
    private TableColumn<Compte, Date> dateExp;


    private ObservableList<CarteBancaire> cardList = FXCollections.observableArrayList();
    private Object actionEvent;

    public void ajouterCarte(CarteBancaire newCarte) {
        cardList.add(newCarte);
    }

    private int selectedDurationYears = 0;

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Erreur !! ");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static int duration; // Make duration static if you intend to access it from static methods

    public static void setDuration(int duration) {
        CardsController.setDuration(duration); // Access static variable directly within a static method
    }

    @FXML
    void Add_Card() throws SQLException {
        String rib = tf_rib.getText().trim();
        int loggedInUserId = MyConnection.getInstance().getLoggedInUserId();
        User user = CompteCrud.getInstance().retrieveUserById(loggedInUserId);
        if (user == null) {
            showAlert(Alert.AlertType.ERROR, "User not found for the provided RIB.");
            return;
        }

        // Generate card number
        String numCarte = CarteBancaire.generateNum();
        selectedDurationYears = 3;
        // Generate code and CVV2
        int code = CarteBancaire.generateCode();
        int cvv2 = CarteBancaire.generateCVV2();

        // Get current date
        java.sql.Date dateExp = java.sql.Date.valueOf(LocalDate.now());

        // Add the card to the database
        CarteBancaire carte = new CarteBancaire(numCarte, cvv2, code, dateExp, rib);
        String generatednum = carte.getNum_carte();
        CardCrud.getInstance().addCard(carte, rib);
        tf_card.setText(generatednum);
        tf_code.setText(String.valueOf(code));
        tf_CVV2.setText(String.valueOf(cvv2));

        // ajouterCarte(carte);
        initialize();
        card.refresh();

        // Display the generated card details


        showAlert(Alert.AlertType.INFORMATION, "Card added successfully.");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Card.fxml"));
        Parent root;
        try {
            root = loader.load();
            CardsController controller = loader.getController();
            controller.setCardAndUserDetails(carte, user, selectedDurationYears); // Pass both the card and user details
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        tf_card.setText("");
        tf_code.setText(String.valueOf(""));
        tf_CVV2.setText(String.valueOf(""));
    }

    @FXML
    void Delete_Card() {
        CarteBancaire selectedCard = card.getSelectionModel().getSelectedItem();
        if (selectedCard != null) {
            CardCrud.deleteCard(selectedCard.getNum_carte());
            cardList.remove(selectedCard);
            showAlert(Alert.AlertType.INFORMATION, "Card deleted successfully.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Please select a card to delete.");
        }
    }

    @FXML
    public void handleRadioButtonAction(ActionEvent actionEvent) {
        RadioButton selectedRadioButton = (RadioButton) actionEvent.getSource();

        // Deselect all radio buttons except the selected one
        if (selectedRadioButton == tf_3y) {
            tf_5y.setSelected(false);
        } else if (selectedRadioButton == tf_5y) {
            tf_3y.setSelected(false);
        }

        handleDuration(actionEvent);
    }


    @FXML
    void initialize() throws SQLException {

        card.setEditable(true);
        refreshListView();
        tf_rib.setOnAction(event -> onRIBFieldClicked());

        cardNumber.setCellValueFactory(new PropertyValueFactory<CarteBancaire, String>("num_carte"));
        code.setCellValueFactory(new PropertyValueFactory<CarteBancaire, Integer>("code"));
        cvv2.setCellValueFactory(new PropertyValueFactory<CarteBancaire, Integer>("CVV2"));
        rib.setCellValueFactory(new PropertyValueFactory<CarteBancaire, String>("rib"));
        dateExp.setCellValueFactory(new PropertyValueFactory<Compte, Date>("date_exp"));
        card.setRowFactory(tv -> {
            TableRow<CarteBancaire> row = new TableRow<>();
            row.setOnMouseClicked(event2 -> {
                if (!row.isEmpty() && event2.getButton() == MouseButton.PRIMARY && event2.getClickCount() == 1) {
                    CarteBancaire selectedItem = row.getItem();
                    // Activer le bouton de suppression si une ligne est sélectionnée
                    btndelete.setDisable(false);
                }
            });
            return row;
        });


        CardCrud cc = new CardCrud();
        cardList.clear();
        // cardList.addAll(cc.displayCard());
        CardCrud.sendExpirationReminders();


    }


    @FXML
    void onRIBFieldClicked() {
        String rib = tf_rib.getText().trim();
        User user = CompteCrud.retrieveUserByRIB(rib);
        if (user != null) {
            tf_tel.setText(String.valueOf(user.getTel()));

            // Generate card details
            String numCarte = CarteBancaire.generateNum();
            int code = CarteBancaire.generateCode();
            int cvv2 = CarteBancaire.generateCVV2();

            // Display generated card details
            tf_card.setText(numCarte);
            tf_code.setText(String.valueOf(code));
            tf_CVV2.setText(String.valueOf(cvv2));

            // Add the generated card to the database
            java.sql.Date dateExp = java.sql.Date.valueOf(LocalDate.now());
            CarteBancaire carte = new CarteBancaire(numCarte, cvv2, code, dateExp, rib);
            CardCrud.getInstance().addCard(carte, rib);
        } else {

        }
    }

    @FXML
    private void handleDuration(ActionEvent event) {
        RadioButton selectedRadioButton = (RadioButton) event.getSource();
        LocalDate currentDate = LocalDate.now(); // Get the current date

        if (selectedRadioButton == tf_3y) {
            // Add 3 years to the current date
            LocalDate expirationDate = currentDate.plusYears(3);
            date.setValue(expirationDate);
            selectedDurationYears = 3;
        } else if (selectedRadioButton == tf_5y) {
            // Add 5 years to the current date
            LocalDate expirationDate = currentDate.plusYears(5);
            date.setValue(expirationDate);
            selectedDurationYears = 5;
        }
    }


    @FXML
    private void handleDelete(KeyEvent event) throws SQLException {
        // Check if the Delete key was pressed
        if (event.getCode() == KeyCode.DELETE) {
            // Retrieve the selected item from the TableView
            CarteBancaire selectedCarte = card.getSelectionModel().getSelectedItem();

            // Check if an item is selected
            if (selectedCarte != null) {
                // Show a confirmation dialog
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("Delete Card?");
                alert.setContentText("Are you sure you want to delete this Card? " + selectedCarte.getNum_carte());

                // Handle the user's response
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    // User clicked OK, proceed with deletion

                    // Remove the selected item from the TableView
                    card.getItems().remove(selectedCarte);

                    // Update your data source (e.g., database) to reflect the deletion
                    CardCrud.deleteCard(selectedCarte.getNum_carte());
                }
            }
        }
    }


    @FXML
    private void refreshListView() {
        // Fetch accounts from the database using CompteCrud singleton instance
        List<CarteBancaire> cartes = CardCrud.getInstance().displayCard();

        // Populate the ListView with the fetched accounts
        card.getItems().setAll(cartes);
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