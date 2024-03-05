package pidev.esprit.Controllers.Commentaire;

import pidev.esprit.Utils.BadWordsLoader;

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

    //Constructeur : Initialisation du service ProjetServices.
    public CrudCommentaire() {
        projetServices = new ProjetServices();
    }

    @FXML
    private void initialize() {
        // Initialize ,configuration table columns
        id_projetColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId_projet()).asObject());
        nom_projetColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom_projet()));
        montant_reqColumn.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getMontant_req()).asObject());
        type_projetColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType_projet()));
        longitudeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLongitude()));
        latitudeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLatitude()));

        // Appel à la méthode afficherProjets
        afficherProjets();

        // Add event handler to the comment section AnchorPane
        commentSection.setOnMouseClicked(event -> projetTableView.getSelectionModel().clearSelection());
    }

    // Cette méthode récupère la liste des projets à partir du service ProjetServices et les ajoute à la TableView.
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

    //Cette méthode est appelée pour afficher la zone de commentaire
    private void displayCommentAreaFor(Projet projet) {
        commentSection.setVisible(true);
        commentTextArea.setVisible(true);
        submitCommentButton.setVisible(true);
        commentTextArea.requestFocus(); // Set focus to the text area

        // Set prompt text to the text area
        commentTextArea.setPromptText("Write your comment here");

        // Définir le style CSS directement dans le code Java
        commentTextArea.setStyle("-fx-font-size: 14px; " +
                "-fx-font-family: Arial, sans-serif; " +
                "-fx-padding: 10px; " +
                "-fx-pref-width: 300px; " +
                "-fx-pref-height: 100px; " +
                "-fx-border-color: #333; " + // Dark border color
                "-fx-border-width: 2px; " + // Thicker border
                "-fx-border-radius: 5px; " +
                "-fx-background-color:  #77B5FE ; " +
                "-fx-background-insets: 10; " +
                "-fx-effect: innershadow(gaussian, #eaeaea, 2, 0, 0, 0); " +
                "-fx-text-alignment: center; " + // Alignement horizontal du texte
                "-fx-alignment: center;"); // Alignement vertical et horizontal du contenu
    }



    @FXML
    public void submitComment() {
        String commentContent = commentTextArea.getText().trim();
        Projet selectedProject = projetTableView.getSelectionModel().getSelectedItem();

        // Charger la liste des mots indésirables
        List<String> badWords = BadWordsLoader.loadBadWords("C:\\Users\\ASUS\\Downloads\\validationmetier\\List.txt");

        // Check if comment content is empty
        if (commentContent.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a comment!", ButtonType.OK);
            alert.show();
            return;
        }

        // Check if the comment is identical to the previous comment
        if (commentContent.equals(lastCommentContent)) {
            // Show an error message for potential spam
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please avoid spamming the same comment!", ButtonType.OK);
            alert.show();
            return;
        }

        // Vérifier si le commentaire contient des mots indésirables
        for (String badWord : badWords) {
            if (commentContent.toLowerCase().contains(badWord)) {
                // Afficher un message d'erreur et ne pas ajouter le commentaire
                Alert alert = new Alert(Alert.AlertType.ERROR, "Your comment contains inappropriate words!", ButtonType.OK);
                alert.show();
                return;
            }
        }

        // Le commentaire est valide, continuez avec l'ajout à la base de données
        Commentaire commentaire = new Commentaire();
        commentaire.setContenue(commentContent);
        commentaire.setDate_commentaire(LocalDate.now());
        commentaire.setId_projet(selectedProject.getId_projet());

        CommentaireCrud commentaireCrud = new CommentaireCrud();
        commentaireCrud.ajouterEntite(commentaire);

        // Autres actions après l'ajout du commentaire...

        // Clear the comment text area
        commentTextArea.clear();

        // Hide the comment section after submission (optional)
        commentSection.setVisible(false);

        // Update last comment content
        lastCommentContent = commentContent;

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
