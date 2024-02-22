package pidev.esprit.services;
import pidev.esprit.entities.Compte;

import java.util.List;

public interface CRUD <T> {
    public void addCompte(T c);
    public List<T> displayCompte();
    public void updateCompte(Compte c);

    public static void deleteCompte(String rib) {

    }
}

