package pidev.esprit.Controllers.Credit;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pidev.esprit.Entities.Credit;
import pidev.esprit.Entities.CreditGarantieWrapper;
import pidev.esprit.Services.CreditCrud;
import pidev.esprit.Services.GarantieCrud;
import pidev.esprit.Entities.Garantie;
import pidev.esprit.Entities.Nature;
import pidev.esprit.Controllers.Credit.GarantieController;
import pidev.esprit.Controllers.Credit.AjouterCredit;

import java.util.List;

public class AdminCreditController {

    @FXML
    private TableView<Credit> tableAdmin;

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
    }


    @FXML
    private void handleAcceptedCredit() {
        CreditGarantieWrapper selectedWrapper = (CreditGarantieWrapper) tableAdmin.getSelectionModel().getSelectedItem();
        if (selectedWrapper != null) {
            Credit selectedCredit = selectedWrapper.getCredit();
            selectedCredit.setStatus("Accepted"); // Mettre à jour le statut localement
            CreditCrud creditCrud = new CreditCrud();
            creditCrud.updateCreditStatus(selectedCredit.getId_credit(), "Accepted"); // Mettre à jour dans la base de données


            // Rafraîchir la TableView pour refléter les changements
            refreshTableView();
        }
    }

    @FXML
    private void handleRejectedCredit() {
        CreditGarantieWrapper selectedWrapper = (CreditGarantieWrapper) tableAdmin.getSelectionModel().getSelectedItem();
        if (selectedWrapper != null) {
            Credit selectedCredit = selectedWrapper.getCredit();
            selectedCredit.setStatus("Rejected"); // Mettre à jour le statut localement
            CreditCrud creditCrud = new CreditCrud();
            creditCrud.updateCreditStatus(selectedCredit.getId_credit(), "Rejected"); // Mettre à jour dans la base de données

            // Rafraîchir la TableView pour refléter les changements
            refreshTableView();
        }
    }

    private void refreshTableView() {
        // Effacer le TableView et recharger les données
        tableAdmin.getItems().clear();
        initialize(); // Recharger les données
    }

}