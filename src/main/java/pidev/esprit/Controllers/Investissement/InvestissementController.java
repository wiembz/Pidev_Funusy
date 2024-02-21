    package pidev.esprit.Controllers.Investissement;

    import javafx.fxml.FXML;
    import javafx.scene.control.*;
    import pidev.esprit.Entities.Investissement;
    import pidev.esprit.Services.InvestissementServices;
    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;
    import javafx.scene.control.TableColumn;
    import javafx.scene.control.TableView;
    import javafx.scene.control.cell.PropertyValueFactory;

    import java.sql.Date;
    import java.time.LocalDate;
    import java.util.List;

    public class InvestissementController {

        @FXML
        private TextField id_userField;
        @FXML
        private TextField montantField;

        @FXML
        private TextField periodeField;

        @FXML
        private DatePicker datePicker; // Change the type to DatePicker

        @FXML
        private Label warningLabel;

        @FXML
        private TableView<Investissement> table; // Add TableView declaration

        @FXML
        private TableColumn<Investissement, Integer> id_investissement; // Add TableColumn declarations
        @FXML
        private TableColumn<Investissement, Integer> id_user;
        @FXML
        private TableColumn<Investissement, Float> montant;
        @FXML
        private TableColumn<Investissement, Date> date_investissement;
        @FXML
        private TableColumn<Investissement, Integer> periode;

        private InvestissementServices investissementServices;

        public InvestissementController() {
            investissementServices = new InvestissementServices();
        }

        @FXML
        private void initialize() {
            populateInvestissementTable();
        }

        private void populateInvestissementTable() {
            List<Investissement> investissements = investissementServices.afficherEntite();
            ObservableList<Investissement> investissementData = FXCollections.observableArrayList(investissements);

            id_investissement.setCellValueFactory(new PropertyValueFactory<>("id_investissement"));
            id_user.setCellValueFactory(new PropertyValueFactory<>("id_user"));
            montant.setCellValueFactory(new PropertyValueFactory<>("montant"));
            date_investissement.setCellValueFactory(new PropertyValueFactory<>("date_investissement"));
            periode.setCellValueFactory(new PropertyValueFactory<>("periode"));

            table.setItems(investissementData);
        }
        @FXML
        private void handleAddButtonAction() {
            // Get input values
            String id_userText = id_userField.getText().trim();
            String montantText = montantField.getText().trim();
            String periodeText = periodeField.getText().trim();
            LocalDate selectedDate = datePicker.getValue();

            // Check for empty fields
            if (id_userText.isEmpty() || montantText.isEmpty() || periodeText.isEmpty() || selectedDate == null) {
                showErrorDialog("Empty fields", "Please fill all the fields.");
                return;
            }

            // Validate numeric values
            int id_user;
            float montant;
            int periode;
            try {
                id_user = Integer.parseInt(id_userText);
                montant = Float.parseFloat(montantText);
                periode = Integer.parseInt(periodeText);
            } catch (NumberFormatException e) {
                showErrorDialog("Invalid input", "Please enter valid numeric values");
                return;
            }

            // Check if montant is not negative
            if (montant < 0) {
                showErrorDialog("Invalid montant", "Montant cannot be negative.");
                return;
            }

            LocalDate currentDate = LocalDate.now(); // Get the current date

            // Check if the selected date is not in the past
            if (selectedDate.isBefore(currentDate)) {
                showErrorDialog("Invalid date", "Investissement date cannot be in the past.");
                return;
            }

            Date date_inv = java.sql.Date.valueOf(selectedDate);

            // Check if periode is not negative
            if (periode < 0) {
                showErrorDialog("Invalid periode", "Periode cannot be negative.");
                return;
            }

            Investissement investissement = new Investissement();
            investissement.setId_user(id_user);
            investissement.setMontant(montant);
            investissement.setPeriode(periode);
            investissement.setDate_investissement(date_inv);

            // Check if the investment already exists
            if (investissementServices.EntiteExists(investissement)) {
                showErrorDialog("Investment exists", "An investment with the same attributes already exists.");
                return;
            }

            if (isInvestissementValid(investissement)) {
                investissementServices.ajouterEntite(investissement);
                // Clear fields after successful addition
                clearFields();
                populateInvestissementTable();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Investissement added successfully!", ButtonType.OK);
                alert.showAndWait();
            }
        }


        private boolean isInvestissementValid(Investissement investissement) {
            // Additional custom validation checks can be added here

            // If all checks pass, return true indicating investissement is valid
            return true;
        }

        private void clearFields() {
            montantField.clear();
            periodeField.clear();
            id_userField.clear();
            datePicker.getEditor().clear(); // Clear the DatePicker
        }

        private void showErrorDialog(String title, String content) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.showAndWait();
        }
    }
