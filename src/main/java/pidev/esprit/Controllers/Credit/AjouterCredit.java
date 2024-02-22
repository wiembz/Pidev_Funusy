package pidev.esprit.Controllers.Credit;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import pidev.esprit.Entities.Credit;
import pidev.esprit.Services.CreditCrud;

import java.net.URL;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class AjouterCredit {
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private Button btn;
    @FXML
    private TextField tf_montant;
    @FXML
    private TextField tf_duree;
    @FXML
    private TextField tf_taux;
    @FXML
    private TextField tf_user;
    @FXML
    private TableView<Credit> table;
    @FXML
    private TableColumn<Credit, Date> date_credit;
    @FXML
    private TableColumn<Credit, Integer> duree_credit;
    @FXML
    private TableColumn<Credit, Integer> id_credit;
    @FXML
    private TableColumn<Credit, Integer> id_user;
    @FXML
    private TableColumn<Credit, Double> montant_credit;
    @FXML
    private TableColumn<Credit, Double> taux_credit;
    @FXML
    private Button deleteButton;

    private CreditCrud creditServices = new CreditCrud();
    private ObservableList<Credit> creditList = FXCollections.observableArrayList();// Créez une liste observable pour stocker les données de crédit


    public void addCredit(Credit newCredit) {
        // Ajoutez le nouveau crédit à la liste observable
        creditList.add(newCredit);

    }


    @FXML
    void SaveCredit(ActionEvent event) {
        // Valider les entrées de l'utilisateur
        if (!validateInputs()) {
            return; // Sortir de la méthode si les entrées ne sont pas valides
        }

        // Les entrées de l'utilisateur sont valides, continuer avec l'ajout
        Credit c = new Credit(Double.parseDouble(tf_montant.getText()), Integer.valueOf(tf_duree.getText()), Double.parseDouble(tf_taux.getText()), Integer.valueOf(tf_user.getText()));
        CreditCrud cc = new CreditCrud();
        cc.ajouterEntite(c);
        addCredit(c);
        table.refresh();
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Crédit ajouté", ButtonType.OK);
        alert.show();
    }

    boolean validateInputs() {
        // Valider le montant
        try {
            double montant = Double.parseDouble(tf_montant.getText());
            if (montant < 0) {
                showErrorDialog("Erreur de saisie", "Le montant doit être positif.");
                return false;
            }
        } catch (NumberFormatException e) {
            showErrorDialog("Erreur de saisie", "Veuillez saisir un montant valide.");
            return false;
        }

        // Valider la durée
        try {
            int duree = Integer.parseInt(tf_duree.getText());
            if (duree < 0) {
                showErrorDialog("Erreur de saisie", "La durée doit être positive.");
                return false;
            }
        } catch (NumberFormatException e) {
            showErrorDialog("Erreur de saisie", "Veuillez saisir une durée valide.");
            return false;
        }

        // Valider le taux
        try {
            double taux = Double.parseDouble(tf_taux.getText());
            if (taux< 0) {
                showErrorDialog("Erreur de saisie", "Le taux doit être positif.");
                return false;
            }
        } catch (NumberFormatException e) {
            showErrorDialog("Erreur de saisie", "Veuillez saisir un taux valide.");
            return false;
        }

        // Valider l'utilisateur
        try {
            int Id_user = Integer.parseInt(tf_user.getText());
            if ( Id_user< 0) {
                showErrorDialog("Erreur de saisie", "L'ID utilisateur doit être positif.");
                return false;
            }
        } catch (NumberFormatException e) {
            showErrorDialog("Erreur de saisie", "Veuillez saisir un ID utilisateur valide.");
            return false;
        }

        // Toutes les entrées sont valides
        return true;
    }

    // Méthode pour supprimer la ligne sélectionnée
    @FXML
    private void deleteRow() {
        // Récupérer la ligne sélectionnée dans la TableView
        Credit selectedCredit = table.getSelectionModel().getSelectedItem();
        if (selectedCredit == null) {
            // Aucun élément sélectionné, afficher un message d'avertissement
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un crédit à supprimer.");
            alert.showAndWait();
            return;
        }

        // Afficher une boîte de dialogue de confirmation
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation de suppression");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer ce crédit ?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // L'utilisateur a confirmé la suppression, supprimer le crédit de la base de données
            CreditCrud cc = new CreditCrud();
            cc.deleteEntite(selectedCredit);

            // Supprimer le crédit de la liste observable et rafraîchir la TableView
            creditList.remove(selectedCredit);
            table.refresh();

            // Afficher un message de confirmation
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Suppression réussie");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Le crédit a été supprimé avec succès.");
            successAlert.showAndWait();
        }
    }

    private void showErrorDialog(String noSelection, String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(noSelection);
        alert.setContentText(s);
        alert.showAndWait();
    }


    private void setupEditableCells() {
        // Montant_credit
        montant_credit.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        montant_credit.setOnEditCommit(event -> {
            Double newValue = event.getNewValue();
            Credit selectedCredit = event.getTableView().getItems().get(event.getTablePosition().getRow());

            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation");
            confirmationAlert.setHeaderText("Modifier le crédit");
            confirmationAlert.setContentText("Êtes-vous sûr de vouloir modifier ce crédit ?");

            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    selectedCredit.setMontant_credit(newValue);
                    table.refresh(); // Rafraîchir la TableView pour refléter les modifications
                    creditServices.updateEntite(selectedCredit); // Enregistrer les modifications dans la base de données
                } else {
                    // Annuler la modification, ne rien faire
                }
            });
        });

        // Duree_credit
        duree_credit.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        duree_credit.setOnEditCommit(event -> {
            Integer newValue = event.getNewValue();
            Credit selectedCredit = event.getTableView().getItems().get(event.getTablePosition().getRow());

            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation");
            confirmationAlert.setHeaderText("Modifier le crédit");
            confirmationAlert.setContentText("Êtes-vous sûr de vouloir modifier ce crédit ?");

            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    selectedCredit.setDuree_credit(newValue);
                    table.refresh(); // Rafraîchir la TableView pour refléter les modifications
                    creditServices.updateEntite(selectedCredit); // Enregistrer les modifications dans la base de données
                } else {
                    // Annuler la modification, ne rien faire
                }
            });
        });

        // Taux_credit
        taux_credit.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        taux_credit.setOnEditCommit(event -> {
            Double newValue = event.getNewValue();
            Credit selectedCredit = event.getTableView().getItems().get(event.getTablePosition().getRow());

            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation");
            confirmationAlert.setHeaderText("Modifier le crédit");
            confirmationAlert.setContentText("Êtes-vous sûr de vouloir modifier ce crédit ?");

            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    selectedCredit.setTaux_credit(newValue);
                    table.refresh(); // Rafraîchir la TableView pour refléter les modifications
                    creditServices.updateEntite(selectedCredit); // Enregistrer les modifications dans la base de données
                } else {
                    // Annuler la modification, ne rien faire
                }
            });
        });
    }


    @FXML
    void initialize() {
        // Initialisez les cellules de la TableView avec les valeurs appropriées
        id_credit.setCellValueFactory(new PropertyValueFactory<Credit, Integer>("id_credit"));
        montant_credit.setCellValueFactory(new PropertyValueFactory<Credit, Double>("montant_credit"));
        duree_credit.setCellValueFactory(new PropertyValueFactory<Credit, Integer>("duree_credit"));
        taux_credit.setCellValueFactory(new PropertyValueFactory<Credit, Double>("taux_credit"));
        date_credit.setCellValueFactory(new PropertyValueFactory<Credit, Date>("date_credit"));
        id_user.setCellValueFactory(new PropertyValueFactory<Credit, Integer>("id_user"));
        CreditCrud cc = new CreditCrud();
        creditList.addAll(cc.afficherEntite());
        table.setItems(creditList);// Associez la liste observable à votre TableView
        // Configurer la sélection de ligne
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Ajouter un écouteur d'événements pour les clics sur les lignes de la TableView
        table.setRowFactory(tv -> {
            TableRow<Credit> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY
                        && event.getClickCount() == 1) {
                    Credit selectedItem = row.getItem();
                    // Sélectionner la ligne et mettre à jour les boutons
                    deleteButton.setDisable(false);
                }
            });
            return row;
        });
        setupEditableCells();
    }

}



