package pidev.esprit.tests;
import pidev.esprit.entities.CarteBancaire;


import pidev.esprit.services.CardCrud;
import pidev.esprit.Tools.MyConnection;

import java.text.ParseException;
import java.time.LocalDate;

import static java.time.LocalDate.now;


public class MainClass {
    public static void main(String[] args) throws ParseException {
        MyConnection mc = MyConnection.getInstance();
        MyConnection mc2 = MyConnection.getInstance();


       /* TypeCompte tp = TypeCompte.1;

        Compte c = new Compte("FR1234567890123456789", 1000.0,tp);*/


        // Create a Compte object
       /* Compte c = new Compte("1010678", 500.258, tp);


        pcd.addCompte(c);*/
        //CompteCrud pcd=new CompteCrud();
       /* System.out.println(pcd.displayCompte());

        pcd.deleteCompte("12345678910121314150");*/

      // Compte updatedCompte = new Compte("123678", 870,"Epargne");
      // pcd.updateCompte(updatedCompte); // Provide the updated Compte object
        java.sql.Date sqlDate = java.sql.Date.valueOf(LocalDate.of(2025, 2, 24));

        CarteBancaire c =new CarteBancaire("123086789",1234,0000,sqlDate,"9784");
        CardCrud pc=new CardCrud();
        pc.addCard(c,c.getRib());
        System.out.println(pc.displayCard());


    }
}
