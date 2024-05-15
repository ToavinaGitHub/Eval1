package com.eval1.mg.Repository;

import com.eval1.mg.Model.Payement;
import com.eval1.mg.Model.Payement_csv;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PayementCsvRepository extends JpaRepository<Payement_csv,Integer> {

    @Transactional
    @Modifying
    @Query(value = "insert into payement(daty, etat, montant, id_construction, ref_payement)(select c.date_paiement,1 as etatP,c.montant,cons.id , c.ref_paiement from payement_csv c join construction cons on c.ref_construction=cons.ref_construction left join payement p on p.ref_payement=c.ref_paiement where p.ref_payement is null group by c.date_paiement, etatP, c.montant, cons.id, c.ref_paiement)",nativeQuery = true)
    public void insertIntoPayement();


}
