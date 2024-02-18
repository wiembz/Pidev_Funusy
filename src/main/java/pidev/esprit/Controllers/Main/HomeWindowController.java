package pidev.esprit.Controllers.Main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;


public class HomeWindowController {
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private AnchorPane contentPlaceholder;

    @FXML
    private void loadUser() {
        loadFXML("GestionProfile.fxml");
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

    public void handleAccountsButtonClick(ActionEvent actionEvent) {
        try {
            Parent GestionAccount = FXMLLoader.load(getClass().getResource("/GestionAccount.fxml"));
            mainBorderPane.setCenter(GestionAccount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void handleInvestmentsButtonClick(ActionEvent actionEvent) {
        try {
            Parent gestionInvestissement = FXMLLoader.load(getClass().getResource("/GestionInvestissement.fxml"));
            mainBorderPane.setCenter(gestionInvestissement);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void handleTransactionsButtonClick(ActionEvent mouseEvent) {
        try {
            Parent GestionTransaction = FXMLLoader.load(getClass().getResource("/GestionTransaction.fxml"));
            mainBorderPane.setCenter(GestionTransaction);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void handleCreditsButtonClick(ActionEvent mouseEvent) {
        try {
            Parent GestionCredit = FXMLLoader.load(getClass().getResource("/GestionCredit.fxml"));
            mainBorderPane.setCenter(GestionCredit);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void handleCommentairesButtonClick(ActionEvent actionEvent) {
        try {
            Parent GestionCommentaire = FXMLLoader.load(getClass().getResource("/GestionCommentaire.fxml"));
            mainBorderPane.setCenter(GestionCommentaire);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleProfilesButtonClick(ActionEvent actionEvent) {
        try {
            Parent gestionProfile = FXMLLoader.load(getClass().getResource("/GestionProfile.fxml"));
            mainBorderPane.setCenter(gestionProfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}