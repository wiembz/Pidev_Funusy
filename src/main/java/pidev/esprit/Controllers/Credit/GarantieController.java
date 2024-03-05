package pidev.esprit.Controllers.Credit;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import pidev.esprit.Entities.Garantie;
import pidev.esprit.Entities.Nature;
import pidev.esprit.Services.CreditCrud;
import pidev.esprit.Services.GarantieCrud;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

import static jdk.jfr.consumer.EventStream.openFile;

public class GarantieController {

    @FXML
    private TableColumn<Garantie, Integer> id_credit;

    @FXML
    private TableColumn<Garantie, Integer> id_garantie;

    @FXML
    private TableColumn<Garantie, Nature> nature_garantie;

    @FXML
    private TableColumn<Garantie, String> preuve;

    @FXML
    private TableView<Garantie> table_garantie;

    @FXML
    private TableColumn<Garantie, Double> Valeur_Garantie;

    @FXML
    private Button btnAjout;

    @FXML
    private Button btnChoose;

    @FXML
    private Button btndelete;

    @FXML
    private ComboBox<Nature> tf_nature;

    @FXML
    private TextField tf_valeur;

    private String fileContent;
    private File selectedFile;


    GarantieCrud garantieServices = new GarantieCrud();

    private int idcredit;
    private ObservableList<Garantie> GarantieList = FXCollections.observableArrayList();

    public void setId_credit(int idcredit) {
        this.idcredit = idcredit;
    }

    public void addGarantie(Garantie newGarantie) {
        GarantieList.add(newGarantie);
    }

    @FXML
    void ChooseFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a file");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichiers PDF (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setSelectedExtensionFilter(extFilter);

        selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            try {
                fileContent = new String(Files.readAllBytes(selectedFile.toPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            btnChoose.setText(selectedFile.getName());
        }
    }


    private void showErrorDialog(String noSelection, String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(noSelection);
        alert.setContentText(s);
        alert.showAndWait();
    }

    public void initialize() {
        tf_nature.setItems(FXCollections.observableArrayList(Nature.values()));

        id_garantie.setCellValueFactory(new PropertyValueFactory<>("id_garantie"));
        id_credit.setCellValueFactory(new PropertyValueFactory<>("id_credit"));
        nature_garantie.setCellValueFactory(new PropertyValueFactory<>("nature_garantie"));
        Valeur_Garantie.setCellValueFactory(new PropertyValueFactory<>("Valeur_Garantie"));
        preuve.setCellValueFactory(new PropertyValueFactory<>("preuve"));

        GarantieCrud gc = new GarantieCrud();
        GarantieList.clear();
        GarantieList.addAll(gc.afficherEntite());
        table_garantie.setItems(GarantieList);

        table_garantie.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        table_garantie.setRowFactory(tv -> {
            TableRow<Garantie> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                    btndelete.setDisable(false);
                }
            });
            return row;
        });
        setupEditableGarantie();
    }

    @FXML
    void InsertGarantie(ActionEvent actionEvent) {
        if (fileContent != null) {
            if (tf_nature.getValue() != null && !tf_nature.getValue().isEmpty()) {
                try {
                    CreditCrud cc = new CreditCrud();
                    double montantCredit = cc.getLastInsertedMontantCredit();
                    double valeurGarantie = Double.parseDouble(tf_valeur.getText());

                    if (montantCredit <= valeurGarantie) {
                        Garantie G = new Garantie();
                        G.setId_credit(idcredit);
                        G.setNature_garantie(String.valueOf(tf_nature.getValue()));
                        G.setValeur_Garantie(valeurGarantie);
                        G.setPreuve(selectedFile.getAbsolutePath()); // Assuming selectedFile is the selected PDF file

                        if (garantieServices.EntiteExists(G)) {
                            showErrorDialog("Existing Warranty", "This warranty already exists.");
                            return;
                        }

                        GarantieCrud gc = new GarantieCrud();
                        gc.ajouterGarantie(G, idcredit);
                        addGarantie(G);
                        initialize();
                        table_garantie.refresh();
                        showAlert(Alert.AlertType.INFORMATION, "Added Warranty");

                        // Open the file associated with the inserted garantie
                        openFile(fileContent);
                    } else {
                        showAlert(Alert.AlertType.ERROR, "The amount of the credit is greater than the value of the guarantee");
                    }
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Invalid warranty value");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Please select a type of guarantee");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Please choose a file");
        }
    }
    @FXML
    void deleteGarantie(ActionEvent event) {
        Garantie selectedGarantie = table_garantie.getSelectionModel().getSelectedItem();
        if (selectedGarantie == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a warranty to delete.");
            alert.showAndWait();
            return;

        }
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Deletion confirmation");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Are you sure you want to remove this warranty ?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            GarantieCrud cc = new GarantieCrud();
            cc.deleteEntite(selectedGarantie);

            GarantieList.remove(selectedGarantie);
            table_garantie.refresh();

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Deletion successful");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Warranty has been successfully removed.");
            successAlert.showAndWait();
        }
    }

    private void setupEditableGarantie() {
        ObservableList<Nature> natureOptions = FXCollections.observableArrayList(Nature.values());

        nature_garantie.setCellFactory(ComboBoxTableCell.forTableColumn(natureOptions));
        nature_garantie.setOnEditCommit(event -> {
            String newValueString = String.valueOf(event.getNewValue());
            Nature newValue = Nature.valueOf(newValueString);
            Garantie selectedGarantie = event.getTableView().getItems().get(event.getTablePosition().getRow());

            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation");
            confirmationAlert.setHeaderText("Change warranty");
            confirmationAlert.setContentText("Are you sure you want to change this warranty ?");

            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    selectedGarantie.setNature_garantie(newValue.getTypeName());
                    table_garantie.refresh();
                    garantieServices.updateEntite(selectedGarantie);
                } else {
                    // Cancel the modification
                }
            });
        });

        Valeur_Garantie.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        Valeur_Garantie.setOnEditCommit(event -> {
            Double newValue = event.getNewValue();
            Garantie selectedGarantie = event.getTableView().getItems().get(event.getTablePosition().getRow());

            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation");
            confirmationAlert.setHeaderText("Change warranty");
            confirmationAlert.setContentText("Are you sure you want to change this warranty  ?");

            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    selectedGarantie.setValeur_Garantie(newValue);
                    table_garantie.refresh();
                    garantieServices.updateEntite(selectedGarantie);
                } else {
                    // Cancel the modification
                }
            });
        });

        preuve.setCellFactory(col -> new TableCell<Garantie, String>() {
            final Button button = new Button("Open File");

            {
                button.setOnAction(event -> {
                    Garantie garantie = getTableView().getItems().get(getIndex());
                    if (garantie != null && garantie.getPreuve() != null) {
                        openFile(garantie.getPreuve());
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(button);
                }
            }
        });
    }
    private void openFile(String filePath) {
        try {
            File file = new File(filePath);
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.show();
    }
}
