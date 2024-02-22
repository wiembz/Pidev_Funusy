package pidev.esprit.Services;

import java.util.List;

public interface ICrud<T>{
    public void ajouterEntite(T p);
    public void updateEntite(T p);
    public void deleteEntite(T p);
    public List<T> afficherEntite();
}
