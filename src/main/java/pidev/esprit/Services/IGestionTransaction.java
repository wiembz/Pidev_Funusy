package pidev.esprit.Services;

import java.sql.SQLException;
import java.util.List;
public interface IGestionTransaction <T>{
    void ajouter (T t) throws SQLException;
    void modifier (T t) throws SQLException;
    void supprimer (int id) throws SQLException;
    List<T> afficher() throws SQLException;
}
