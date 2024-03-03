package pidev.esprit.Controllers.Credit;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import pidev.esprit.Entities.Echeance;
import pidev.esprit.Services.SimulateurCrud;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.ObservableList;
import pidev.esprit.Entities.Echeance;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;


import java.time.LocalDate;

public class SimulationController {
    @FXML
    private TextField textFieldMontant;

    @FXML
    private TextField textFieldDuree;

    @FXML
    private TextField textFieldTaux;

    @FXML
    private Button buttonValider;

    @FXML
    private TableView<Echeance> tableView;

    @FXML
    private TableColumn<Echeance, Number> numero;

    @FXML
    private TableColumn<Echeance, LocalDate> echeance;

    @FXML
    private TableColumn<Echeance, Number> principal;

    @FXML
    private TableColumn<Echeance, Number> valeurResiduelle;

    @FXML
    private TableColumn<Echeance, Number> interets;

    @FXML
    private TableColumn<Echeance, Number> mensualite;

    @FXML
    private TableColumn<Echeance, Number> totalColumn;

    private CreditModel creditModel = new CreditModel();

    private SimulateurCrud simulateurCrud;


    public class CreditModel {
        public ObservableList<Echeance> calculerEcheances(double montant, int duree, double taux) {
            ObservableList<Echeance> echeances = FXCollections.observableArrayList();
            double tauxMensuel = taux / 100 / 12;
            double mensualite = montant * (tauxMensuel) / (1 - Math.pow(1 + tauxMensuel, -duree));
            double valeurResiduelle = montant;

            for (int i = 1; i <= duree; i++) {
                double interets = valeurResiduelle * tauxMensuel;
                double principal = mensualite - interets;
                valeurResiduelle -= principal;


                if (i == duree) {
                    valeurResiduelle = 0.00;
                }

                Echeance echeance = new Echeance(i, LocalDate.now().plusMonths(i), principal, valeurResiduelle, interets, mensualite);
                echeances.add(echeance);
            }

            // Ajouter une ligne pour le total
            double totalPrincipal = montant;
            double totalInterets = echeances.stream().mapToDouble(Echeance::getInterets).sum();
            double totalMensualites = echeances.stream().mapToDouble(Echeance::getMensualite).sum();
            Echeance totalEcheance = new Echeance(duree + 1, null, totalPrincipal, 0, totalInterets, totalMensualites);
            echeances.add(totalEcheance);

            return echeances;
        }


    }


    public SimulationController() {
        simulateurCrud = new SimulateurCrud();
    }

    @FXML
    private void initialize() {
        numero.setCellValueFactory(cellData -> cellData.getValue().numeroProperty());
        echeance.setCellValueFactory(cellData -> cellData.getValue().echeanceProperty());
        principal.setCellValueFactory(cellData -> cellData.getValue().principalProperty());
        valeurResiduelle.setCellValueFactory(cellData -> cellData.getValue().valeurResiduelleProperty());
        interets.setCellValueFactory(cellData -> cellData.getValue().interetsProperty());
        mensualite.setCellValueFactory(cellData -> cellData.getValue().mensualiteProperty());
    }

    public class PDFGenerator {

        public static void generatePDF(ObservableList<Echeance> echeances) {
            Document document = new Document(PageSize.A4);
            try {
                PdfWriter.getInstance(document, new FileOutputStream("echeances.pdf"));
                document.open();

                // Ajouter un titre au document
                Paragraph title = new Paragraph("Échéances");
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);
                document.add(new Paragraph("\n"));

                // Créer une table pour les données
                PdfPTable table = new PdfPTable(6);
                table.setWidthPercentage(100);

                // Ajouter les en-têtes de colonne
                table.addCell("Numéro");
                table.addCell("Échéance");
                table.addCell("Principal");
                table.addCell("Valeur Résiduelle");
                table.addCell("Intérêts");
                table.addCell("Mensualité");

                // Ajouter les données de la TableView à la table PDF
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                for (Echeance echeance : echeances) {
                    table.addCell(String.valueOf(echeance.getNumero()));
                    if (echeance.getEcheance() != null) {
                        table.addCell(echeance.getEcheance().format(dateFormatter));
                    } else {
                        table.addCell("");
                    }
                    table.addCell(String.valueOf(echeance.getPrincipal()));
                    table.addCell(String.valueOf(echeance.getValeurResiduelle()));
                    table.addCell(String.valueOf(echeance.getInterets()));
                    table.addCell(String.valueOf(echeance.getMensualite()));
                }

                // Ajouter la table au document
                document.add(table);

                // Fermer le document
                document.close();

                System.out.println("Le fichier PDF a été généré avec succès.");
            } catch (DocumentException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void ClickValider() {
        try {
            double montant = Double.parseDouble(textFieldMontant.getText());
            int duree = Integer.parseInt(textFieldDuree.getText());
            double taux = Double.parseDouble(textFieldTaux.getText());

            ObservableList<Echeance> echeances = creditModel.calculerEcheances(montant, duree, taux);
            tableView.setItems(echeances);
        } catch (NumberFormatException e) {
            // Gérer l'exception si les champs ne contiennent pas des nombres valides
            System.err.println("Veuillez saisir des valeurs numériques valides.");
        }
    }


    public void handleGenererPDFButtonClicked(ActionEvent actionEvent) {
        try {
            // Récupérer les données de la TableView
            ObservableList<Echeance> echeances = tableView.getItems();

            // Générer le PDF avec les données de la TableView
            PDFGenerator.generatePDF(echeances);

            // Afficher un message de réussite ou une notification à l'utilisateur
            System.out.println("Le fichier PDF a été généré avec succès.");

            // Ouvrir le fichier PDF généré
            File file = new File("echeances.pdf");
            Desktop.getDesktop().open(file);
        } catch (Exception e) {
            // Gérer les erreurs et afficher un message d'erreur à l'utilisateur
            System.err.println("Erreur lors de la génération du fichier PDF : " + e.getMessage());
        }

    }
}



