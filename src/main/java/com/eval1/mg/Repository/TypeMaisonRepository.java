package com.eval1.mg.Repository;


import com.eval1.mg.Model.TypeMaison;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface TypeMaisonRepository extends CrudRepository<TypeMaison, String> , JpaRepository<TypeMaison, String> {


        List<TypeMaison> findTypeMaisonByEtat(int etat);

        Page<TypeMaison> findTypeMaisonByEtatAndDesignationContainingIgnoreCase(int etat, String key, Pageable pageable);
        Page<TypeMaison> findTypeMaisonByEtat(int etat, Pageable p);

		/*
		@Query("SELECT t FROM Type_maison t WHERE DATE(t.date) = DATE(?1) and t.etat= ?2 ")
		Page<Type_maison> getByDateAsc(Date key,int etat, Pageable pageable);*/





}
