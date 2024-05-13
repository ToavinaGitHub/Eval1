package com.eval1.mg.Model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "metier_travaux_employe")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MetierTravauxEmploye {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne
            @JoinColumn(name = "id_metier_travaux",nullable = false)
    MetierTravaux metierTravaux;


    @ManyToOne
            @JoinColumn(name = "id_employe",nullable = false)
    Employe employe;

    int etat;

}
