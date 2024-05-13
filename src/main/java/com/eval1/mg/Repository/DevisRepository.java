package com.eval1.mg.Repository;


import com.eval1.mg.Model.Devis;
import com.eval1.mg.Model.TypeMaison;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface DevisRepository extends CrudRepository<Devis, String> , JpaRepository<Devis, String> {


        public Devis findDevisByTypeMaison(TypeMaison typeMaison);

        


		/*Page<Devis> findMatiereByEtatAndLibelleContainingIgnoreCase(int etat,String key, Pageable pageable);
		List<Devis> findMatiereByEtat(int etat);
		Page<Devis> findMatiereByEtat(int etat,Pageable p);
		@Query("SELECT t FROM Devis t WHERE DATE(t.date) = DATE(?1) and t.etat= ?2 ")
		Page<Devis> getByDateAsc(Date key,int etat, Pageable pageable);*/





}
