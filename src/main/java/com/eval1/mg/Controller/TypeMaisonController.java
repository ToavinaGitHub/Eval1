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

import com.eval1.mg.Model.TypeMaison;
import com.eval1.mg.Repository. TypeMaisonRepository;

import org.springframework.ui.Model;

@RequestMapping("/v1/typeMaison")
@Controller
public class TypeMaisonController {

    @Autowired
    private TypeMaisonRepository  typeMaisonRepository;

    @GetMapping("")
    public String index(Model model , @RequestParam(name = "keyword" ,required = false ,defaultValue="") String key,
      @RequestParam(defaultValue = "1" , required = false ,name = "page") int page, @RequestParam(defaultValue = "5" , required = false ,name = "size") int size,
      @RequestParam(defaultValue = "idTypeMaison", required = false, name = "sortField") String sortField,@RequestParam(defaultValue = "asc", required = false, name = "sortOrder") String sortOrder)  {

        List<TypeMaison> typeMaisons = new ArrayList<TypeMaison>();

        Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Sort sort = Sort.by(direction, sortField);

        Pageable pageable =PageRequest.of(page-1,size,sort);

        Page<TypeMaison> pageCateg;
        if(key.compareTo("")==0){
            pageCateg = typeMaisonRepository.findTypeMaisonByEtat(1,pageable);
        }else{
            /*  
             SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
             Date d = s.parse(key);
            */
            pageCateg =  typeMaisonRepository.findTypeMaisonByEtatAndDesignationContainingIgnoreCase(1,key,pageable);
        }

        TypeMaison c = new TypeMaison();

        typeMaisons = pageCateg.getContent();

        model.addAttribute("keyword" , key);
        model.addAttribute("typeMaison" , c);
        model.addAttribute("typeMaisons", typeMaisons);

        model.addAttribute("currentPage", pageCateg.getNumber() + 1);
        model.addAttribute("totalItems", pageCateg.getTotalElements());
        model.addAttribute("totalPages", pageCateg.getTotalPages());
        model.addAttribute("pageSize", size);

        return "TypeMaison/index";
    }

    @PostMapping("")
    public String save(@Valid TypeMaison typeMaison , BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()){
            String message = "";
            for (int i = 0; i < bindingResult.getAllErrors().size(); i++) {
                message += bindingResult.getAllErrors().get(i).getDefaultMessage()+";";
            }
            redirectAttributes.addFlashAttribute("error", message);
            return "redirect:/v1/typeMaison";
        }
        try{
            typeMaison.setEtat(1);
            typeMaisonRepository.save(typeMaison);
            redirectAttributes.addFlashAttribute("success", "TypeMaison ajoutée avec succès");
            redirectAttributes.addFlashAttribute("message" , "Insertion avec succes");
        }catch(Exception e){
            redirectAttributes.addFlashAttribute("error", "Transaction echouée");
        }

        return "redirect:/v1/typeMaison";
    }
    @GetMapping("/{id}")
    public String editTutorial(@PathVariable("id") String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            TypeMaison t = typeMaisonRepository.findById(id).get();

            model.addAttribute("typeMaison", t);
            model.addAttribute("pageTitle", "Edit TypeMaison (ID: " + id + ")");

            //List<TypeMaison> typeMaisons = typeMaisonRepository.findAll();

            List<TypeMaison> typeMaisons = typeMaisonRepository.findTypeMaisonByEtat(1);
            
            model.addAttribute("isUpdate" , 1);
            model.addAttribute("price",t.getPrix());
            model.addAttribute("typeMaisons", typeMaisons);
            return "TypeMaison/index";
        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("error", "Transaction echouée");
            return "redirect:/v1/typeMaison";
        }
    }

    @GetMapping("/delOption")
    public String delete(@RequestParam String id , RedirectAttributes redirectAttributes){
        TypeMaison ct =  typeMaisonRepository.findById(id).get();
        //typeMaisonRepository.delete(ct);
        ct.setEtat(0);
        typeMaisonRepository.save(ct);

        redirectAttributes.addFlashAttribute("message" , "Supprimé avec succés");
        return "redirect:/v1/typeMaison";
    }

    @PostMapping("/edit")
    public String modifier(@Valid @ModelAttribute TypeMaison typeMaison,BindingResult bindingResult,RedirectAttributes redirectAttributes){
        String message = "";
        if(bindingResult.hasErrors()){
            for (int i = 0; i < bindingResult.getAllErrors().size(); i++) {
                message += bindingResult.getAllErrors().get(i).getDefaultMessage()+";";
            }
            redirectAttributes.addFlashAttribute("error", message);
    
            return "redirect:/v1/typeMaison";
        }
        try{
             typeMaisonRepository.save(typeMaison);
        }catch (Exception e){
            message += e.getMessage()+" ; ";
            redirectAttributes.addFlashAttribute("error", message);
            return "redirect:/v1/typeMaison";
        }

        redirectAttributes.addFlashAttribute("message" , "Modification avec succes");
        return "redirect:/v1/typeMaison";
    }

}
