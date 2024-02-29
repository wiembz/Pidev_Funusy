package pidev.esprit.Controllers.Investissement;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import pidev.esprit.Entities.Projet;
import pidev.esprit.Services.ProjetServices;

import pidev.esprit.Entities.ProjectType;

import java.io.File;
import java.net.MalformedURLException;


public class AddProjectAdminController {

    public Button Addbtn;
    @FXML
    private TextField id_userField;
    @FXML
    private TextField searchField;
    @FXML
    private TextField montant_reqField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField nom_projetField;
    @FXML
    private TextField longitudeField;
    @FXML
    private TextField latitudeField;
    @FXML
    private ComboBox<ProjectType> type_projetField;
    @FXML
    private WebView gifWebView;
    @FXML
    private ProjetServices projetServices;
    private ProjetAdminController parentController;
    public AddProjectAdminController(){projetServices = new ProjetServices();}

public void initialize() {
        type_projetField.getItems().setAll(ProjectType.values());
    }


    @FXML
    private void AddProject() {
        String id_userText = id_userField.getText().trim();
        String montant_reqText = montant_reqField.getText().trim();
        String nom_projetText = nom_projetField.getText().trim();
        String longitudeText = longitudeField.getText().trim();
        String latitudeText = latitudeField.getText().trim();
        ProjectType id_type_projet = type_projetField.getValue();
        String descriptionText = descriptionField.getText().trim();

        if (id_userText.isEmpty() || montant_reqText.isEmpty() || nom_projetText.isEmpty() ||
                longitudeText.isEmpty() || latitudeText.isEmpty() || id_type_projet == null || descriptionText.isEmpty()) {
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
        projet.setDescription(descriptionText);

        if (projetServices.EntiteExists(projet)) {
            showErrorDialog("Project Exists", "This project already exists.");
            return;
        }

        projetServices.ajouterEntite(projet);
        populateProjetTableInParentController();

        clearFields();
        showSuccessGif();
    }
    private void populateProjetTableInParentController() {
        if (parentController != null) {
            parentController.populateProjetTable();
        }
    }
    @FXML
    private void CancelButtonAction() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) Addbtn.getScene().getWindow();
        stage.close();
    }
    public void setParentController(ProjetAdminController parentController) {
        this.parentController = parentController;
    }
    private void showErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private void showSuccessGif() {
        String gifPath = "src/main/resources/Assets/giphyy.gif";

        try {
            // Convert the local file path to a URL
            String fileUrl = new File(gifPath).toURI().toURL().toString();

            // Load the GIF from the local file into the WebView
            String htmlContent = "<html><body style=\"margin:0;overflow:hidden;\">"
                    + "<img id=\"gif\" src=\"" + fileUrl + "\" style=\"width:100%;height:100%;object-fit:contain;\">"
                    + "<script>"
                    + "var gif = document.getElementById('gif');"
                    + "setTimeout(function() { gif.style.display = 'none'; }, 3000);"
                    + "</script>"
                    + "</body></html>";

            WebEngine webEngine = gifWebView.getEngine();
            webEngine.loadContent(htmlContent);

            gifWebView.setVisible(true);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    private void clearFields() {
        id_userField.clear();
        montant_reqField.clear();
        nom_projetField.clear();
        longitudeField.clear();
        latitudeField.clear();
        type_projetField.getSelectionModel().clearSelection();
        descriptionField.clear();
    }
}