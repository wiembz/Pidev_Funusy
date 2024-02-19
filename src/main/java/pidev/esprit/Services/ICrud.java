package pidev.esprit.Services;

import java.util.List;

public interface ICrud<T> {
    public void ajouterEntite(T i);

    public List<T> afficherEntite();

    public void updateEntite(T i);

    public void deleteEntite(int id);

}