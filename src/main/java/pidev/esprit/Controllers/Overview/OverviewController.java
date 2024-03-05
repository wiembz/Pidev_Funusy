package pidev.esprit.Controllers.Overview;


import javafx.animation.Animation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
    private AnchorPane top_Anchor;
    @FXML

    private GestionTransaction gestionTransaction;
    private Timeline timeline;

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
        //System.out.println(generateExchangeRates());
        //message.setText(generateExchangeRates());
        message.setText("EXCHANGES RATES EXCHANGES RATES EXCHANGES RATES ");
        createMarquee(exchange_rates, message,10,600,50);
        populateTransactionsTable();


    }

    private String generateExchangeRates(){
        String urlString = "https://v6.exchangerate-api.com/v6/41fcfd977b145ffc1d85eb84/latest/TND";

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) { // success
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                reader.close();

                return builder.toString();
            } else {
                System.out.println("Error: " + responseCode);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }




    public void createMarquee(Pane container, Text text, double speed, double width, double height) {
        // Text
        text.setFont(Font.font("Arial", 24));
        text.setFill(Color.GREEN);
        text.setLayoutX(width);
        text.setLayoutY((height - text.getBoundsInLocal().getHeight()) / 2);

        // Timeline
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(speed), event -> {
                    double layoutX = text.getLayoutX();
                    layoutX -= 1;
                    text.setLayoutX(layoutX);

                    if (layoutX + text.getBoundsInLocal().getWidth() < 0) {
                        layoutX = width; // Reset layoutX to width of container
                    }
                })
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        // Add text to container and set clip property
        container.getChildren().remove(text); // Remove text from container if it already exists
        container.getChildren().add(text);
        container.setClip(new Rectangle(0, 0, width, height));
    }

    }











