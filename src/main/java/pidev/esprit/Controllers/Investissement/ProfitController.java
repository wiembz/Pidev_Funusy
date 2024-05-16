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
            resultLabel.setText("Expected Profit: " + String.format("%.2f", profit)+ " DT");
        } catch (NumberFormatException e) {
            resultLabel.setText("Please enter a valid investment amount in DT.");
        }
    }

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
                profitPercentage = 0.01f;
        }

        float profitPerMonth = investmentAmount * profitPercentage / 12;
        return profitPerMonth * investmentPeriod;
    }

    private static float calculateProfitPercentageForAgriculture(int investmentPeriod) {
        if (investmentPeriod <= 6) {
            return 0.1f;
        } else {
            return 0.15f;
        }
    }

    private static float calculateProfitPercentageForTechnologique(int investmentPeriod) {
        if (investmentPeriod <= 6) {
            return 0.15f;
        } else {
            return 0.2f;
        }
    }

    private static float calculateProfitPercentageForBourse(int investmentPeriod) {
        if (investmentPeriod <= 12) {
            return 0.2f;
        } else {
            return 0.3f;
        }
    }

    private static float calculateProfitPercentageForImmobilier(int investmentPeriod) {
        if (investmentPeriod <= 24) {
            return 0.3f;
        } else {
            return 0.35f;
        }
    }
}
