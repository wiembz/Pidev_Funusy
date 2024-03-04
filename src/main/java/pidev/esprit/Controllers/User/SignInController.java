package pidev.esprit.Controllers.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pidev.esprit.User.Entities.Role;
import pidev.esprit.User.Entities.User;
import pidev.esprit.User.Services.GestionUser;

import java.io.IOException;

public class SignInController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    private GestionUser gestionUser;

    public SignInController() {
        gestionUser = new GestionUser();
    }

    @FXML
    void signIn(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        User user = gestionUser.SignIn(email, password);
        if (user != null) {
            // Successful sign-in
            showAlert(Alert.AlertType.INFORMATION, "Connexion réussie", "Bienvenue!");

            try {
                FXMLLoader loader;
                if (user.getRole_user().equals(Role.ADMIN.getRole())) {
                    loader = new FXMLLoader(getClass().getResource("/DashboardAdmin.fxml"));
                } else if (user.getRole_user().equals(Role.CLIENT.getRole())){
                    loader = new FXMLLoader(getClass().getResource("/DashboardClient.fxml"));
                }
                else return;

                Parent dashboardParent = loader.load();
                Scene dashboardScene = new Scene(dashboardParent);

                // Get the stage from the event and set the new scene
                Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                appStage.setScene(dashboardScene);
                appStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Invalid credentials
            showAlert(Alert.AlertType.ERROR, "Erreur de connexion", "Email ou mot de passe incorrect");
        }
    }

    @FXML
    void forgotPassword(ActionEvent event) {
        // Handle the "Forgot Password" action
        String email = emailField.getText();
        User user = gestionUser.getUserByEmail(email);

        if (user != null) {
            gestionUser.sendForgotPasswordEmail(user);
            showAlert(Alert.AlertType.INFORMATION, "Forgot Password", "A password reset email has been sent to your registered email address.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Forgot Password", "No user found with the provided email address.");
        }
    }

    // ... (existing code)

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void goToSignUp(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SignUp.fxml"));
            Parent signUpParent = loader.load();
            Scene signUpScene = new Scene(signUpParent);
            Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            appStage.setScene(signUpScene);
            appStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
