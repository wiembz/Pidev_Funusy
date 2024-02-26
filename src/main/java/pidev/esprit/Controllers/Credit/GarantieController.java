package pidev.esprit.Controllers.Credit;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import pidev.esprit.Entities.Credit;
import pidev.esprit.Entities.Garantie;
import pidev.esprit.Entities.Nature;
import pidev.esprit.Services.CreditCrud;
import pidev.esprit.Services.GarantieCrud;
import pidev.esprit.Controllers.Credit.AjouterCredit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import java.util.Optional;


public class GarantieController {

    @FXML
    private TableColumn<Garantie, Integer> id_credit;

    @FXML
    private TableColumn<Garantie, Integer> id_garantie;

    @FXML
    private TableColumn<Garantie, String> nature_garantie;

    @FXML
    private TableColumn<Garantie, String> preuve;

    @FXML
    private TableView<Garantie> table_garantie;

    @FXML
    private TableColumn<Garantie, Double> Valeur_Garantie;

    @FXML
    private Button btnAjout;

    @FXML
    private Button btnChoose;

    @FXML
    private Button btndelete;

    @FXML
    private ComboBox<Nature> tf_nature;

    @FXML
    private TextField tf_valeur;

    private String fileContent; // Variable de classe pour stocker le contenu du fichier

    GarantieCrud garantieServices = new GarantieCrud();

    private int idcredit; // Variable to store id_credit
    private ObservableList<Garantie> GarantieList = FXCollections.observableArrayList();// Créez une liste observable pour stocker les données de crédit


    // Setter method for id_credit
    public void setId_credit(int idcredit) {
        this.idcredit = idcredit;
    }

    public void addGarantie(Garantie newGarantie) {
        // Ajoutez le nouveau crédit à la liste observable
        GarantieList.add(newGarantie);

    }

    @FXML
    void ChooseFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir un fichier");
        // Filtre pour les fichiers de type texte (txt)
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichiers texte (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            try {
                // Lire le contenu du fichier sélectionné en tant que String
                fileContent = new String(Files.readAllBytes(selectedFile.toPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Afficher le nom du fichier sélectionné sur le bouton
            btnChoose.setText(selectedFile.getName());
        }
    }

    private void showErrorDialog(String noSelection, String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(noSelection);
        alert.setContentText(s);
        alert.showAndWait();
    }

    public void initialize() {
        // Initialisation de la ComboBox avec les valeurs de l'énumération Garantie.Nature
        tf_nature.setItems(FXCollections.observableArrayList(Nature.values()));

        id_garantie.setCellValueFactory(new PropertyValueFactory<>("id_garantie"));
        id_credit.setCellValueFactory(new PropertyValueFactory<>("id_credit"));
        nature_garantie.setCellValueFactory(new PropertyValueFactory<>("nature_garantie"));
        Valeur_Garantie.setCellValueFactory(new PropertyValueFactory<>("Valeur_Garantie"));
        preuve.setCellValueFactory(new PropertyValueFactory<>("preuve"));
        GarantieCrud gc = new GarantieCrud();
        GarantieList.clear();
        GarantieList.addAll(gc.afficherEntite());
        table_garantie.setItems(GarantieList);
        // Configurer la sélection de ligne
        table_garantie.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Ajouter un écouteur d'événements pour les clics sur les lignes de la TableView
        table_garantie.setRowFactory(tv -> {
            TableRow<Garantie> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                    Garantie selectedItem = row.getItem();
                    // Sélectionner la ligne et mettre à jour les boutons
                    btndelete.setDisable(false);
                }
            });
            return row;
        });
        setupEditableGarantie();
    }


    public void InsertGarantie(ActionEvent actionEvent) {
        if (fileContent != null) {
            if (tf_nature.getValue() != null && !tf_nature.getValue().isEmpty()) { // Vérifiez si une nature de garantie est sélectionnée
                try {
                    CreditCrud cc = new CreditCrud();
                    double montantCredit = cc.getLastInsertedMontantCredit();
                    double valeurGarantie = Double.parseDouble(tf_valeur.getText());

                    if (montantCredit <= valeurGarantie) {
                        // Créer un nouvel objet Garantie
                        Garantie G = new Garantie();
                        G.setId_credit(idcredit);
                        G.setNature_garantie(String.valueOf(Nature.valueOf(String.valueOf(tf_nature.getValue()))));
                        G.setValeur_Garantie(valeurGarantie);
                        G.setPreuve(fileContent);

                        // Vérifier si la garantie existe déjà
                        if (garantieServices.EntiteExists(G)) {
                            showErrorDialog("Garantie Existante", "Cette garantie existe déjà.");
                            return;
                        }

                        // Si la garantie n'existe pas, l'ajouter à la base de données
                        GarantieCrud gc = new GarantieCrud();
                        gc.ajouterGarantie(G, idcredit);
                        addGarantie(G);
                        initialize();
                        table_garantie.refresh();
                        showAlert(Alert.AlertType.INFORMATION, "Garantie ajoutée");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Le montant du crédit est supérieur à la valeur de la garantie");
                    }
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Valeur de garantie invalide");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Veuillez sélectionner une nature de garantie");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Veuillez choisir un fichier");
        }
    }

    @FXML
    void deleteGarantie(ActionEvent event) {
// Récupérer la ligne sélectionnée dans la TableView
        Garantie selectedGarantie = table_garantie.getSelectionModel().getSelectedItem();
        if (selectedGarantie == null) {
            // Aucun élément sélectionné, afficher un message d'avertissement
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un garantie à supprimer.");
            alert.showAndWait();
            return;

        }
        // Afficher une boîte de dialogue de confirmation
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation de suppression");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer ce garantie ?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // L'utilisateur a confirmé la suppression, supprimer le crédit de la base de données
            GarantieCrud cc = new GarantieCrud();
            cc.deleteEntite(selectedGarantie);

            // Supprimer le crédit de la liste observable et rafraîchir la TableView
            GarantieList.remove(selectedGarantie);
            table_garantie.refresh();

            // Afficher un message de confirmation
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Suppression réussie");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Le garantie a été supprimé avec succès.");
            successAlert.showAndWait();
        }
    }
    private void setupEditableGarantie() {
        // Nature_garantie
        nature_garantie.setCellFactory(TextFieldTableCell.forTableColumn());
        nature_garantie.setOnEditCommit(event -> {
            String newValue = event.getNewValue();
            Garantie selectedGarantie = event.getTableView().getItems().get(event.getTablePosition().getRow());

            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation");
            confirmationAlert.setHeaderText("Modifier la garantie");
            confirmationAlert.setContentText("Êtes-vous sûr de vouloir modifier cette garantie ?");

            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    selectedGarantie.setNature_garantie(newValue);
                    table_garantie.refresh(); // Rafraîchir la TableView pour refléter les modifications
                    garantieServices.updateEntite(selectedGarantie); // Enregistrer les modifications dans la base de données
                } else {
                    // Annuler la modification, ne rien faire
                }
            });
        });

        // Valeur_Garantie
        Valeur_Garantie.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        Valeur_Garantie.setOnEditCommit(event -> {
            Double newValue = event.getNewValue();
            Garantie selectedGarantie = event.getTableView().getItems().get(event.getTablePosition().getRow());

            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation");
            confirmationAlert.setHeaderText("Modifier la garantie");
            confirmationAlert.setContentText("Êtes-vous sûr de vouloir modifier cette garantie ?");

            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    selectedGarantie.setValeur_Garantie(newValue);
                    table_garantie.refresh(); // Rafraîchir la TableView pour refléter les modifications
                    garantieServices.updateEntite(selectedGarantie); // Enregistrer les modifications dans la base de données
                } else {
                    // Annuler la modification, ne rien faire
                }
            });
        });

        // preuve
        preuve.setCellFactory(TextFieldTableCell.forTableColumn());
        preuve.setOnEditCommit(event -> {
            String newValue = event.getNewValue();
            Garantie selectedGarantie = event.getTableView().getItems().get(event.getTablePosition().getRow());

            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation");
            confirmationAlert.setHeaderText("Modifier la garantie");
            confirmationAlert.setContentText("Êtes-vous sûr de vouloir modifier cette garantie ?");

            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    selectedGarantie.setPreuve(newValue);
                    table_garantie.refresh(); // Rafraîchir la TableView pour refléter les modifications
                    garantieServices.updateEntite(selectedGarantie); // Enregistrer les modifications dans la base de données
                } else {
                    // Annuler la modification, ne rien faire
                }
            });
        });
    }


    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.show();
    }

}