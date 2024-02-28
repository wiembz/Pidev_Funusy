package pidev.esprit.Controllers.Account;
import pidev.esprit.Controllers.Account.CardsController;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import pidev.esprit.entities.CarteBancaire;
import pidev.esprit.entities.User;

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



    public void setCardAndUserDetails(CarteBancaire carteBancaire, User user) {
        cardNumberField.setText(carteBancaire.getNum_carte());
        expiryDateField.setText(carteBancaire.getDate_exp().toString());
        // Assuming you have access to client's name in your CarteBancaire class
        cardholderNameField.setText(user.getNom_user() + " " + user.getPrenom_user());
    }

}
