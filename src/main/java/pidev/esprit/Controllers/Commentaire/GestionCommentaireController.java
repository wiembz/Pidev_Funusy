package pidev.esprit.Controllers.Commentaire;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class GestionCommentaireController {
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private AnchorPane contentPlaceholder;


    @FXML
    private void loadCommentaire() {loadFXML("CrudCommentaire.fxml");}
    private void loadSignal() {loadFXML("CrudSignal.fxml");}



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
    public void handleCommentairesButtonClick(ActionEvent actionEvent) {
        try {
            Parent GestionCommentaire = FXMLLoader.load(getClass().getResource("/CrudCommentaire.fxml"));
            mainBorderPane.setCenter(GestionCommentaire);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void handleSignalButtonClick(ActionEvent actionEvent){
        try {
            Parent GestionCommentaire = FXMLLoader.load(getClass().getResource("/CrudSignal.fxml"));
            mainBorderPane.setCenter(GestionCommentaire);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    }



