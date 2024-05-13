package com.eval1.mg.Model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "travaux_construction")
@Getter
@Setter
@NoArgsConstructor
public class TravauxConstruction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @NotBlank(message = "Numero travaux ne doit pas etre vide")
    String numero;
    @NotBlank(message = "Designation travaux ne doit pas etre vide")
    String designation;

    @Min(value = 0, message = "Le prix unitaire doit être supérieur à zéro")
    double prixUnitaire;
    @Min(value = 0, message = "Quantite  doit être supérieur à zéro")
    double quantite;
    @Min(value = 0, message = "Montant total doit être supérieur à zéro")
    double montantTotal;

    int etat;

    @ManyToOne
    @JoinColumn(name = "id_construction",nullable = false)
    Construction construction;

    @ManyToOne
    @JoinColumn(name = "id_type_travail",nullable = false)
    TypeTravail typeTravail;

    @ManyToOne
    @JoinColumn(name = "id_unite",nullable = false)
    Unite unite;

    @ManyToOne
    @JoinColumn(name = "id_parent",nullable = true)
    TravauxConstruction travauxConstruction;

    @Transient
    String etatString;

    public TravauxConstruction(int id, String numero, String designation, double prixUnitaire, double quantite, double montantTotal, int etat, TypeTravail typeTravail, Unite unite, TravauxConstruction travauxConstruction) {
        this.id = id;
        this.numero = numero;
        this.designation = designation;
        this.prixUnitaire = prixUnitaire;
        this.quantite = quantite;
        this.montantTotal = montantTotal;
        this.etat = etat;
        this.typeTravail = typeTravail;
        this.unite = unite;
        this.travauxConstruction = travauxConstruction;
        this.etatString = EtatConfiguration.getEtatTravaux(etat);
    }

    public TravauxConstruction( String numero, String designation, double prixUnitaire, double quantite, double montantTotal, int etat, TypeTravail typeTravail, Unite unite, TravauxConstruction travauxConstruction) {

        this.numero = numero;
        this.designation = designation;
        this.prixUnitaire = prixUnitaire;
        this.quantite = quantite;
        this.montantTotal = montantTotal;
        this.etat = etat;
        this.typeTravail = typeTravail;
        this.unite = unite;
        this.travauxConstruction = travauxConstruction;
        this.etatString = EtatConfiguration.getEtatTravaux(etat);
    }

    public double getTotal(ArrayList<TravauxConstruction> allTravaux){
        double res = 0;
        System.out.println("haha");
        for (TravauxConstruction t:allTravaux
             ) {
            res+=t.getMontantTotal();
        }

        return res;
    }

}
