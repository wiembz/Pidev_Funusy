package pidev.esprit.Services;

import pidev.esprit.Entities.CarteBancaire;


import java.util.List;

public interface CRUDC <T>{
    public void addCard(T c,String rib);
    public List<T> displayCard();
    public void updateCard(CarteBancaire c);

    public static void deleteCard(String num) {

    }


}
