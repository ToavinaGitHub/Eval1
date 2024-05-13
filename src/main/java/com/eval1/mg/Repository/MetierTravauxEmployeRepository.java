package com.eval1.mg.Repository;


import com.eval1.mg.Model.MetierTravauxEmploye;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface MetierTravauxEmployeRepository extends CrudRepository<MetierTravauxEmploye, Integer> , JpaRepository<MetierTravauxEmploye, Integer> {



		/*Page<Metier_travaux_employe> findMatiereByEtatAndLibelleContainingIgnoreCase(int etat,String key, Pageable pageable);
		List<Metier_travaux_employe> findMatiereByEtat(int etat);
		Page<Metier_travaux_employe> findMatiereByEtat(int etat,Pageable p);
		@Query("SELECT t FROM Metier_travaux_employe t WHERE DATE(t.date) = DATE(?1) and t.etat= ?2 ")
		Page<Metier_travaux_employe> getByDateAsc(Date key,int etat, Pageable pageable);*/





}
