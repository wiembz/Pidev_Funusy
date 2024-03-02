package pidev.esprit.Entities;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Echeance {
    private final IntegerProperty numero;
    private final IntegerProperty echeance;
    private final DoubleProperty principal;
    private final DoubleProperty valeurResiduelle;
    private final DoubleProperty interets;
    private final DoubleProperty mensualite;

    public Echeance(int numero, double principal, double valeurResiduelle, double interets, double mensualite) {
        this.numero = new SimpleIntegerProperty(numero);
        this.echeance = new SimpleIntegerProperty(numero - 1);
        this.principal = new SimpleDoubleProperty(principal);
        this.valeurResiduelle = new SimpleDoubleProperty(valeurResiduelle);
        this.interets = new SimpleDoubleProperty(interets);
        this.mensualite = new SimpleDoubleProperty(mensualite);
    }

    public IntegerProperty numeroProperty() {
        return numero;
    }

    public IntegerProperty echeanceProperty() {
        return echeance;
    }

    public DoubleProperty principalProperty() {
        return principal;
    }

    public DoubleProperty valeurResiduelleProperty() {
        return valeurResiduelle;
    }

    public DoubleProperty interetsProperty() {
        return interets;
    }

    public DoubleProperty mensualiteProperty() {
        return mensualite;
    }
}
