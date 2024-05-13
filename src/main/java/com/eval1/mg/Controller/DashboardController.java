package com.eval1.mg.Controller;

import com.eval1.mg.Repository.ConstructionRepository;
import com.eval1.mg.View.V_devis_mois_annee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/v1/dashboard")
public class DashboardController {

    @Autowired
    ConstructionRepository constructionRepository;

    @GetMapping("")
    String toDashboard(@RequestParam(name = "anne" ,defaultValue = "2024")String anne, Model model){

        double a = constructionRepository.getSommeDevis();

        model.addAttribute("total",a);

        List<V_devis_mois_annee> stats = constructionRepository.getDevisMoisAnnee(Integer.parseInt(anne));

        String[] allMois = new String[12];
        double[] montant = new double[12];
        int i=0;

        for (V_devis_mois_annee mois :stats){
            allMois[i] = mois.getMois();
            montant[i] = mois.getMontant();
            i+=1;
        }

        model.addAttribute("annee",anne);
        model.addAttribute("allMois" ,allMois);
        model.addAttribute("allMontant" ,montant);

        return "Admin/Dashboard";
    }
}
