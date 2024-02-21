package pidev.esprit.Controllers.Account;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class GestionAccountController {
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private AnchorPane contentPlaceholder;

        @FXML
        private Pane accountsPane;

        @FXML
        private void loadAccount(ActionEvent event) {
            loadFXML( "/AddAccounts.fxml");
        }


    private void loadFXML(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + fxmlFile));
            AnchorPane content = loader.load();
            AnchorPane.setTopAnchor(content, 0.0);
            AnchorPane.setBottomAnchor(content, 0.0);
            AnchorPane.setLeftAnchor(content, 0.0);
            AnchorPane.setRightAnchor(content, 0.0);
            contentPlaceholder.getChildren().setAll(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleAccountsButtonClick(javafx.event.ActionEvent actionEvent) {

        try {
            Parent GestionAccount = FXMLLoader.load(getClass().getResource("/GestionAccount.fxml"));
            mainBorderPane.setCenter(GestionAccount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void loadAccount(javafx.event.ActionEvent actionEvent) {

        try {
            Parent AddAccounts = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/AddAccounts.fxml")));
            mainBorderPane.setCenter(AddAccounts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}




