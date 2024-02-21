    package pidev.esprit.Controllers.Investissement;

    import javafx.fxml.FXML;
    import javafx.scene.control.*;
    import javafx.util.converter.FloatStringConverter;
    import javafx.util.converter.IntegerStringConverter;
    import pidev.esprit.Entities.Investissement;
    import pidev.esprit.Services.InvestissementServices;
    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;
    import javafx.scene.control.TableColumn;
    import javafx.scene.control.TableView;
    import javafx.scene.control.cell.PropertyValueFactory;
    import javafx.scene.control.cell.TextFieldTableCell;
    import javafx.util.StringConverter;

    import java.sql.Date;
    import java.time.LocalDate;
    import java.time.format.DateTimeFormatter;
    import java.util.List;

    public class InvestissementController {
        @FXML
        private TextField id_userField;
        @FXML
        private TextField montantField;
        @FXML
        private TextField periodeField;
        @FXML
        private DatePicker datePicker;
        @FXML
        private TableView<Investissement> table;
        @FXML
        private TableColumn<Investissement, Integer> id_investissement;
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
            setupCellFactories();
            populateInvestissementTable();
        }
        private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


        private void setupCellFactories() {
            // Set cell factories for each editable column
            montant.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
            periode.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
            date_investissement.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Date>() {
                @Override
                public String toString(Date date) {
                    if (date != null) {
                        return dateFormatter.format(date.toLocalDate());
                    }
                    return null;
                }

                @Override
                public Date fromString(String string) {
                    if (string != null && !string.trim().isEmpty()) {
                        return Date.valueOf(LocalDate.parse(string, dateFormatter));
                    }
                    return null;
                }
            }));

            // Set cell value factories for each column
            id_user.setCellValueFactory(new PropertyValueFactory<>("id_user"));
            montant.setCellValueFactory(new PropertyValueFactory<>("montant"));
            periode.setCellValueFactory(new PropertyValueFactory<>("periode"));
            date_investissement.setCellValueFactory(new PropertyValueFactory<>("date_investissement"));
        }
        @FXML
        private void handleCellEditCommit(TableColumn.CellEditEvent<Investissement, ?> event) {
            // Handle cell edit commit
            TableView.TableViewSelectionModel<Investissement> selectionModel = table.getSelectionModel();
            Investissement selectedInvestissement = selectionModel.getSelectedItem();
            String columnName = event.getTableColumn().getText();

            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation");
            confirmationAlert.setHeaderText("Edit Record");
            confirmationAlert.setContentText("Are you sure you want to update this record in column '" + columnName + "'?");

            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Update the entity in the database
                    switch (columnName) {
                        case "Id_user":
                            selectedInvestissement.setId_user((Integer) event.getNewValue());
                            break;
                        case "Montant":
                            selectedInvestissement.setMontant((Float) event.getNewValue());
                            break;
                        case "Date_investissement":
                            selectedInvestissement.setDate_investissement((Date) event.getNewValue());
                            break;
                        case "Periode":
                            selectedInvestissement.setPeriode((Integer) event.getNewValue());
                            break;
                    }
                    investissementServices.updateEntite(selectedInvestissement);
                } else {
                    // Refresh the table to revert changes if the user cancels
                    table.refresh();
                }
            });

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


                investissementServices.ajouterEntite(investissement);
                // Clear fields after successful addition
                clearFields();
                populateInvestissementTable();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Investissement added successfully!", ButtonType.OK);
                alert.showAndWait();

        }
        @FXML
        private void handleDeleteButtonAction() {
            // Get the selected item from the table
            Investissement selectedInvestissement = table.getSelectionModel().getSelectedItem();

            if (selectedInvestissement == null) {
                // If no item is selected, show an error message
                showErrorDialog("No Selection", "Please select an investment to delete.");
                return;
            }

            // Show confirmation dialog
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation");
            confirmationAlert.setHeaderText("Delete Record");
            confirmationAlert.setContentText("Are you sure you want to delete this record?");

            // Show the confirmation dialog and wait for the user's response
            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // User confirmed the deletion
                    int idToDelete = selectedInvestissement.getId_investissement();
                    investissementServices.deleteEntite(idToDelete);

                    // Remove the item from the table
                    table.getItems().remove(selectedInvestissement);
                }
            });
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
