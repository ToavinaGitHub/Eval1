package com.eval1.mg.Repository;


import com.eval1.mg.Model.Metier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface MetierRepository extends CrudRepository<Metier, Integer> , JpaRepository<Metier, Integer> {

    Page<Metier> findMetierByEtatAndDesignationContainingIgnoreCase(int etat, String key, Pageable pageable);
    List<Metier> findMetierByEtat(int etat);
    Page<Metier> findMetierByEtat(int etat, Pageable p);

		/*
		@Query("SELECT t FROM Metier t WHERE DATE(t.date) = DATE(?1) and t.etat= ?2 ")
		Page<Metier> getByDateAsc(Date key,int etat, Pageable pageable);*/





}
