package pidev.esprit.Controllers.User;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import pidev.esprit.User.Entities.User;
import pidev.esprit.User.Services.GestionUser;
import pidev.esprit.User.Entities.Role;
import pidev.esprit.User.Entities.AdresseUser;

public class DashboardClientController {

    private final GestionUser userGestion;

    @FXML
    private TextField nomTextField;

    @FXML
    private TextField prenomTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField mdpTextField;

    @FXML
    private TextField salaireTextField;

    @FXML
    private DatePicker dateNaissancePicker;

    @FXML
    private TextField cinTextField;

    @FXML
    private TextField telTextField;

    @FXML
    private ChoiceBox<Role> roleChoiceBox;

    @FXML
    private ChoiceBox<AdresseUser> adresseChoiceBox;
    @FXML
    void initialize() {
        adresseChoiceBox.setItems(FXCollections.observableArrayList(AdresseUser.values()));
    }


        private User currentUser;

    public void setCurrentUser(User user) {
        this.currentUser = user;
        afficherInfos();
    }

    private void afficherInfos() {
        nomTextField.setText(currentUser.getNom_user());
        prenomTextField.setText(currentUser.getPrenom_user());
        emailTextField.setText(currentUser.getEmail_user());
        mdpTextField.setText(currentUser.getMdp());
        salaireTextField.setText(String.valueOf(currentUser.getSalaire()));
        cinTextField.setText(String.valueOf(currentUser.getCIN()));
        telTextField.setText(String.valueOf(currentUser.getTel()));
        adresseChoiceBox.setValue(AdresseUser.valueOf(currentUser.getAdresse_user())); // Set the address choice box value
    }

    @FXML
    private void modifierInfos() {
        User updatedUser=new User();
        String nom = nomTextField.getText();
        String prenom = prenomTextField.getText();
        String email = emailTextField.getText();
        String mdp = mdpTextField.getText();
        double salaire = Double.parseDouble(salaireTextField.getText());
        int cin = Integer.parseInt(cinTextField.getText());
        int tel = Integer.parseInt(telTextField.getText());
        AdresseUser adresse = adresseChoiceBox.getValue();// Get selected address from choice box

        updatedUser.setNom_user(nom);
        updatedUser.setPrenom_user(prenom);
        updatedUser.setEmail_user(email);
        updatedUser.setMdp(mdp);
        updatedUser.setSalaire(salaire);
        updatedUser.setCIN(cin);
        updatedUser.setTel(tel);
        updatedUser.setAdresse_user(adresse.getAdresseUser()); // Set address

        userGestion.updateUserClient(updatedUser); // Call update method
        // You may also want to show a confirmation message here
        setCurrentUser(updatedUser);
        userGestion.afficherUser();
    }

    public DashboardClientController() {
        userGestion = new GestionUser();
    }

    public void handleAccountsButtonClick(ActionEvent event) {
    }

    public void handleTransactionsButtonClick(ActionEvent event) {
    }

    public void handleCreditsButtonClick(ActionEvent event) {
    }

    public void handleInvestmentsButtonClick(ActionEvent event) {
    }

    public void handleCommentairesButtonClick(ActionEvent event) {
    }

    public void handleProfilesButtonClick(ActionEvent event) {
    }

    // Handle other button actions if needed

}
