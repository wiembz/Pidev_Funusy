package pidev.esprit.Controllers.Account;


import java.awt.event.KeyEvent;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import pidev.esprit.entities.CarteBancaire;
import pidev.esprit.entities.Compte;
import pidev.esprit.services.CardCrud;
import pidev.esprit.services.CompteCrud;

public class AddCardsController  {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField tf_CVV2;

    @FXML
    private TextField tf_card;

    @FXML
    private TextField tf_code;

    @FXML
    private DatePicker tf_date;
    @FXML
    private TextField tf_rib;
    @FXML
    private ListView<CarteBancaire> CB;
    private ObservableList<CarteBancaire> cardList = FXCollections.observableArrayList();


    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Erreur !! ");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private boolean validateFields(String numCarte, int code, int cvv2, java.sql.Date dateExp) {
        // Check if any field is empty
        if (numCarte.isEmpty() || String.valueOf(code).isEmpty() || String.valueOf(cvv2).isEmpty() || dateExp == null) {
            showAlert(Alert.AlertType.ERROR, "Please fill in all fields.");
            return false;
        }

        // Check the length of the card number, code, and CVV2
        if (numCarte.length() != 16 || String.valueOf(code).length() != 4 || String.valueOf(cvv2).length() != 3) {
            showAlert(Alert.AlertType.ERROR, "Card number 16 chiffres, code must be 4 chiffre, and CVV2 must be 3 chiffre.");
            return false;
        }

        // Check if the expiration date is at least 6 months from the current date
        LocalDate currentDate = LocalDate.now();
        LocalDate expirationDate = dateExp.toLocalDate();
        if (currentDate.plusMonths(6).isAfter(expirationDate)) {
            showAlert(Alert.AlertType.ERROR, "Expiration date doit etre dans 6 mois au moins !.");
            return false;
        }

        // All validation passed
        return true;}

        public void Add_Card () {
            String numCarte = tf_card.getText().trim();
            String codeStr = tf_code.getText().trim();
            String cvv2Str = tf_CVV2.getText().trim();
            String rib = tf_rib.getText().trim(); // Assuming you have this field in your FXML

            // Parse code and cvv2 to integers
            int code = 0;
            int cvv2 = 0;
            try {
                code = Integer.parseInt(codeStr);
                cvv2 = Integer.parseInt(cvv2Str);
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR,"Code / CVV2 doit etre des entiers .");
                return;
            }

            java.sql.Date dateExp = java.sql.Date.valueOf(tf_date.getValue());

            if (validateFields(numCarte, code, cvv2, dateExp)) {
                CarteBancaire card = new CarteBancaire(numCarte, code, cvv2, dateExp, rib);
                CardCrud.getInstance().addCard(card, card.getRib());

                cardList.add(card);
                showAlert(Alert.AlertType.INFORMATION,"Card added successfully.");
                ObservableList<CarteBancaire> items = CB.getItems();
            }
        }


        @FXML
    void Delete_Card() {
            CarteBancaire selectedCard = CB.getSelectionModel().getSelectedItem();
            if (selectedCard != null) {
                CardCrud.getInstance().deleteCard(selectedCard.getNum_carte());
                cardList.remove(selectedCard);
                showAlert(Alert.AlertType.INFORMATION,"Card deleted successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR,"Please select a card to delete.");
            }
    }

//    @FXML
//    void Update_Card(MouseEvent event) {
//        CarteBancaire selectedCard = cardListView.getSelectionModel().getSelectedItem();
//        if (selectedCard != null) {
//            CardCrud.getInstance().deleteCard(selectedCard.getNum_carte());
//            cardList.remove(selectedCard);
//            showAlert(Alert.AlertType.INFORMATION,"Card deleted successfully.");
//        } else {
//            showAlert(Alert.AlertType.ERROR,"Please select a card to delete.");
//        }
//    }

//    @FXML
//    void handleCardDoubleClick(MouseEvent event) {
//        if (event.getClickCount() == 2) { // Check if it's a double-click
//            CarteBancaire selectedCard = cardListView.getSelectionModel().getSelectedItem();
//            if (selectedCard != null) {
//                // Call the method to show the update dialog and get the new card number
//                Optional<String> updatedCardNumber = showUpdateCardDialog(selectedCard.getNum_carte());
//
//                // Process the user's input
//                if (updatedCardNumber.isPresent()) {
//                    // Update the selected card's number with the new value
//                    selectedCard.setNum_carte(updatedCardNumber.get());
//
//                    // Update the card in the database
//                    CardCrud.getInstance().updateCard(selectedCard);
//
//                    // Show a confirmation message
//                    showAlert(Alert.AlertType.ERROR,"Card number updated successfully.");
//
//                    // Optionally, refresh the ListView to reflect the changes
//                    refreshListView();
//                } else {
//                    showAlert(Alert.AlertType.ERROR,"No changes made or update cancelled.");
//                }
//            } else {
//                showAlert(Alert.AlertType.ERROR,"Please select a card to update.");
//            }
//        }
//    }
//


    private CardCrud cc = new CardCrud();

    @FXML
    void initialize() {

        List<CarteBancaire> cards = cc.displayCard();


        ObservableList<CarteBancaire> items = FXCollections.observableArrayList();
        items.addAll(cards);

        CB.setItems(items);




    }

    @FXML private void refreshListView() throws SQLException {
        // Fetch accounts from the database using CompteCrud singleton instance
        List<CarteBancaire> cards = CardCrud.getInstance().displayCard();

        // Populate the ListView with the fetched accounts
        CB.getItems().setAll(cards);}

//
////        assert cardListView != null : "fx:id=\"cardListView\" was not injected: check your FXML file 'AddCards.fxml'.";
////
////
////        populateCardList();
//    }

    private void populateCardList() {
        // Retrieve the list of cards from the database
        List<CarteBancaire> existingCards = CardCrud.getInstance().displayCard();

        // Clear the existing items in the observable list and add the retrieved cards
        cardList.clear();
        cardList.addAll(existingCards);

        // Bind the observable list to the ListView
        CB.setItems(cardList);
    }


}
