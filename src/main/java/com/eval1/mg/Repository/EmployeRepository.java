package com.eval1.mg.Repository;


import com.eval1.mg.Model.Employe;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface EmployeRepository extends CrudRepository<Employe, String> , JpaRepository<Employe, String> {



		/*Page<Employe> findMatiereByEtatAndLibelleContainingIgnoreCase(int etat,String key, Pageable pageable);
		List<Employe> findMatiereByEtat(int etat);
		Page<Employe> findMatiereByEtat(int etat,Pageable p);
		@Query("SELECT t FROM Employe t WHERE DATE(t.date) = DATE(?1) and t.etat= ?2 ")
		Page<Employe> getByDateAsc(Date key,int etat, Pageable pageable);*/





}
