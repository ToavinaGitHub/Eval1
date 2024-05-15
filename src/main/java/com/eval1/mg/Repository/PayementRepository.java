package com.eval1.mg.Repository;


import com.eval1.mg.Model.Construction;
import com.eval1.mg.Model.Payement;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface PayementRepository extends CrudRepository<Payement, String> , JpaRepository<Payement, String> {



    public List<Payement> findPayementByConstruction(Construction construction);


    @Query(value = "SELECT coalesce(sum(montant),0) from payement where id_construction=?1",nativeQuery = true)
    public double getSommePayeParConstruction(String idConstruction);

		/*Page<Payement> findMatiereByEtatAndLibelleContainingIgnoreCase(int etat,String key, Pageable pageable);
		List<Payement> findMatiereByEtat(int etat);
		Page<Payement> findMatiereByEtat(int etat,Pageable p);
		@Query("SELECT t FROM Payement t WHERE DATE(t.date) = DATE(?1) and t.etat= ?2 ")
		Page<Payement> getByDateAsc(Date key,int etat, Pageable pageable);*/





}
