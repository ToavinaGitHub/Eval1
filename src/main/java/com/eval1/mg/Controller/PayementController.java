package com.eval1.mg.Controller;


import com.eval1.mg.Model.Construction;
import com.eval1.mg.Model.Payement;
import com.eval1.mg.Repository.ConstructionRepository;
import com.eval1.mg.Repository.PayementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
    public String addPayement(@RequestParam(name = "idConstruction")String idConstruction, @RequestParam(name = "daty")List<String> allDaty, RedirectAttributes redirectAttributes, @RequestParam(name = "montant") List<String> allMontant) throws ParseException {

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



}
