package pidev.esprit.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import pidev.esprit.API.QRCodeGenerator;
import pidev.esprit.Entities.AdresseUser;
import pidev.esprit.Entities.Role;
import pidev.esprit.Entities.User;
import pidev.esprit.Services.GestionUser;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProfileAdminController {


        private final GestionUser userGestion;
        @FXML
        private Button btn_generate_qr_code;
        @FXML
        private ResourceBundle resources;

        @FXML
        private URL location;

        @FXML
        private Button accountsbtn;

        @FXML
        private TableColumn<User, String> adresseUserCol;

        @FXML
        private ChoiceBox<String> adresse_choicebox;

        @FXML
        private TableColumn<User, Date> birthDatecol;

        @FXML
        private DatePicker birthdate_picker;

        @FXML
        private Button btn_add_user;

        @FXML
        private TableColumn<User, Integer> cinCol;

        @FXML
        private TextField cin_textfeild;

        @FXML
        private Button commentsbtn;

        @FXML
        private Button creditsbtn;

        @FXML
        private TextField email_textfeild;

        @FXML
        private TableColumn<User, String>email_userCol;

        @FXML
        private TableColumn<User, Integer> idCol;

        @FXML
        private Button investissementsbtn;

        @FXML
        private Button mailingbtn;

        @FXML
        private BorderPane mainBorderPane;

        @FXML
        private TableColumn<User, String> mdpCol;

        @FXML
        private TextField mdp_textfeild;

        @FXML
        private TableColumn<User, String> nomUserCol;

        @FXML
        private TextField nom_textfeild;

        @FXML
        private Button overviewbtn;

        @FXML
        private TableColumn<User, String> prenomUserCol;

        @FXML
        private TextField prenom_textfeild;

        @FXML
        private Button profilebtn;

        @FXML
        private TableColumn<User, String> roleUserCol;

        @FXML
        private ChoiceBox<String> role_choicebox;

        @FXML
        private TableColumn<User, Double> salaireCol;

        @FXML
        private TextField salaire_textfeild;

        @FXML
        private TableView<User> tableUsers;

        @FXML
        private TableColumn<User, Integer> telCol;

        @FXML
        private TextField tel_textfeild;

        @FXML
        private Button transactionsbtn;
        private User currentUser;

    public ProfileAdminController(GestionUser userGestion) {
        this.userGestion = userGestion;
    }

    public void setCurrentUser(User user) {
            this.currentUser = user;
        }

        private User getCurrentUser() {

            return currentUser ;
        }

        private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        public ProfileAdminController() {
            userGestion=new GestionUser();
        }

        @FXML
        void handleInvestmentsButtonClick(MouseEvent event) {

        }

        @FXML
        void handleProfilesButtonClick(MouseEvent event) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Registration successful");
            alert.setTitle("REAPER.TEK BANK");
            alert.setContentText("Account details\n" +
                    "prenom: "+nom_textfeild.getText()+"\nnom: "+prenom_textfeild.getText()+"\n email: "+email_textfeild.getText()+"\n mot de passe : "+mdp_textfeild.getText()+"\n salaire : " +salaire_textfeild.getText()+"\n telephone : "+tel_textfeild.getText()+"\n CIN : "+cin_textfeild.getText()+"\n adresse : "+adresse_choicebox.getValue()+"\n role : "+role_choicebox.getValue()+"\n date naissance : "+birthDatecol.getText()+
                    "Welcoem"+nom_textfeild.getText()+" "+ prenom_textfeild.getText());
            alert.showAndWait();
        }



        @FXML
        void initialize() {
            btn_generate_qr_code.setOnAction(this::handleGenerateQRCodeButtonClick);

            adresse_choicebox.setItems(FXCollections.observableArrayList(
                    "ARIANA", "BEJA", "BEN_AROUS", "BIZERTE", "GABES", "GAFSA",
                    "JENDOUBA", "KAIROUAN", "KASSERINE", "KEBILI", "KEF", "MAHDIA",
                    "MANOUBA", "MEDENINE", "MONASTIR", "NABEUL", "SFAX", "SIDI_BOUZID",
                    "SILIANA", "SOUSSE", "TATAOUINE", "TOZEUR", "TUNIS_CAPITALE", "ZAGHOUAN"
            ));

            role_choicebox.setItems(FXCollections.observableArrayList(
                    Role.ADMIN.toString(), Role.CLIENT.toString()
            ));
            setupCellFactories();

            populateUserTable();
            tableUsers.refresh();

        }
        private void showAlert(Alert.AlertType alertType, String title, String content) {
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setContentText(content);
            alert.showAndWait();
        }

        private void setupCellFactories() {
            // Set cell factories for each editable column
            idCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
            nomUserCol.setCellFactory(TextFieldTableCell.forTableColumn());
            prenomUserCol.setCellFactory(TextFieldTableCell.forTableColumn() );

            email_userCol.setCellFactory(TextFieldTableCell.forTableColumn());
            mdpCol.setCellFactory(TextFieldTableCell.forTableColumn());
            salaireCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
            birthDatecol.setCellFactory(TextFieldTableCell.forTableColumn(dateConverter));
            cinCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
            telCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

            adresseUserCol.setCellFactory(column -> {
                return new TableCell<User, String>() {
                    private final ComboBox<AdresseUser> comboBox = new ComboBox<>(FXCollections.observableArrayList(AdresseUser.values()));

                    {
                        comboBox.setVisible(false);
                        comboBox.setOnAction(event -> {
                            commitEdit(comboBox.getSelectionModel().getSelectedItem().getAdresseUser());
                        });
                    }

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty) {
                            setGraphic(null);
                        } else {
                            setText(item);
                            setGraphic(null);
                        }
                    }

                    @Override
                    public void startEdit() {
                        if (!isEmpty()) {
                            super.startEdit();
                            comboBox.getSelectionModel().select(AdresseUser.valueOf(getItem()));
                            setGraphic(comboBox);
                            comboBox.setVisible(true);
                            comboBox.requestFocus();
                        }
                    }

                    @Override
                    public void cancelEdit() {
                        super.cancelEdit();
                        comboBox.setVisible(false);
                        setGraphic(null);
                        setText(getItem());
                    }
                };
            });

            roleUserCol.setCellFactory(column -> {
                return new TableCell<>() {
                    private final ComboBox<Role> comboBox = new ComboBox<>(FXCollections.observableArrayList(Role.values()));

                    {
                        comboBox.setVisible(false);
                        comboBox.setOnAction(event -> commitEdit(comboBox.getSelectionModel().getSelectedItem().getRole()));
                    }

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty) {
                            setGraphic(null);
                        } else {
                            setText(item);
                            setGraphic(null);
                        }
                    }

                    @Override
                    public void startEdit() {
                        if (!isEmpty()) {
                            super.startEdit();
                            comboBox.getSelectionModel().select(Role.valueOf(getItem()));
                            setGraphic(comboBox);
                            comboBox.setVisible(true);
                            comboBox.requestFocus();
                        }
                    }

                    @Override
                    public void cancelEdit() {
                        super.cancelEdit();
                        comboBox.setVisible(false);
                        setGraphic(null);
                        setText(getItem());
                    }
                };
            });

            idCol.setCellValueFactory(new PropertyValueFactory<>("id_user"));
            nomUserCol.setCellValueFactory(new PropertyValueFactory<>("nom_user"));
            nomUserCol.setEditable(true);

            prenomUserCol.setCellValueFactory(new PropertyValueFactory<>("prenom_user"));
            email_userCol.setCellValueFactory(new PropertyValueFactory<>("email_user"));
            mdpCol.setCellValueFactory(new PropertyValueFactory<>("mdp"));
            salaireCol.setCellValueFactory(new PropertyValueFactory<>("salaire"));
            birthDatecol.setCellValueFactory(new PropertyValueFactory<>("date_naissance"));
            cinCol.setCellValueFactory(new PropertyValueFactory<>("CIN"));
            telCol.setCellValueFactory(new PropertyValueFactory<>("tel"));
            adresseUserCol.setCellValueFactory(new PropertyValueFactory<>("adresse_user"));
            roleUserCol.setCellValueFactory(new PropertyValueFactory<>("role_user"));
        }


        @FXML
        public void handleCellEditCommit(TableColumn.CellEditEvent<User, ?> event) {
            User selectedUser = event.getRowValue();
            String columnName = event.getTableColumn().getText();

            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation");
            confirmationAlert.setHeaderText("Edit Record");
            confirmationAlert.setContentText("Are you sure you want to update this record in column '" + columnName + "'?");

            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Update the entity in the database
                    try {
                        Object newValue = event.getNewValue();

                        switch (columnName) {

                            case "nom_user":
                                selectedUser.setNom_user((String) event.getNewValue());
                                break;
                            case "prenom_user":
                                selectedUser.setPrenom_user((String) event.getNewValue());
                                break;
                            case "email_user":
                                selectedUser.setEmail_user((String) event.getNewValue());
                                break;
                            case "mdp":
                                selectedUser.setMdp((String) event.getNewValue());
                                break;
                            case "salaire":
                                if (newValue instanceof Double) {
                                    selectedUser.setSalaire((Double) newValue);
                                } else if (newValue instanceof String) {
                                    selectedUser.setSalaire(Double.parseDouble((String) newValue));
                                }
                                break;
                            case "date_naissance":
                                selectedUser.setDate_naissance((java.sql.Date) event.getNewValue());
                                break;
                            case "CIN":
                                selectedUser.setCIN((Integer)event.getNewValue());
                                break;
                            case "tel":
                                selectedUser.setTel((Integer)event.getNewValue());
                                break;
                            case "adresse_user":
                                selectedUser.setAdresse_user((String) event.getNewValue());
                                break;
                            //TODO check afterwords the gestionUser and user if the project didn't run

                            case "role_user":
                                selectedUser.setRole_user((String) event.getNewValue());
                                break;
                            case "id_user":
                                selectedUser.setId_user((Integer)event. getNewValue());
                                break;
                            default:
                                showErrorDialog("Invalid Column", "Cannot edit this column.");
                                tableUsers.refresh();
                                return;
                        }
                        userGestion.updateUser(selectedUser); // Call update method
                    } catch (Exception e) {
                        System.err.println("Error updating record: " + e.getMessage());
                    }
                } else {
                    // Refresh the table to revert changes if the user cancels
                    tableUsers.refresh();
                    userGestion.updateUser(selectedUser);
                }
            });
        }

        private void showErrorDialog(String title, String content) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.showAndWait();
        }

        @FXML
        private void handleDeleteButtonAction() {
            User selectedUser = tableUsers.getSelectionModel().getSelectedItem();
            if (selectedUser == null) {
                showErrorDialog("No Selection", "Please select a user to delete.");
                return;
            }

            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation");
            confirmationAlert.setHeaderText("Delete Record");
            confirmationAlert.setContentText("Are you sure you want to delete this record?");

            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // User confirmed the deletion
                    int idToDelete = selectedUser.getId_user();
                    userGestion.deleteUser(idToDelete);
                    populateUserTable();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "User deleted successfully!", ButtonType.OK);
                    alert.showAndWait();
                }
            });
        }
        private void populateUserTable() {
            List<User> userData = userGestion.afficherUser();
            ObservableList<User> observableList = FXCollections.observableArrayList(userData);
            nomUserCol.setCellValueFactory(new PropertyValueFactory<>("nom_user"));
            prenomUserCol.setCellValueFactory(new PropertyValueFactory<>("prenom_user"));
            email_userCol.setCellValueFactory(new PropertyValueFactory<>("email_user"));
            mdpCol.setCellValueFactory(new PropertyValueFactory<>("mdp"));
            salaireCol.setCellValueFactory(new PropertyValueFactory<>("salaire"));
            birthDatecol.setCellValueFactory(new PropertyValueFactory<>("date_naissance"));
            cinCol.setCellValueFactory(new PropertyValueFactory<>("CIN"));
            telCol.setCellValueFactory(new PropertyValueFactory<>("tel"));

            adresseUserCol.setCellValueFactory(new PropertyValueFactory<>("adresse_user"));
            roleUserCol.setCellValueFactory(new PropertyValueFactory<>("role_user"));


            tableUsers.setItems(observableList);
            tableUsers.refresh();
        }


        @FXML
        private void handleAddButtonAction() {
            // Get input values
            String nom_user = nom_textfeild.getText().trim();
            String prenom_user = prenom_textfeild.getText().trim();
            String email_user = email_textfeild.getText().trim();
            String mdp = mdp_textfeild.getText().trim();

            String salaire = salaire_textfeild.getText().trim();
            LocalDate date_naissance = birthdate_picker.getValue();

            String CIN = cin_textfeild.getText().trim();

            String tel = tel_textfeild.getText().trim();
            AdresseUser adresse_user = null;
            String selectedAdresse = adresse_choicebox.getValue();
            if (selectedAdresse != null) {
                adresse_user = AdresseUser.valueOf(selectedAdresse);
            }

            Role role_user = null;
            String selectedRole = role_choicebox.getValue();
            if (selectedRole != null) {
                role_user = Role.valueOf(selectedRole);
            }

            // Validate input fields
            if (nom_user.isEmpty() || prenom_user.isEmpty() || email_user.isEmpty() || mdp.isEmpty() ||
                    salaire.isEmpty() || date_naissance == null || CIN.isEmpty() || tel.isEmpty() ||
                    adresse_user == null || role_user == null) {
                showErrorDialog("Empty fields", "Please fill all the fields.");
                return;
            }

            // Validate email format
            if (!isValidEmail(email_user)) {
                showErrorDialog("Invalid Email", "Please enter a valid email address.");
                return;
            }

            // Validate CIN length
            if (CIN.length() != 8) {
                showErrorDialog("Invalid CIN", "CIN must be 8 digits long.");
                return;
            }

            // Validate telephone length
            if (tel.length() != 8) {
                showErrorDialog("Invalid Telephone", "Telephone number must be 8 digits long.");
                return;
            }

            double salary;
            int telephone, cin_user;

            try {
                telephone = Integer.parseInt(tel);
                cin_user = Integer.parseInt(CIN);
                salary = Double.parseDouble(salaire);

                if (salary <= 0) {
                    showErrorDialog("Invalid Salary", "Salary must be a positive number.");
                    return;
                }

                if (date_naissance.isAfter(LocalDate.now())) {
                    showErrorDialog("Invalid Date of Birth", "Date of birth cannot be in the future.");
                    return;
                }
            } catch (NumberFormatException e) {
                showErrorDialog("Invalid Input", "Please enter valid numeric values.");
                return;
            }

            java.sql.Date birthday = java.sql.Date.valueOf(date_naissance);

            // Create new User object
            User newuser = new User();
            newuser.setNom_user(nom_user);
            newuser.setPrenom_user(prenom_user);
            newuser.setEmail_user(email_user);
            newuser.setMdp(mdp);
            newuser.setSalaire(salary);
            newuser.setDate_naissance(birthday);
            newuser.setCIN(cin_user);
            newuser.setTel(telephone);
            newuser.setAdresse_user(adresse_user.toString());
            newuser.setRole_user(role_user.toString());

            // Check if user already exists
            if (userGestion.userExists(newuser)) {
                showErrorDialog("User Exists", "This user already exists.");
                return;
            }

            // Create user
            userGestion.createUser(newuser);
            populateUserTable();
            clearFields();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "User added successfully!", ButtonType.OK);
            alert.showAndWait();
        }

        private void clearFields() {
            nom_textfeild.clear();
            prenom_textfeild.clear();
            email_textfeild.clear();
            birthdate_picker.setValue(null);
            mdp_textfeild.clear();
            salaire_textfeild.clear();
            cin_textfeild.clear();
            tel_textfeild.clear();
            adresse_choicebox.setValue(null);
            role_choicebox.setValue(null);
        }


        //    private void clearFields() {
//        nom_textfeild.clear();
//        prenom_textfeild.clear();
//        email_textfeild.clear();
//        birthdate_picker.getEditor().clear(); // Clear the DatePicker
//        mdp_textfeild.clear();
//        salaire_textfeild.clear();
//        tel_textfeild.clear();
//        cin_textfeild.clear();
//        role_choicebox.getItems().clear();
//        adresse_choicebox.getItems().clear();
//    }
        private boolean isValidEmail(String email) {
            String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
            return email.matches(emailRegex);
        }
        private StringConverter<Date> dateConverter = new StringConverter<Date>() {
            private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            @Override
            public String toString(Date date) {
                if (date == null) {
                    return "";
                }
                return sdf.format(date);
            }

            @Override
            public Date fromString(String string) {
                if (string == null || string.trim().isEmpty()) {
                    return null;
                }
                try {
                    return sdf.parse(string);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };

        private String generateQRContent(List<User> users) {
            StringBuilder content = new StringBuilder();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String formattedDate = now.format(formatter);

            content.append("Sign-in time: ").append(formattedDate).append("\n");

            for (User user : users) {
                content.append("ID: ").append(user.getId_user()).append("\n");
                content.append("Name: ").append(user.getNom_user()).append("\n");
                content.append("Surname: ").append(user.getPrenom_user()).append("\n");
                content.append("Date of Birth: ").append(user.getDate_naissance()).append("\n");
                // Add other user details as needed
                content.append("\n");
            }

            return content.toString();
        }

        private String generateQRCode(String content) {
            // Generate the QR code logic here
            // Combine the time of sign-in and user details into a single string
            StringBuilder qrCodeData = new StringBuilder();
            qrCodeData.append(content);

            // Generate the QR code and return the path to the generated image
            String qrCodePath = "path_to_save_qr_code_image";
            QRCodeGenerator.generateQRCode(qrCodeData.toString(), 500, 500, qrCodePath);
            return qrCodePath;
        }
        @FXML
        void handleGenerateQRCodeButtonClick(ActionEvent event) {
            // Generate QR code content
            List<User> users = tableUsers.getItems();
            String qrCodeContent = generateQRContent(users);

            // Generate QR code and save it
            String qrCodePath = generateQRCode(qrCodeContent);

            // Load the generated QR code image
            Image qrCodeImage = new Image("file:" + qrCodePath);

            // Display the QR code image in a new window
            ImageView imageView = new ImageView(qrCodeImage);
            Group root = new Group(imageView);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Generated QR Code");
            stage.show();

            // Show success message
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("QR Code Generated");
            successAlert.setHeaderText(null);
            successAlert.setContentText("QR code generated successfully. Path: " + qrCodePath);
            successAlert.showAndWait();

            // Print scanned data to the console
            String scannedData = "Your scanned data here";
            System.out.println("Scanned Data: " + scannedData);

            // Log scanned data
            Logger.getLogger(getClass().getName()).log(Level.INFO, "Scanned Data: " + scannedData);

        }


    }


