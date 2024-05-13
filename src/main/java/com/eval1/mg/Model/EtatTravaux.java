package com.eval1.mg.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "etat_travaux")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EtatTravaux {

    @Id
    int id;

    @NotBlank(message = "Designation etat travaux ne doit pas etre vide")
    String designation;

}
