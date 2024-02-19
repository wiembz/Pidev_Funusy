package pidev.esprit.Controllers.Investissement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import java.io.IOException;

public class GestionInvestissementController {
    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private AnchorPane contentPlaceholder;

    @FXML
    private void loadInvestissement() {
        loadFXML("Investissement.fxml");
    }

    @FXML
    private void loadProfits() {
        loadFXML("Profit.fxml");
    }

    @FXML
    private void loadProjet() {
        loadFXML("Projet.fxml");
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

    @FXML
    public void handleInvestmentsButtonClick(ActionEvent actionEvent) {
        try {
            Parent Investissement = FXMLLoader.load(getClass().getResource("/Investissement.fxml"));
            mainBorderPane.setCenter(Investissement);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBenefitsButtonClick(ActionEvent actionEvent) {
        try {
            Parent Profit = FXMLLoader.load(getClass().getResource("/Profit.fxml"));
            mainBorderPane.setCenter(Profit);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleProjectsButtonClick(ActionEvent actionEvent) {
        try {
            Parent Projet = FXMLLoader.load(getClass().getResource("/Projet.fxml"));
            mainBorderPane.setCenter(Projet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
