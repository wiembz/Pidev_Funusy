package pidev.esprit.Controllers.Credit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class GestionCreditController {
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private AnchorPane contentPlaceholder;
    @FXML
    private void loadCredits() {
        loadFXML("AdminCredit.fxml");
    } // AdminCredit AjouterCredit



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
    public void handleCreditsButtonClick(ActionEvent mouseEvent) {
        try {
            Parent GestionCredit = FXMLLoader.load(getClass().getResource("/AdminCredit.fxml"));
            mainBorderPane.setCenter(GestionCredit);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void handleEstimationButtonClick(ActionEvent mouseEvent) {
        try {
            Parent GestionCredit = FXMLLoader.load(getClass().getResource("/Simulateur.fxml"));
            mainBorderPane.setCenter(GestionCredit);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

