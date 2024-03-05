package pidev.esprit.Controllers.User;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HomePage extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/HomePage.fxml"));
            Parent root = loader.load();

            // Create a scene
            Scene scene = new Scene(root);

            // Set the scene to the stage
            primaryStage.setScene(scene);

            // Set the title of the stage
            primaryStage.setTitle("Login and Signup");

            // Show the stage
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., log it or display an error message)
        }
    }



    @FXML
    void goToSignIn(ActionEvent event) {
        loadPage(event, "/SignIn.fxml", "Connexion");
    }

    @FXML
    void goToSignUp(ActionEvent event) {
        loadPage(event, "/SignUp.fxml", "Inscription");
    }


    private void loadPage(ActionEvent event, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent pageParent = loader.load();
            Scene pageScene = new Scene(pageParent);
            Stage appStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            appStage.setScene(pageScene);
            appStage.setTitle(title);
            appStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer l'erreur de chargement du fichier FXML (par exemple, afficher un message d'erreur à l'utilisateur)
        }
    }

}
