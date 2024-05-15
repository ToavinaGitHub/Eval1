package com.eval1.mg.Model;


import com.eval1.mg.Exception.ValeurInvalideException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "construction_csv")
@Getter
@Setter
@NoArgsConstructor
public class Construction_csv
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @NotBlank(message = "Client ne doit pas etre vide ")
    String client;

    @NotBlank(message = "Reference construction ne doit pas etre vide ")
    String refConstruction;

    @NotBlank(message = "Type maison ne doit pas etre vide ")
    String typeMaison;

    @NotBlank(message = "Finition ne doit pas etre vide ")
    String finition;

    double tauxFinition;

    Date dateDevis;

    Date date_debut;

    @NotBlank(message = "Lieu ne doit pas etre vide ")
    String lieu;

    public Construction_csv(int id, String client, String refConstruction, String typeMaison, String finition, double tauxFinition, Date dateDevis, Date date_debut, String lieu) throws ValeurInvalideException {
        this.id = id;
        this.client = client;
        this.refConstruction = refConstruction;
        this.typeMaison = typeMaison;
        this.finition = finition;
        this.setTauxFinition(tauxFinition);
        this.dateDevis = dateDevis;
        this.date_debut = date_debut;
        this.lieu = lieu;
    }

    public Construction_csv( String client, String refConstruction, String typeMaison, String finition, double tauxFinition, Date dateDevis, Date date_debut, String lieu) throws ValeurInvalideException {
        this.client = client;
        this.refConstruction = refConstruction;
        this.typeMaison = typeMaison;
        this.finition = finition;
        this.setTauxFinition(tauxFinition);
        this.dateDevis = dateDevis;
        this.date_debut = date_debut;
        this.lieu = lieu;
    }

    public void setTauxFinition(double tauxFinition) throws ValeurInvalideException {
        if(tauxFinition<0){
            throw new ValeurInvalideException("Taux finition");
        }
        this.tauxFinition = tauxFinition;
    }
}
