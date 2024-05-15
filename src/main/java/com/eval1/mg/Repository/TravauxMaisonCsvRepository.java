package com.eval1.mg.Repository;

import com.eval1.mg.Model.Travaux_maison_csv;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TravauxMaisonCsvRepository extends JpaRepository<Travaux_maison_csv,Integer> {


    @Transactional
    @Modifying
    @Query(value = "INSERT INTO type_maison(designation, duree, etat, prix, description, surface)   (select c.type_maison,c.duree_travaux,1 as etat,v.somme,c.description,c.surface as prix from travaux_maison_csv c join v_somme_travaux_typeMaison v on c.type_maison=v.type_maison left join type_maison t on t.designation=c.type_maison where t.designation is null group by c.type_maison,c.duree_travaux,v.somme,c.description,c.surface)",nativeQuery = true)
    public void insertIntoTypeMaison();

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO devis(designation, duree_total, etat, montant_total, id_type_maison) (select concat('Devis',c.type_maison) as designationDevis,0 as duree,1 as etatM,v.somme as montant,t.id_type_maison from travaux_maison_csv c join type_maison t on c.type_maison=t.designation join v_somme_travaux_typeMaison v on t.designation=v.type_maison left join devis d on d.designation=concat('Devis',c.type_maison) where d.designation is null group by designationDevis,duree,etatM,montant,t.id_type_maison)",nativeQuery = true)
    public void insertIntoDevis();

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO unite(designation) (select c.unite from travaux_maison_csv c left join  unite u on u.designation=c.unite where u.designation is null group by c.unite)",nativeQuery = true)
    public void insertIntoUnite();

    @Transactional
    @Modifying
    @Query(value = "insert into travaux(designation, etat, montant_total, numero, prix_unitaire, quantite, id_devis, id_unite)" +
            "(select c.type_travaux as designationTravaux,1 as etat,c.prix_unitaire*c.quantite montantTotal,c.code_travaux codeTravaux,c.prix_unitaire," +
            "c.quantite,d.id,u.id as unite from travaux_maison_csv c join unite u on c.unite=u.designation join devis d on concat('Devis',c.type_maison)=d.designation" +
            "        left join travaux tr on tr.numero=code_travaux and tr.designation=c.type_travaux where tr.numero is null and tr.designation is null group by" +
            "        designationTravaux,d.etat,d.montant_total,codeTravaux,c.prix_unitaire,c.quantite,d.id,u.id)",nativeQuery = true)
    public void insertIntoTravaux();











}
