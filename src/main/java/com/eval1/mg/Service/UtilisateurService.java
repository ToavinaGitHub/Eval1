package com.eval1.mg.Service;

import com.eval1.mg.Model.Utilisateur;
import com.eval1.mg.Repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurService
{

    @Autowired
    UtilisateurRepository utilisateurRepository;

    public Utilisateur getByContact(String contact){
        return utilisateurRepository.findUtilisateurByContact(contact);
    }
}
