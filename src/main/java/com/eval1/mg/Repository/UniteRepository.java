package com.eval1.mg.Repository;


import com.eval1.mg.Model.Unite;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface UniteRepository extends CrudRepository<Unite, Integer> , JpaRepository<Unite, Integer> {



		/*Page<Unite> findMatiereByEtatAndLibelleContainingIgnoreCase(int etat,String key, Pageable pageable);
		List<Unite> findMatiereByEtat(int etat);
		Page<Unite> findMatiereByEtat(int etat,Pageable p);
		@Query("SELECT t FROM Unite t WHERE DATE(t.date) = DATE(?1) and t.etat= ?2 ")
		Page<Unite> getByDateAsc(Date key,int etat, Pageable pageable);*/





}
