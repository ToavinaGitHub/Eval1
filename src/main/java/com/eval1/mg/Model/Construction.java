package com.eval1.mg.Model;

import jakarta.annotation.Nullable;
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
@Table(name = "construction")
@Getter
@Setter
@NoArgsConstructor
public class Construction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    String id;

    @NotBlank(message = "Demande construction ne doit pas etre vide")
    String demande;

    @ManyToOne
            @JoinColumn(name = "id_utilisateur" ,nullable = false)
    Utilisateur utilisateur;

    //Save finition , Save Devis (Details)
    @ManyToOne
            @JoinColumn(name = "id_devis" ,nullable = false)
    Devis devis;

    @ManyToOne
            @JoinColumn(name = "id_type_finition" ,nullable = false)
    TypeFinition typeFinition;

    @Nullable
    double pourcentageFinition;

    @Nullable
    double duree;

    @Min(value = 0,message = "Montant total doit etre superieur a 0")
            //Total travaux + finition
    double montantTotal;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date daty;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date debut;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date fin;

    int etat;

    @Transient
    String etatString;

    public Construction(String id, String demande, Date daty, int etat) {
        this.id = id;
        this.demande = demande;
        this.daty = daty;
        this.etat = etat;
        this.etatString = EtatConfiguration.getEtatConstruction(etat);
    }

    public Construction(String id, String demande, Devis devis, TypeFinition typeFinition, double montantTotal, Date daty, Date fin, int etat) {
        this.id = id;
        this.demande = demande;
        this.devis = devis;
        this.typeFinition = typeFinition;
        this.montantTotal = montantTotal;
        this.daty = daty;
        this.fin = fin;
        this.etat = etat;
        this.etatString = EtatConfiguration.getEtatConstruction(etat);
    }
}
