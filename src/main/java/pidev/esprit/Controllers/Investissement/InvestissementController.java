package pidev.esprit.Controllers.Investissement;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import pidev.esprit.Entities.Investissement;
import pidev.esprit.Entities.Projet;
import pidev.esprit.Services.InvestissementServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import pidev.esprit.Services.ProjetServices;

import java.io.File;
import java.net.MalformedURLException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class InvestissementController {
    @FXML
    private TextField id_userField;
    @FXML
    private TextField montantField;
    @FXML
    private TextField periodeField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TableView<Investissement> table;
    @FXML
    private TableColumn<Investissement, Integer> id_investissement;
    @FXML
    private TableColumn<Investissement, Integer> id_user;
    @FXML
    private TableColumn<Investissement, Float> montant;
    @FXML
    private TableColumn<Investissement, Date> date_investissement;
    @FXML
    private TableColumn<Investissement, Integer> periode;
    @FXML
    private TableColumn<Investissement, Integer> id_projet;
    @FXML
    private WebView gifWebView;
    private ProjetServices projetServices;
    private InvestissementServices investissementServices;

    public InvestissementController() {
        investissementServices = new InvestissementServices();
        projetServices = new ProjetServices();
    }

    @FXML
    private void initialize() {
        setupCellFactories();
        populateInvestissementTable();
    }

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    private void setupCellFactories() {
        // Set cell factories for each editable column
        montant.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        periode.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        date_investissement.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Date>() {
            @Override
            public String toString(Date date) {
                if (date != null) {
                    return dateFormatter.format(date.toLocalDate());
                }
                return null;
            }
            @Override
            public Date fromString(String string) {
                if (string != null && !string.trim().isEmpty()) {
                    return Date.valueOf(LocalDate.parse(string, dateFormatter));
                }
                return null;
            }
        }));

        // Set cell value factories for each column
        id_user.setCellValueFactory(new PropertyValueFactory<>("id_user"));
        montant.setCellValueFactory(new PropertyValueFactory<>("montant"));
        periode.setCellValueFactory(new PropertyValueFactory<>("periode"));
        date_investissement.setCellValueFactory(new PropertyValueFactory<>("date_investissement"));
    }
    @FXML
    private void handleCellEditCommit(TableColumn.CellEditEvent<Investissement, ?> event) {
        // Handle cell edit commit
        TableView.TableViewSelectionModel<Investissement> selectionModel = table.getSelectionModel();
        Investissement selectedInvestissement = selectionModel.getSelectedItem();
        String columnName = event.getTableColumn().getText();

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText("Edit Record");
        confirmationAlert.setContentText("Are you sure you want to update this record in column '" + columnName + "'?");

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Update the entity in the database
                switch (columnName) {
                    case "Id_user":

                        selectedInvestissement.setId_user((Integer) event.getNewValue());
                        break;
                    case "Montant":
                        selectedInvestissement.setMontant((Float) event.getNewValue());
                        break;
                    case "Date_investment":
                        selectedInvestissement.setDate_investissement((Date) event.getNewValue());
                        break;
                    case "Periode":
                        selectedInvestissement.setPeriode((Integer) event.getNewValue());
                        break;
                }
                investissementServices.updateEntite(selectedInvestissement);
            } else {
                // Refresh the table to revert changes if the user cancels
                table.refresh();
            }
        });

    }
    private void populateInvestissementTable() {
        List<Investissement> investissements = investissementServices.afficherEntite();
        ObservableList<Investissement> investissementData = FXCollections.observableArrayList(investissements);

        id_investissement.setCellValueFactory(new PropertyValueFactory<>("id_investissement"));
        id_user.setCellValueFactory(new PropertyValueFactory<>("id_user"));
        montant.setCellValueFactory(new PropertyValueFactory<>("montant"));
        date_investissement.setCellValueFactory(new PropertyValueFactory<>("date_investissement"));
        periode.setCellValueFactory(new PropertyValueFactory<>("periode"));
        id_projet.setCellValueFactory(new PropertyValueFactory<>("id_projet"));


        table.setItems(investissementData);
    }
    private void openProjectSelectionDialog() {
        // Fetch the list of projects
        List<Projet> projects = projetServices.afficherEntite();
        ObservableList<Projet> projectList = FXCollections.observableArrayList(projects);

        // Create a dialog to display the list of projects
        Dialog<Projet> dialog = new Dialog<>();
        dialog.setTitle("Select a Project");
        dialog.setHeaderText("Choose a project to invest in:");

        // Set the buttons
        ButtonType selectButtonType = new ButtonType("Select", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(selectButtonType, ButtonType.CANCEL);

        // Create a TableView to display projects
        TableView<Projet> projectTableView = new TableView<>();
        projectTableView.setItems(projectList);

        // Define columns for the TableView
        TableColumn<Projet, Integer> id_projetCol = new TableColumn<>("ID");
        id_projetCol.setCellValueFactory(new PropertyValueFactory<>("id_projet"));

        TableColumn<Projet, String> nom_projetCol = new TableColumn<>("Name");
        nom_projetCol.setCellValueFactory(new PropertyValueFactory<>("nom_projet"));

        TableColumn<Projet, Float> montant_reqCol = new TableColumn<>("Required Amount");
        montant_reqCol.setCellValueFactory(new PropertyValueFactory<>("montant_req"));

        projectTableView.getColumns().addAll(id_projetCol, nom_projetCol, montant_reqCol);

        // Enable selecting a single row
        projectTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Set the dialog content
        dialog.getDialogPane().setContent(projectTableView);

        // Convert the result to a project object when the select button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == selectButtonType) {
                return projectTableView.getSelectionModel().getSelectedItem();
            }
            return null;
        });

        // Show the dialog and wait for user action
        Optional<Projet> result = dialog.showAndWait();

        // If the user selects a project, proceed with adding the investment
        result.ifPresent(selectedProject -> {
            addInvestment(selectedProject);
        });
    }

    @FXML
    private void addInvestment(Projet selectedProject) {
        // Get input values
        // Get input values
        String id_userText = id_userField.getText().trim();
        String montantText = montantField.getText().trim();
        String periodeText = periodeField.getText().trim();
        LocalDate selectedDate = datePicker.getValue();

        // Check for empty fields
        if (id_userText.isEmpty() || montantText.isEmpty() || periodeText.isEmpty() || selectedDate == null) {
            showErrorDialog("Empty fields", "Please fill all the fields.");
            return;
        }

        int id_user;
        float montant;
        int periode;
        try {
            id_user = Integer.parseInt(id_userText);
            montant = Float.parseFloat(montantText);
            periode = Integer.parseInt(periodeText);
        } catch (NumberFormatException e) {
            showErrorDialog("Invalid input", "Please enter valid numeric values");
            return;
        }

        if (montant < 0) {
            showErrorDialog("Invalid montant", "Montant cannot be negative.");
            return;
        }

        LocalDate currentDate = LocalDate.now(); // Get the current date
        if (selectedDate.isBefore(currentDate)) {
            showErrorDialog("Invalid date", "Investissement date cannot be in the past.");
            return;
        }

        if (periode < 0) {
            showErrorDialog("Invalid periode", "Periode cannot be negative.");
            return;
        }

        Date date_inv = java.sql.Date.valueOf(selectedDate);
        Investissement investissement = new Investissement();
        investissement.setId_user(id_user);
        investissement.setMontant(montant);
        investissement.setPeriode(periode);
        investissement.setDate_investissement(date_inv);
        investissement.setId_projet(selectedProject.getId_projet());

        if (investissementServices.EntiteExists(investissement)) {
            showErrorDialog("Investment exists", "An investment with the same attributes already exists.");
            return;
        }

        investissementServices.ajouterEntite(investissement);
        clearFields();
        populateInvestissementTable();
        showSuccessGif();
//        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Investissement added successfully!", ButtonType.OK);
//        alert.showAndWait();

    }
    @FXML
    private void handleAddButtonAction() {
        openProjectSelectionDialog();
    }
    @FXML
    private void handleDeleteButtonAction() {
        // Get the selected item from the table
        Investissement selectedInvestissement = table.getSelectionModel().getSelectedItem();

        if (selectedInvestissement == null) {
            // If no item is selected, show an error message
            showErrorDialog("No Selection", "Please select an investment to delete.");
            return;
        }

        // Show confirmation dialog
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText("Delete Record");
        confirmationAlert.setContentText("Are you sure you want to delete this record?");

        // Show the confirmation dialog and wait for the user's response
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // User confirmed the delet
                // ion
                int idToDelete = selectedInvestissement.getId_investissement();
                investissementServices.deleteEntite(idToDelete);

                // Remove the item from the table
                table.getItems().remove(selectedInvestissement);
            }
        });
    }
    private void clearFields() {
        montantField.clear();
        periodeField.clear();
        id_userField.clear();
        datePicker.getEditor().clear(); // Clear the DatePicker
    }
    private void showErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showSuccessGif() {
        String gifPath = "src/main/resources/Assets/giphyy.gif";

        try {
            // Convert the local file path to a URL
            String fileUrl = new File(gifPath).toURI().toURL().toString();

            // Load the GIF from the local file into the WebView
            String htmlContent ="<html><body style=\"margin:0;overflow:hidden;\">"
                    + "<img id=\"gif\" src=\"" + fileUrl + "\" style=\"width:100%;height:100%;object-fit:contain;\">"
                    + "<script>"
                    + "var gif = document.getElementById('gif');"
                    + "setTimeout(function() { gif.style.display = 'none'; }, 3000);"
                    + "</script>"
                    + "</body></html>";

            WebEngine webEngine = gifWebView.getEngine();
            webEngine.loadContent(htmlContent);

            // Make the WebView visible
            gifWebView.setVisible(true);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}