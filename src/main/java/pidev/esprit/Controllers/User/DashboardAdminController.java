
package pidev.esprit.Controllers.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;


public class DashboardAdminController {
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private AnchorPane contentPlaceholder;
    private Button selectedButton;

    @FXML
    private void loadUser() {
        loadFXML("Overview.fxml");
    }

    @FXML
    private void loadAccounts() {
        loadFXML("GestionAccount.fxml");
    }

    @FXML
    private void loadTransactions() {
        loadFXML("GestionTransaction.fxml");
    }

    @FXML
    private void loadCredits() {
        loadFXML("GestionCredit.fxml");
    }

    @FXML
    private void loadInvestments() {
        loadFXML("GestionInvestissement.fxml");
    }

    @FXML
    private void loadCommentaire() {
        loadFXML("GestionCommentaire.fxml");
    }
    @FXML
    private void loadProfile() {
        loadFXML("ProfileAdmin.fxml");
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
    public void OverviewAdminButton(ActionEvent actionEvent) {
        try {
            Parent GestionAccount = FXMLLoader.load(getClass().getResource("/OverviewAdmin.fxml"));
            mainBorderPane.setCenter(GestionAccount);
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateButtonStyle((Button) actionEvent.getSource());

    }
    public void handleAccountsButtonClick(ActionEvent actionEvent) {
        try {
            Parent GestionAccount = FXMLLoader.load(getClass().getResource("/GestionAccount.fxml"));
            mainBorderPane.setCenter(GestionAccount);
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateButtonStyle((Button) actionEvent.getSource());

    }

    public void handleInvestmentsButtonClick(ActionEvent actionEvent) {
        try {
            Parent gestionInvestissement = FXMLLoader.load(getClass().getResource("/GestionInvestissementAdmin.fxml"));
            mainBorderPane.setCenter(gestionInvestissement);
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateButtonStyle((Button) actionEvent.getSource());

    }

    private void updateButtonStyle(Button clickedButton) {
        if (selectedButton != null) {
            selectedButton.getStyleClass().remove("clicked");
        }
        clickedButton.getStyleClass().add("clicked");
        selectedButton = clickedButton;
    }

    public void handleTransactionsButtonClick(ActionEvent actionEvent) {
        try {
            Parent GestionTransaction = FXMLLoader.load(getClass().getResource("/GestionTransactionAdmin.fxml"));
            mainBorderPane.setCenter(GestionTransaction);
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateButtonStyle((Button) actionEvent.getSource());

    }
    public void mailbutton(ActionEvent actionEvent) {
        try {
            Parent GestionTransaction = FXMLLoader.load(getClass().getResource("/MailingCommentaire.fxml"));
            mainBorderPane.setCenter(GestionTransaction);
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateButtonStyle((Button) actionEvent.getSource());

    }

    public void handleCreditsButtonClick(ActionEvent mouseEvent) {
        try {
            Parent GestionCredit = FXMLLoader.load(getClass().getResource("/GestionCreditAdminController.fxml"));
            mainBorderPane.setCenter(GestionCredit);
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateButtonStyle((Button) mouseEvent.getSource());

    }

    public void handleCommentairesButtonClick(ActionEvent actionEvent) {
        try {
            Parent GestionCommentaire = FXMLLoader.load(getClass().getResource("/AdminCommentaire.fxml"));
            mainBorderPane.setCenter(GestionCommentaire);
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateButtonStyle((Button) actionEvent.getSource());

    }

    public void handleProfilesButtonClick(ActionEvent actionEvent) {
        try {
            Parent gestionProfile = FXMLLoader.load(getClass().getResource("/ProfileAdmin.fxml"));
            mainBorderPane.setCenter(gestionProfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateButtonStyle((Button) actionEvent.getSource());

    }

}