package com.eval1.mg.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "metier_travaux")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MetierTravaux {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne
            @JoinColumn(name = "id_metier" , nullable = false)
    Metier metier;

    @ManyToOne
            @JoinColumn(name = "id_travaux" , nullable = false)
    Travaux travaux;

    @Min(value = 0, message = "Quantite  doit être supérieur à zéro")
    double quantite;
    @Min(value = 0, message = "Salaire  doit être supérieur à zéro")
    double salaire;
    @Min(value = 0, message = "Montant total  metier travaux doit être supérieur à zéro")
    double montantTotal;

    int etat;


}
