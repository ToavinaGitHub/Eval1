package com.eval1.mg.Service;

import com.eval1.mg.Model.TravauxConstruction;
import com.eval1.mg.Model.TypeTravail;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ConstructionService {





    public HashMap<TypeTravail, List<TravauxConstruction>> getTravauxParTravail(List<TravauxConstruction> all){

        HashMap<TypeTravail,List<TravauxConstruction>> res = new HashMap<>();

        for (TravauxConstruction t : all) {

            // Si le TypeTravail de t existe déjà dans la clé de la HashMap
            if (res.containsKey(t.getTypeTravail())) {
                // Ajoutez t à la liste existante correspondant à ce TypeTravail
                res.get(t.getTypeTravail()).add(t);
            } else {
                // Sinon, créez une nouvelle clé pour ce TypeTravail et une nouvelle liste pour y stocker t
                List<TravauxConstruction> newList = new ArrayList<>();
                newList.add(t);
                res.put(t.getTypeTravail(), newList);
            }
        }
        return res;
    }
}
