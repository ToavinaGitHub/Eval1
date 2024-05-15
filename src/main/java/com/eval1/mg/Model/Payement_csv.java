package com.eval1.mg.Model;


import com.eval1.mg.Exception.ValeurInvalideException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "payement_csv")
@Getter
@Setter
@NoArgsConstructor
public class Payement_csv {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @NotBlank(message = "Reference construction ne doit pas etre vide")
    String refConstruction;

    @NotBlank(message = "Reference paiement ne doit pas etre vide")
    String refPaiement;

    Date datePaiement;

    double montant;

    public Payement_csv(int id, String refPaiement,String refConstruction, Date datePaiement, double montant) throws ValeurInvalideException {
        this.id = id;
        this.setRefConstruction(refConstruction);
        this.refPaiement = refPaiement;
        this.datePaiement = datePaiement;
        this.setMontant(montant);
    }


    public void setMontant(double montant) throws ValeurInvalideException {
        if(montant<=0){
            throw new ValeurInvalideException("Montant");
        }
        this.montant = montant;
    }
}
