package pidev.esprit.Controllers.Account;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import pidev.esprit.Entities.CarteBancaire;
import pidev.esprit.Entities.User;
import pidev.esprit.Services.CompteCrud;

public class CardsController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField cardNumberField;

    @FXML
    private TextField cardholderNameField;

    @FXML
    private TextField expiryDateField;


    private static int duration;

    public static void setDuration(int duration) {
        CardsController.duration = duration; // Access static variable directly within a static method
    }
    public void setCardAndUserDetails(CarteBancaire carteBancaire, User user, int duration) {

        // Calculate expiration date based on the duration
        LocalDate currentDate = LocalDate.now();
        LocalDate expirationDate = currentDate.plusYears(duration);

        // Set the expiration date in the expiryDateField
        expiryDateField.setText(expirationDate.toString());

        // Set cardholder name
        cardholderNameField.setText((user.getNom_user() + " " + user.getPrenom_user()).toUpperCase());

        // Format card number with spaces
        String numeroCarte = carteBancaire.getNum_carte();
        StringBuilder formattedNumber = new StringBuilder();
        for (int i = 0; i < numeroCarte.length(); i += 4) {
            if (i + 4 <= numeroCarte.length()) {
                formattedNumber.append(numeroCarte, i, i + 4).append(" ");
            } else {
                formattedNumber.append(numeroCarte, i, numeroCarte.length());
            }
        }

        // Set formatted card number
        cardNumberField.setText(formattedNumber.toString().trim());
    }
}