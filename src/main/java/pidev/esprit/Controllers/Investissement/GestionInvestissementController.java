package pidev.esprit.Controllers.Investissement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import java.io.IOException;

public class GestionInvestissementController {

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private AnchorPane contentPlaceholder;

    // Keep track of the currently selected button
    private Button selectedButton;

    @FXML
    public void handleInvestmentsButtonClick(ActionEvent actionEvent) {
        loadFXML("Investissement.fxml");
        updateButtonStyle((Button) actionEvent.getSource());
    }

    @FXML
    public void handleBenefitsButtonClick(ActionEvent actionEvent) {
        loadFXML("Profit.fxml");
        updateButtonStyle((Button) actionEvent.getSource());
    }

    @FXML
    public void handleProjectsButtonClick(ActionEvent actionEvent) {
        loadFXML("Projet.fxml");
        updateButtonStyle((Button) actionEvent.getSource());
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

    private void updateButtonStyle(Button clickedButton) {
        if (selectedButton != null) {
            selectedButton.getStyleClass().remove("clicked");
        }
        clickedButton.getStyleClass().add("clicked");
        selectedButton = clickedButton;
    }
}
