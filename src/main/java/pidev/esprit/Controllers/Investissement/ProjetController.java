package pidev.esprit.Controllers.Investissement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import pidev.esprit.Entities.Investissement;
import pidev.esprit.Entities.Projet;
import pidev.esprit.Services.ProjetServices;
import pidev.esprit.Entities.ProjectType;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class ProjetController {

    @FXML
    private TextField id_userField;
    @FXML
    private TextField montant_reqField;
    @FXML
    private TextField nom_projetField;
    @FXML
    private TextField longitudeField;
    @FXML
    private TextField latitudeField;
    @FXML
    private ComboBox<ProjectType> type_projetField;
    @FXML
    private TableView<Projet> table;
    @FXML
    private TableColumn<Projet, Integer> id_projet;
    @FXML
    private TableColumn<Projet, Integer> id_user;
    @FXML
    private TableColumn<Projet, Float> montant_req;
    @FXML
    private TableColumn<Projet, String> longitude;
    @FXML
    private TableColumn<Projet, String> latitude;
    @FXML
    private TableColumn<Projet, String> nom_projet;
    @FXML
    private TableColumn<Projet, String> type_projet;

    private ProjetServices projetServices;

    public ProjetController() {
        projetServices = new ProjetServices();
    }

    @FXML
    private void initialize() {
        setupCellFactories();
        populateProjetTable();
        type_projetField.setItems(FXCollections.observableArrayList(ProjectType.values()));
    }
    private void populateProjetTable() {
        List<Projet> projets = projetServices.afficherEntite();
        ObservableList<Projet> observableList = FXCollections.observableArrayList(projets);
        id_projet.setCellValueFactory(new PropertyValueFactory<>("id_projet"));
        id_user.setCellValueFactory(new PropertyValueFactory<>("id_user"));
        montant_req.setCellValueFactory(new PropertyValueFactory<>("montant_req"));
        longitude.setCellValueFactory(new PropertyValueFactory<>("longitude"));
        latitude.setCellValueFactory(new PropertyValueFactory<>("latitude"));
        nom_projet.setCellValueFactory(new PropertyValueFactory<>("nom_projet"));
        type_projet.setCellValueFactory(new PropertyValueFactory<>("type_projet"));
        table.setItems(observableList);
    }
    @FXML
    private void handleAddButtonAction() {
        // Get input values
        String id_userText = id_userField.getText().trim();
        String montant_reqText = montant_reqField.getText().trim();
        String nom_projetText = nom_projetField.getText().trim();
        String longitudeText = longitudeField.getText().trim();
        String latitudeText = latitudeField.getText().trim();
        ProjectType id_type_projet = type_projetField.getValue();

        if (id_userText.isEmpty() || montant_reqText.isEmpty() || nom_projetText.isEmpty() || longitudeText.isEmpty() || latitudeText.isEmpty() || id_type_projet == null) {
            showErrorDialog("Empty fields", "Please fill all the fields.");
            return;
        }

        int id_user;
        float montant_req;
        try {
            id_user = Integer.parseInt(id_userText);
            montant_req = Float.parseFloat(montant_reqText);
        } catch (NumberFormatException e) {
            showErrorDialog("Invalid Input", "Please enter valid numeric values for 'ID User' and 'Montant Req'.");
            return;
        }

        Projet projet = new Projet();
        projet.setId_user(id_user);
        projet.setMontant_req(montant_req);
        projet.setNom_projet(nom_projetText);
        projet.setLongitude(longitudeText);
        projet.setLatitude(latitudeText);
        projet.setType_projet(id_type_projet.toString());

        if (projetServices.EntiteExists(projet)) {
            showErrorDialog("Project Exists", "This project already exists.");
            return;
        }

        projetServices.ajouterEntite(projet);
        populateProjetTable();
        clearFields();
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Project added successfully!", ButtonType.OK);
        alert.showAndWait();
    }

    @FXML
    private void handleDeleteButtonAction() {
        Projet selectedproject = table.getSelectionModel().getSelectedItem();
        if (selectedproject == null) {
            showErrorDialog("No Selection", "Please select a project to delete.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText("Delete Record");
        confirmationAlert.setContentText("Are you sure you want to delete this record?");

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // User confirmed the deletion
                int idToDelete = selectedproject.getId_projet();
                projetServices.deleteEntite(idToDelete);
                populateProjetTable();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Project deleted successfully!", ButtonType.OK);
                alert.showAndWait();
            }
        });
    }

    private void clearFields() {
        id_userField.clear();
        montant_reqField.clear();
        nom_projetField.clear();
        longitudeField.clear();
        latitudeField.clear();
        type_projetField.getSelectionModel().clearSelection();
    }

    private void showErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    private void setupCellFactories() {
        montant_req.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        longitude.setCellFactory(TextFieldTableCell.forTableColumn());
        latitude.setCellFactory(TextFieldTableCell.forTableColumn());
        nom_projet.setCellFactory(TextFieldTableCell.forTableColumn());
        type_projet.setCellFactory(TextFieldTableCell.forTableColumn());

        montant_req.setCellValueFactory(new PropertyValueFactory<>("montant_req"));
        longitude.setCellValueFactory(new PropertyValueFactory<>("longitude"));
        latitude.setCellValueFactory(new PropertyValueFactory<>("latitude"));
        nom_projet.setCellValueFactory(new PropertyValueFactory<>("nom_projet"));
        type_projet.setCellValueFactory(new PropertyValueFactory<>("type_projet"));    }
    @FXML
    public void handleCellEditCommit(TableColumn.CellEditEvent<Projet, ?> event) {
        Projet selectedProject = event.getRowValue();
        String columnName = event.getTableColumn().getText();

        if ("type_projet".equals(columnName)) {
            // If the edited column is type_projet, update the ComboBox items
            type_projetField.setItems(FXCollections.observableArrayList(ProjectType.values()));
        }
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText("Edit Record");
        confirmationAlert.setContentText("Are you sure you want to update this record in column '" + columnName + "'?");

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Update the entity in the database
                try {
                    switch (columnName) {
                        case "Id_projet":
                            selectedProject.setId_projet((Integer) event.getNewValue());
                            break;
                        case "id_user":
                            selectedProject.setId_user((Integer) event.getNewValue());
                            break;
                        case "montant_req":
                            selectedProject.setMontant_req((Float) event.getNewValue());
                            break;
                        case "longitude":
                            selectedProject.setLongitude((String) event.getNewValue());
                            break;
                        case "latitude":
                            selectedProject.setLatitude((String) event.getNewValue());
                            break;
                        case "type_projet":
                            selectedProject.setType_projet((String) event.getNewValue());
                            break;
                        case "Nom Projet":
                            selectedProject.setNom_projet((String) event.getNewValue());
                            break;
                    }
                    projetServices.updateEntite(selectedProject); // Call update method
                } catch (Exception e) {
                    System.err.println("Error updating record: " + e.getMessage());
                }
            } else {
                // Refresh the table to revert changes if the user cancels
                table.refresh();
            }
        });
    }


}