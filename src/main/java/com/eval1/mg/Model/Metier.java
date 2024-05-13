package com.eval1.mg.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "metier")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Metier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @NotBlank(message = "Designation metier ne doit pas etre null")
    String designation;

    int etat;
}
