package pidev.esprit.Controllers.Commentaire;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import pidev.esprit.Entities.Commentaire;
import pidev.esprit.Entities.Projet;
import pidev.esprit.Entities.Signal;
import pidev.esprit.Services.CommentaireCrud;
import pidev.esprit.Services.ProjetServices;
import pidev.esprit.Services.SignalCrud;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminCommentaireController {

    @FXML
    private TableColumn<?, ?> contenue;

    @FXML
    private TableColumn<?, ?> date_commentaire;

    @FXML
    private TableColumn<?, ?> id_commentaire;

    @FXML
    private TableColumn<?, ?> id_projet;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private TableView<Commentaire> tableComment;

    @FXML
    private Button signal_btn;

    private ObservableList<Commentaire> commentaireList = FXCollections.observableArrayList();
    private ObservableList<Projet> projetList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        id_commentaire.setCellValueFactory(new PropertyValueFactory<>("id_commentaire"));
        id_projet.setCellValueFactory(new PropertyValueFactory<>("id_projet"));
        contenue.setCellValueFactory(new PropertyValueFactory<>("contenue"));
        date_commentaire.setCellValueFactory(new PropertyValueFactory<>("date_commentaire"));

        CommentaireCrud cc = new CommentaireCrud();
        commentaireList.clear();
        commentaireList.addAll(cc.afficherEntite());
        tableComment.setItems(commentaireList);

        // Fetch and populate projects
        ProjetServices projetServices = new ProjetServices();
        projetList.addAll(projetServices.afficherEntite());

        updateBarChart();
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

    private void updateBarChart() {
        // Count the number of comments for each project
        Map<String, Integer> commentCounts = new HashMap<>();
        for (Projet projet : projetList) {
            int projectId = projet.getId_projet();
            List<Commentaire> comments = new CommentaireCrud().getCommentsForProjet(projectId);
            commentCounts.put(projet.getNom_projet(), comments.size());
        }

        // Clear previous data from the chart
        barChart.getData().clear();

        // Create a new series for the bar chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        // Populate data to the series
        for (Map.Entry<String, Integer> entry : commentCounts.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        // Add the series to the chart
        barChart.getData().add(series);

        // Update x-axis categories
        xAxis.setCategories(FXCollections.observableArrayList(commentCounts.keySet()));

        // Set label formatter for y-axis (optional)
        yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis) {
            @Override
            public String toString(Number object) {
                return String.format("%d", object.intValue());
            }
        });
    }
}
