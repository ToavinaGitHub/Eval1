package com.eval1.mg.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "details_type_maison")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetailsTypeMaison {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    String id;

    @ManyToOne
            @JoinColumn(name = "id_type_maison" , nullable = false)
    TypeMaison typeMaison;

    String designation;

    int etat;


}
