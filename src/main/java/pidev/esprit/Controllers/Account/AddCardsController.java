//package pidev.esprit.Controllers.Account;
//
//
//import java.awt.event.KeyEvent;
//import java.net.URL;
//import java.sql.SQLException;
//import java.time.LocalDate;
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//import java.util.ResourceBundle;
//
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.event.ActionEvent;
//        import javafx.fxml.FXML;
//import javafx.scene.control.Alert;
//import javafx.scene.control.DatePicker;
//import javafx.scene.control.ListView;
//import javafx.scene.control.TextField;
//import javafx.scene.input.KeyCode;
//import javafx.scene.input.MouseEvent;
//import pidev.esprit.entities.CarteBancaire;
//import pidev.esprit.entities.Compte;
//import pidev.esprit.entities.User;
//import pidev.esprit.services.CardCrud;
//import pidev.esprit.services.CompteCrud;
//
//import static pidev.esprit.entities.CarteBancaire.generateNum;
//
//public class AddCardsController  {
//
//    @FXML
//    private ResourceBundle resources;
//
//    @FXML
//    private URL location;
//
//    @FXML
//    private TextField tf_CVV2;
//
//    @FXML
//    private TextField tf_card;
//
//    @FXML
//    private TextField tf_code;
//    @FXML
//    private TextField tf_tel;
//
//
//    @FXML
//    private DatePicker tf_date;
//    @FXML
//    private TextField tf_rib;
//    @FXML
//    private ListView<CarteBancaire> CB;
//    private ObservableList<CarteBancaire> cardList = FXCollections.observableArrayList();
//
//
//    private void showAlert(Alert.AlertType alertType, String message) {
//        Alert alert = new Alert(alertType);
//        alert.setTitle("Erreur !! ");
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//    private boolean validateFields(String numCarte, int code, int cvv2, java.sql.Date dateExp) {
//        // Check if any field is empty
//        if (numCarte.isEmpty() || String.valueOf(code).isEmpty() || String.valueOf(cvv2).isEmpty() || dateExp == null) {
//            showAlert(Alert.AlertType.ERROR, "Please fill in all fields.");
//            return false;
//        }
//
//        // Check the length of the card number, code, and CVV2
//        if (numCarte.length() != 16 || String.valueOf(code).length() != 4 || String.valueOf(cvv2).length() != 3) {
//            showAlert(Alert.AlertType.ERROR, "Card number 16 chiffres, code must be 4 chiffre, and CVV2 must be 3 chiffre.");
//            return false;
//        }
//
//        // Check if the expiration date is at least 6 months from the current date
//        LocalDate currentDate = LocalDate.now();
//        LocalDate expirationDate = dateExp.toLocalDate();
//        if (currentDate.plusMonths(6).isAfter(expirationDate)) {
//            showAlert(Alert.AlertType.ERROR, "Expiration date doit etre dans 6 mois au moins !.");
//            return false;
//        }
//
//        // All validation passed
//        return true;}
//
//    @FXML
//    void Add_Card() {
//        String codeStr = tf_code.getText().trim();
//        String cvv2Str = tf_CVV2.getText().trim();
//        String rib = tf_rib.getText().trim();
//
//        java.sql.Date dateExp = java.sql.Date.valueOf(LocalDate.now());
//
//        // Check if the user exists
//        User user = CompteCrud.getInstance().retrieveUserByRIB(rib);
//        if (user == null) {
//            showAlert(Alert.AlertType.ERROR, "User not found for the provided RIB.");
//            return;
//        }
//
//        // Generate a random card number
//        String numCarte = CarteBancaire.generateNum();
//
//        // Create a new CarteBancaire object
//        CarteBancaire carte = new CarteBancaire(numCarte, cvv2, code, dateExp, rib);
//
//        // Add the card to the database
//        CardCrud.getInstance().addCard(carte, rib);
//
//        // Add the card to the UI list
//        CB.getItems().add(carte);
//
//        // Display the generated card number in the tf_card text field
//        tf_card.setText(numCarte);
//
//        showAlert(Alert.AlertType.INFORMATION, "Card added successfully.");
//    }
//
//
//
//
//
//    @FXML
//    void Delete_Card() {
//            CarteBancaire selectedCard = CB.getSelectionModel().getSelectedItem();
//            if (selectedCard != null) {
//                CardCrud.getInstance().deleteCard(selectedCard.getNum_carte());
//                cardList.remove(selectedCard);
//                showAlert(Alert.AlertType.INFORMATION,"Card deleted successfully.");
//            } else {
//                showAlert(Alert.AlertType.ERROR,"Please select a card to delete.");
//            }
//    }
//
////    @FXML
////    void Update_Card(MouseEvent event) {
////        CarteBancaire selectedCard = cardListView.getSelectionModel().getSelectedItem();
////        if (selectedCard != null) {
////            CardCrud.getInstance().deleteCard(selectedCard.getNum_carte());
////            cardList.remove(selectedCard);
////            showAlert(Alert.AlertType.INFORMATION,"Card deleted successfully.");
////        } else {
////            showAlert(Alert.AlertType.ERROR,"Please select a card to delete.");
////        }
////    }
//
////    @FXML
////    void handleCardDoubleClick(MouseEvent event) {
////        if (event.getClickCount() == 2) { // Check if it's a double-click
////            CarteBancaire selectedCard = cardListView.getSelectionModel().getSelectedItem();
////            if (selectedCard != null) {
////                // Call the method to show the update dialog and get the new card number
////                Optional<String> updatedCardNumber = showUpdateCardDialog(selectedCard.getNum_carte());
////
////                // Process the user's input
////                if (updatedCardNumber.isPresent()) {
////                    // Update the selected card's number with the new value
////                    selectedCard.setNum_carte(updatedCardNumber.get());
////
////                    // Update the card in the database
////                    CardCrud.getInstance().updateCard(selectedCard);
////
////                    // Show a confirmation message
////                    showAlert(Alert.AlertType.ERROR,"Card number updated successfully.");
////
////                    // Optionally, refresh the ListView to reflect the changes
////                    refreshListView();
////                } else {
////                    showAlert(Alert.AlertType.ERROR,"No changes made or update cancelled.");
////                }
////            } else {
////                showAlert(Alert.AlertType.ERROR,"Please select a card to update.");
////            }
////        }
////    }
////
//
//
//    private CardCrud cc = new CardCrud();
//
//    @FXML
//    void initialize() {
//
//        List<CarteBancaire> cards = cc.displayCard();
//
//        ObservableList<CarteBancaire> items = FXCollections.observableArrayList();
//        items.addAll(cards);
//
//        CB.setItems(items);
//
//
//        tf_rib.setOnAction(event -> onRIBFieldClicked());
//
//
//    }
//
//    @FXML private void refreshListView() throws SQLException {
//        // Fetch accounts from the database using CompteCrud singleton instance
//        List<CarteBancaire> cards = CardCrud.getInstance().displayCard();
//
//        // Populate the ListView with the fetched accounts
//        CB.getItems().setAll(cards);}
//
////
//////        assert cardListView != null : "fx:id=\"cardListView\" was not injected: check your FXML file 'AddCards.fxml'.";
//////
//////
//////        populateCardList();
////    }
//
//    private void populateCardList() {
//        // Retrieve the list of cards from the database
//        List<CarteBancaire> existingCards = CardCrud.getInstance().displayCard();
//
//        // Clear the existing items in the observable list and add the retrieved cards
//        cardList.clear();
//        cardList.addAll(existingCards);
//
//        // Bind the observable list to the ListView
//        CB.setItems(cardList);
//    }
//    @FXML
//    void onRIBFieldClicked() {
//        // Assuming you have a TextField named tf_rib for inputting the RIB
//
//        showAlert(Alert.AlertType.ERROR,"executed!");
//        String rib = tf_rib.getText().trim();
//
//
//        // Retrieve the user information by RIB
//        User user = CompteCrud.retrieveUserByRIB(rib);
//        showAlert(Alert.AlertType.ERROR,String.valueOf(user.getTel()));
//
//        if (user != null) {
//            // Populate your UI fields with user information
//            // Assuming you have a TextField named tf_tel
//            tf_tel.setText(String.valueOf(User.getTel()));
//
//
//            // Populate other UI fields as needed
//        } else {
//            // Handle case when user is not found
//            showAlert(Alert.AlertType.ERROR, "User not found for the provided RIB.");
//            // Clear UI fields
//
//        }
//    }
//
//
//}
package pidev.esprit.Controllers.Account;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import pidev.esprit.entities.CarteBancaire;
import pidev.esprit.entities.Compte;
import pidev.esprit.entities.User;
import pidev.esprit.services.CardCrud;
import pidev.esprit.services.CompteCrud;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static pidev.esprit.Controllers.Account.TwilioService.sendSMS;
import static pidev.esprit.services.CardCrud.sendExpirationReminders;


public class AddCardsController {

    @FXML
    private TextField tf_CVV2;

    @FXML
    private TextField tf_card;

    @FXML
    private TextField tf_code;

    @FXML
    private DatePicker date;

    @FXML
    private TextField tf_rib;

    @FXML
    private TextField tf_tel;

    @FXML
    private ListView<CarteBancaire> CB;
    @FXML
    private RadioButton tf_3y;

    @FXML
    private RadioButton tf_5y;

    private ObservableList<CarteBancaire> cardList = FXCollections.observableArrayList();
    private Object actionEvent;

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
    void Add_Card() {
        String rib = tf_rib.getText().trim();
        User user = CompteCrud.getInstance().retrieveUserByRIB(rib);
        if (user == null) {
            showAlert(Alert.AlertType.ERROR, "User not found for the provided RIB.");
            return;
        }

        // Generate card number
        String numCarte = CarteBancaire.generateNum();
selectedDurationYears=3;
        // Generate code and CVV2
        int code = CarteBancaire.generateCode();
        int cvv2 = CarteBancaire.generateCVV2();

        // Get current date
        java.sql.Date dateExp = java.sql.Date.valueOf(LocalDate.now());

        // Add the card to the database
        CarteBancaire carte = new CarteBancaire(numCarte, cvv2, code, dateExp, rib);
        CardCrud.getInstance().addCard(carte, rib);

        // Add the card to the UI list
        cardList.add(carte);

        // Display the generated card details
        tf_card.setText(numCarte);
        tf_code.setText(String.valueOf(code));
        tf_CVV2.setText(String.valueOf(cvv2));

        showAlert(Alert.AlertType.INFORMATION, "Card added successfully.");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Card.fxml"));
        Parent root;
        try {
            root = loader.load();
            CardsController controller = loader.getController();
            controller.setCardAndUserDetails(carte, user,selectedDurationYears); // Pass both the card and user details
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void Delete_Card() {
        CarteBancaire selectedCard = CB.getSelectionModel().getSelectedItem();
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
        if (selectedRadioButton ==tf_3y) {
            tf_5y.setSelected(false);
        } else if (selectedRadioButton == tf_5y) {
            tf_3y.setSelected(false);
        }

        handleDuration(actionEvent);
    }
    @FXML
    void initialize() throws SQLException {
        System.out.println("c bon ");
        if (CB == null) {
            System.out.println("CB is null");
        } else {
            CB.setEditable(true);
            List<CarteBancaire> cards = CardCrud.getInstance().displayCard();
            cardList.addAll(cards);
            CB.setItems(cardList);
            tf_rib.setOnAction(event -> onRIBFieldClicked());
          System.out.println("avant appel");

            sendExpirationReminders();
          System.out.println("apres appel");

            }

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
            selectedDurationYears=3;
        } else if (selectedRadioButton == tf_5y) {
            // Add 5 years to the current date
            LocalDate expirationDate = currentDate.plusYears(5);
            date.setValue(expirationDate);
            selectedDurationYears=5;
        }
    }



    @FXML
    private void handleDelete(KeyEvent event) throws SQLException {
        CB.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        if (event.getCode() == KeyCode.DELETE) {
            // Retrieve the selected item
            CarteBancaire selectedCarte = CB.getSelectionModel().getSelectedItem();

            // Check if an item is selected
            if (selectedCarte != null) {
                // Show a confirmation dialog
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("Delete Card ?");
                alert.setContentText("Are you sure you want to delete this Card?"+selectedCarte.getNum_carte());

                // Handle the user's response
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    // User clicked OK, proceed with deletion

                    // Remove the selected item from the ListView
                    CB.getItems().remove(selectedCarte);

                    // Update your data source (e.g., database) to reflect the deletion
                    CardCrud.deleteCard(selectedCarte.getNum_carte());
                } else {
                    // User clicked Cancel or closed the dialog, do nothing
                }
            }
        }
    }


    @FXML
    private void refreshListView() {
        // Fetch accounts from the database using CompteCrud singleton instance
        List<CarteBancaire> cartes = CardCrud.getInstance().displayCard();

        // Populate the ListView with the fetched accounts
       CB.getItems().setAll(cartes);
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




