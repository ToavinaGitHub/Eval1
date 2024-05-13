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

import com.eval1.mg.Model.TypeFinition;
import com.eval1.mg.Repository. TypeFinitionRepository;

import org.springframework.ui.Model;

@RequestMapping("/v1/typeFinition")
@Controller
public class TypeFinitionController {

    @Autowired
    private TypeFinitionRepository  typeFinitionRepository;

    @GetMapping("")
    public String index(Model model , @RequestParam(name = "keyword" ,required = false ,defaultValue="") String key,
      @RequestParam(defaultValue = "1" , required = false ,name = "page") int page, @RequestParam(defaultValue = "3" , required = false ,name = "size") int size,
      @RequestParam(defaultValue = "id", required = false, name = "sortField") String sortField,@RequestParam(defaultValue = "asc", required = false, name = "sortOrder") String sortOrder)  {
       
        List<TypeFinition> typeFinitions = new ArrayList<TypeFinition>();

        Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Sort sort = Sort.by(direction, sortField);

        Pageable pageable =PageRequest.of(page-1,size,sort);

        Page<TypeFinition> pageCateg;
        if(key.compareTo("")==0){
            pageCateg = typeFinitionRepository.findTypeFinitionByEtat(1,pageable);
        }else{
            /*  
             SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
             Date d = s.parse(key);
            */
            pageCateg =  typeFinitionRepository.findTypeFinitionByEtatAndDesignationContainingIgnoreCase(1,key,pageable);
        }

        TypeFinition c = new TypeFinition();

        typeFinitions = pageCateg.getContent();

        model.addAttribute("keyword" , key);
        model.addAttribute("typeFinition" , c);
        model.addAttribute("typeFinitions", typeFinitions);

        model.addAttribute("currentPage", pageCateg.getNumber() + 1);
        model.addAttribute("totalItems", pageCateg.getTotalElements());
        model.addAttribute("totalPages", pageCateg.getTotalPages());
        model.addAttribute("pageSize", size);

        return "TypeFinition/index";
    }

    @PostMapping("")
    public String save(@Valid TypeFinition typeFinition , BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()){
            String message = "";
            for (int i = 0; i < bindingResult.getAllErrors().size(); i++) {
                message += bindingResult.getAllErrors().get(i).getDefaultMessage()+";";
            }
            redirectAttributes.addFlashAttribute("error", message);
            return "redirect:/v1/typeFinition";
        }
        try{
            typeFinition.setEtat(1);
            typeFinitionRepository.save(typeFinition);
            redirectAttributes.addFlashAttribute("success", "TypeFinition ajoutée avec succès");
            redirectAttributes.addFlashAttribute("message" , "Insertion avec succes");
        }catch(Exception e){
            redirectAttributes.addFlashAttribute("error", "Transaction echouée");
        }

        return "redirect:/v1/typeFinition";
    }
    @GetMapping("/{id}")
    public String editTutorial(@PathVariable("id") String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            TypeFinition t = typeFinitionRepository.findById(id).get();

            model.addAttribute("typeFinition", t);
            model.addAttribute("pageTitle", "Edit TypeFinition (ID: " + id + ")");

            //List<TypeFinition> typeFinitions = typeFinitionRepository.findAll();

            List<TypeFinition> typeFinitions = typeFinitionRepository.findTypeFinitionByEtat(1);
            
            model.addAttribute("isUpdate" , 1);
            model.addAttribute("typeFinitions", typeFinitions);
            return "TypeFinition/index";
        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("error", "Transaction echouée");
            return "redirect:/v1/typeFinition";
        }
    }

    @GetMapping("/delOption")
    public String delete(@RequestParam String id , RedirectAttributes redirectAttributes){
        TypeFinition ct =  typeFinitionRepository.findById(id).get();
        //typeFinitionRepository.delete(ct);
        ct.setEtat(0);
        typeFinitionRepository.save(ct);

        redirectAttributes.addFlashAttribute("message" , "Supprimé avec succés");
        return "redirect:/v1/typeFinition";
    }

    @PostMapping("/edit")
    public String modifier(@Valid @ModelAttribute TypeFinition typeFinition,BindingResult bindingResult,RedirectAttributes redirectAttributes){
        String message = "";
        if(bindingResult.hasErrors()){
            for (int i = 0; i < bindingResult.getAllErrors().size(); i++) {
                message += bindingResult.getAllErrors().get(i).getDefaultMessage()+";";
            }
            redirectAttributes.addFlashAttribute("error", message);
    
            return "redirect:/v1/typeFinition";
        }
        try{
             typeFinitionRepository.save(typeFinition);
        }catch (Exception e){
            message += e.getMessage()+" ; ";
            redirectAttributes.addFlashAttribute("error", message);
            return "redirect:/v1/typeFinition";
        }

        redirectAttributes.addFlashAttribute("message" , "Modification avec succes");
        return "redirect:/v1/typeFinition";
    }

}
