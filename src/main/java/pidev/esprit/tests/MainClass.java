package pidev.esprit.tests;
import pidev.esprit.entities.Compte;
import pidev.esprit.entities.TypeCompte;

import pidev.esprit.services.CompteCrud;
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
        TypeCompte tp = TypeCompte.EPARGNE;

        // Create a Compte object
       /* Compte c = new Compte("1010678", 500.258, tp);


        pcd.addCompte(c);*/
        CompteCrud pcd=new CompteCrud();
        System.out.println(pcd.displayCompte());

        pcd.deleteCompte("12345678910121314150");

        Compte updatedCompte = new Compte("123678", 150, TypeCompte.EPARGNE);
        pcd.updateCompte(updatedCompte); // Provide the updated Compte object

    }
}
