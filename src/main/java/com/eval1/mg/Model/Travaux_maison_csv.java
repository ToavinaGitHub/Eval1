package com.eval1.mg.Model;

import com.eval1.mg.Exception.ValeurInvalideException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "travaux_maison_csv")
@Getter
@Setter
@NoArgsConstructor
public class Travaux_maison_csv
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;


    @NotBlank(message = "Type maison csv ne doit pas etre vide")
    String typeMaison;


    @NotBlank(message = "Description csv ne doit pas etre vide")
    String description;

    @Min(value = 0,message = "Surface doit etre sup à 0")
    double surface;

    @NotBlank(message = "Code travaux csv ne doit pas etre vide")
    String codeTravaux;

    @NotBlank(message = "Type travaux csv ne doit pas etre vide")
    String typeTravaux;

    String unite;

    @Min(value = 0,message = "¨Prix unitaire doit etre sup à 0")
    double prixUnitaire;

    @Min(value = 0,message = "Quantite doit etre sup à 0")
    double quantite;

    @Min(value = 0,message = "Duree travaux doit etre sup à 0")
    double dureeTravaux;

    public Travaux_maison_csv(int id,String typeMaison, String description,String typeTravaux, double surface, String codeTravaux, String unite, double prixUnitaire, double quantite, double dureeTravaux) throws ValeurInvalideException {
        this.id = id;
        this.typeMaison = typeMaison;
        this.typeTravaux = typeTravaux;
        this.description = description;
        this.setSurface(surface);
        this.codeTravaux = codeTravaux;
        this.unite = unite;
        this.setPrixUnitaire(prixUnitaire);
        this.setQuantite(quantite);
        this.setDureeTravaux(dureeTravaux);
    }
    public Travaux_maison_csv( String typeMaison,String description,String typeTravaux, double surface, String codeTravaux, String unite, double prixUnitaire, double quantite, double dureeTravaux) throws ValeurInvalideException {
        this.description = description;
        this.typeMaison = typeMaison;
        this.typeTravaux=typeTravaux;
        this.setSurface(surface);
        this.codeTravaux = codeTravaux;
        this.unite = unite;
        this.setPrixUnitaire(prixUnitaire);
        this.setQuantite(quantite);
        this.setDureeTravaux(dureeTravaux);
    }

    public void setSurface(double surface) throws ValeurInvalideException {
        if(surface<=0){
            throw new ValeurInvalideException("Surface");
        }
        this.surface = surface;
    }

    public void setPrixUnitaire(double prixUnitaire) throws ValeurInvalideException {
        if(prixUnitaire<=0){
            throw new ValeurInvalideException("Prix unitaire");
        }
        this.prixUnitaire = prixUnitaire;
    }

    public void setQuantite(double quantite) throws ValeurInvalideException {
        if(quantite<=0){
            throw new ValeurInvalideException("Quantite");
        }
        this.quantite = quantite;
    }

    public void setDureeTravaux(double dureeTravaux) throws ValeurInvalideException {
        if(dureeTravaux<=0){
            throw new ValeurInvalideException("Surface");
        }
        this.dureeTravaux = dureeTravaux;
    }
}
