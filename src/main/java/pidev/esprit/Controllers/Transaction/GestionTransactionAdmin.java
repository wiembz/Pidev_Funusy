package pidev.esprit.Controllers.Transaction;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import pidev.esprit.Entities.Transaction;
import pidev.esprit.Services.GestionTransaction;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class GestionTransactionAdmin {

    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private AnchorPane contentPlaceholder;

    @FXML
    private Button load_Admin;
    @FXML
    private Pane accountsPane;

    @FXML
    private Button load_transactions;


    @FXML
    private void loadUser() {
        loadFXML("Transaction.fxml");
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
    void load_Admin(javafx.event.ActionEvent actionEvent) {
        try {
            Parent GestionAgence = FXMLLoader.load(getClass().getResource("/GestionAgenceAdmin.fxml"));
            mainBorderPane.setCenter(GestionAgence);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
