package com.eval1.mg.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "type_finition")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TypeFinition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    String id;

    @NotBlank(message = "Designation type de finition ne doit pas etre vide")
    String designation;

    double augmentation;

    int etat;
}
