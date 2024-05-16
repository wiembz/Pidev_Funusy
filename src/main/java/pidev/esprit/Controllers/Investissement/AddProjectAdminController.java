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
import org.json.JSONException;
import org.json.JSONObject;
import pidev.esprit.Entities.*;
import pidev.esprit.Services.*;
import pidev.esprit.Tools.MyConnection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
    private TextArea descriptionField;
    @FXML
    private Button askAiButton;
    @FXML
    private TextField fulladdress;
    @FXML
    private WebView mapView;
    @FXML
    private Button mapbtn;

    private ProjetServices projetServices;
    private ProjetAdminController parentController;
    private static final String GEMINI_API_KEY = "AIzaSyDO8XvJHwgslZeTJWYCy868Z0OfgqCD4dA";
    private static final String GEMINI_API_URL = "https://api.gemini.com/";

    private boolean mapLoaded = false;

    public AddProjectAdminController() {
        projetServices = new ProjetServices();
    }

    @FXML
    public void initialize() {
        type_projetField.getItems().setAll(ProjectType.values());
        descriptionField.textProperty().addListener((observable, oldValue, newValue) -> {
            askAiButton.setVisible(!newValue.isEmpty());
        });

        mapView.setVisible(false);

        mapbtn.setOnAction(event -> {
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
        addressField.setText(location);
        String detailedAddress = reverseGeocode(location);
        fulladdress.setText(detailedAddress);
    }

    private String reverseGeocode(String location) {
        String apiUrl = "https://nominatim.openstreetmap.org/reverse?lat=" + location.split(",")[0] + "&lon=" + location.split(",")[1] + "&format=json";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(apiUrl)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                String detailedAddress = parseAddressFromJson(responseBody);
                return detailedAddress;
            } else {
                return "Error: " + response.code();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String parseAddressFromJson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            return jsonObject.getString("display_name");
        } catch (JSONException e) {
            e.printStackTrace();
            return "Error parsing JSON";
        }
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
            String montant_reqText = montant_reqField.getText().trim();
            String nom_projetText = nom_projetField.getText().trim();
            String descriptionText = descriptionField.getText().trim();
            String address = addressField.getText().trim();
            ProjectType id_type_projet = type_projetField.getSelectionModel().getSelectedItem();

            if (montant_reqText.isEmpty() || nom_projetText.isEmpty() ||
                    id_type_projet == null || descriptionText.isEmpty() || address.isEmpty()) {
                showErrorDialog("Empty fields", "Please fill all the fields.");
                return;
            }

            int loggedInUserId = MyConnection.getInstance().getLoggedInUserId();
            float montant_req;
            try {
                montant_req = Float.parseFloat(montant_reqText);
            } catch (NumberFormatException e) {
                showErrorDialog("Invalid Input", "Please enter a valid numeric value for the required amount.");
                return;
            }

            Projet projet = new Projet();
            projet.setId_user(loggedInUserId);
            projet.setMontant_req(montant_req);
            projet.setNom_projet(nom_projetText);
            projet.setType_projet(id_type_projet.getTypeName());
            projet.setDescription(descriptionText);

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
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Added Project", ButtonType.OK);
            alert.show();
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

        CompletableFuture.supplyAsync(() -> callGeminiApi(descriptionText))
                .thenAccept(this::handleAiResponse)
                .exceptionally(this::handleApiException);
    }


    private String callGeminiApi(String description) {
        System.out.println("Description: " + description);
        List<String> amounts = extractAmountFromResponse(description);

        for (String amount : amounts) {
            String cleanedAmount = amount.replaceAll("[^\\d.]", "");
            System.out.println("Original Amount: " + amount + ", Cleaned Amount: " + cleanedAmount);
        }

        float totalFundingRequired = calculateTotalFunding(amounts);
        return String.valueOf(totalFundingRequired);
    }




    private float calculateTotalFunding(List<String> amounts) {
        float totalFunding = 0.0f;
        for (String amount : amounts) {
            String cleanedAmount = amount.replaceAll("[^\\d.]", "");
            if (!cleanedAmount.isEmpty()) {
                float value = Float.parseFloat(cleanedAmount);
                totalFunding += value;
            }
        }

        return totalFunding;
    }
    private void handleAiResponse(String totalFunding) {

        montant_reqField.setText(totalFunding);
    }
    private List<String> extractAmountFromResponse(String description) {
        List<String> amounts = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\b(?:\\d+(?:\\.\\d+)?|\\d*(?:\\.\\d+))\\s*(?:usd|dollars|\\$|tnd|dinar|gbp|pound|£|€|euros)?\\b", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(description);
        while (matcher.find()) {
            amounts.add(matcher.group());
        }

        return amounts;
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
        fulladdress.clear();
    }

    public void setParentController(ProjetAdminController parentController) {
        this.parentController = parentController;
    }
}
