package com.eval1.mg.Controller;

import com.eval1.mg.Model.*;
import com.eval1.mg.Repository.*;
import com.eval1.mg.Service.ConstructionService;
import com.eval1.mg.View.V_construction_complet;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.hibernate.annotations.Parent;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.ui.Model;
import org.thymeleaf.TemplateEngine;

@RequestMapping("/v1/construction/")
@Controller
public class ConstructionController {

    @Autowired
    private ConstructionRepository  constructionRepository;

    @Autowired
    private  TypeMaisonRepository typeMaisonRepository;

    @Autowired
    private TypeFinitionRepository typeFinitionRepository;

    @Autowired
    private ConstructionService constructionService;

    @Autowired
    DevisRepository devisRepository;

    @Autowired
    TravauxRepository travauxRepository;

    @Autowired
    TravauxConstructionRepository travauxConstructionRepository;


    @Autowired
    ServletContext servletContext;

    private final TemplateEngine templateEngine;

    public ConstructionController(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }


    @GetMapping("/user")
    public String index(Model model , @RequestParam(name = "keyword" ,required = false ,defaultValue="") String key,
                        @RequestParam(defaultValue = "1" , required = false ,name = "page") int page, @RequestParam(defaultValue = "3" , required = false ,name = "size") int size,
                        @RequestParam(defaultValue = "idConstruction", required = false, name = "sortField") String sortField, @RequestParam(defaultValue = "asc", required = false, name = "sortOrder") String sortOrder, HttpServletRequest request) throws ParseException {



        List<V_construction_complet> constructions = new ArrayList<V_construction_complet>();

        Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Sort sort = Sort.by(direction, sortField);

        Pageable pageable =PageRequest.of(page-1,size,sort);

        Page<V_construction_complet> pageCateg;

        HttpSession session = request.getSession();
        Utilisateur sessU = (Utilisateur) session.getAttribute("user");

        if(key.compareTo("")==0){
            //pageCateg = constructionRepository.findConstructionByUtilisateurAndEtatGreaterThan(sessU,-1,pageable);
            pageCateg = constructionRepository.getConstructionCompletEtatUtilisateur(sessU.getId(),-1,pageable);
        }else{

             SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
             Date d = s.parse(key);

            // pageCateg =  constructionRepository.getConstructionByDatyAndEtatAndUtilisateur(d,sessU.getId(),pageable);
            pageCateg = constructionRepository.getConstructionCompletDatyEtatUtilisateur(d,sessU.getId(),pageable);
        }

        Construction c = new Construction();

        constructions = pageCateg.getContent();

        model.addAttribute("keyword" , key);
        model.addAttribute("construction" , c);
        model.addAttribute("constructions", constructions);

        model.addAttribute("currentPage", pageCateg.getNumber() + 1);
        model.addAttribute("totalItems", pageCateg.getTotalElements());
        model.addAttribute("totalPages", pageCateg.getTotalPages());
        model.addAttribute("pageSize", size);

        return "Client/accueil";
    }

    @PostMapping("/user")
    public String save(@Valid Construction construction , BindingResult bindingResult, RedirectAttributes redirectAttributes,HttpServletRequest request) {
        if(bindingResult.hasErrors()){
            String message = "";
            for (int i = 0; i < bindingResult.getAllErrors().size(); i++) {
                message += bindingResult.getAllErrors().get(i).getDefaultMessage()+";";
            }
            redirectAttributes.addFlashAttribute("error", message);
            return "redirect:/v1/construction/user";
        }
        try{
            construction.setEtat(0);
            construction.setDaty(new Date());
            construction.setUtilisateur(((Utilisateur) request.getSession().getAttribute("user")));
            constructionRepository.save(construction);
            redirectAttributes.addFlashAttribute("success", "Construction ajoutée avec succès");
            redirectAttributes.addFlashAttribute("message" , "Insertion avec succes");
        }catch(Exception e){
            redirectAttributes.addFlashAttribute("error", "Transaction echouée");
        }

        return "redirect:/v1/construction/user";
    }
    @GetMapping("/{id}")
    public String editTutorial(@PathVariable("id") String id, Model model,HttpServletRequest request, RedirectAttributes redirectAttributes) {
        try {
            Construction t = constructionRepository.findById(id).get();

            model.addAttribute("construction", t);
            model.addAttribute("pageTitle", "Edit Construction (ID: " + id + ")");

            //List<Construction> constructions = constructionRepository.findAll();

            List<Construction> constructions = constructionRepository.findConstructionByUtilisateurAndEtatGreaterThan((Utilisateur) request.getSession().getAttribute("user"),-1);
            
            model.addAttribute("isUpdate" , 1);
            model.addAttribute("constructions", constructions);
            return "Client/accueil";
        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("error", "Transaction echouée");
            return "redirect:/v1/construction/user";
        }
    }

    @GetMapping("/delOption")
    public String delete(@RequestParam String id , RedirectAttributes redirectAttributes){
        Construction ct =  constructionRepository.findById(id).get();
        //constructionRepository.delete(ct);
        //ct.setEtat(0);
        constructionRepository.save(ct);

        redirectAttributes.addFlashAttribute("message" , "Supprimé avec succés");
        return "redirect:/v1/construction/user";
    }

    @PostMapping("/edit")
    public String modifier(@Valid @ModelAttribute Construction construction,HttpServletRequest request,BindingResult bindingResult,RedirectAttributes redirectAttributes){
        String message = "";
        if(bindingResult.hasErrors()){
            for (int i = 0; i < bindingResult.getAllErrors().size(); i++) {
                message += bindingResult.getAllErrors().get(i).getDefaultMessage()+";";
            }
            redirectAttributes.addFlashAttribute("error", message);
    
            return "redirect:/v1/construction/user";
        }
        try{
            construction.setUtilisateur(((Utilisateur) request.getSession().getAttribute("user")));
            constructionRepository.save(construction);
        }catch (Exception e){
            message += e.getMessage()+" ; ";
            redirectAttributes.addFlashAttribute("error", message);
            return "redirect:/v1/construction/user";
        }

        redirectAttributes.addFlashAttribute("message" , "Modification avec succes");
        return "redirect:/v1/construction/user";
    }


    @GetMapping("/user/addDevis")
    public String toAddDevis(Model model){

        List<TypeMaison> allTypeMaison = typeMaisonRepository.findTypeMaisonByEtat(1);

        List<TypeFinition> allTypeFinition = typeFinitionRepository.findTypeFinitionByEtat(1);

        Construction construction = new Construction();

        model.addAttribute("construction" , construction);
        model.addAttribute("allTypeMaison" , allTypeMaison);
        model.addAttribute("allTypeFinition",allTypeFinition);

        return "Client/addDevis";
    }

    @PostMapping("/user/addDevis")
    public String addDevis(@Valid Construction construction,@RequestParam(name = "typeMaison") String typeMaison,HttpServletRequest request,RedirectAttributes redirectAttributes,BindingResult bindingResult,Model model){

        if(bindingResult.hasErrors()){
            String message = "";
            for (int i = 0; i < bindingResult.getAllErrors().size(); i++) {
                message += bindingResult.getAllErrors().get(i).getDefaultMessage()+";";
            }
            redirectAttributes.addFlashAttribute("error", message);
            return "redirect:/v1/construction/user";
        }
        try{
            construction.setEtat(0);

            System.out.println(typeMaison);
            TypeMaison t = typeMaisonRepository.findById(typeMaison).get();

            Devis d = devisRepository.findDevisByTypeMaison(t);
            construction.setDevis(d);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(construction.getDebut());
            calendar.add(Calendar.DAY_OF_YEAR, (int) t.getDuree());

            Date fin = calendar.getTime();

            double montantTotal = t.getPrix()+( (t.getPrix()*construction.getTypeFinition().getAugmentation())/100);

            construction.setPourcentageFinition(construction.getTypeFinition().getAugmentation());
            construction.setMontantTotal(montantTotal);
            construction.setFin(fin);
            construction.setUtilisateur(((Utilisateur) request.getSession().getAttribute("user")));
            construction.setDuree(t.getDuree());
            Construction a = constructionRepository.save(construction);


            List<Travaux> allTravaux = travauxRepository.findTravauxByDevis(d);
            for (Travaux tr:allTravaux
            ) {
                TravauxConstruction parent = null;
                if(tr.getTravaux()!=null){
                    parent = travauxConstructionRepository.getTravauxConstructionByConstructionAndNumero(construction.getId(),tr.getNumero());
                    //System.out.println("tsy null" + parent.getConstruction()+"--"+parent.getNumero());
                }
                TravauxConstruction tvx = new TravauxConstruction(tr.getNumero(),tr.getDesignation(),tr.getPrixUnitaire(),tr.getQuantite(),tr.getMontantTotal(),0,tr.getTypeTravail(),tr.getUnite(),parent);
                tvx.setConstruction(a);

                travauxConstructionRepository.save(tvx);
            }

            redirectAttributes.addFlashAttribute("success", "Construction ajoutée avec succès");
            redirectAttributes.addFlashAttribute("message" , "Insertion avec succes");
        }catch(Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Transaction echouée");
        }

        return "redirect:/v1/construction/user";
    }

    @GetMapping("/admin")
    public String indexAdmin(Model model , @RequestParam(name = "keyword" ,required = false ,defaultValue="") String key,
                        @RequestParam(defaultValue = "1" , required = false ,name = "page") int page, @RequestParam(defaultValue = "5" , required = false ,name = "size") int size,
                        @RequestParam(defaultValue = "idConstruction", required = false, name = "sortField") String sortField, @RequestParam(defaultValue = "asc", required = false, name = "sortOrder") String sortOrder, HttpServletRequest request) throws ParseException {


        List<V_construction_complet> constructions = new ArrayList<V_construction_complet>();

        Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Sort sort = Sort.by(direction, sortField);

        Pageable pageable =PageRequest.of(page-1,size,sort);

        Page<V_construction_complet> pageCateg;

        HttpSession session = request.getSession();
        Utilisateur sessU = (Utilisateur) session.getAttribute("user");

        if(key.compareTo("")==0){
            //pageCateg = constructionRepository.findConstructionByUtilisateurAndEtatGreaterThan(sessU,-1,pageable);
            pageCateg = constructionRepository.getConstructionCompletEtat(-1,pageable);
        }else{

            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
            Date d = s.parse(key);

            // pageCateg =  constructionRepository.getConstructionByDatyAndEtatAndUtilisateur(d,sessU.getId(),pageable);
            pageCateg = constructionRepository.getConstructionCompletDatyEtat(d,pageable);
        }

        Construction c = new Construction();

        constructions = pageCateg.getContent();

        model.addAttribute("keyword" , key);
        model.addAttribute("construction" , c);
        model.addAttribute("constructions", constructions);

        model.addAttribute("currentPage", pageCateg.getNumber() + 1);
        model.addAttribute("totalItems", pageCateg.getTotalElements());
        model.addAttribute("totalPages", pageCateg.getTotalPages());
        model.addAttribute("pageSize", size);

        return "Admin/accueil";
    }


    @GetMapping("/admin/details")
    public String toDetailsConstruction(@RequestParam(name = "idConstruction") String idConstruction,Model model){


        Construction c = constructionRepository.getById(idConstruction);
        List<TravauxConstruction> all = travauxConstructionRepository.findTravauxConstructionByConstruction(c);
        HashMap<TypeTravail,List<TravauxConstruction>> a = constructionService.getTravauxParTravail(all);
        model.addAttribute("all",a);

        return "Admin/detailsConstruction";
    }

    @GetMapping("/pdf")
    public void toPdf(@RequestParam("idConstruction") String idConstruction, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes, Model model) throws IOException, IOException {
        Construction c = constructionRepository.getById(idConstruction);
        List<TravauxConstruction> all = travauxConstructionRepository.findTravauxConstructionByConstruction(c);
        HashMap<TypeTravail,List<TravauxConstruction>> a = constructionService.getTravauxParTravail(all);

        Context context = new Context();

        context.setVariable("all", a);

        String orderHtml = templateEngine.process("Client/detailsDevisPdf", context);

        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setBaseUri("http://localhost:8080");

        /* Convert HTML to PDF */
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(orderHtml, target, converterProperties);

        response.setContentType("application/pdf");

        String nom = c.getId()+".pdf";
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+nom+"\"");

        response.getOutputStream().write(target.toByteArray());
    }



}
