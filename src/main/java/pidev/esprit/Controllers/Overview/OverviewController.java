package pidev.esprit.Controllers.Overview;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import pidev.esprit.Controllers.Transaction.AddTransactionController;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import pidev.esprit.Entities.Transaction;
import pidev.esprit.Services.GestionTransaction;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class OverviewController {



    @FXML
    private Pane exchange_rates;

    @FXML
    private Text message;



    @FXML
    private TableView<Transaction> transactionsTable;

    @FXML
    private TableColumn<Transaction, Integer> idColumn;

    @FXML
    private TableColumn<Transaction, Integer> sourceColumn;

    @FXML
    private TableColumn<Transaction, Double> amountColumn;

    @FXML
    private TableColumn<Transaction, Date> dateColumn;
    @FXML

    private GestionTransaction gestionTransaction;

    public OverviewController() {
        gestionTransaction = new GestionTransaction();
    }
    private void populateTransactionsTable() throws Error{


        try {
            List<Transaction> transactions = gestionTransaction.afficher();
            ObservableList<Transaction> transactionsData = FXCollections.observableArrayList(transactions);

            idColumn.setCellValueFactory(new PropertyValueFactory<>("id")); // Assuming "id" is the ID field in Transaction
            sourceColumn.setCellValueFactory(new PropertyValueFactory<>("Source")); // Assuming "source" exists in Transaction
            amountColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("Destination"));

            transactionsTable.setItems(transactionsData);
        }catch (Error e){
            System.out.println(e);
        }
    }


    public void initialize() {
        message.setText("EXCHANGE RATES");
        createMarquee(exchange_rates, message);
        populateTransactionsTable();

    }



    public void createMarquee(Pane container, Text message) {
        // Scene width
        double sceneWidth = container.prefWidthProperty().get();

        // Text width
        message.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
            double textWidth = message.getLayoutBounds().getWidth();

            Timeline timeline = new Timeline();
            timeline.setCycleCount(Timeline.INDEFINITE);

            // Keyframe to update text position
            KeyFrame keyFrame = new KeyFrame(Duration.millis(50), event -> {
                double layoutX = message.getLayoutX();
                layoutX -= 1;
                message.setLayoutX(layoutX);

                if (layoutX + textWidth < 0) {
                    layoutX = sceneWidth;
                }
            });

            timeline.getKeyFrames().add(keyFrame);
            timeline.play();
        });
    }








}





