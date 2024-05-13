package com.eval1.mg.Repository;


import com.eval1.mg.Model.Construction;
import com.eval1.mg.Model.Utilisateur;
import com.eval1.mg.View.V_construction_complet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;


public interface ConstructionRepository extends CrudRepository<Construction, String> , JpaRepository<Construction, String> {


    @Query("SELECT t FROM Construction t WHERE DATE(t.daty) = DATE(?1) and t.etat>=0 ")
    Page<Construction> getByDateAsc(Date key, Pageable pageable);

    @Query(value = "SELECT idConstruction,daty,debut,demande,etat,fin,total,dejaPaye,idDevis,idTypeFinition,pourcentageFinition,reste,idUtilisateur,designationMaison,designationFinition,contactUser,duree from v_construction_complet where daty=?1 and etat>=0",nativeQuery = true)
    Page<V_construction_complet> getConstructionComplet(Date key,Pageable pageable);

    @Query("SELECT t FROM Construction t WHERE DATE(t.daty) = DATE(?1) and t.etat >=0 and t.utilisateur.id= ?2")
    Page<Construction> getConstructionByDatyAndEtatAndUtilisateur(Date key,  String idUser,Pageable pageable);


    @Query(value = "SELECT idConstruction,daty,debut,demande,etat,fin,total,dejaPaye,idDevis,idTypeFinition,pourcentageFinition,reste,idUtilisateur,designationMaison,designationFinition,contactUser,duree from v_construction_complet where daty=?1 and etat>=0 and idutilisateur=?2",nativeQuery = true)
    Page<V_construction_complet> getConstructionCompletDatyEtatUtilisateur(Date key,  String idUser,Pageable pageable);

    @Query(value = "SELECT idConstruction,daty,debut,demande,etat,fin,total,dejaPaye,idDevis,idTypeFinition,pourcentageFinition,reste,idUtilisateur,designationMaison,designationFinition,contactUser,duree from v_construction_complet where daty=?1 and etat>=0",nativeQuery = true)
    Page<V_construction_complet> getConstructionCompletDatyEtat(Date key,Pageable pageable);



    List<Construction> findConstructionByEtatGreaterThan(int etat);

    List<Construction> findConstructionByUtilisateurAndEtatGreaterThan( Utilisateur utilisateur,int etat);


    Page<Construction> findConstructionByEtatGreaterThan(int etat,Pageable p);

    Page<Construction> findConstructionByUtilisateurAndEtatGreaterThan(Utilisateur utilisateur,int etat,Pageable p);


    @Query(value = "SELECT idConstruction,daty,debut,demande,etat,fin,total,dejaPaye,idDevis,idTypeFinition,pourcentageFinition,reste,idUtilisateur,designationMaison,designationFinition,contactUser,duree from v_construction_complet where daty=?1 and etat>=?2 and idutilisateur=?1",nativeQuery = true)
    List<V_construction_complet> getConstructionCompletEtatUtilisateur(String idUser,int etat);

    @Query(value = "SELECT idConstruction,daty,debut,demande,etat,fin,total,dejaPaye,idDevis,idTypeFinition,pourcentageFinition,reste,idUtilisateur,designationMaison,designationFinition,contactUser,duree from v_construction_complet where etat>=?2 and idutilisateur=?1",nativeQuery = true)
    Page<V_construction_complet> getConstructionCompletEtatUtilisateur(String idUser,int etat,Pageable pageable);

    @Query(value = "SELECT idConstruction,daty,debut,demande,etat,fin,total,dejaPaye,idDevis,idTypeFinition,pourcentageFinition,reste,idUtilisateur,designationMaison,designationFinition,contactUser,duree from v_construction_complet where etat>=?1",nativeQuery = true)
    Page<V_construction_complet> getConstructionCompletEtat(int etat,Pageable pageable);



    @Query(value = "SELECT idConstruction,daty,debut,demande,etat,fin,total,dejaPaye,idDevis,idTypeFinition,pourcentageFinition,reste,idUtilisateur,designationMaison,designationFinition,contactUser,duree from v_construction_complet",nativeQuery = true)
    List<V_construction_complet> getConstructionComplet();


    @Query(value = "SELECT sum(montant_total) as Somme FROM construction",nativeQuery = true)
    public Integer getSommeDevis();



		/*
		@Query("SELECT t FROM Construction t WHERE DATE(t.date) = DATE(?1) and t.etat= ?2 ")
		Page<Construction> getByDateAsc(Date key,int etat, Pageable pageable);*/





}
