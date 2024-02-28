package pidev.esprit.Controllers.Commentaire;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pidev.esprit.Entities.Signal;
import pidev.esprit.Services.SignalCrud;

import java.util.List;

public class AdminSignal {
    @FXML
    private TableColumn<?, ?> date_signal;

    @FXML
    private TableColumn<?, ?> description;

    @FXML
    private TableColumn<?, ?> id_commentaire;

    @FXML
    private TableColumn<?, ?> id_signal;

    @FXML
    private TableView<Signal> tablesignal;
    SignalCrud signalservices = new SignalCrud();
    private ObservableList<Signal> signalList = FXCollections.observableArrayList();
    private List<Signal> signals;

    @FXML
    public void initialize() {
        // Initialisez votre TableView et vos colonnes ici
        id_signal.setCellValueFactory(new PropertyValueFactory<>("id_signal"));
        id_commentaire.setCellValueFactory(new PropertyValueFactory<>("id_commentaire"));
        date_signal.setCellValueFactory(new PropertyValueFactory<>("date_signal"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        SignalCrud sc = new SignalCrud();
        signalList.clear();
        signalList.addAll(sc.afficherEntite());
        tablesignal.setItems(signalList);

    }

    public void setSignals(List<Signal> signals) {
        this.signals = signals;

    }
}
