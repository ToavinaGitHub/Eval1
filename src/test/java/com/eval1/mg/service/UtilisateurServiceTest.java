package com.eval1.mg.service;


import com.eval1.mg.Model.Profil;
import com.eval1.mg.Model.Utilisateur;
import com.eval1.mg.Repository.UtilisateurRepository;
import com.eval1.mg.Service.UtilisateurService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UtilisateurServiceTest {

    @Autowired
    private UtilisateurService userService;

    @MockBean
    private UtilisateurRepository userRepository;

    @Test
    public void testFindUserByContact() {
        Utilisateur mockUser = new Utilisateur();
        mockUser.setContact("0380982607");
        mockUser.setProfil(Profil.CLIENT);
        mockUser.setGenre(1);

        Mockito.when(userRepository.findUtilisateurByContact(mockUser.getContact())).thenReturn((mockUser));

        Utilisateur user = userService.getByContact(mockUser.getContact());

        assertNotNull(user);
        assertEquals(1, user.getGenre());
    }
}
