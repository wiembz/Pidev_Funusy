package pidev.esprit.Controllers.Commentaire;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import pidev.esprit.Entities.Signal;
import pidev.esprit.Services.SignalCrud;
import pidev.esprit.Utils.Translator;

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
    private Button traduction_btn; // Ajout du bouton de traduction

    @FXML
    public void initialize() {
        idSignalColumn.setCellValueFactory(new PropertyValueFactory<>("id_signal"));
        idCommentColumn.setCellValueFactory(new PropertyValueFactory<>("id_commentaire"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateSignalColumn.setCellValueFactory(new PropertyValueFactory<>("date_signal"));

        signalTableView.setEditable(true);

        idCommentColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        idCommentColumn.setOnEditCommit(event -> {
            Signal signal = event.getRowValue();
            signal.setId_commentaire(event.getNewValue());

            SignalCrud signalCrud = new SignalCrud();
            signalCrud.updateEntite(signal);

            showAlert("Success", "Comment ID updated successfully!");
        });

        descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        descriptionColumn.setOnEditCommit(event -> {
            Signal signal = event.getRowValue();
            signal.setDescription(event.getNewValue());

            SignalCrud signalCrud = new SignalCrud();
            signalCrud.updateEntite(signal);

            showAlert("Success", "Description updated successfully!");
        });
    }

    public void setSignals(List<Signal> signals) {
        signalTableView.getItems().clear();
        signalTableView.getItems().addAll(signals);
    }

    @FXML
    public void deleteSignal(ActionEvent event) {
        Signal selectedSignal = signalTableView.getSelectionModel().getSelectedItem();
        if (selectedSignal == null) {
            showErrorDialog("No Selection", "Please select a Signal to delete.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText("Delete Record");
        confirmationAlert.setContentText("Are you sure you want to delete this record?");

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                int idToDelete = selectedSignal.getId_signal();
                SignalCrud signalCrud = new SignalCrud();
                signalCrud.deleteEntite(idToDelete);
                signalTableView.getItems().remove(selectedSignal);
                showAlert("Success", "Signal deleted successfully!");
            }
        });
    }

    // Nouvelle méthode pour traduire le signal
    @FXML
    public void translateSignal(ActionEvent event) {
        Signal selectedSignal = signalTableView.getSelectionModel().getSelectedItem();
        if (selectedSignal == null) {
            showErrorDialog("No Selection", "Please select a Signal to translate.");
            return;
        }

        try {
            String frenchDescription = selectedSignal.getDescription();
            String translatedDescription = Translator.translate("fr", "en", frenchDescription);

            selectedSignal.setDescription(translatedDescription);

            signalTableView.refresh();
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Translation Error", "An error occurred while translating the description.");
        }
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
