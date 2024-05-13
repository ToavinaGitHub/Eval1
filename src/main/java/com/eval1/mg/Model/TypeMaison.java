package com.eval1.mg.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "type_maison")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TypeMaison {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    String idTypeMaison;

    @NotBlank(message = "Designation ne doit pas etre vide")
    String designation;

    @Min(value = 0, message = "Prix type Maison doit être supérieur à zéro")
    double prix;
    @Min(value = 0, message = "Duree type Maison doit être supérieur à zéro")
    double duree;

    int etat;


}
