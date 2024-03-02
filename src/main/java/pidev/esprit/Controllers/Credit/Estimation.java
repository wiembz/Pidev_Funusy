package pidev.esprit.Controllers.Credit;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pidev.esprit.Entities.Echeance;

public class Estimation {
    @FXML
    private TextField textFieldMontant;

    @FXML
    private TextField textFieldDuree;

    @FXML
    private TextField textFieldTaux;

    @FXML
    private Button buttonValider;

    @FXML
    private TableView<Echeance> tableView;

    private CreditModel creditModel;

    public class CreditModel {
        public ObservableList<Echeance> calculerEcheances(double montant, int duree, double taux) {
            ObservableList<Echeance> echeances = FXCollections.observableArrayList();
            double mensualite = montant * (taux / 100) / (1 - Math.pow(1 + (taux / 100), -duree));
            double valeurResiduelle = montant;
            for (int i = 1; i <= duree; i++) {
                double interets = valeurResiduelle * (taux / 100);
                double principal = mensualite - interets;
                valeurResiduelle -= principal;
                Echeance echeance = new Echeance(i, principal, valeurResiduelle, interets, mensualite);
                echeances.add(echeance);
            }
            return echeances;
        }

        public void Estimation() {
            creditModel = new CreditModel();
        }

        @FXML
        private void initialize() {
            buttonValider.setOnAction(event -> {
                double montant = Double.parseDouble(textFieldMontant.getText());
                int duree = Integer.parseInt(textFieldDuree.getText());
                double taux = Double.parseDouble(textFieldTaux.getText());

                tableView.setItems(creditModel.calculerEcheances(montant, duree, taux));
            });
        }
    }
}


