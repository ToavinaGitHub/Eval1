package com.eval1.mg.Repository;

import com.eval1.mg.Model.Construction;
import com.eval1.mg.Model.TravauxConstruction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TravauxConstructionRepository extends JpaRepository<TravauxConstruction,Integer> {

    TravauxConstruction findTravauxConstructionByConstructionAndNumero(Construction construction,String numero);

    List<TravauxConstruction> findTravauxConstructionByConstruction(Construction construction);

    @Query(value = "SELECT c from TravauxConstruction c WHERE c.construction.id=?1 AND c.numero=?2")
    TravauxConstruction getTravauxConstructionByConstructionAndNumero(String idConstruction,String numero);
}
