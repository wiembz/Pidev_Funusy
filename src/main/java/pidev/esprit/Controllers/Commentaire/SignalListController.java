package pidev.esprit.Controllers.Commentaire;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import pidev.esprit.Entities.Signal;
import pidev.esprit.Services.SignalCrud;

import java.time.LocalDate;
import java.util.List;

public class SignalListController {
    @FXML
    private TableView<Signal> signalTableView;

    @FXML
    private TableColumn<Signal, Integer> idSignalColumn;

    @FXML
    private TableColumn<Signal, Integer> idCommentColumn;

    @FXML
    private TableColumn<Signal, String> descriptionColumn;

    @FXML
    private TableColumn<Signal, LocalDate> dateSignalColumn;

    @FXML
    private Button deleteButton;

    @FXML
    public void initialize() {
        // Set up cell value factories
        idSignalColumn.setCellValueFactory(new PropertyValueFactory<>("id_signal"));
        idCommentColumn.setCellValueFactory(new PropertyValueFactory<>("id_commentaire"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateSignalColumn.setCellValueFactory(new PropertyValueFactory<>("date_signal"));

        // Enable tableview to start editing with a single click
        signalTableView.setEditable(true);

        // Make idCommentColumn editable
        idCommentColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        idCommentColumn.setOnEditCommit(event -> {
            // Get the edited value and update the corresponding Signal object
            Signal signal = event.getRowValue();
            signal.setId_commentaire(event.getNewValue());

            // Update the Signal object in the database
            SignalCrud signalCrud = new SignalCrud();
            signalCrud.updateEntite(signal);

            // Show a success message
            showAlert("Success", "Comment ID updated successfully!");
        });

        // Make DescriptionColumn editable
        descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        descriptionColumn.setOnEditCommit(event -> {
            // Get the edited value and update the corresponding Signal object
            Signal signal = event.getRowValue();
            signal.setDescription(event.getNewValue());

            // Update the Signal object in the database
            SignalCrud signalCrud = new SignalCrud();
            signalCrud.updateEntite(signal);

            // Show a success message
            showAlert("Success", "Description updated successfully!");
        });
    }

    public void setSignals(List<Signal> signals) {
        // Clear existing items in the table view
        signalTableView.getItems().clear();

        // Add new Signals to the table view
        signalTableView.getItems().addAll(signals);
    }

    @FXML
    public void deleteSignal(ActionEvent event) {
        // Get the selected signal from the table view
        Signal selectedSignal = signalTableView.getSelectionModel().getSelectedItem();
        if (selectedSignal == null) {
            // If no signal is selected, show an error dialog
            showErrorDialog("No Selection", "Please select a Signal to delete.");
            return;
        }

        // Show a confirmation dialog before deleting the Signal
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText("Delete Record");
        confirmationAlert.setContentText("Are you sure you want to delete this record?");

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // User confirmed the deletion
                int idToDelete = selectedSignal.getId_signal();
                SignalCrud signalCrud = new SignalCrud(); // Create an instance of SignalCrud
                signalCrud.deleteEntite(idToDelete); // Call deleteEntite on the instance
                // Remove the deleted Signal from the table view
                signalTableView.getItems().remove(selectedSignal);
                // Show a success message
                showAlert("Success", "Signal deleted successfully!");
            }
        });
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
