package com.eval1.mg.Model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "type_travail")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TypeTravail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @NotBlank(message = "Numero ne doit pas etre vide")
    String numero;

    @NotBlank(message = "Designation de type travail ne doit pas etre vide")
    String designation;

    int etat;

}
