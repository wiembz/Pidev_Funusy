package pidev.esprit.Entities;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;

import java.time.LocalDate;

public class Echeance {
    private final IntegerProperty numero;
    private final ObjectProperty<LocalDate> echeance;
    private final DoubleProperty principal;
    private final DoubleProperty valeurResiduelle;
    private final DoubleProperty interets;
    private final DoubleProperty mensualite;

    public Echeance(int numero, LocalDate echeance, double principal, double valeurResiduelle, double interets, double mensualite) {
        this.numero = new SimpleIntegerProperty(numero);
        this.echeance = new SimpleObjectProperty<>(echeance);
        this.principal = new SimpleDoubleProperty(principal);
        this.valeurResiduelle = new SimpleDoubleProperty(valeurResiduelle);
        this.interets = new SimpleDoubleProperty(interets);
        this.mensualite = new SimpleDoubleProperty(mensualite);
    }

    public IntegerProperty numeroProperty() {
        return numero;
    }

    public ObjectProperty<LocalDate> echeanceProperty() {
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

    public int getNumero() {
        return numero.get();
    }

    public void setNumero(int numero) {
        this.numero.set(numero);
    }

    public LocalDate getEcheance() {
        return echeance.get();
    }

    public void setEcheance(LocalDate echeance) {
        this.echeance.set(echeance);
    }

    public double getPrincipal() {
        return principal.get();
    }

    public void setPrincipal(double principal) {
        this.principal.set(principal);
    }

    public double getValeurResiduelle() {
        return valeurResiduelle.get();
    }

    public void setValeurResiduelle(double valeurResiduelle) {
        this.valeurResiduelle.set(valeurResiduelle);
    }

    public double getInterets() {
        return interets.get();
    }

    public void setInterets(double interets) {
        this.interets.set(interets);
    }

    public double getMensualite() {
        return mensualite.get();
    }

    public void setMensualite(double mensualite) {
        this.mensualite.set(mensualite);
    }

    @Override
    public String toString() {
        return "Echeance{" +
                "numero=" + numero +
                ", echeance=" + echeance +
                ", principal=" + principal +
                ", valeurResiduelle=" + valeurResiduelle +
                ", interets=" + interets +
                ", mensualite=" + mensualite +
                '}';
    }

    public ObservableValue<Number> totalProperty() {
        return new SimpleDoubleProperty(principal.get() + interets.get() + mensualite.get());
    }
}
