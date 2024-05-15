package com.eval1.mg.Controller;

import com.eval1.mg.Model.Unite;
import com.eval1.mg.Repository.UniteRepository;
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

import com.eval1.mg.Model.Travaux;
import com.eval1.mg.Repository. TravauxRepository;

import org.springframework.ui.Model;

@RequestMapping("/v1/travaux")
@Controller
public class TravauxController {

    @Autowired
    private TravauxRepository  travauxRepository;

    @Autowired
    private UniteRepository uniteRepository;

    @GetMapping("")
    public String index(Model model , @RequestParam(name = "keyword" ,required = false ,defaultValue="") String key,
      @RequestParam(defaultValue = "1" , required = false ,name = "page") int page, @RequestParam(defaultValue = "3" , required = false ,name = "size") int size,
      @RequestParam(defaultValue = "id", required = false, name = "sortField") String sortField,@RequestParam(defaultValue = "asc", required = false, name = "sortOrder") String sortOrder)  {
       
        List<Travaux> travauxs = new ArrayList<Travaux>();

        Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Sort sort = Sort.by(direction, sortField);

        Pageable pageable =PageRequest.of(page-1,size,sort);

        Page<Travaux> pageCateg;
        if(key.compareTo("")==0){
            pageCateg = travauxRepository.findTravauxByEtat(1,pageable);
        }else{
            /*  
             SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
             Date d = s.parse(key);
            */
            pageCateg =  travauxRepository.findTravauxByEtatAndDesignationContainingIgnoreCase(1,key,pageable);
        }

        Travaux c = new Travaux();

        travauxs = pageCateg.getContent();

        List<Unite> allUnite = uniteRepository.findAll();

        model.addAttribute("allUnite",allUnite);
        model.addAttribute("keyword" , key);
        model.addAttribute("travaux" , c);
        model.addAttribute("travauxs", travauxs);

        model.addAttribute("currentPage", pageCateg.getNumber() + 1);
        model.addAttribute("totalItems", pageCateg.getTotalElements());
        model.addAttribute("totalPages", pageCateg.getTotalPages());
        model.addAttribute("pageSize", size);

        return "Travaux/index";
    }

    @PostMapping("")
    public String save(@Valid Travaux travaux , BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()){
            String message = "";
            for (int i = 0; i < bindingResult.getAllErrors().size(); i++) {
                message += bindingResult.getAllErrors().get(i).getDefaultMessage()+";";
            }
            redirectAttributes.addFlashAttribute("error", message);
            return "redirect:/v1/travaux";
        }
        try{
            travaux.setEtat(1);
            travauxRepository.save(travaux);
            redirectAttributes.addFlashAttribute("success", "Travaux ajoutée avec succès");
            redirectAttributes.addFlashAttribute("message" , "Insertion avec succes");
        }catch(Exception e){
            redirectAttributes.addFlashAttribute("error", "Transaction echouée");
        }

        return "redirect:/v1/travaux";
    }
    @GetMapping("/{id}")
    public String editTutorial(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Travaux t = travauxRepository.findById(id).get();

            model.addAttribute("travaux", t);
            model.addAttribute("pageTitle", "Edit Travaux (ID: " + id + ")");

            //List<Travaux> travauxs = travauxRepository.findAll();

            List<Travaux> travauxs = travauxRepository.findTravauxByEtat(1);

            List<Unite> allUnite = uniteRepository.findAll();

            model.addAttribute("allUnite",allUnite);

            model.addAttribute("isUpdate" , 1);
            model.addAttribute("travauxs", travauxs);
            return "Travaux/index";
        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("error", "Transaction echouée");
            return "redirect:/v1/travaux";
        }
    }

    @GetMapping("/delOption")
    public String delete(@RequestParam Integer id , RedirectAttributes redirectAttributes){
        Travaux ct =  travauxRepository.findById(id).get();
        //travauxRepository.delete(ct);
        ct.setEtat(0);
        travauxRepository.save(ct);

        redirectAttributes.addFlashAttribute("message" , "Supprimé avec succés");
        return "redirect:/v1/travaux";
    }

    @PostMapping("/edit")
    public String modifier(@Valid @ModelAttribute Travaux travaux,BindingResult bindingResult,RedirectAttributes redirectAttributes){
        String message = "";
        if(bindingResult.hasErrors()){
            for (int i = 0; i < bindingResult.getAllErrors().size(); i++) {
                message += bindingResult.getAllErrors().get(i).getDefaultMessage()+";";
            }
            redirectAttributes.addFlashAttribute("error", message);
    
            return "redirect:/v1/travaux";
        }
        try{
             travauxRepository.save(travaux);
        }catch (Exception e){
            message += e.getMessage()+" ; ";
            redirectAttributes.addFlashAttribute("error", message);
            return "redirect:/v1/travaux";
        }

        redirectAttributes.addFlashAttribute("message" , "Modification avec succes");
        return "redirect:/v1/travaux";
    }

}
