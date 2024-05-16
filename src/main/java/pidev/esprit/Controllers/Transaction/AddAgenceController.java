package pidev.esprit.Controllers.Transaction;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import pidev.esprit.Entities.Agence;
import pidev.esprit.Entities.Transaction;
import pidev.esprit.Services.GestionAgence;
import pidev.esprit.Services.GestionTransaction;

import java.sql.SQLException;
import java.util.List;

public class AddAgenceController {

    @FXML
    private Button delete_agence;

    @FXML
    private TableColumn<Agence, Integer> PostalColumn;

    @FXML
    private TableColumn<Agence, String> adressColumn;

    @FXML
    private Button ajouter_agence;

    @FXML
    private TableColumn<Agence,Integer> codeColumn;

    @FXML
    private TableView<Agence> table_agence;

    @FXML
    private TextField tf_adresse;

    @FXML
    private TextField tf_code_agence;

    @FXML
    private TextField tf_code_postal;

    private GestionAgence gestionAgence;

    public AddAgenceController() {
        gestionAgence = new GestionAgence();
    }


    private void showErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void ajouter_agence(ActionEvent event) throws SQLException {

        if (tf_adresse.getText().isEmpty() || tf_code_postal.getText().isEmpty() || tf_code_agence.getText().isEmpty() ) {
            showErrorDialog("Agence Invalid", "Please enter valid Agency credentials");
            return;
        }


        Agence a = new Agence(Integer.valueOf(tf_code_agence.getText()),tf_adresse.getText(),Integer.valueOf(tf_code_postal.getText()));
        GestionAgence gestionAgence = new GestionAgence();
        gestionAgence.ajouter(a);
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Agence saved successfully!", ButtonType.OK);
        alert.showAndWait();
        populateTransactionsTable();
    }

    private void populateTransactionsTable() throws Error{


        try {
            List<Agence> agences = gestionAgence.afficher();
            ObservableList<Agence> AgenceData = FXCollections.observableArrayList(agences);

            adressColumn.setCellValueFactory(new PropertyValueFactory<>("adresse")); // Assuming "id" is the ID field in Transaction
            codeColumn.setCellValueFactory(new PropertyValueFactory<>("codeAgence")); // Assuming "source" exists in Transaction
            PostalColumn.setCellValueFactory(new PropertyValueFactory<>("codepostal"));

            table_agence.setItems(AgenceData);
        }catch (Error | SQLException e){
            System.out.println(e);
        }
    }
    @FXML
    void delete_agence(ActionEvent event) throws SQLException {
        if (!tf_adresse.getText().isEmpty() || !tf_code_postal.getText().isEmpty() ||   tf_code_agence.getText().isEmpty() ) {
            showErrorDialog("Agence Invalid", "entrer juste le code agence pour supprimer");
            return;
        }

        GestionAgence gestionAgence = new GestionAgence();
        gestionAgence.supprimer(Integer.valueOf(tf_code_agence.getText()) );
        populateTransactionsTable();
    }
    public static void generateCode(TextField textField1, TextField textField2, TextField outputTextField) {
        String text1 = textField1.getText().toUpperCase();
        String text2 = textField2.getText().toUpperCase();

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 5; i++) {
            if (i < text1.length()) {
                sb.append(text1.charAt(i));
            } else if (i < text1.length() + text2.length()) {
                sb.append(text2.charAt(i - text1.length()));
            } else {
                sb.append('X'); // fill remaining characters with 'X'
            }
        }

        // Keep only digits in output string
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < sb.length(); i++) {
            if (Character.isDigit(sb.charAt(i))) {
                output.append(sb.charAt(i));
            }
        }

        outputTextField.setText(output.toString());
    }

    @FXML
    void initialize() {
        tf_code_postal.addEventFilter(KeyEvent.KEY_TYPED, event -> {

            if (!event.getCharacter().matches("\\d")) {

                event.consume();

            }


            if (tf_code_postal.getText().length() == 4) {

                event.consume();

            }

        });

        tf_adresse.textProperty().addListener((observable, oldValue, newValue) -> generateCode(tf_code_postal, tf_adresse, tf_code_agence));
        tf_code_postal.textProperty().addListener((observable, oldValue, newValue) -> generateCode(tf_code_postal, tf_adresse, tf_code_agence));
        populateTransactionsTable();
    }

}