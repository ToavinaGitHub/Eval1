package com.eval1.mg.Repository;


import com.eval1.mg.Model.Travaux;
import com.eval1.mg.Model.Devis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface TravauxRepository extends CrudRepository<Travaux, Integer> , JpaRepository<Travaux, Integer> {



        public List<Travaux> findTravauxByDevis(Devis devis);

        Page<Travaux> findTravauxByEtatAndDesignationContainingIgnoreCase(int etat, String key, Pageable pageable);
        List<Travaux> findTravauxByEtat(int etat);
        Page<Travaux> findTravauxByEtat(int etat, Pageable p);


		/*Page<Travaux> findMatiereByEtatAndLibelleContainingIgnoreCase(int etat,String key, Pageable pageable);
		List<Travaux> findMatiereByEtat(int etat);
		Page<Travaux> findMatiereByEtat(int etat,Pageable p);
		@Query("SELECT t FROM Travaux t WHERE DATE(t.date) = DATE(?1) and t.etat= ?2 ")
		Page<Travaux> getByDateAsc(Date key,int etat, Pageable pageable);*/





}
