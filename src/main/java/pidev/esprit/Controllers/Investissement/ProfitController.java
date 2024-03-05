package pidev.esprit.Controllers.Investissement;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

public class ProfitController {
    @FXML
    private TextField investmentAmountField;

    @FXML
    private Spinner<Integer> periodeField;

    @FXML
    private ComboBox<String> type_projetField;

    @FXML
    private Button calculateButton;

    @FXML
    private Label resultLabel;

    // Removed @FXML from the static methods

    @FXML
    private void initialize() {
        calculateButton.setOnAction(event -> calculateProfit());
    }

    @FXML
    private void calculateProfit() {
        try {
            float investmentAmount = Float.parseFloat(investmentAmountField.getText());
            int investmentPeriod = periodeField.getValue();
            String projectType = type_projetField.getValue();

            float profit = calculateProfit(investmentAmount, investmentPeriod, projectType);
            resultLabel.setText("Expected Profit: " + String.format("%.2f", profit));
        } catch (NumberFormatException e) {
            resultLabel.setText("Please enter a valid investment amount.");
        }
    }

    // Static methods for calculating profit
    public static float calculateProfit(float investmentAmount, int investmentPeriod, String projectType) {
        float profitPercentage;

        switch (projectType) {
            case "AGRICULTURE":
                profitPercentage = calculateProfitPercentageForAgriculture(investmentPeriod);
                break;
            case "TECHNOLOGIQUE":
                profitPercentage = calculateProfitPercentageForTechnologique(investmentPeriod);
                break;
            case "BOURSE":
                profitPercentage = calculateProfitPercentageForBourse(investmentPeriod);
                break;
            case "IMMOBILIER":
                profitPercentage = calculateProfitPercentageForImmobilier(investmentPeriod);
                break;
            default:
                profitPercentage = 0.01f; // Default profit percentage
        }

        float profitPerMonth = investmentAmount * profitPercentage / 12;
        return profitPerMonth * investmentPeriod;
    }

    // Static methods for calculating profit percentage for each project type
    private static float calculateProfitPercentageForAgriculture(int investmentPeriod) {
        if (investmentPeriod <= 6) {
            return 0.1f; // 10% interest rate for investments less than or equal to 6 months
        } else {
            return 0.15f; //
        }
    }

    private static float calculateProfitPercentageForTechnologique(int investmentPeriod) {
        if (investmentPeriod <= 6) {
            return 0.15f; // 1.5% interest rate for investments less than or equal to 6 months
        } else {
            return 0.2f; // 2% interest rate for investments greater than 6 months
        }
    }

    private static float calculateProfitPercentageForBourse(int investmentPeriod) {
        if (investmentPeriod <= 12) {
            return 0.2f; // 2% interest rate for investments less than or equal to 12 months
        } else {
            return 0.3f; // 3% interest rate for investments greater than 12 months
        }
    }

    private static float calculateProfitPercentageForImmobilier(int investmentPeriod) {
        if (investmentPeriod <= 24) {
            return 0.3f; // 2.5% interest rate for investments less than or equal to 24 months
        } else {
            return 0.35f; // 3% interest rate for investments greater than 24 months
        }
    }
}
