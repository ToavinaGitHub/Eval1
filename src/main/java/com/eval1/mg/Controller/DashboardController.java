package com.eval1.mg.Controller;

import com.eval1.mg.Repository.ConstructionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/v1/dashboard")
public class DashboardController {

    @Autowired
    ConstructionRepository constructionRepository;

    @GetMapping("")
    String toDashboard(Model model){

        int a = constructionRepository.getSommeDevis();

        model.addAttribute("total",a);

        System.out.println(a+"hahaha");

        return "Admin/Dashboard";
    }
}
