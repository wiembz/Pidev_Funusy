package pidev.esprit.Controllers.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import pidev.esprit.Entities.User;
import pidev.esprit.Services.GestionUser;

import java.io.IOException;
import java.sql.Date;

public class SignUpController {

    @FXML
    private TextField nom_userField;

    @FXML
    private TextField prenom_userField;

    @FXML
    private TextField email_userField;

    @FXML
    private PasswordField mdpField;

    @FXML
    private TextField salaireField;

    @FXML
    private DatePicker date_naissancePicker;

    @FXML
    private TextField CINField;

    @FXML
    private TextField telField;

    @FXML
    private ChoiceBox<String> adresse_userChoiceBox;

    @FXML
    private ChoiceBox<String> role_userChoiceBox;

    @FXML
    void initialize() {
        // Initialize choice boxes
        adresse_userChoiceBox.getItems().addAll(
                "ARIANA", "BEJA", "BEN_AROUS", "BIZERTE", "GABES", "GAFSA", "JENDOUBA",
                "KAIROUAN", "KASSERINE", "KEBILI", "KEF", "MAHDIA", "MANOUBA", "MEDENINE",
                "MONASTIR", "NABEUL", "SFAX", "SIDI_BOUZID", "SILIANA", "SOUSSE", "TATAOUINE",
                "TOZEUR", "TUNIS_CAPITALE", "ZAGHOUAN"
        );


        role_userChoiceBox.getItems().addAll("CLIENT", "ADMIN");
    }

    @FXML
    void signUp(ActionEvent event) {
        if (validateFields()) {
            String nom_user = nom_userField.getText();
            String prenom_user = prenom_userField.getText();
            String email_user = email_userField.getText();
            String mdp = mdpField.getText();
            double salaire = Double.parseDouble(salaireField.getText());
            Date date_naissance = Date.valueOf(date_naissancePicker.getValue());
            int CIN = Integer.parseInt(CINField.getText());
            int tel = Integer.parseInt(telField.getText());
            String adresse_user = adresse_userChoiceBox.getValue();
            String role_user = role_userChoiceBox.getValue();

            boolean registrationSuccessful = UserController.registerUser(
                    nom_user, prenom_user, email_user, mdp, salaire, date_naissance.toLocalDate(), CIN, tel, adresse_user, role_user);

            if (registrationSuccessful) {
                User newUser = new User(nom_user, prenom_user, email_user, mdp, salaire, date_naissance, CIN, tel, adresse_user, role_user);

                // Call the createUser method to save the user in the database
                GestionUser gestionUser = new GestionUser();
                gestionUser.createUser(newUser);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Registration successful");
                alert.setTitle("REAPER.TEK BANK");
                alert.setContentText("Account details\n" +
                        "prenom: "+nom_userField.getText()+"\nnom: "+prenom_userField.getText()+"\n email: "+email_userField.getText()+"\n mot de passe : "+mdpField.getText()+"\n salaire : " +salaireField.getText()+"\n telephone : "+telField.getText()+"\n CIN : "+CINField.getText()+"\n adresse : "+adresse_userChoiceBox.getValue()+"\n role : "+role_userChoiceBox.getValue()+"\n date naissance : "+date_naissancePicker.getValue().toString()+
                        "Welcoem"+nom_userField.getText()+" "+ prenom_userField.getText());
                alert.showAndWait();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur d'inscription", "L'utilisateur existe déjà ou le mot de passe ne répond pas aux exigences");
            }
        }
    }

    private boolean validateFields() {
        // Validation for nom_user
        if (nom_userField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez entrer un nom");
            return false;
        }

        // Validation for prenom_user
        if (prenom_userField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez entrer un prénom");
            return false;
        }

        // Validation for email_user
        String emailPattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
        if (!email_userField.getText().matches(emailPattern)) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez entrer une adresse e-mail valide");
            return false;
        }

        // Validation for mdp
        if (mdpField.getText().length() < 8) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Le mot de passe doit contenir au moins 8 caractères");
            return false;
        }

        // Validation for salaire
        double salaire = 0.0;
        try {
            salaire = Double.parseDouble(salaireField.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez entrer un salaire valide");
            return false;
        }

        // Validation for date_naissance
        if (date_naissancePicker.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez sélectionner une date de naissance");
            return false;
        }

        // Validation for CIN
        int CIN = 0;
        try {
            CIN = Integer.parseInt(CINField.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez entrer un numéro CIN valide");
            return false;
        }

        // Validation for tel
        int tel = 0;
        try {
            tel = Integer.parseInt(telField.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez entrer un numéro de téléphone valide");
            return false;
        }

        // Validation for adresse_user
        String adresse_user = adresse_userChoiceBox.getValue();
        if (adresse_user == null || adresse_user.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez sélectionner une adresse");
            return false;
        }

        return true;  // If all validations pass
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void goToSignIn(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SignIn.fxml"));
            Parent signInParent = loader.load();
            Scene signInScene = new Scene(signInParent);
            Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            appStage.setScene(signInScene);
            appStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
