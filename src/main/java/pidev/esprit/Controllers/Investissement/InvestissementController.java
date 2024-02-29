package pidev.esprit.Controllers.Investissement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
import java.io.IOException;
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
    private Projet selectedProject;

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

    @FXML
    private void handleAddButtonAction() {
        if (!validateInput()) {
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProjectList.fxml"));
            Parent root = loader.load();

            ProjectListController controller = loader.getController();
            controller.setParentController(this); // Pass a reference to this controller

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Select a Project");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSelectedProject(Projet project) {
        this.selectedProject = project;
        addInvestment();
    }

    private boolean validateInput() {
        String id_userText = id_userField.getText().trim();
        String montantText = montantField.getText().trim();
        String periodeText = periodeField.getText().trim();
        LocalDate selectedDate = datePicker.getValue();

        if (id_userText.isEmpty() || montantText.isEmpty() || periodeText.isEmpty() || selectedDate == null) {
            showErrorDialog("Empty fields", "Please fill all the fields.");
            return false;
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
            return false;
        }

        if (montant < 0) {
            showErrorDialog("Invalid montant", "Montant cannot be negative.");
            return false;
        }

        LocalDate currentDate = LocalDate.now(); // Get the current date
        if (selectedDate.isBefore(currentDate)) {
            showErrorDialog("Invalid date", "Investissement date cannot be in the past.");
            return false;
        }

        if (periode < 0) {
            showErrorDialog("Invalid periode", "Periode cannot be negative.");
            return false;
        }

        return true;
    }

    private void addInvestment() {
        int id_user = Integer.parseInt(id_userField.getText().trim());
        float montant = Float.parseFloat(montantField.getText().trim());
        int periode = Integer.parseInt(periodeField.getText().trim());
        LocalDate selectedDate = datePicker.getValue();
        Date date_inv = Date.valueOf(selectedDate);

        // Create investment object
        Investissement investissement = new Investissement();
        investissement.setId_user(id_user);
        investissement.setMontant(montant);
        investissement.setPeriode(periode);
        investissement.setDate_investissement(date_inv);
        investissement.setId_projet(selectedProject.getId_projet());

        // Add investment
        investissementServices.ajouterEntite(investissement);
        clearFields();
        showSuccessGif();
        populateInvestissementTable();
    }

    @FXML
    private void handleDeleteButtonAction() {
        Investissement selectedInvestissement = table.getSelectionModel().getSelectedItem();

        if (selectedInvestissement == null) {
            showErrorDialog("No Selection", "Please select an investment to delete.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText("Delete Record");
        confirmationAlert.setContentText("Are you sure you want to delete this record?");

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {

                int idToDelete = selectedInvestissement.getId_investissement();
                List<Investissement> relatedInvestments = investissementServices.findInvestmentsByProjectId(selectedInvestissement.getId_projet());

                for (Investissement investment : relatedInvestments) {
                    investissementServices.deleteEntite(investment.getId_investissement());
                    table.getItems().remove(investment);
                }

                table.getItems().remove(selectedInvestissement);
            }
        });
    }
    private void clearFields() {
        montantField.clear();
        periodeField.clear();
        id_userField.clear();
        datePicker.getEditor().clear();
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
            String fileUrl = new File(gifPath).toURI().toURL().toString();

            String htmlContent ="<html><body style=\"margin:0;overflow:hidden;\">"
                    + "<img id=\"gif\" src=\"" + fileUrl + "\" style=\"width:100%;height:100%;object-fit:contain;\">"
                    + "<script>"
                    + "var gif = document.getElementById('gif');"
                    + "setTimeout(function() { gif.style.display = 'none'; }, 3000);"
                    + "</script>"
                    + "</body></html>";

            WebEngine webEngine = gifWebView.getEngine();
            webEngine.loadContent(htmlContent);

            gifWebView.setVisible(true);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}