package com.eval1.mg.Repository;


import com.eval1.mg.Model.EtatTravaux;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface EtatTravauxRepository extends CrudRepository<EtatTravaux, Integer> , JpaRepository<EtatTravaux, Integer> {



		/*Page<Etat_travaux> findMatiereByEtatAndLibelleContainingIgnoreCase(int etat,String key, Pageable pageable);
		List<Etat_travaux> findMatiereByEtat(int etat);
		Page<Etat_travaux> findMatiereByEtat(int etat,Pageable p);
		@Query("SELECT t FROM Etat_travaux t WHERE DATE(t.date) = DATE(?1) and t.etat= ?2 ")
		Page<Etat_travaux> getByDateAsc(Date key,int etat, Pageable pageable);*/





}
