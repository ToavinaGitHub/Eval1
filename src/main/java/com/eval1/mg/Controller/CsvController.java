package com.eval1.mg.Controller;

import com.eval1.mg.Model.Construction;
import com.eval1.mg.Model.Travaux;
import com.eval1.mg.Model.TravauxConstruction;
import com.eval1.mg.Model.TypeTravail;
import com.eval1.mg.Repository.*;
import com.eval1.mg.Service.CsvService;
import com.eval1.mg.View.V_construction_csv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Calendar;
import java.util.List;

@Controller
@RequestMapping("/v1/importCsv")
public class CsvController {

    @Autowired
    CsvService csvService;

    @Autowired
    PayementCsvRepository payementCsvRepository;

    @Autowired
    ConstructionCsvRepository constructionCsvRepository;

    @Autowired
    TravauxMaisonCsvRepository travauxMaisonCsvRepository;

    @Autowired
    DevisRepository devisRepository;

    @Autowired
    TravauxConstructionRepository travauxConstructionRepository;

    @Autowired
    ConstructionRepository constructionRepository;

    @Autowired
    UtilisateurRepository utilisateurRepository;

    @Autowired
    EtatConstructionRepository etatConstructionRepository;

    @Autowired
    TypeFinitionRepository typeFinitionRepository;

    @Autowired
    TravauxRepository travauxRepository;

    @Autowired
    TypeTravailRepository typeTravailRepository;


    @GetMapping("")
    public String toImportPage(){
        return "Admin/import";
    }

    @PostMapping("/maisonTravauxDevis")
    public String saveMaisonTravauxDevisCsv(@RequestParam(name = "fileMaisonTravaux")MultipartFile fileMaisonTravaux, @RequestParam(name = "fileDevis")MultipartFile fileDevis, RedirectAttributes redirectAttributes) throws Exception {
        try{
                String[] messMaison = csvService.insertMaisonTravauxCsv(fileMaisonTravaux);
                if(!messMaison[1].equals(" ")){
                    redirectAttributes.addFlashAttribute("error" , messMaison[1]);
                    travauxMaisonCsvRepository.deleteAll();
                    constructionCsvRepository.deleteAll();
                    return "redirect:/v1/importCsv";
                }else {
                    String[] messDevis = csvService.insertConstructionCsv(fileDevis);
                    if(!messDevis[1].equals(" ")){
                        redirectAttributes.addFlashAttribute("error" , messDevis[1]);
                        travauxMaisonCsvRepository.deleteAll();
                        constructionCsvRepository.deleteAll();
                        return "redirect:/v1/importCsv";
                    }else {
                        travauxMaisonCsvRepository.insertIntoTypeMaison();
                        travauxMaisonCsvRepository.insertIntoDevis();
                        travauxMaisonCsvRepository.insertIntoUnite();
                        travauxMaisonCsvRepository.insertIntoTravaux();
                        constructionCsvRepository.insertIntoUser();
                        constructionCsvRepository.insertIntoFinition();
                        //constructionCsvRepository.insertIntoConstruction();

                        List<V_construction_csv> allConstr = constructionCsvRepository.getAllConstrCsv();
                        for (V_construction_csv a:allConstr
                             ) {
                            Construction c = new Construction();
                            c.setDevis(devisRepository.getById(a.getIdDevis()));
                            c.setDuree(a.getDuree());
                            c.setDaty(a.getDateDevis());
                            c.setDebut(a.getDateDebut());
                            c.setPourcentageFinition(a.getAugmentation());
                            c.setUtilisateur(utilisateurRepository.getById(a.getIdUser()));
                            c.setMontantTotal(a.getMontantTotal());
                            c.setEtat(a.getEtat());
                            c.setDemande(a.getDemande());
                            c.setTypeFinition(typeFinitionRepository.getById(a.getIdTypeFinition()));
                            c.setLieu(a.getLieu());
                            c.setPrixMaison(devisRepository.getById(a.getIdDevis()).getTypeMaison().getPrix());

                            c.setRefConstruction(a.getRefConstruction());
                            c.setEtatConstruction(etatConstructionRepository.getById(0));

                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(c.getDebut());
                            calendar.add(Calendar.DAY_OF_YEAR, (int) c.getDuree());

                            c.setFin(calendar.getTime());

                            Construction temp = constructionRepository.save(c);

                            List<Travaux> allTravaux = travauxRepository.findTravauxByDevis(c.getDevis());
                            for (Travaux tr:allTravaux
                            ) {
                                TravauxConstruction tvx = new TravauxConstruction(tr.getNumero(),tr.getDesignation(),tr.getPrixUnitaire(),tr.getQuantite(),tr.getMontantTotal(),0,tr.getTypeTravail(),tr.getUnite(),null);
                                tvx.setConstruction(temp);
                                tvx.setTypeTravail(typeTravailRepository.getById(3));

                                travauxConstructionRepository.save(tvx);
                            }
                        }

                        travauxMaisonCsvRepository.deleteAll();
                        constructionCsvRepository.deleteAll();

                    }
                    redirectAttributes.addFlashAttribute("message" , "Fichier maison travaux : "+messMaison[0]+" inseré avec succes et Fichier devis : "+messDevis[0]+" inseré avec succes ");
                }
        }catch (Exception e){
            e.printStackTrace();
        }


        return "redirect:/v1/importCsv";
    }

    @PostMapping("/paiement")
    public String savePaiementCsv(@RequestParam(name = "filePayement")MultipartFile filePayement,RedirectAttributes redirectAttributes) throws Exception {

        String[] messMaison = csvService.insertPaiementCsv(filePayement);
        try{

            if(!messMaison[1].equals(" ")){
                redirectAttributes.addFlashAttribute("error" , messMaison[1]);
                redirectAttributes.addFlashAttribute("message" , messMaison[0]+" peuvent etre inséré");
                payementCsvRepository.deleteAll();
                return "redirect:/v1/importCsv";
            }else {
                payementCsvRepository.insertIntoPayement();
                payementCsvRepository.deleteAll();
            }
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", "Erreur d'importation : " + e.getMessage());
            return "redirect:/v1/importCsv";
        }

        redirectAttributes.addFlashAttribute("message" ,messMaison[0]+" données inserés avec succes");
        return "redirect:/v1/importCsv";
    }
}
