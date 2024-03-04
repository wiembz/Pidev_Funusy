package pidev.esprit.Controllers.Investissement;

import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import okhttp3.*;
import pidev.esprit.Entities.*;
import pidev.esprit.Services.*;

import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddProjectAdminController {

    @FXML
    private TextField id_userField;
    @FXML
    private TextField montant_reqField;
    @FXML
    private TextField nom_projetField;
    @FXML
    private TextField addressField;
    @FXML
    private ComboBox<ProjectType> type_projetField;
    @FXML
    private TextField descriptionField;
    @FXML
    private Button askAiButton;

    @FXML
    private WebView mapView;
    @FXML
    private Button mapbtn; // Add this line

    private ProjetServices projetServices;
    private ProjetAdminController parentController;
    private static final String OPENAI_API_KEY = "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";
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/engines/text-davinci-003/completions";

    private boolean mapLoaded = false;

    public AddProjectAdminController() {
        projetServices = new ProjetServices();
    }

    @FXML
    public void initialize() {
        type_projetField.getItems().setAll(ProjectType.values());
        // Show the 'Ask Ai' button only if description field is not empty
        descriptionField.textProperty().addListener((observable, oldValue, newValue) -> {
            askAiButton.setVisible(!newValue.isEmpty());
        });

        // Initially hide the map
        mapView.setVisible(false);

        mapbtn.setOnAction(event -> {
            // Toggle map visibility
            mapView.setVisible(!mapView.isVisible());
            if (!mapLoaded) {
                loadMap();
                mapLoaded = true;
            }
        });
    }



    @FXML
    private void loadMap() {
        WebEngine webEngine = mapView.getEngine();
        webEngine.load(getClass().getResource("/map.html").toExternalForm());
        webEngine.setJavaScriptEnabled(true);

        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("javaApp", this);
            }
        });
    }

    @FXML
    public void setLocation(String location) {
        // Update the addressField with the selected location
        addressField.setText(location);
    }

    @FXML
    private void showMap(ActionEvent event) {
        if (!mapLoaded) {
            loadMap();
            mapLoaded = true;
        }
    }

    @FXML
    private void AddProject() {
        String id_userText = id_userField.getText().trim();
        String montant_reqText = montant_reqField.getText().trim();
        String nom_projetText = nom_projetField.getText().trim();
        String descriptionText = descriptionField.getText().trim();
        String address = addressField.getText().trim();
        ProjectType id_type_projet = type_projetField.getSelectionModel().getSelectedItem();

        if (id_userText.isEmpty() || montant_reqText.isEmpty() || nom_projetText.isEmpty() ||
                id_type_projet == null || descriptionText.isEmpty() || address.isEmpty()) {
            showErrorDialog("Empty fields", "Please fill all the fields.");
            return;
        }

        int id_user;
        float montant_req;
        try {
            id_user = Integer.parseInt(id_userText);
            montant_req = Float.parseFloat(montant_reqText);
        } catch (NumberFormatException e) {
            showErrorDialog("Invalid Input", "Please enter valid numeric values.");
            return;
        }

        Projet projet = new Projet();
        projet.setId_user(id_user);
        projet.setMontant_req(montant_req);
        projet.setNom_projet(nom_projetText);
        projet.setType_projet(id_type_projet.getTypeName());
        projet.setDescription(descriptionText);

        // Set latitude and longitude from the address field
        String[] latLng = address.split(",");
        if (latLng.length == 2) {
            try {
                double latitude = Double.parseDouble(latLng[0].trim());
                double longitude = Double.parseDouble(latLng[1].trim());
                projet.setLatitude(String.valueOf(latitude));
                projet.setLongitude(String.valueOf(longitude));
            } catch (NumberFormatException e) {
                showErrorDialog("Invalid Latitude/Longitude", "Please enter valid latitude and longitude.");
                return;
            }
        } else {
            showErrorDialog("Invalid Address Format", "Please enter latitude and longitude separated by comma.");
            return;
        }

        if (projetServices.EntiteExists(projet)) {
            showErrorDialog("Project Exists", "This project already exists.");
            return;
        }

        projetServices.ajouterEntite(projet);
        populateProjetTableInParentController();

        clearFields();
    }

    @FXML
    private void CancelButtonAction() {
        closeWindow();
    }

    @FXML
    private void askAi() {
        String descriptionText = descriptionField.getText().trim();
        if (descriptionText.isEmpty()) {
            showErrorDialog("Empty Description", "Please enter a description for the project.");
            return;
        }

        CompletableFuture.supplyAsync(() -> callChatGpt(descriptionText))
                .thenAccept(this::handleAiResponse)
                .exceptionally(this::handleApiException);
    }

    private String callChatGpt(String description) {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        RequestBody body = RequestBody.create(mediaType, "{\"prompt\":\"The project requires " + description + "\",\"max_tokens\":50}");

        Request request = new Request.Builder()
                .url(OPENAI_API_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + OPENAI_API_KEY)
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void handleAiResponse(String response) {
        String amountRequired = calculateAmountFromResponse(response);
        montant_reqField.setText(amountRequired);
    }

    private String calculateAmountFromResponse(String response) {
        // Convert the response to lowercase for case-insensitive matching
        String lowercaseResponse = response.toLowerCase();

        // Define patterns to match different representations of amounts in text
        // Patterns for USD
        Pattern patternUSD1 = Pattern.compile("\\d+ usd"); // Matches "123 usd"
        Pattern patternUSD2 = Pattern.compile("\\d+ dollars"); // Matches "123 dollars"
        Pattern patternUSD3 = Pattern.compile("\\d+ \\$"); // Matches "123 $"
        // Patterns for TND
        Pattern patternTND1 = Pattern.compile("\\d+ tnd"); // Matches "123 tnd"
        Pattern patternTND2 = Pattern.compile("\\d+ dinar"); // Matches "123 dinar"
        // Patterns for GBP
        Pattern patternGBP1 = Pattern.compile("\\d+ gbp"); // Matches "123 gbp"
        Pattern patternGBP2 = Pattern.compile("\\d+ pound"); // Matches "123 pound"
        Pattern patternGBP3 = Pattern.compile("\\d+ £"); // Matches "123 £"

        // Try to find matches for each currency pattern
        Matcher matcherUSD1 = patternUSD1.matcher(lowercaseResponse);
        Matcher matcherUSD2 = patternUSD2.matcher(lowercaseResponse);
        Matcher matcherUSD3 = patternUSD3.matcher(lowercaseResponse);
        Matcher matcherTND1 = patternTND1.matcher(lowercaseResponse);
        Matcher matcherTND2 = patternTND2.matcher(lowercaseResponse);
        Matcher matcherGBP1 = patternGBP1.matcher(lowercaseResponse);
        Matcher matcherGBP2 = patternGBP2.matcher(lowercaseResponse);
        Matcher matcherGBP3 = patternGBP3.matcher(lowercaseResponse);

        // If any pattern matches, return the found amount with currency
        if (matcherUSD1.find()) {
            return matcherUSD1.group();
        } else if (matcherUSD2.find()) {
            return matcherUSD2.group();
        } else if (matcherUSD3.find()) {
            return matcherUSD3.group();
        } else if (matcherTND1.find()) {
            return matcherTND1.group();
        } else if (matcherTND2.find()) {
            return matcherTND2.group();
        } else if (matcherGBP1.find()) {
            return matcherGBP1.group();
        } else if (matcherGBP2.find()) {
            return matcherGBP2.group();
        } else if (matcherGBP3.find()) {
            return matcherGBP3.group();
        }

        // If no amount is found, return a default value in TND
        return "1000 TND"; // Default value in TND
    }


    private Void handleApiException(Throwable throwable) {
        showErrorDialog("API Error", "Failed to fetch data from the API.");
        throwable.printStackTrace();
        return null;
    }

    private void populateProjetTableInParentController() {
        if (parentController != null) {
            parentController.populateProjetTable();
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) nom_projetField.getScene().getWindow();
        stage.close();
    }

    private void showErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearFields() {
        id_userField.clear();
        montant_reqField.clear();
        nom_projetField.clear();
        type_projetField.getSelectionModel().clearSelection();
        descriptionField.clear();
        addressField.clear();
    }

    public void setParentController(ProjetAdminController parentController) {
        this.parentController = parentController;
    }
}
