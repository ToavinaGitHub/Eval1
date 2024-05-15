package com.eval1.mg.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "payement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    String id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date daty;

    @ManyToOne
            @JoinColumn(name = "id_construction" , nullable = false)
    Construction construction;

    @Min(value = 0,message = "Montant payement doit etre superieur a 0")
    double montant;

    int etat;

    @NotBlank(message = "Reference paiement ne doit pas etre vide")
    String refPayement;
}
