package com.eval1.mg.Repository;

import com.eval1.mg.Model.Utilisateur;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UtilisateurRepository  extends CrudRepository<Utilisateur, String>, JpaRepository<Utilisateur, String> {


    public Utilisateur findUtilisateurByEmailAndPassword(String email, String password);

    public Utilisateur findUtilisateurByEmail(String email);

    public Utilisateur findUtilisateurByContact(String contact);

}
