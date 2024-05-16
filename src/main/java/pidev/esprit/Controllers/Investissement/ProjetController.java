package pidev.esprit.Controllers.Investissement;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import pidev.esprit.API.QRCodeGeneratorProjet;

public class ProjetController {
    @FXML
    private PieChart pieChart;

    private ProjetServices projetServices;
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
    @FXML
    private TextField searchField;
    @FXML
    private Button btn_generate_qr_code;
    private ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

    public ProjetController() {
        projetServices = new ProjetServices();
        this.investissementServices = new InvestissementServices();

    }

    @FXML
    private void initialize() {
        updatePieChartData();
        pieChart.setData(pieChartData);
        populatePieChart();
        populateProjetTable();
        btn_generate_qr_code.setOnAction(this::handleGenerateQRCodeButtonClick);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                populateProjetTable();
            } else {
                handleSearch(newValue);
            }
        });
        id_projet.setCellValueFactory(new PropertyValueFactory<>("nom_projet"));
    }

    private void populateProjetTable() {
        List<Projet> projets = projetServices.afficherEntite();
        ObservableList<Projet> observableList = FXCollections.observableArrayList(projets);
        id_projet.setCellValueFactory(new PropertyValueFactory<>("id_projet"));
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
                List<Investissement> investments = investissementServices.findInvestmentsByProjectId(selectedProject.getId_projet());

                for (Investissement investment : investments) {
                    investissementServices.deleteEntite(investment.getId_investissement());
                }

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
    @FXML
    private void handleSearch(String query) {
        List<Projet> searchResults = projetServices.search(query);
        displaySearchResults(searchResults);
    }

    private void displaySearchResults(List<Projet> searchResults) {
        ObservableList<Projet> searchResultList = FXCollections.observableArrayList(searchResults);
        table.setItems(searchResultList);
    }


    private String generateQRContent(Projet projet) {
        StringBuilder content = new StringBuilder();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String formattedDate = now.format(formatter);

        content.append("Generated on: ").append(formattedDate).append("\n\n");

        content.append("Project ID: ").append(projet.getId_projet()).append("\n");
        content.append("Project Name: ").append(projet.getNom_projet()).append("\n");
        content.append("Project Type: ").append(projet.getType_projet()).append("\n");
        content.append("Amount Required: ").append(projet.getMontant_req()).append("\n");
        content.append("Longitude: ").append(projet.getLongitude()).append("\n");
        content.append("Latitude: ").append(projet.getLatitude()).append("\n");
        content.append("Description: ").append(projet.getDescription()).append("\n");

        return content.toString();
    }


    private String generateQRCode(String content) {

        StringBuilder qrCodeData = new StringBuilder();
        qrCodeData.append(content);
        String qrCodePath = "path_to_save_qr_code_imagePROJET";
        QRCodeGeneratorProjet.generateQRCode(qrCodeData.toString(), 400, 400, qrCodePath);
        return qrCodePath;
    }
    @FXML
    void handleGenerateQRCodeButtonClick(ActionEvent event) {
        Projet selectedProject = table.getSelectionModel().getSelectedItem();
        if (selectedProject == null) {
            showErrorDialog("No Selection", "Please select a project to generate QR code.");
            return;
        }
        String qrCodeContent = generateQRContent(selectedProject);
        String qrCodePath = generateQRCode(qrCodeContent);
        Image qrCodeImage = new Image("file:" + qrCodePath);

        ImageView imageView = new ImageView(qrCodeImage);
        Group root = new Group(imageView);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Generated QR Code");
        stage.show();
    }

    @FXML
    private void populatePieChart() {
        Map<ProjectType, Integer> typeCountMap = projetServices.getProjectCountByType();

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<ProjectType, Integer> entry : typeCountMap.entrySet()) {
            PieChart.Data data = new PieChart.Data(entry.getKey().getTypeName(), entry.getValue());
            data.setName(entry.getKey().getTypeName() + " (" + entry.getValue() + ")");
            pieChartData.add(data);
        }

        pieChart.setData(pieChartData);

        int colorIndex = 0;
        for (PieChart.Data data : pieChart.getData()) {
            switch (colorIndex % 5) { // Assuming we have 5 project types
                case 0:
                    data.getNode().setStyle("-fx-pie-color: #66c2a5;");
                    break;
                case 1:
                    data.getNode().setStyle("-fx-pie-color: #fc8d62;");
                    break;
                case 2:
                    data.getNode().setStyle("-fx-pie-color: #8da0cb;");
                    break;
                case 3:
                    data.getNode().setStyle("-fx-pie-color: #e78ac3;");
                    break;
                case 4:
                    data.getNode().setStyle("-fx-pie-color: #a6d854;");
                    break;
            }
            colorIndex++;

            data.getNode().setOnMouseClicked(event -> {
                Bounds bounds = data.getNode().getBoundsInLocal();
                double newX = (bounds.getWidth()) / 2.0 + bounds.getMinX();
                double newY = (bounds.getHeight()) / 2.0 + bounds.getMinY();

                data.getNode().setTranslateX(0);
                data.getNode().setTranslateY(0);

                TranslateTransition translateTransition = new TranslateTransition(Duration.millis(1500), data.getNode());
                translateTransition.setByX(newX);
                translateTransition.setByY(newY);
                translateTransition.setAutoReverse(true);
                translateTransition.setCycleCount(2);

                translateTransition.play();

                double percentage = (data.getPieValue() / pieChart.getData().stream().mapToDouble(PieChart.Data::getPieValue).sum()) * 100;
                Alert alert = new Alert(Alert.AlertType.INFORMATION, data.getName() + " Percentage: " + percentage + "%", ButtonType.OK);
                alert.setHeaderText(null);
                alert.setTitle("Percentage of Project Type");
                alert.showAndWait();
            });
        }
    }




    private void updatePieChartData() {
        pieChart.getData().clear();

        Map<ProjectType, Integer> typeCountMap = projetServices.getProjectCountByType();
        for (Map.Entry<ProjectType, Integer> entry : typeCountMap.entrySet()) {
            pieChart.getData().add(new PieChart.Data(entry.getKey().getTypeName(), entry.getValue()));
        }
    }

}