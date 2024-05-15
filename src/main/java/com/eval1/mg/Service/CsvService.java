package com.eval1.mg.Service;


import com.eval1.mg.Model.Construction_csv;
import com.eval1.mg.Model.Payement_csv;
import com.eval1.mg.Model.Travaux_maison_csv;
import com.eval1.mg.Repository.ConstructionCsvRepository;
import com.eval1.mg.Repository.ConstructionRepository;
import com.eval1.mg.Repository.PayementCsvRepository;
import com.eval1.mg.Repository.TravauxMaisonCsvRepository;
import com.eval1.mg.View.V_construction_complet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

@Service
public class CsvService {

    @Autowired
    ConstructionCsvRepository constructionCsvRepository;

    @Autowired
    PayementCsvRepository payementCsvRepository;

    @Autowired
    TravauxMaisonCsvRepository travauxMaisonCsvRepository;

    @Autowired
    ConstructionRepository constructionRepository;

    public String[] insertMaisonTravauxCsv(MultipartFile file) throws Exception {
        String[] mess = new String[2];

        int nombreTafiditra = 0;
        String messError = " ";
        if (file.isEmpty()) {
            throw new Exception("File vide");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            int ligne =1;

            while ((line = reader.readLine()) != null) {
                if (ligne==1){
                    ligne+=1;

                    continue;
                }
                String[] data = line.split(";");
                try{
                    Travaux_maison_csv travauxMaisonCsv = new Travaux_maison_csv();
                    String typeMaison = data[0].trim();
                    String description = data[1].trim();
                    double surface = Double.parseDouble(data[2].replace(',', '.'));
                    String code_travaux = data[3].trim();
                    String typeTravaux = data[4].trim();
                    String unite = data[5].trim();
                    String puString = data[6].replace(',', '.').trim(); // Remplace la virgule par un point
                    double pu = Double.parseDouble(puString);

                    String qtString = data[7].replace(',', '.').trim(); // Remplace la virgule par un point
                    double qt = Double.parseDouble(qtString);

                    double dureeTravaux = Double.parseDouble(data[8].replace(',', '.'));

                    travauxMaisonCsv = new Travaux_maison_csv(typeMaison,description,typeTravaux,surface,code_travaux,unite,pu,qt,dureeTravaux);

                    travauxMaisonCsvRepository.save(travauxMaisonCsv);

                    nombreTafiditra+=1;
                }catch (Exception e){
                    String tempMess ="Fichier maisonTravaux : "+ e.getMessage()+" Ligne : "+ligne+" ---- ";
                    if(data.length==1){
                        tempMess = "Fichier maisonTravaux : "+"Ligne vide sur Ligne : "+ligne+" ---- ";
                    }
                    messError+=tempMess;
                }
                ligne++;
            }
        }catch (Exception e){
            e.printStackTrace();
            messError+=e.getMessage();
        }

        mess[0] = Integer.toString(nombreTafiditra);
        mess[1] = messError;

        return mess;
    }

    public String[] insertConstructionCsv(MultipartFile file) throws Exception {
        String[] mess = new String[2];

        int nombreTafiditra = 0;
        String messError = " ";
        if (file.isEmpty()) {
            throw new Exception("File vide");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            int ligne =1;

            while ((line = reader.readLine()) != null) {
                if (ligne==1){
                    ligne+=1;

                    continue;
                }
                String[] data = line.split(";");
                try{
                    Construction_csv constructionCsv = new Construction_csv();
                    String client = data[0].trim();
                    String refDevis = data[1].trim();
                    String typeMaison = data[2].trim();
                    String finition = data[3].trim();
                    double tauxFinition = Double.parseDouble((data[4].replaceAll("[\"%]", "")).replace(',', '.'));
                    String datyDevis = data[5].trim();
                    String datyDebut = data[6].trim();
                    String lieu = data[7].trim();

                    Date dateDevis = null;
                    Date dateDebut = null;
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
                    format.setLenient(false);
                    dateDevis = format.parse(datyDevis);
                    dateDebut = format.parse(datyDebut);

                    constructionCsv = new Construction_csv(client,refDevis,typeMaison,finition,tauxFinition,dateDevis,dateDebut,lieu);

                    constructionCsvRepository.save(constructionCsv);

                    nombreTafiditra+=1;

                }catch (Exception e){
                    String tempMess = "Fichier construction : "+e.getMessage()+" Ligne : "+ligne+" ---- ";
                    if(data.length==1){
                        tempMess = "Fichier construction : Ligne vide sur Ligne : "+ligne+" ---- ";
                    }
                    messError+=tempMess;
                }
                ligne++;
            }
        }catch (Exception e){
            e.printStackTrace();
            messError+=e.getMessage();
        }

        mess[0] = Integer.toString(nombreTafiditra);
        mess[1] = messError;
        return mess;
    }
    public String[] insertPaiementCsv(MultipartFile file) throws Exception {
        String[] mess = new String[2];

        int nombreTafiditra = 0;
        String messError = " ";
        if (file.isEmpty()) {
            throw new Exception("File vide");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            int ligne =1;

            while ((line = reader.readLine()) != null) {
                if (ligne==1){
                    ligne+=1;

                    continue;
                }
                String[] data = line.split(";");
                try{
                    Payement_csv payementCsv = new Payement_csv();
                    String refDevis = data[0].trim();

                    System.out.println(refDevis);

                    /*V_construction_complet v = constructionRepository.getConstructionByRefConstruction(refDevis);

                    System.out.println(v.getReste());*/

                    String refPaiement = data[1].trim();
                    String datePaiement = data[2];
                    String montant = data[3].trim();

                    Date date = null;
                    int count = 0;
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
                    format.setLenient(false);
                    date = format.parse(datePaiement);

                    payementCsv.setRefPaiement(refPaiement);
                    payementCsv.setMontant(Double.parseDouble(montant.replace(',', '.')));
                    payementCsv.setDatePaiement(date);
                    payementCsv.setRefConstruction(refDevis);

                    payementCsvRepository.save(payementCsv);

                    nombreTafiditra+=1;

                }catch (Exception e){
                    String tempMess = "Fichier payement : "+e.getMessage()+" Ligne : "+ligne+" ---- ";
                    if(data.length==1){
                        tempMess = "Fichier payement : Ligne vide sur Ligne : "+ligne+" ---- ";
                    }
                    messError+=tempMess;
                }
                ligne++;
            }
        }catch (Exception e){
            e.printStackTrace();
            messError+=e.getMessage();
        }

        mess[0] = Integer.toString(nombreTafiditra);
        mess[1] = messError;

        return mess;
    }

}
