package pidev.esprit.Controllers.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.w3c.dom.Node;
import pidev.esprit.User.Entities.User;
import pidev.esprit.User.Services.GestionUser;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class ResetPasswordController {

    @FXML
    private TextField recoveryCodeField;

    @FXML
    private PasswordField newPasswordField;
    private User user;
    private GestionUser gestionUser;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GestionUser getGestionUser() {
        return gestionUser;
    }

    public void setGestionUser(GestionUser gestionUser) {
        this.gestionUser = gestionUser;
    }

    @FXML
    void backToSignIn() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SignIn.fxml"));
            Parent signInParent = loader.load();
            Scene signInScene = new Scene(signInParent);
            Stage stage = (Stage) ((Node) signInParent.getScene().getWindow());
            stage.setScene(signInScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }


    public void resetPassword(javafx.event.ActionEvent event) {
        String recoveryCode = recoveryCodeField.getText();
        String newPassword = newPasswordField.getText();

        // Call the resetPassword method of the GestionUser instance
        if (gestionUser.resetPassword(user.getEmail_user(), recoveryCode, newPassword)) {
            // Password reset successful
            showAlert(Alert.AlertType.INFORMATION, "Mot de passe réinitialisé", "Votre mot de passe a été réinitialisé avec succès.");
            gestionUser.updateUser(user);
        } else {
            // Password reset failed
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le code de réinitialisation est incorrect.");
        }
    }
}