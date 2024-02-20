package pidev.esprit.Controllers.Account;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;

public class GestionAccountController {


        @FXML
        private Pane accountsPane;

        @FXML
        private void handleAccountsButtonClick(ActionEvent event) {
            loadFxmlFile( "/AddAccounts.fxml");
        }

        @FXML
        private void handleCardButtonClick(ActionEvent event) {
            loadFxmlFile( "/AddCard.fxml");
        }

    private void loadFxmlFile(String fxmlPath) {
            try {
                // Try to find the FXML file using its path
                URL resourceUrl = getClass().getResource(fxmlPath);

                // If the file is not found, throw an exception
                if (resourceUrl == null) {
                    throw new IllegalArgumentException("Sorryy 404 not found :( ! " + fxmlPath);
                }

                // Create an FXMLLoader object to load the FXML file
                FXMLLoader loader = new FXMLLoader(resourceUrl);

                // Load the FXML file and add the loaded content to the investmentsPane
                Pane content = loader.load();
                accountsPane.getChildren().add(content);

                // Catch any exceptions that occur during loading
            }catch (IOException e) {
            e.printStackTrace();
            // Handle the error here, for example by displaying a message to the user
        }
    }

}


