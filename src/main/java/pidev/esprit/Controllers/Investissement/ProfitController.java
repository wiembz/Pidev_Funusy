package pidev.esprit.Controllers.Investissement;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfitController implements Initializable {

    @FXML
    private TextField investmentAmountField;
    @FXML
    private Spinner<Integer> periodeField;
    @FXML
    private ComboBox<String> projectTypeComboBox;
    @FXML
    private Button calculateButton;
    @FXML
    private Label resultLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Populate the project type ComboBox
        projectTypeComboBox.getItems().addAll("Agriculture", "Technologique", "Bourse", "Immobilier");

        calculateButton.setOnAction(actionEvent -> calculateProfit());
    }

    private void calculateProfit() {
        try {
            float investmentAmount = Float.parseFloat(investmentAmountField.getText());
            int investmentPeriod = Integer.parseInt(periodeField.getValue().toString());
            String selectedProjectType = projectTypeComboBox.getValue();

            // Calculate profit using your calculation method
            float profit = calculateBenefit(investmentAmount, investmentPeriod, selectedProjectType);

            resultLabel.setText("Profit: " + profit + " TND");
        } catch (NumberFormatException e) {
            resultLabel.setText("Invalid input. Please enter numeric values.");
        }
    }

    private float calculateBenefit(float montant, int periode, String projectType) {
        // Determine the annual interest rate based on the selected project type
        float annualInterestRate;
        switch (projectType) {
            case "Agriculture":
                annualInterestRate = 0.005f; // Define the interest rate for Agriculture projects
                break;
            case "Technologique":
                annualInterestRate = 0.01f; // Define the interest rate for Technologique projects
                break;
            case "Bourse":
                annualInterestRate = 0.015f; // Define the interest rate for Bourse projects
                break;
            case "Immobilier":
                annualInterestRate = 0.02f; // Define the interest rate for Immobilier projects
                break;
            default:
                annualInterestRate = 0.025f; // Default interest rate
        }

        float monthlyInterestRate = annualInterestRate / 12;

        // Calculate the benefit using compound interest formula
        float benefit = (float) (montant * (Math.pow(1 + monthlyInterestRate, periode) - 1));

        return benefit;
    }

}
