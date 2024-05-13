package com.eval1.mg.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "historique_prix_travaux")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HistoriquePrixTravaux
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne
            @JoinColumn(name = "id_travaux",nullable = false)
    Travaux travaux;

    double prixUnitaire;

    double quantite;

    Date daty;

    int etat;
}
