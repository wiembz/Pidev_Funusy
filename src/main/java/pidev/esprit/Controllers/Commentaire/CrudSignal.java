package pidev.esprit.Controllers.Commentaire;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pidev.esprit.Entities.Commentaire;
import pidev.esprit.Entities.Signal;
import pidev.esprit.Services.CommentaireCrud;
import pidev.esprit.Services.SignalCrud;

public class CrudSignal {

    @FXML
    private TableView<Commentaire> commentTableView;

    @FXML
    private TableColumn<Commentaire, Integer> idCommentColumn;

    @FXML
    private TableColumn<Commentaire, LocalDate> dateColumn;

    @FXML
    private TableColumn<Commentaire, String> contentColumn;

    @FXML
    private TableColumn<Commentaire, Integer> id_projetColumn;

    @FXML
    private AnchorPane signalSection;

    @FXML
    private TextArea signalTextArea;

    @FXML
    private Button submitSignalButton;

    @FXML
    private Button signalButton;

    private CommentaireCrud commentaireCrud;

    public CrudSignal() {
        commentaireCrud = new CommentaireCrud();
    }

    @FXML
    private void initialize() {
        // Initialize table columns
        idCommentColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId_projet()).asObject());
        id_projetColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId_projet()).asObject());
        dateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDate_commentaire()));
        contentColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getContenue()));

        // Populate the TableView with comments data
        afficherComments();

        // Add event handler to the comment section AnchorPane
        signalSection.setOnMouseClicked(event -> commentTableView.getSelectionModel().clearSelection());
    }

    private void afficherComments() {
        List<Commentaire> commentaireList = commentaireCrud.afficherEntite();
        commentTableView.getItems().addAll(commentaireList);
    }

    @FXML
    public void Signaler() {
        Commentaire selectedComment = commentTableView.getSelectionModel().getSelectedItem();
        if (selectedComment != null) {
            displaySignalAreaFor(selectedComment);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a comment first!", ButtonType.OK);
            alert.show();
        }
    }

    private void displaySignalAreaFor(Commentaire commentaire) {
        signalSection.setVisible(true);
        signalTextArea.clear(); // Clear text area when showing
        signalTextArea.requestFocus(); // Set focus to the text area
    }

    @FXML
    public void submitSignal() {
        String signalContent = signalTextArea.getText().trim();
        Commentaire selectedComment = commentTableView.getSelectionModel().getSelectedItem();

        // Create a Signal object and add it to the database
        Signal signal = new Signal();
        signal.setId_commentaire(selectedComment.getId_commentaire());
        signal.setDate_signal(LocalDate.now());
        signal.setDescription(signalContent);

        // Call the service to add the Signal to the database
        SignalCrud signalCrud = new SignalCrud();
        signalCrud.ajouterEntite(signal);

        // Clear the comment text area
        signalTextArea.clear();

        // Hide the comment section after submission (optional)
        signalSection.setVisible(false);

        // Show a success message
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Signal added successfully!", ButtonType.OK);
        alert.show();
    }

    @FXML
    private void listSignals() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SignalList.fxml"));
            Parent root = loader.load();

            // Get the controller
            SignalListController controller = loader.getController();

            // Retrieve the list of signals
            List<Signal> signals = new SignalCrud().afficherEntite();

            // Pass the list of signals to the controller
            controller.setSignals(signals);

            // Create a new stage for the signal display window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Signals");
            stage.show();
            commentTableView.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clearSelection(MouseEvent event) {
        commentTableView.getSelectionModel().clearSelection();
    }
}
