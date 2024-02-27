package pidev.esprit.Controllers.Commentaire;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pidev.esprit.Controllers.Commentaire.CommentListController;
import pidev.esprit.Entities.Commentaire;
import pidev.esprit.Entities.Projet;
import pidev.esprit.Services.CommentaireCrud;
import pidev.esprit.Services.ProjetServices;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class CrudCommentaire {

    @FXML
    private TableView<Projet> projetTableView;

    @FXML
    private TableColumn<Projet, Integer> id_projetColumn;

    @FXML
    private TableColumn<Projet, String> nom_projetColumn;

    @FXML
    private TableColumn<Projet, Float> montant_reqColumn;

    @FXML
    private TableColumn<Projet, String> type_projetColumn;

    @FXML
    private TableColumn<Projet, String> longitudeColumn;

    @FXML
    private TableColumn<Projet, String> latitudeColumn;

    @FXML
    private AnchorPane commentSection;

    @FXML
    private TextArea commentTextArea;

    @FXML
    private Button submitCommentButton;

    @FXML
    private Button commentbtn;

    private ProjetServices projetServices;
    private String lastCommentContent = "";

    public CrudCommentaire() {
        projetServices = new ProjetServices();
    }

    @FXML
    private void initialize() {
        // Initialize table columns
        id_projetColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId_projet()).asObject());
        nom_projetColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom_projet()));
        montant_reqColumn.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getMontant_req()).asObject());
        type_projetColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType_projet()));
        longitudeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLongitude()));
        latitudeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLatitude()));

        // Populate the TableView with project data
        afficherProjets();

        // Add event handler to the comment section AnchorPane
        commentSection.setOnMouseClicked(event -> projetTableView.getSelectionModel().clearSelection());
    }

    private void afficherProjets() {
        List<Projet> projetList = projetServices.afficherEntite();
        projetTableView.getItems().addAll(projetList);
    }

    @FXML
    public void commenter() {
        Projet selectedProject = projetTableView.getSelectionModel().getSelectedItem();
        if (selectedProject != null) {
            displayCommentAreaFor(selectedProject);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a project first!", ButtonType.OK);
            alert.show();
        }
    }

    private void displayCommentAreaFor(Projet projet) {
        commentSection.setVisible(true);
        commentTextArea.setVisible(true);
        submitCommentButton.setVisible(true);
        commentTextArea.requestFocus(); // Set focus to the text area
        submitCommentButton.setOnAction(event -> submitComment()); // Set action handler for the submit button
    }

    @FXML
    public void submitComment() {
        String commentContent = commentTextArea.getText().trim();
        Projet selectedProject = projetTableView.getSelectionModel().getSelectedItem();

        // Check if the comment field is empty
        if (commentContent.isEmpty()) {
            // Show an error message if the comment field is empty
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill the comment field!", ButtonType.OK);
            alert.show();
            return; // Exit the method without submitting the comment
        }

        // Check if the comment is identical to the previous comment
        if (commentContent.equals(lastCommentContent)) {
            // Show an error message for potential spam
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please avoid spamming the same comment!", ButtonType.OK);
            alert.show();
            return; // Exit the method without submitting the comment
        }

        // Create a Commentaire object and add it to the database
        Commentaire commentaire = new Commentaire();
        commentaire.setContenue(commentContent);
        commentaire.setDate_commentaire(LocalDate.now());
        commentaire.setId_projet(selectedProject.getId_projet());

        // Call the service to add the Commentaire to the database
        CommentaireCrud commentaireCrud = new CommentaireCrud();
        commentaireCrud.ajouterEntite(commentaire);

        // Store the current comment content as the last comment content
        lastCommentContent = commentContent;

        // Clear the comment text area
        commentTextArea.clear();

        // Hide the comment section after submission (optional)
        commentSection.setVisible(false);

        // Show a success message
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Comment added successfully!", ButtonType.OK);
        alert.show();
    }

    @FXML
    private void listComments() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CommentList.fxml"));
            Parent root = loader.load();

            // Get the controller
            CommentListController controller = loader.getController();

            // Retrieve the list of comments
            List<Commentaire> comments = new CommentaireCrud().afficherEntite();

            // Pass the list of comments to the controller
            controller.setComments(comments);

            // Create a new stage for the comment display window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Comments");
            stage.show();
            projetTableView.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clearSelection(MouseEvent event) {
        projetTableView.getSelectionModel().clearSelection();
    }
}
