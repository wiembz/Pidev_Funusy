package pidev.esprit.Controllers.Investissement;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import pidev.esprit.Entities.Investissement;
import pidev.esprit.Entities.Projet;
import pidev.esprit.Services.InvestissementServices;
import pidev.esprit.Services.ProjetServices;

import java.util.List;

public class OverviewProjectController {
    @FXML
    private TableView<Projet> projetTable;
    @FXML
    private TableColumn<Projet, Integer> id_projetColumn;
    @FXML
    private TableColumn<Projet, String> nom_projetColumn;
    @FXML
    private TableColumn<Projet, Float> montant_reqColumn;
    @FXML
    private TableColumn<Projet, String> descriptionColumn;
    @FXML
    private TableColumn<Projet, List<Investissement>> investissementsColumn;

    private ProjetServices projetServices;
    private InvestissementServices investissementServices;

    public OverviewProjectController() {
        projetServices = new ProjetServices();
        investissementServices = new InvestissementServices();
    }

    @FXML
    private void initialize() {
        // Initialize tables
        id_projetColumn.setCellValueFactory(new PropertyValueFactory<>("id_projet"));
        nom_projetColumn.setCellValueFactory(new PropertyValueFactory<>("nom_projet"));
        montant_reqColumn.setCellValueFactory(new PropertyValueFactory<>("montant_req"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        investissementsColumn.setCellValueFactory(cellData -> {
            Projet projet = cellData.getValue();
            List<Investissement> investissements = investissementServices.findInvestmentsByProjectId(projet.getId_projet());
            return new ReadOnlyObjectWrapper<>(investissements);
        });

        investissementsColumn.setCellFactory(column -> {
            return new TableCell<Projet, List<Investissement>>() {
                @Override
                protected void updateItem(List<Investissement> investissements, boolean empty) {
                    super.updateItem(investissements, empty);
                    if (investissements == null || empty) {
                        setText(null);
                    } else {
                        StringBuilder stringBuilder = new StringBuilder();
                        investissements.forEach(investissement -> stringBuilder.append(investissement.getId_investissement()).append(", "));
                        setText(stringBuilder.toString());
                    }
                }
            };
        });

        // Add row click listener to open investment details
        projetTable.setRowFactory(tv -> {
            TableRow<Projet> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    Projet selectedProjet = row.getItem();
                    List<Investissement> investissements = investissementServices.findInvestmentsByProjectId(selectedProjet.getId_projet());
                    showInvestmentsDetailsDialog(investissements);
                }
            });
            return row;
        });
        loadProjects();
    }

    private void loadProjects() {
        List<Projet> projetList = projetServices.afficherEntite();
        ObservableList<Projet> projetData = FXCollections.observableArrayList(projetList);
        projetTable.setItems(projetData);
    }

    private void showInvestmentsDetailsDialog(List<Investissement> investissements) {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Investment Details");
        dialog.setHeaderText("Investments for selected project");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(5);

        gridPane.addRow(0, new Label("Investment ID"), new Label("User ID"), new Label("Amount"), new Label("Investment Date"));

        int row = 1;
        for (Investissement investissement : investissements) {
            gridPane.addRow(row++, new Label(String.valueOf(investissement.getId_investissement())),
                    new Label(String.valueOf(investissement.getId_user())),
                    new Label(String.valueOf(investissement.getMontant())),
                    new Label(String.valueOf(investissement.getDate_investissement())));
        }

        dialog.getDialogPane().setContent(gridPane);
        dialog.showAndWait();
    }

}
