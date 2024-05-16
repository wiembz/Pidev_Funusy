package pidev.esprit.Controllers.Investissement;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pidev.esprit.Entities.Projet;
import pidev.esprit.Services.ProjetServices;

import java.util.List;
import java.util.Optional;

public class ProjectListController {
    @FXML
    private TableView<Projet> projectTableView;

    private InvestissementController parentController;
    private ProjetServices projetServices;
    private Stage blurStage;
    @FXML
    private Button selectButton;


    public ProjectListController() {
        projetServices = new ProjetServices();
    }

    @FXML
    private void initialize() {
        populateProjectTable();
    }

    private void populateProjectTable() {
        List<Projet> projects = projetServices.afficherEntite();
        projectTableView.getItems().clear(); // Clear existing items
        projectTableView.getItems().addAll(projects);

        TableColumn<Projet, Integer> id_projetCol = new TableColumn<>("ID");
        id_projetCol.setCellValueFactory(new PropertyValueFactory<>("id_projet"));

        TableColumn<Projet, String> nom_projetCol = new TableColumn<>("Name");
        nom_projetCol.setCellValueFactory(new PropertyValueFactory<>("nom_projet"));

        TableColumn<Projet, Float> montant_reqCol = new TableColumn<>("Required Amount");
        montant_reqCol.setCellValueFactory(new PropertyValueFactory<>("montant_req"));

        TableColumn<Projet, String> type_projetCol = new TableColumn<>("Type");
        type_projetCol.setCellValueFactory(new PropertyValueFactory<>("type_projet"));

        TableColumn<Projet, String> descriptionCol = new TableColumn<>("Description");
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Clear existing columns before adding new ones
        projectTableView.getColumns().clear();
        projectTableView.getColumns().addAll(id_projetCol, nom_projetCol, montant_reqCol, type_projetCol, descriptionCol);
    }


    @FXML
    private void handleSelectButtonAction() {
        Projet selectedProject = projectTableView.getSelectionModel().getSelectedItem();

        if (selectedProject == null) {
            showErrorDialog("No Selection", "Please select a project to invest in.");
            return;
        }

        if (parentController != null) {
            parentController.setSelectedProject(selectedProject);
        } else {
            System.err.println("Parent controller is not set!");
        }
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