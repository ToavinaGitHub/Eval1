package com.eval1.mg.Repository;


import com.eval1.mg.Model.EtatConstruction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface EtatConstructionRepository extends CrudRepository<EtatConstruction, Integer> , JpaRepository<EtatConstruction, Integer> {



		/*Page<Etat_construction> findMatiereByEtatAndLibelleContainingIgnoreCase(int etat,String key, Pageable pageable);
		List<Etat_construction> findMatiereByEtat(int etat);
		Page<Etat_construction> findMatiereByEtat(int etat,Pageable p);
		@Query("SELECT t FROM Etat_construction t WHERE DATE(t.date) = DATE(?1) and t.etat= ?2 ")
		Page<Etat_construction> getByDateAsc(Date key,int etat, Pageable pageable);*/





}
