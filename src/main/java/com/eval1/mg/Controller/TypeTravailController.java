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

import com.eval1.mg.Model.TypeTravail;
import com.eval1.mg.Repository. TypeTravailRepository;

import org.springframework.ui.Model;

@RequestMapping("/v1/typeTravail")
@Controller
public class TypeTravailController {

    @Autowired
    private TypeTravailRepository  typeTravailRepository;

    @GetMapping("")
    public String index(Model model , @RequestParam(name = "keyword" ,required = false ,defaultValue="") String key,
      @RequestParam(defaultValue = "1" , required = false ,name = "page") int page, @RequestParam(defaultValue = "3" , required = false ,name = "size") int size,
      @RequestParam(defaultValue = "id", required = false, name = "sortField") String sortField,@RequestParam(defaultValue = "asc", required = false, name = "sortOrder") String sortOrder)  {
       
        List<TypeTravail> typeTravails = new ArrayList<TypeTravail>();

        Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Sort sort = Sort.by(direction, sortField);

        Pageable pageable =PageRequest.of(page-1,size,sort);

        Page<TypeTravail> pageCateg;
        if(key.compareTo("")==0){
            pageCateg = typeTravailRepository.findTypeTravailByEtat(1,pageable);
        }else{
            /*  
             SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
             Date d = s.parse(key);
            */
            pageCateg =  typeTravailRepository.findTypeTravailByEtatAndDesignationContainingIgnoreCase(1,key,pageable);
        }

        TypeTravail c = new TypeTravail();

        typeTravails = pageCateg.getContent();

        model.addAttribute("keyword" , key);
        model.addAttribute("typeTravail" , c);
        model.addAttribute("typeTravails", typeTravails);

        model.addAttribute("currentPage", pageCateg.getNumber() + 1);
        model.addAttribute("totalItems", pageCateg.getTotalElements());
        model.addAttribute("totalPages", pageCateg.getTotalPages());
        model.addAttribute("pageSize", size);

        return "TypeTravail/index";
    }

    @PostMapping("")
    public String save(@Valid TypeTravail typeTravail , BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()){
            String message = "";
            for (int i = 0; i < bindingResult.getAllErrors().size(); i++) {
                message += bindingResult.getAllErrors().get(i).getDefaultMessage()+";";
            }
            redirectAttributes.addFlashAttribute("error", message);
            return "redirect:/v1/typeTravail";
        }
        try{
            typeTravail.setEtat(1);
            typeTravailRepository.save(typeTravail);
            redirectAttributes.addFlashAttribute("success", "TypeTravail ajoutée avec succès");
            redirectAttributes.addFlashAttribute("message" , "Insertion avec succes");
        }catch(Exception e){
            redirectAttributes.addFlashAttribute("error", "Transaction echouée");
        }

        return "redirect:/v1/typeTravail";
    }
    @GetMapping("/{id}")
    public String editTutorial(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            TypeTravail t = typeTravailRepository.findById(id).get();

            model.addAttribute("typeTravail", t);
            model.addAttribute("pageTitle", "Edit TypeTravail (ID: " + id + ")");

            //List<TypeTravail> typeTravails = typeTravailRepository.findAll();

            List<TypeTravail> typeTravails = typeTravailRepository.findTypeTravailByEtat(1);
            
            model.addAttribute("isUpdate" , 1);
            model.addAttribute("typeTravails", typeTravails);
            return "TypeTravail/index";
        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("error", "Transaction echouée");
            return "redirect:/v1/typeTravail";
        }
    }

    @GetMapping("/delOption")
    public String delete(@RequestParam Integer id , RedirectAttributes redirectAttributes){
        TypeTravail ct =  typeTravailRepository.findById(id).get();
        //typeTravailRepository.delete(ct);
        ct.setEtat(0);
        typeTravailRepository.save(ct);

        redirectAttributes.addFlashAttribute("message" , "Supprimé avec succés");
        return "redirect:/v1/typeTravail";
    }

    @PostMapping("/edit")
    public String modifier(@Valid @ModelAttribute TypeTravail typeTravail,BindingResult bindingResult,RedirectAttributes redirectAttributes){
        String message = "";
        if(bindingResult.hasErrors()){
            for (int i = 0; i < bindingResult.getAllErrors().size(); i++) {
                message += bindingResult.getAllErrors().get(i).getDefaultMessage()+";";
            }
            redirectAttributes.addFlashAttribute("error", message);
    
            return "redirect:/v1/typeTravail";
        }
        try{
             typeTravailRepository.save(typeTravail);
        }catch (Exception e){
            message += e.getMessage()+" ; ";
            redirectAttributes.addFlashAttribute("error", message);
            return "redirect:/v1/typeTravail";
        }

        redirectAttributes.addFlashAttribute("message" , "Modification avec succes");
        return "redirect:/v1/typeTravail";
    }

}
