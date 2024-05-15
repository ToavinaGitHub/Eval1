package com.eval1.mg.Repository;

import com.eval1.mg.Model.Construction;
import com.eval1.mg.Model.Construction_csv;
import com.eval1.mg.View.V_construction_csv;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConstructionCsvRepository extends JpaRepository<Construction_csv,Integer> {


    @Transactional
    @Modifying
    @Query(value = "insert into utilisateur(contact,genre,profil) (select c.client,1 as genre,'CLIENT' as profil from construction_csv c left join utilisateur u on c.client=u.contact where u.contact is null group by c.client)",nativeQuery = true)
    public void insertIntoUser();

    @Transactional
    @Modifying
    @Query(value = "insert into construction(daty, debut, demande, etat, fin, montant_total, id_devis, id_type_finition, id_utilisateur, pourcentage_finition, duree, id_etat_construction, ref_construction, lieu)(select c.date_devis,c.date_debut,'ha',0 as etatP,c.date_debut::DATE + INTERVAL '1 day'*tp.duree as fin,tp.prix,d.id,tf.id,us.id,tf.augmentation,tp.duree,0 as idEtat,c.ref_construction,c.lieu from construction_csv c join type_maison tp on tp.designation=c.type_maison join type_finition tf on tf.designation=c.finition  join utilisateur us on us.contact=c.client join devis d on d.designation=concat('Devis',c.type_maison) left join construction cons on cons.ref_construction=c.ref_construction where cons.ref_construction is null group by c.date_devis, c.date_debut,etatP,fin, tp.prix, d.id, tf.id, us.id, tf.augmentation, tp.duree, idEtat,c.ref_construction, c.lieu)",nativeQuery = true)
     public void insertIntoConstruction();



    @Query(value = "select c.date_devis as dateDevis,c.date_debut as dateDebut,'ha' as demande,0 as etat,tp.prix+((tp.prix*tf.augmentation)/100) as montantTotal,d.id as idDevis,tf.id as idTypeFinition,us.id as idUser,tf.augmentation as augmentation,tp.duree as duree,0 as idEtat,c.ref_construction as refConstruction,c.lieu as lieu from construction_csv c join type_maison tp on tp.designation=c.type_maison join type_finition tf on tf.designation=c.finition  join utilisateur us on us.contact=c.client join devis d on d.designation=concat('Devis',c.type_maison) left join construction cons on cons.ref_construction=c.ref_construction where cons.ref_construction is null group by c.date_devis, c.date_debut,fin, tp.prix, d.id, tf.id, us.id, tf.augmentation, tp.duree, idEtat,c.ref_construction, c.lieu",nativeQuery = true)
    public List<V_construction_csv> getAllConstrCsv();

    @Transactional
    @Modifying
    @Query(value = "insert into type_finition(augmentation, designation, etat) (select c.taux_finition,c.finition,1 as etat from construction_csv c left join type_finition t on t.designation=c.finition where t.designation is null GROUP BY c.taux_finition, c.finition)",nativeQuery = true)
    public void insertIntoFinition();




}
