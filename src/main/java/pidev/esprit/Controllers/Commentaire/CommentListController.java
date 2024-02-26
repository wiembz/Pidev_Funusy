package pidev.esprit.Controllers.Commentaire;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import pidev.esprit.Entities.Commentaire;
import pidev.esprit.Services.CommentaireCrud;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
    public void initialize() {
        // Set up cell value factories
        idCommentColumn.setCellValueFactory(new PropertyValueFactory<>("id_commentaire"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date_commentaire"));
        contentColumn.setCellValueFactory(new PropertyValueFactory<>("contenue"));
        idProjetColumn.setCellValueFactory(new PropertyValueFactory<>("id_projet"));


        // Make idProjetColumn editable
        idProjetColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        idProjetColumn.setOnEditCommit(event -> {
            // Get the edited value and update the corresponding Commentaire object
            Commentaire commentaire = event.getRowValue();
            commentaire.setId_projet(event.getNewValue());

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
            Commentaire commentaire = event.getRowValue();
            commentaire.setContenue(event.getNewValue());

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

    private void showErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
