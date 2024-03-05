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

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Properties;

public class SignInController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    private GestionUser gestionUser;
    private User user;

    public SignInController() {
        gestionUser = new GestionUser();
    }

    @FXML
    void signIn(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        // Check if the user exists
        User user = gestionUser.selectUserByEmail(email);
        if (user != null) {
            // Check if the email and password match
            if (user.getEmail_user().equals(email) && user.getMdp().equals(password)) {
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
                // Invalid password
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ResetPassword.fxml"));
                    Parent resetPasswordParent = loader.load();
                    ResetPasswordController resetPasswordController = loader.getController();
                    resetPasswordController.setGestionUser(gestionUser);
                    resetPasswordController.setUser(user);

                    Scene resetPasswordScene = new Scene(resetPasswordParent);

                    // Get the stage from the event and set the new scene
                    Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    appStage.setScene(resetPasswordScene);
                    appStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            // Invalid credentials
            showAlert(Alert.AlertType.ERROR, "Erreur de connexion", "Email ou mot de passe incorrect");
        }
    }

    @FXML
    void forgotPassword(ActionEvent event) {
        // Get the user's email from the emailField
        String email = emailField.getText();

        // Check if the user exists
        User user = gestionUser.selectUserByEmail(email);
        if (user != null) {
            // User exists, redirect to ResetPassword.fxml
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ResetPassword.fxml"));
                Parent resetPasswordParent = loader.load();
                ResetPasswordController resetPasswordController = loader.getController();
                resetPasswordController.setGestionUser(gestionUser);
                resetPasswordController.setUser(user);

                Scene resetPasswordScene = new Scene(resetPasswordParent);

                // Get the stage from the event and set the new scene
                Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                appStage.setScene(resetPasswordScene);
                appStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Password Reset", "No user found with the provided email address.");
        }
    }


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
    public void setGestionUser(GestionUser gestionUser) {
        this.gestionUser = gestionUser;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
