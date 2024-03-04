package pidev.esprit.Controllers.Transaction;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Objects;

public class GestionTransactionController {
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


    public void open_transaction_window(javafx.event.ActionEvent actionEvent) {
        try {
            Parent GestionTransaction = FXMLLoader.load(getClass().getResource("/Transaction.fxml"));
            mainBorderPane.setCenter(GestionTransaction);
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
