package com.eval1.mg.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "travaux")
@Getter
@Setter
@NoArgsConstructor
public class Travaux {

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
            @JoinColumn(name = "id_devis",nullable = false)
    Devis devis;

    @ManyToOne
            @JoinColumn(name = "id_type_travail",nullable = true)
    TypeTravail typeTravail;

    @ManyToOne
            @JoinColumn(name = "id_unite",nullable = false)
    Unite unite;

    @ManyToOne
            @JoinColumn(name = "id_parent",nullable = true)
    Travaux travaux;

    @Transient
    String etatString;

    public Travaux(int id, String numero, String designation,Devis devis, double prixUnitaire, double quantite, double montantTotal, int etat, TypeTravail typeTravail, Unite unite, Travaux travaux) {
        this.id = id;
        this.numero = numero;
        this.designation = designation;
        this.prixUnitaire = prixUnitaire;
        this.quantite = quantite;
        this.montantTotal = montantTotal;
        this.devis =devis;
        this.etat = etat;
        this.typeTravail = typeTravail;
        this.unite = unite;
        this.travaux = travaux;
        this.etatString = EtatConfiguration.getEtatTravaux(etat);
    }
}
