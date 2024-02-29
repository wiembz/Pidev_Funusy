package pidev.esprit.Controllers.Investissement;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pidev.esprit.Entities.Projet;
import pidev.esprit.Services.ProjetServices;

import java.util.List;
import java.util.Optional;

public class ProjectListController {
    @FXML
    private TableView<Projet> projectTableView;

    private InvestissementController parentController;
    private ProjetServices projetServices;

    public ProjectListController() {
        projetServices = new ProjetServices();
    }

    @FXML
    private void initialize() {
        populateProjectTable();
    }

    private void populateProjectTable() {
        List<Projet> projects = projetServices.afficherEntite();
        projectTableView.getItems().addAll(projects);

        TableColumn<Projet, Integer> id_projetCol = new TableColumn<>("ID");
        id_projetCol.setCellValueFactory(new PropertyValueFactory<>("id_projet"));

        TableColumn<Projet, String> nom_projetCol = new TableColumn<>("Name");
        nom_projetCol.setCellValueFactory(new PropertyValueFactory<>("nom_projet"));

        TableColumn<Projet, Float> montant_reqCol = new TableColumn<>("Required Amount");
        montant_reqCol.setCellValueFactory(new PropertyValueFactory<>("montant_req"));

        projectTableView.getColumns().addAll(id_projetCol, nom_projetCol, montant_reqCol);
    }

    @FXML
    private void handleSelectButtonAction() {
        Projet selectedProject = projectTableView.getSelectionModel().getSelectedItem();

        if (selectedProject == null) {
            showErrorDialog("No Selection", "Please select a project to invest in.");
            return;
        }

        parentController.setSelectedProject(selectedProject);
        closeWindow();
    }

    @FXML
    private void handleCancelButtonAction() {
        closeWindow();
    }

    private void closeWindow() {
        projectTableView.getScene().getWindow().hide();
    }

    public void setParentController(InvestissementController parentController) {
        this.parentController = parentController;
    }

    private void showErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
