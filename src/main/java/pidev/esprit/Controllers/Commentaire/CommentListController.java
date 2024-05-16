package pidev.esprit.Controllers.Commentaire;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import pidev.esprit.Entities.Commentaire;
import pidev.esprit.Entities.Signal;
import pidev.esprit.Services.CommentaireCrud;
import pidev.esprit.Services.SignalCrud;
import pidev.esprit.Utils.Translator;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class CommentListController {
    private String lastSignalContent = "";

    @FXML
    private GridPane commentGrid;

    public void initialize() {
        // Initialize the GridPane if needed
    }

    public void setComments(List<Commentaire> comments) {
        commentGrid.getChildren().clear(); // Clear existing items in the GridPane

        int rowIndex = 0;
        for (Commentaire commentaire : comments) {
            // Create a block for each comment
            AnchorPane commentPane = createCommentBlock(commentaire);
            commentGrid.add(commentPane, 0, rowIndex);
            rowIndex++;
        }
    }

    private AnchorPane createCommentBlock(Commentaire commentaire) {
        AnchorPane commentPane = new AnchorPane();
        commentPane.setPrefWidth(400); // Set preferred width for the comment block
        commentPane.setPrefHeight(50); // Set preferred height for the comment block

        TextArea commentTextArea = new TextArea(commentaire.getContenue());
        commentTextArea.setPrefWidth(350); // Set preferred width for the text area
        commentTextArea.setPrefHeight(30); // Set preferred height for the text area
        commentTextArea.setWrapText(true);
        commentTextArea.setEditable(false);
        AnchorPane.setTopAnchor(commentTextArea, 5.0);
        AnchorPane.setLeftAnchor(commentTextArea, 5.0);

        // Add event handler for editing comment on double click
        commentTextArea.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                commentTextArea.setEditable(true);
                commentTextArea.requestFocus();
            }
        });

        // Add event handler for updating comment on Enter key press
        commentTextArea.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                updateComment(commentTextArea, commentaire);
            }
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> deleteComment(event, commentaire));
        AnchorPane.setBottomAnchor(deleteButton, 5.0);
        AnchorPane.setRightAnchor(deleteButton, 70.0);

        Button signalButton = new Button("Signal");
        signalButton.setOnAction(event -> displayCommentAreaFor(commentaire));
        AnchorPane.setBottomAnchor(signalButton, 5.0);
        AnchorPane.setRightAnchor(signalButton, 130.0);

        Button translateButton = new Button("Translate");
        translateButton.setOnAction(event -> translateComment(event, commentTextArea));
        AnchorPane.setBottomAnchor(translateButton, 5.0);
        AnchorPane.setRightAnchor(translateButton, 190.0);

        commentPane.getChildren().addAll(commentTextArea, deleteButton, signalButton, translateButton);

        return commentPane;
    }

    @FXML
    public void deleteComment(ActionEvent event, Commentaire commentaire) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText("Delete Comment");
        confirmationAlert.setContentText("Are you sure you want to delete this comment?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            CommentaireCrud commentaireCrud = new CommentaireCrud();
            commentaireCrud.deleteEntite(commentaire.getId_commentaire());
            commentGrid.getChildren().remove(((Button) event.getSource()).getParent());
            showAlert("Comment Deleted", "The comment has been successfully deleted.");
        }
    }

    @FXML
    public void translateComment(ActionEvent event, TextArea commentTextArea) {
        String frenchComment = commentTextArea.getText();
        try {
            String translatedComment = Translator.translate("fr", "en", frenchComment);
            commentTextArea.setText(translatedComment);
            showAlert("Translation", "The comment has been translated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Translation Error", "An error occurred while translating the comment.");
        }
    }

    private void updateComment(TextArea commentTextArea, Commentaire commentaire) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText("Update Comment");
        confirmationAlert.setContentText("Are you sure you want to update this comment?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            CommentaireCrud commentaireCrud = new CommentaireCrud();
            commentaire.setContenue(commentTextArea.getText());
            commentaireCrud.updateEntite(commentaire);
            showAlert("Comment Updated", "The comment has been updated successfully.");
            commentTextArea.setEditable(false); // Make the text area non-editable after updating
        }
    }

    private void displayCommentAreaFor(Commentaire commentaire) {
        // Logic for displaying the comment area for signaling
        Alert signalAlert = new Alert(Alert.AlertType.CONFIRMATION);
        signalAlert.setTitle("Signal Confirmation");
        signalAlert.setHeaderText("Signal Comment");
        signalAlert.setContentText("Are you sure you want to signal this comment?");

        Optional<ButtonType> result = signalAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Alert descriptionAlert = new Alert(Alert.AlertType.INFORMATION);
            descriptionAlert.setTitle("Description");
            descriptionAlert.setHeaderText("Signal Description");
            descriptionAlert.setContentText("Please enter your signal description:");

            TextArea descriptionTextArea = new TextArea();
            descriptionTextArea.setPromptText("Enter your signal description here");

            descriptionAlert.getDialogPane().setContent(descriptionTextArea);
            descriptionAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    String description = descriptionTextArea.getText();
                    // Logic to handle the signaled comment and its description
                    addSignalToDatabase(commentaire, description);
                }
            });
        }
    }

    private void addSignalToDatabase(Commentaire commentaire, String description) {
        // Check if the signal description is empty
        if (description.isEmpty()) {
            Alert emptyAlert = new Alert(Alert.AlertType.ERROR, "Please enter your signal description!", ButtonType.OK);
            emptyAlert.show();
            return;
        }

        // Check if the signal is identical to the previous signal
        if (lastSignalContent.equals(description)) {
            // Show an error message for potential spam
            Alert spamAlert = new Alert(Alert.AlertType.ERROR, "Please avoid spamming the same signal!", ButtonType.OK);
            spamAlert.show();
            return;
        }

        // Logic to add the signal to the database
        Signal signal = new Signal();
        signal.setId_commentaire(commentaire.getId_commentaire());
        signal.setDate_signal(LocalDate.now());
        signal.setDescription(description);

        SignalCrud signalCrud = new SignalCrud();
        signalCrud.ajouterEntite(signal);

        // Store the current signal content as the last signal content
        lastSignalContent = description;

        // Show a success message
        showAlert("Signal Added", "The signal has been added successfully!");
    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
