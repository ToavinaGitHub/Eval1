package com.eval1.mg.Repository;


import com.eval1.mg.Model.TypeTravail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface TypeTravailRepository extends CrudRepository<TypeTravail, Integer> , JpaRepository<TypeTravail, Integer> {

    Page<TypeTravail> findTypeTravailByEtatAndDesignationContainingIgnoreCase(int etat, String key, Pageable pageable);
    List<TypeTravail> findTypeTravailByEtat(int etat);
    Page<TypeTravail> findTypeTravailByEtat(int etat,Pageable p);


		/*
		@Query("SELECT t FROM Type_travail t WHERE DATE(t.date) = DATE(?1) and t.etat= ?2 ")
		Page<Type_travail> getByDateAsc(Date key,int etat, Pageable pageable);*/





}
