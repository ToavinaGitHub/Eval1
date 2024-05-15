package com.eval1.mg.Controller;


import com.eval1.mg.Model.Construction;
import com.eval1.mg.Model.Payement;
import com.eval1.mg.Repository.ConstructionRepository;
import com.eval1.mg.Repository.PayementRepository;
import com.eval1.mg.View.V_construction_complet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/v1/payement")
public class PayementController {

    @Autowired
    PayementRepository payementRepository;

    @Autowired
    ConstructionRepository constructionRepository;

    @PostMapping("")
    public String toPayement(@RequestParam(name = "idContruction") String id,Model model){

        Construction c = constructionRepository.getById(id);

        model.addAttribute("construction" , c);

        return "Client/Payement";
    }

    @PostMapping("/addPayement")
    public String addPayement(@RequestParam(name = "idConstruction")String idConstruction, @RequestParam(name = "daty")List<String> allDaty, @RequestParam(name = "payement")List<String> allPayement,RedirectAttributes redirectAttributes, @RequestParam(name = "montant") List<String> allMontant) throws ParseException {

        Construction c = constructionRepository.getById(idConstruction);

        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        List<Payement> allP = new ArrayList<>();
        for(int i=0;i<allDaty.size();i++){
            try{
                Date d = s.parse(allDaty.get(i));
                double mt = Double.parseDouble(allMontant.get(i));
                Payement p = new Payement();
                p.setConstruction(c);
                p.setDaty(d);
                p.setEtat(1);
                p.setRefPayement(allPayement.get(i));
                p.setMontant(mt);
                allP.add(p);
            }catch (Exception e){
                redirectAttributes.addFlashAttribute("error" , e.getMessage());
                return "redirect:/v1/construction/user";
            }
        }
        payementRepository.saveAll(allP);
        redirectAttributes.addFlashAttribute("message" ,"Payement effectué avec succes");
        return "redirect:/v1/construction/user";
    }


    @PostMapping("/addPayementAjax")
    @ResponseBody
    public ResponseEntity<Map<String, String>> addPayment(@RequestParam(name = "idConstruction")String idConstruction, @RequestParam(name = "daty")List<String> allDaty, @RequestParam(name = "payement")List<String> allPayement,@RequestParam(name = "montant") List<String> allMontant){
        Map<String, String> responseData = new HashMap<>();

        String messError = "";
        try{
            Construction c = constructionRepository.getById(idConstruction);

            V_construction_complet complet = constructionRepository.getConstructionByIdConstruction(c.getId());

            double sommeApaye = 0;
            for (String m:allMontant
            ) {
                sommeApaye+=Double.parseDouble(m);
            }

            if(complet.getReste()<sommeApaye){
                responseData.put("error", "Montant mihoatra "+ (sommeApaye-complet.getReste()));
                return ResponseEntity.ok(responseData);
            }else{
                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
                List<Payement> allP = new ArrayList<>();
                for(int i=0;i<allDaty.size();i++){
                    try{
                        Date d = s.parse(allDaty.get(i));
                        double mt = Double.parseDouble(allMontant.get(i));
                        Payement p = new Payement();
                        p.setConstruction(c);
                        p.setDaty(d);
                        p.setEtat(1);
                        p.setRefPayement(allPayement.get(i));
                        p.setMontant(mt);
                        allP.add(p);
                    }catch (Exception e){
                        messError+=e.getMessage()+"--";
                    }
                }
                if(messError.compareTo("")!=0){
                    responseData.put("error",messError);
                }else{
                    payementRepository.saveAll(allP);
                    responseData.put("message","Insertion des payement avec succes");
                }
            }
        }catch (Exception e){
            responseData.put("error" , e.getMessage());
            return ResponseEntity.ok(responseData);
        }

        return ResponseEntity.ok(responseData);

    }




}
