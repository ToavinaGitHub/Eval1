package com.eval1.mg.Model;

import java.util.HashMap;

public class EtatConfiguration {

    public static String getEtatConstruction(int etat){

        if(etat==0){
            return "En demande";
        } else if (etat==10) {
            return "Accepté";
        }else if (etat==100) {
            return "En cours";
        }else if (etat==1000) {
            return "Termine";
        }

        return "Inconnu";
    }
    public static String getEtatTravaux(int etat){

        if(etat==0){
            return "Non assigné";
        } else if (etat==10) {
            return "Assigné";
        }else if (etat==100) {
            return "En cours";
        }else if (etat==1000) {
            return "Termine";
        }

        return "Inconnu";
    }
}
