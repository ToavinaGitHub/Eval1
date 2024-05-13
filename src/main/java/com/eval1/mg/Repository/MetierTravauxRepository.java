package com.eval1.mg.Repository;


import com.eval1.mg.Model.MetierTravaux;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface MetierTravauxRepository extends CrudRepository<MetierTravaux, Integer> , JpaRepository<MetierTravaux, Integer> {



		/*Page<Metier_travaux> findMatiereByEtatAndLibelleContainingIgnoreCase(int etat,String key, Pageable pageable);
		List<Metier_travaux> findMatiereByEtat(int etat);
		Page<Metier_travaux> findMatiereByEtat(int etat,Pageable p);
		@Query("SELECT t FROM Metier_travaux t WHERE DATE(t.date) = DATE(?1) and t.etat= ?2 ")
		Page<Metier_travaux> getByDateAsc(Date key,int etat, Pageable pageable);*/





}
