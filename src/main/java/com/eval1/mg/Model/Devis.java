package com.eval1.mg.Model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "devis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Devis {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    String id;
    String designation;

    @ManyToOne
            @JoinColumn(name = "id_type_maison" , nullable = false)
    TypeMaison typeMaison;

    int etat;

    double montantTotal;

    double dureeTotal;

}
