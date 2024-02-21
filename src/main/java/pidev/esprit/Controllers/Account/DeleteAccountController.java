

package pidev.esprit.Controllers.Account;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class DeleteAccountController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField tf_ribD;

    @FXML
    private BorderPane mainBorderPane;


   /* @FXML
    private void deleteButtonClicked(ActionEvent event) {
        try {
            // Load the DeleteAccount.fxml file
            Parent deleteAccount = FXMLLoader.load(getClass().getResource("/DeleteAccount.fxml"));

            // Set the loaded FXML as the center of your layout
            mainBorderPane.setCenter(deleteAccount);
        } catch (IOException e) {
            e.printStackTrace(); // Handle loading error
        }
    }

    @FXML
    void initialize() {
        assert tf_ribD != null : "fx:id=\"tf_ribD\" was not injected: check your FXML file 'DeleteAccount.fxml'.";

    }*/

}
