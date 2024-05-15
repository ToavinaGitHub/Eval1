package com.eval1.mg.Model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.*;

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

    @Min(value = 0, message = "Surface type Maison doit être supérieur à zéro")
    double surface;

    @NotBlank(message = "Description type Maison ne doit pas etre vide")
    String description;

    int etat;


}
