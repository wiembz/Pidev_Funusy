package pidev.esprit.Controllers.Investissement;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.converter.FloatStringConverter;
import pidev.esprit.Entities.Investissement;
import pidev.esprit.Entities.Projet;
import pidev.esprit.Services.ProjetServices;
import pidev.esprit.Services.InvestissementServices;
import pidev.esprit.Entities.ProjectType;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import javafx.scene.control.ComboBox;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;


import java.io.File;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;

public class ProjetController {

    @FXML
    private TableView<Projet> table;
    @FXML
    private TableColumn<Projet, Integer> id_projet;
    @FXML
    private TableColumn<Projet, Integer> id_user;
    @FXML
    private TableColumn<Projet, Float> montant_req;
    @FXML
    private TableColumn<Projet, String> longitude;
    @FXML
    private TableColumn<Projet, String> latitude;
    @FXML
    private TableColumn<Projet, String> nom_projet;
    @FXML
    private TableColumn<Projet, String> type_projet;
    @FXML
    private TableColumn<Projet, String> description;
    private InvestissementServices investissementServices;
    private ProjetServices projetServices;

    public ProjetController() {
        projetServices = new ProjetServices();
        this.investissementServices = new InvestissementServices();

    }

    @FXML
    private void initialize() {
        populateProjetTable();

    }

    private void populateProjetTable() {
        List<Projet> projets = projetServices.afficherEntite();
        ObservableList<Projet> observableList = FXCollections.observableArrayList(projets);
        id_projet.setCellValueFactory(new PropertyValueFactory<>("id_projet"));
        id_user.setCellValueFactory(new PropertyValueFactory<>("id_user"));
        montant_req.setCellValueFactory(new PropertyValueFactory<>("montant_req"));
        longitude.setCellValueFactory(new PropertyValueFactory<>("longitude"));
        latitude.setCellValueFactory(new PropertyValueFactory<>("latitude"));
        nom_projet.setCellValueFactory(new PropertyValueFactory<>("nom_projet"));
        type_projet.setCellValueFactory(new PropertyValueFactory<>("type_projet"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        table.setItems(observableList);
    }


    @FXML
    private void handleDeleteButtonAction() {
        Projet selectedProject = table.getSelectionModel().getSelectedItem();
        if (selectedProject == null) {
            showErrorDialog("No Selection", "Please select a project to delete.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText("Delete Record");
        confirmationAlert.setContentText("Are you sure you want to delete this record?");

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Retrieve associated investments for the selected project
                List<Investissement> investments = investissementServices.findInvestmentsByProjectId(selectedProject.getId_projet());

                // Delete each investment individually
                for (Investissement investment : investments) {
                    investissementServices.deleteEntite(investment.getId_investissement());
                }

                // After all investments are deleted, delete the project
                projetServices.deleteEntite(selectedProject.getId_projet());

                populateProjetTable();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Project deleted successfully!", ButtonType.OK);
                alert.showAndWait();
            }
        });
    }

    private void showErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void QrCodeAction(ActionEvent actionEvent) {
    }
}