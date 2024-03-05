package pidev.esprit.Controllers.Commentaire;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import pidev.esprit.Entities.Commentaire;
import pidev.esprit.Services.CommentaireCrud;
import pidev.esprit.Entities.Signal;
import pidev.esprit.Services.SignalCrud;
import pidev.esprit.Controllers.Commentaire.AdminSignal;


import java.io.IOException;
import java.util.List;

public class AdminCommentaireController<SignalDetailsController> {

    @FXML
    private TableColumn<?, ?> contenue;

    @FXML
    private TableColumn<?, ?> date_commentaire;

    @FXML
    private TableColumn<?, ?> id_commentaire;

    @FXML
    private TableColumn<?, ?> id_projet;

    @FXML
    private TableView<Commentaire> tableComment;
    @FXML
    private Button signal_btn;
    CommentaireCrud commentaireservices = new CommentaireCrud();
    private ObservableList<Commentaire> commentaireList = FXCollections.observableArrayList();
    @FXML
    public void initialize() {
        // Initialisez votre TableView et vos colonnes ici
        id_commentaire.setCellValueFactory(new PropertyValueFactory<>("id_commentaire"));
        id_projet.setCellValueFactory(new PropertyValueFactory<>("id_projet"));
        contenue.setCellValueFactory(new PropertyValueFactory<>("contenue"));
        date_commentaire.setCellValueFactory(new PropertyValueFactory<>("date_commentaire"));
        CommentaireCrud cc = new CommentaireCrud();
        commentaireList.clear();
        commentaireList.addAll(cc.afficherEntite());
        tableComment.setItems(commentaireList);

       }
    @FXML
    void signalAffichage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminSignal.fxml"));
            Parent root = loader.load();

            // Get the controller
            AdminSignal controller = loader.getController();

            // Retrieve the list of signals
            List<Signal> signals = new SignalCrud().afficherEntite();

            // Pass the list of signals to the controller
            controller.setSignals(signals);

            // Create a new stage for the signal display window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Signal");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
