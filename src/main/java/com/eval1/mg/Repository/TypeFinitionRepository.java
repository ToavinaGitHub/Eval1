package com.eval1.mg.Repository;


import com.eval1.mg.Model.TypeFinition;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface TypeFinitionRepository extends CrudRepository<TypeFinition, String> , JpaRepository<TypeFinition, String> {


        List<TypeFinition> findTypeFinitionByEtat(int etat);

		/*Page<Type_finition> findMatiereByEtatAndLibelleContainingIgnoreCase(int etat,String key, Pageable pageable);
		List<Type_finition> findMatiereByEtat(int etat);
		Page<Type_finition> findMatiereByEtat(int etat,Pageable p);
		@Query("SELECT t FROM Type_finition t WHERE DATE(t.date) = DATE(?1) and t.etat= ?2 ")
		Page<Type_finition> getByDateAsc(Date key,int etat, Pageable pageable);*/





}
