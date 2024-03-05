package pidev.esprit.Controllers.Credit;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pidev.esprit.Entities.Credit;
import pidev.esprit.Entities.CreditGarantieWrapper;
import pidev.esprit.Services.CreditCrud;
import pidev.esprit.Services.GarantieCrud;
import pidev.esprit.Entities.Garantie;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static pidev.esprit.Controllers.Credit.SmsCredit.sendSMS;

public class AdminCreditController {

    @FXML
    private TableView<CreditGarantieWrapper> tableAdmin;

    @FXML
    private TableColumn<Credit, Integer> id_credit;

    @FXML
    private TableColumn<Garantie, Integer> id_garantie;

    @FXML
    private TableColumn<Credit, Double> montant_credit;

    @FXML
    private TableColumn<Credit, Integer> duree_credit;

    @FXML
    private TableColumn<Credit, Double> taux_credit;
    @FXML
    private TableColumn<Credit, String> Status;
    @FXML
    private TableColumn<Garantie, String> nature_garantie;

    @FXML
    private TableColumn<Garantie, Double> Valeur_Garantie;

    @FXML
    private TableColumn<Garantie, String> preuve;

    @FXML
    private Button acceptedCredit;

    @FXML
    private Button rejectedCredit;

    @FXML
    private TextField tf_search; // Champ de recherche

    public void initialize() {
        // Configurer les colonnes de crédit
        id_credit.setCellValueFactory(new PropertyValueFactory<>("id_credit"));
        montant_credit.setCellValueFactory(new PropertyValueFactory<>("montant_credit"));
        duree_credit.setCellValueFactory(new PropertyValueFactory<>("duree_credit"));
        taux_credit.setCellValueFactory(new PropertyValueFactory<>("taux_credit"));
        Status.setCellValueFactory(new PropertyValueFactory<>("Status"));
        // Configurer les colonnes de garantie
        id_garantie.setCellValueFactory(new PropertyValueFactory<>("id_garantie"));
        nature_garantie.setCellValueFactory(new PropertyValueFactory<>("nature_garantie"));
        Valeur_Garantie.setCellValueFactory(new PropertyValueFactory<>("Valeur_Garantie"));
        preuve.setCellValueFactory(new PropertyValueFactory<>("preuve"));

        // Récupérer les données de crédit
        CreditCrud creditCrud = new CreditCrud();
        List<Credit> credits = creditCrud.getAllCredits();

        // Récupérer les garanties associées à chaque crédit
        GarantieCrud garantieCrud = new GarantieCrud();
        for (Credit credit : credits) {
            List<Garantie> garanties = garantieCrud.getGarantiesByCreditId(credit.getId_credit());
            // Ajouter les crédits et garanties associés à la TableView
            for (Garantie garantie : garanties) {
                CreditGarantieWrapper wrapper = new CreditGarantieWrapper(credit, garantie);
                tableAdmin.getItems().add(wrapper);
            }
        }

        // Appeler la méthode de filtrage lorsque le texte dans le champ de recherche change
        tf_search.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                // Si le champ de recherche est vide, rafraîchissez la liste
                refreshTableView();
            } else {
                // Sinon, effectuez la recherche
                tableAdmin(newValue);
            }
        });


        Preuvebutton();
    }

    private void tableAdmin(String newValue) {
        filterTableView(newValue);
    }

    @FXML
    private void handleAcceptedCredit() {
        CreditGarantieWrapper selectedWrapper = tableAdmin.getSelectionModel().getSelectedItem();
        if (selectedWrapper != null) {
            Credit selectedCredit = selectedWrapper.getCredit();
            selectedCredit.setStatus("Accepted");
            CreditCrud creditCrud = new CreditCrud();
            creditCrud.updateCreditStatus(selectedCredit.getId_credit(), "Accepted");
            refreshTableView();


        }
    }

    @FXML
    private void handleRejectedCredit() {
        CreditGarantieWrapper selectedWrapper = tableAdmin.getSelectionModel().getSelectedItem();
        if (selectedWrapper != null) {
            Credit selectedCredit = selectedWrapper.getCredit();
            selectedCredit.setStatus("Rejected");
            CreditCrud creditCrud = new CreditCrud();
            creditCrud.updateCreditStatus(selectedCredit.getId_credit(), "Rejected");
            refreshTableView();

        }
    }

    private void refreshTableView() {
        tableAdmin.getItems().clear();
        initialize();
    }

    private void Preuvebutton() {
        preuve.setCellFactory(tc -> new TableCell<Garantie, String>() {
            @Override
            protected void updateItem(String preuve, boolean empty) {
                super.updateItem(preuve, empty);
                if (empty) {
                    setText(null);
                } else {
                    Button preuveButton = new Button("Data File");
                    preuveButton.setOnAction(event -> openFile(preuve));
                    setGraphic(preuveButton);
                }
            }
        });
    }

    private void openFile(String filePath) {
        try {
            Desktop.getDesktop().open(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void filterTableView(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            tableAdmin.setItems(getAllCreditGarantieWrappers());
            return;
        }
        ObservableList<CreditGarantieWrapper> filteredList = FXCollections.observableArrayList();
        for (CreditGarantieWrapper wrapper : getAllCreditGarantieWrappers()) {
            if (wrapper.getGarantie().getNature_garantie().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(wrapper);
            }
        }
        tableAdmin.getItems().clear();
        tableAdmin.setItems(filteredList);
        tableAdmin.refresh();

    }

    private ObservableList<CreditGarantieWrapper> getAllCreditGarantieWrappers() {
        ObservableList<CreditGarantieWrapper> wrappers = FXCollections.observableArrayList();
        for (CreditGarantieWrapper wrapper : tableAdmin.getItems()) {
            wrappers.add(wrapper);
        }
        return wrappers;
    }
}
