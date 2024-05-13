package com.eval1.mg.Model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "etat_construction")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EtatConstruction {

    @Id
    int id;

    @NotBlank(message = "Designation etat construction ne doit pas etre vide")
    String designation;

}
