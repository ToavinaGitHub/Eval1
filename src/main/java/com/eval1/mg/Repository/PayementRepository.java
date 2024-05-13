package com.eval1.mg.Repository;


import com.eval1.mg.Model.Payement;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface PayementRepository extends CrudRepository<Payement, String> , JpaRepository<Payement, String> {



		/*Page<Payement> findMatiereByEtatAndLibelleContainingIgnoreCase(int etat,String key, Pageable pageable);
		List<Payement> findMatiereByEtat(int etat);
		Page<Payement> findMatiereByEtat(int etat,Pageable p);
		@Query("SELECT t FROM Payement t WHERE DATE(t.date) = DATE(?1) and t.etat= ?2 ")
		Page<Payement> getByDateAsc(Date key,int etat, Pageable pageable);*/





}
