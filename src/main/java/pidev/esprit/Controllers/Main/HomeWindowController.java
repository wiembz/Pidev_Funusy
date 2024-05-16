package pidev.esprit.Controllers.Main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import pidev.esprit.Controllers.Investissement.GestionInvestissementController;
import pidev.esprit.Tools.MyConnection;

import java.io.IOException;


public class HomeWindowController {
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


    public void handleInvestmentsButtonClick(ActionEvent actionEvent) {
        int userId = MyConnection.getInstance().getLoggedInUserId(); // Get the user's ID

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionInvestissement.fxml"));
            Parent gestionInvestissement = loader.load();
            GestionInvestissementController controller = loader.getController();
            controller.setUserId(userId); // Pass the user's ID
            mainBorderPane.setCenter(gestionInvestissement);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load Investments page.");
        }
        updateButtonStyle((Button) actionEvent.getSource());
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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
            Parent GestionTransaction = FXMLLoader. load(getClass().getResource("/GestionTransaction.fxml"));
            mainBorderPane.setCenter(GestionTransaction);
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateButtonStyle((Button) actionEvent.getSource());

    }
    public void handleCreditsButtonClick(ActionEvent mouseEvent) {
        try {
            Parent GestionCredit = FXMLLoader.load(getClass().getResource("/GestionCredit.fxml"));
            mainBorderPane.setCenter(GestionCredit);
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateButtonStyle((Button) mouseEvent.getSource());

    }
    public void handleCommentairesButtonClick(ActionEvent actionEvent) {
        try {
            Parent GestionCommentaire = FXMLLoader.load(getClass().getResource("/GestionCommentaire.fxml"));
            mainBorderPane.setCenter(GestionCommentaire);
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateButtonStyle((Button) actionEvent.getSource());

    }

    public void handleProfilesButtonClick(ActionEvent actionEvent) {
        try {
            Parent gestionProfile = FXMLLoader.load(getClass().getResource("/Profile.fxml"));
            mainBorderPane.setCenter(gestionProfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateButtonStyle((Button) actionEvent.getSource());

    }

    public void overviewButton(ActionEvent actionEvent) {
        try {
            Parent overview = FXMLLoader.load(getClass().getResource("/Overview.fxml"));
            mainBorderPane.setCenter(overview);
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateButtonStyle((Button) actionEvent.getSource());
    }
}