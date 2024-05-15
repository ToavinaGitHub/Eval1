package com.eval1.mg.Repository;

import com.eval1.mg.Model.Utilisateur;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UtilisateurRepository  extends CrudRepository<Utilisateur, String>, JpaRepository<Utilisateur, String> {


    public Utilisateur findUtilisateurByEmailAndPassword(String email, String password);

    public Utilisateur findUtilisateurByEmail(String email);

    public Utilisateur findUtilisateurByContact(String contact);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM utilisateur WHERE profil=?1",nativeQuery = true)
    public void deleteUtilisateurByProfil(String profil);

}
