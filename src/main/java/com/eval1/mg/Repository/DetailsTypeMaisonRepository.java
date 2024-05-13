package com.eval1.mg.Repository;


import com.eval1.mg.Model.DetailsTypeMaison;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface DetailsTypeMaisonRepository extends CrudRepository<DetailsTypeMaison, String> , JpaRepository<DetailsTypeMaison, String> {



		/*Page<Details_type_maison> findMatiereByEtatAndLibelleContainingIgnoreCase(int etat,String key, Pageable pageable);
		List<Details_type_maison> findMatiereByEtat(int etat);
		Page<Details_type_maison> findMatiereByEtat(int etat,Pageable p);
		@Query("SELECT t FROM Details_type_maison t WHERE DATE(t.date) = DATE(?1) and t.etat= ?2 ")
		Page<Details_type_maison> getByDateAsc(Date key,int etat, Pageable pageable);*/





}
