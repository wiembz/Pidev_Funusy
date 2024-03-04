package pidev.esprit.Entities;

public class    Agence {
    public Agence() {

    }

    public int getCodeAgence() {
        return codeAgence;
    }

    public void setCodeAgence(int codeAgence) {
        this.codeAgence = codeAgence;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public int getCodepostal() {
        return codepostal;
    }

    public void setCodepostal(int codepostal) {
        this.codepostal = codepostal;
    }

    private int codeAgence;
    private String adresse;
    private int codepostal;

    public Agence(int codeAgence, String adresse, int codepostal) {
        this.codeAgence = codeAgence;
        this.adresse = adresse;
        this.codepostal = codepostal;
    }
}
