package com.eval1.mg.Model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import jakarta.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "utilisateur")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    String id;

    @Nullable
    String nom;

    @Nullable
    String prenom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Nullable
    @Past(message = "La date de naissance doit être antérieure à la date actuelle")
    Date dateNaissance;

    @Nullable
    int genre;

    @NotBlank(message = "Contact utilisateur ne doit pas etre vide")
    String contact;

    @Enumerated(EnumType.STRING)
    Profil profil;

    @Email(message = "L'email doit être valide")
    @Nullable
    String email;

    @Nullable
    String password;


}