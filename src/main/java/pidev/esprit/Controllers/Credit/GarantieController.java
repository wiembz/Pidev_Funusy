package pidev.esprit.Controllers.Credit;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pidev.esprit.Entities.Garantie;
import pidev.esprit.Services.GarantieCrud;
import pidev.esprit.Controllers.Credit.AjouterCredit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class GarantieController {

    @FXML
    private Button btnAjout;

    @FXML
    private Button btnChoose;

    @FXML
    private ComboBox<Garantie.Nature> tf_nature;

    @FXML
    private TextField tf_valeur;

    private String fileContent; // Variable de classe pour stocker le contenu du fichier

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
    // Autres méthodes de votre contrôleur


    public void InsertGarantie() {
        // Vérifier si un fichier a été sélectionné
        if (fileContent != null) {
            Garantie G = new Garantie(Garantie.Nature.valueOf(tf_nature.getValue().toString()), Double.parseDouble(tf_valeur.getText()), fileContent);
            GarantieCrud gc = new GarantieCrud();
            gc.ajouterEntite(G);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Garantie ajoutée", ButtonType.OK);
            alert.show();
        } else {
            // Afficher un message d'erreur si aucun fichier n'a été sélectionné
            Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez choisir un fichier", ButtonType.OK);
            alert.show();

        }
    }


    boolean validateInputs() {
        // Valider le montant
        try {
            double valeur = Double.parseDouble(tf_valeur.getText());
            if (valeur < 0) {
                showErrorDialog("Erreur de saisie", "La valeur doit être positif.");
                return false;
            }
        } catch (NumberFormatException e) {
            showErrorDialog("Erreur de saisie", "Veuillez saisir une valeur valide.");
            return false;
        }


        // Toutes les entrées sont valides
        return true;
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
        ObservableList<Garantie.Nature> options = FXCollections.observableArrayList(Garantie.Nature.values());
        tf_nature.setItems(options);
    }

}