package pidev.esprit.Controllers.Commentaire;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import pidev.esprit.Entities.Commentaire;
import pidev.esprit.Services.CommentaireCrud;
import pidev.esprit.Utils.Translator;

import java.time.LocalDate;
import java.util.List;

public class CommentListController {

    @FXML
    private TableView<Commentaire> commentTableView;

    @FXML
    private TableColumn<Commentaire, Integer> idCommentColumn;

    @FXML
    private TableColumn<Commentaire, Integer> idProjetColumn;

    @FXML
    private TableColumn<Commentaire, String> contentColumn;

    @FXML
    private TableColumn<Commentaire, LocalDate> dateColumn;

    @FXML
    private Button deletebtn;

    @FXML
    private Button translateButton; // New button for translation

    @FXML
    public void initialize() {
        // Set up cell value factories
        idCommentColumn.setCellValueFactory(new PropertyValueFactory<>("id_commentaire"));
        idProjetColumn.setCellValueFactory(new PropertyValueFactory<>("id_projet"));
        contentColumn.setCellValueFactory(new PropertyValueFactory<>("contenue"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date_commentaire"));

        // Make idProjetColumn editable
        idProjetColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        idProjetColumn.setOnEditCommit(event -> {
            // Get the edited value and update the corresponding Commentaire object
            TableColumn.CellEditEvent<Commentaire, Integer> cellEditEvent = (TableColumn.CellEditEvent<Commentaire, Integer>) event;
            Commentaire commentaire = cellEditEvent.getRowValue();
            commentaire.setId_projet(cellEditEvent.getNewValue());

            // Update the Commentaire object in the database
            CommentaireCrud commentaireCrud = new CommentaireCrud();
            commentaireCrud.updateEntite(commentaire);

            // Show a success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Project ID updated successfully!");
            alert.showAndWait();
        });

        // Make contentColumn editable
        contentColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        contentColumn.setOnEditCommit(event -> {
            // Get the edited value and update the corresponding Commentaire object
            TableColumn.CellEditEvent<Commentaire, String> cellEditEvent = (TableColumn.CellEditEvent<Commentaire, String>) event;
            Commentaire commentaire = cellEditEvent.getRowValue();
            commentaire.setContenue(cellEditEvent.getNewValue());

            // Update the Commentaire object in the database
            CommentaireCrud commentaireCrud = new CommentaireCrud();
            commentaireCrud.updateEntite(commentaire);

            // Show a success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Content updated successfully!");
            alert.showAndWait();
        });

        // Enable tableview to start editing with a single click
        commentTableView.setEditable(true);
    }

    @FXML
    public void setComments(List<Commentaire> comments) {
        // Clear existing items in the table view
        commentTableView.getItems().clear();

        // Add new comments to the table view
        commentTableView.getItems().addAll(comments);
    }

    @FXML
    public void deleteComment(ActionEvent event) {
        // Get the selected comment from the table view
        Commentaire selectedComment = commentTableView.getSelectionModel().getSelectedItem();
        if (selectedComment == null) {
            // If no comment is selected, show an error dialog
            showErrorDialog("No Selection", "Please select a Comment to delete.");
            return;
        }

        // Show a confirmation dialog before deleting the comment
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText("Delete Record");
        confirmationAlert.setContentText("Are you sure you want to delete this record?");

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // User confirmed the deletion
                int idToDelete = selectedComment.getId_commentaire();
                CommentaireCrud commentaireCrud = new CommentaireCrud(); // Create an instance of CommentaireCrud
                commentaireCrud.deleteEntite(idToDelete); // Call deleteEntite on the instance
                // Remove the deleted comment from the table view
                commentTableView.getItems().remove(selectedComment);
                // Show a success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Comment deleted successfully!", ButtonType.OK);
                alert.showAndWait();
            }
        });
    }

    @FXML
    public void translateComments(ActionEvent event) {
        // Get the selected comment from the table view
        Commentaire selectedComment = commentTableView.getSelectionModel().getSelectedItem();
        if (selectedComment == null) {
            // If no comment is selected, show an error dialog
            showErrorDialog("No Selection", "Please select a Comment to translate.");
            return;
        }

        try {
            // Translate the French comment to English using Translator class
            String frenchComment = selectedComment.getContenue();
            String translatedComment = Translator.translate("fr", "en", frenchComment);


            // Update the Commentaire object in the table with the translated comment
            selectedComment.setContenue(translatedComment);

            // Refresh the table view to reflect the changes
            commentTableView.refresh();
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Translation Error", "An error occurred while translating the comment.");
        }
    }

    private void showErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
