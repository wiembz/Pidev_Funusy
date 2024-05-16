package pidev.esprit.Services;
import pidev.esprit.Entities.Compte;

import java.util.List;

public interface CRUD <T> {
    public void addCompte(T c);
    public List<T> displayCompte();

    public static void updateCompte(Compte c) {

    }

    public static void deleteCompte(String rib) {

    }
}

