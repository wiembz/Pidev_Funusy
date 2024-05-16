package pidev.esprit.Controllers.Investissement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import pidev.esprit.Entities.Investissement;
import pidev.esprit.Entities.Projet;
import pidev.esprit.Services.InvestissementServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.web.WebView;
import javafx.scene.Node;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import pidev.esprit.Tools.MyConnection;

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
    private Spinner periodeField;
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
    private InvestissementServices investissementServices;
    private Projet selectedProject;
    @FXML
    private WebView gifWebView;
    @FXML
    private TextField searchField;
    private ObservableList<Investissement> filteredInvestissementData;

    private ObservableList<Investissement> investissementData;

    public InvestissementController() {
        investissementServices = new InvestissementServices();
    }

    @FXML
    private void initialize() {
        setupCellFactories();
        investissementData = FXCollections.observableArrayList();
        populateInvestissementTable();
        filteredInvestissementData = FXCollections.observableArrayList();

//        int loggedInUserId = MyConnection.getInstance().getLoggedInUserId();
//        id_userField.setText(String.valueOf(loggedInUserId));

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                table.setItems(investissementData);
            } else {
                handleSearch();
            }
        });
    }


    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private void setupCellFactories() {
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
        //id_user.setCellValueFactory(new PropertyValueFactory<>("id_user"));
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
                    case "id_investissement":
                        selectedInvestissement.setId_investissement((Integer) event.getNewValue());
                        break;
//                    case "id_user":
//                        selectedInvestissement.setId_user((Integer) event.getNewValue());
//                        break;
                    case "montant":
                        selectedInvestissement.setMontant((Float) event.getNewValue());
                        break;
                    case "date_investissement":
                        selectedInvestissement.setDate_investissement((Date) event.getNewValue());
                        break;
                    case "periode":
                        selectedInvestissement.setPeriode((Integer) event.getNewValue());
                        break;
                    case "id_projet":
                        selectedInvestissement.setId_projet((Integer) event.getNewValue());
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
        montant.setCellValueFactory(new PropertyValueFactory<>("montant"));
        periode.setCellValueFactory(new PropertyValueFactory<>("periode"));
        date_investissement.setCellValueFactory(new PropertyValueFactory<>("date_investissement"));
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
        showInvestmentInformation(new Dialog<>());
    }

    @FXML
    public void showInvestmentInformation(Dialog<Void> dialog) {
        dialog.setTitle("Investment Information");
        dialog.setHeaderText("Investment Details");

        ButtonType confirmButton = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButton, ButtonType.CANCEL);

        VBox vbox = new VBox();
        vbox.setSpacing(10);

        Label projectLabel = new Label("Project: " + selectedProject.getNom_projet());
        Label amountLabel = new Label("Investment Amount: " + montantField.getText()+ " DT");
        Label periodLabel = new Label("Investment Period: " + periodeField.getValue() + " months");

        float profit = ProfitController.calculateProfit(Float.parseFloat(montantField.getText()), Integer.parseInt(periodeField.getValue().toString()), selectedProject.getType_projet());
        Label profitLabel = new Label("Expected Profit: " + profit+ " DT");

        vbox.getChildren().addAll(projectLabel, amountLabel, periodLabel, profitLabel);

        dialog.getDialogPane().setContent(vbox);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButton) {
                addInvestment();
            }
            return null;
        });

        dialog.showAndWait();
    }

    private boolean validateInput() {
   //     String id_userText = id_userField.getText().trim();
        String montantText = montantField.getText().trim();
        String periodeText = periodeField.getValue().toString();
        LocalDate selectedDate = datePicker.getValue();

        if (montantText.isEmpty() || periodeText.isEmpty() || selectedDate == null) {
            showErrorDialog("Empty fields", "Please fill all the fields.");
            return false;
        }

        int id_user;
        float montant;
        int periode;
        try {
           // id_user = Integer.parseInt(id_userText);
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
        int loggedInUserId = MyConnection.getInstance().getLoggedInUserId();

        float montant = Float.parseFloat(montantField.getText().trim());
        int periode = Integer.parseInt(periodeField.getValue().toString());
        LocalDate selectedDate = datePicker.getValue();
        Date date_inv = Date.valueOf(selectedDate);

        Investissement investissement = new Investissement();
        investissement.setId_user(loggedInUserId);
        investissement.setMontant(montant);
        investissement.setPeriode(periode);
        investissement.setDate_investissement(date_inv);
        investissement.setId_projet(selectedProject.getId_projet());

        investissementServices.ajouterEntite(investissement);
        clearFields();
        showSuccessGif();
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Added Investment", ButtonType.OK);
        alert.show();

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

            gifWebView.getEngine().loadContent(htmlContent);

            gifWebView.setVisible(true);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText().toLowerCase();
        List<Investissement> searchResults = investissementServices.searchInvestments(query);
        ObservableList<Investissement> searchResultList = FXCollections.observableArrayList(searchResults);
        table.setItems(searchResultList);
    }
}
