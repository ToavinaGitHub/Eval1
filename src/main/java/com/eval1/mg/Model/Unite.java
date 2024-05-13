package com.eval1.mg.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "unite")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Unite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @NotBlank(message = "Designation unite ne doit pas etre vide")
    String designation;


}
