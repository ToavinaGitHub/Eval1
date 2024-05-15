package com.eval1.mg.Model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "historique_type_maison")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HistoriqueTypeMaison
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne
            @JoinColumn(name = "idTypeMaison",nullable = false)
    TypeMaison typeMaison;

    String designation;

    double duree;

    double prix;
    String description;

    double surface;

    Date daty;


}
