package com.eval1.mg.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "employe")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @NotBlank(message = "Nom employé ne doit pas etre vide")
    String nom;
    @NotBlank(message = "Prenom employé ne doit pas etre vide")
    String prenom;
    @NotBlank(message = "Genre employé ne doit pas etre vide")
    int genre;

    @ManyToOne
            @JoinColumn(name = "id_metier",nullable = false)
    Metier metier;

    int etat;
}
