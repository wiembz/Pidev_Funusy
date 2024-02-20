package pidev.esprit.entities;

public enum TypeCompte {
    EPARGNE(1, "Epargne");
  //  AUTRE(2, "Autre");

    private final int id_type_compte;
    private final String nature_compte;

    TypeCompte(int id_type_compte, String nature_compte) {
        this.id_type_compte = id_type_compte;
        this.nature_compte = nature_compte;
    }

    public int getId_type_compte() {
        return id_type_compte;
    }

    public String getNature_compte() {
        return nature_compte;
    }

    @Override
    public String toString() {
        return "nature_compte=" + nature_compte;

        };
    }





