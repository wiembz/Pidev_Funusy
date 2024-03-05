package pidev.esprit.Services;

import java.util.List;

public interface ICrud <T>{
    public void ajouterEntite(T c);
    public List<T> afficherEntite();

    public boolean EntiteExists(T c);
    public void updateEntite(T c);
    public void deleteEntite(int id);
}