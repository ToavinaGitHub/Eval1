package com.eval1.mg.Controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eval1.mg.Model.Metier;
import com.eval1.mg.Repository. MetierRepository;

import org.springframework.ui.Model;

@RequestMapping("/v1/metier")
@Controller
public class MetierController {

    @Autowired
    private MetierRepository  metierRepository;

    @GetMapping("")
    public String index(Model model , @RequestParam(name = "keyword" ,required = false ,defaultValue="") String key,
      @RequestParam(defaultValue = "1" , required = false ,name = "page") int page, @RequestParam(defaultValue = "3" , required = false ,name = "size") int size,
      @RequestParam(defaultValue = "id", required = false, name = "sortField") String sortField,@RequestParam(defaultValue = "asc", required = false, name = "sortOrder") String sortOrder)  {
       
        List<Metier> metiers = new ArrayList<Metier>();

        Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Sort sort = Sort.by(direction, sortField);

        Pageable pageable =PageRequest.of(page-1,size,sort);

        Page<Metier> pageCateg;
        if(key.compareTo("")==0){
            pageCateg = metierRepository.findMetierByEtat(1,pageable);
        }else{
            /*  
             SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
             Date d = s.parse(key);
            */
            pageCateg =  metierRepository.findMetierByEtatAndDesignationContainingIgnoreCase(1,key,pageable);
        }

        Metier c = new Metier();

        metiers = pageCateg.getContent();

        model.addAttribute("keyword" , key);
        model.addAttribute("metier" , c);
        model.addAttribute("metiers", metiers);

        model.addAttribute("currentPage", pageCateg.getNumber() + 1);
        model.addAttribute("totalItems", pageCateg.getTotalElements());
        model.addAttribute("totalPages", pageCateg.getTotalPages());
        model.addAttribute("pageSize", size);

        return "Metier/index";
    }

    @PostMapping("")
    public String save(@Valid Metier metier , BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()){
            String message = "";
            for (int i = 0; i < bindingResult.getAllErrors().size(); i++) {
                message += bindingResult.getAllErrors().get(i).getDefaultMessage()+";";
            }
            redirectAttributes.addFlashAttribute("error", message);
            return "redirect:/v1/metier";
        }
        try{
            metier.setEtat(1);
            metierRepository.save(metier);
            redirectAttributes.addFlashAttribute("success", "Metier ajoutée avec succès");
            redirectAttributes.addFlashAttribute("message" , "Insertion avec succes");
        }catch(Exception e){
            redirectAttributes.addFlashAttribute("error", "Transaction echouée");
        }

        return "redirect:/v1/metier";
    }
    @GetMapping("/{id}")
    public String editTutorial(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Metier t = metierRepository.findById(id).get();

            model.addAttribute("metier", t);
            model.addAttribute("pageTitle", "Edit Metier (ID: " + id + ")");

            //List<Metier> metiers = metierRepository.findAll();

            List<Metier> metiers = metierRepository.findMetierByEtat(1);
            
            model.addAttribute("isUpdate" , 1);
            model.addAttribute("metiers", metiers);
            return "Metier/index";
        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("error", "Transaction echouée");
            return "redirect:/v1/metier";
        }
    }

    @GetMapping("/delOption")
    public String delete(@RequestParam Integer id , RedirectAttributes redirectAttributes){
        Metier ct =  metierRepository.findById(id).get();
        //metierRepository.delete(ct);
        ct.setEtat(0);
        metierRepository.save(ct);

        redirectAttributes.addFlashAttribute("message" , "Supprimé avec succés");
        return "redirect:/v1/metier";
    }

    @PostMapping("/edit")
    public String modifier(@Valid @ModelAttribute Metier metier,BindingResult bindingResult,RedirectAttributes redirectAttributes){
        String message = "";
        if(bindingResult.hasErrors()){
            for (int i = 0; i < bindingResult.getAllErrors().size(); i++) {
                message += bindingResult.getAllErrors().get(i).getDefaultMessage()+";";
            }
            redirectAttributes.addFlashAttribute("error", message);
    
            return "redirect:/v1/metier";
        }
        try{
             metierRepository.save(metier);
        }catch (Exception e){
            message += e.getMessage()+" ; ";
            redirectAttributes.addFlashAttribute("error", message);
            return "redirect:/v1/metier";
        }

        redirectAttributes.addFlashAttribute("message" , "Modification avec succes");
        return "redirect:/v1/metier";
    }

}
