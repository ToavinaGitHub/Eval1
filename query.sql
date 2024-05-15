----------- SET id seq for tables----------------



CREATE SEQUENCE utilisateur_id_seq START 1;
CREATE OR REPLACE FUNCTION generate_utilisateur_id() RETURNS VARCHAR AS $$
DECLARE
    next_id INTEGER;
BEGIN
    SELECT nextval('utilisateur_id_seq') INTO next_id;
    RETURN 'USER' || LPAD(next_id::TEXT, 7, '0');
END;
$$ LANGUAGE plpgsql;
ALTER TABLE utilisateur
    ALTER COLUMN id SET DEFAULT generate_utilisateur_id();



CREATE SEQUENCE construction_id_seq START 1;
CREATE OR REPLACE FUNCTION generate_construction_id() RETURNS VARCHAR AS $$
DECLARE
    next_id INTEGER;
BEGIN
    SELECT nextval('construction_id_seq') INTO next_id;
    RETURN 'CONSTR' || LPAD(next_id::TEXT, 7, '0');
END;
$$ LANGUAGE plpgsql;
ALTER TABLE construction
    ALTER COLUMN id SET DEFAULT generate_construction_id();


CREATE SEQUENCE details_type_maison_id_seq START 1;
CREATE OR REPLACE FUNCTION generate_details_type_maison_id() RETURNS VARCHAR AS $$
DECLARE
    next_id INTEGER;
BEGIN
    SELECT nextval('details_type_maison_id_seq') INTO next_id;
    RETURN 'DETMAIS' || LPAD(next_id::TEXT, 9, '0');
END;
$$ LANGUAGE plpgsql;
ALTER TABLE details_type_maison
    ALTER COLUMN id SET DEFAULT generate_details_type_maison_id();


CREATE SEQUENCE devis_id_seq START 1;
CREATE OR REPLACE FUNCTION generate_devis_id() RETURNS VARCHAR AS $$
DECLARE
    next_id INTEGER;
BEGIN
    SELECT nextval('devis_id_seq') INTO next_id;
    RETURN 'DEV' || LPAD(next_id::TEXT, 9, '0');
END;
$$ LANGUAGE plpgsql;
ALTER TABLE devis
    ALTER COLUMN id SET DEFAULT generate_devis_id();


CREATE SEQUENCE payement_id_seq START 1;
CREATE OR REPLACE FUNCTION generate_payement_id() RETURNS VARCHAR AS $$
DECLARE
    next_id INTEGER;
BEGIN
    SELECT nextval('payement_id_seq') INTO next_id;
    RETURN 'PAY' || LPAD(next_id::TEXT, 9, '0');
END;
$$ LANGUAGE plpgsql;
ALTER TABLE payement
    ALTER COLUMN id SET DEFAULT generate_payement_id();

CREATE SEQUENCE type_finition_id_seq START 1;
CREATE OR REPLACE FUNCTION generate_type_finition_id() RETURNS VARCHAR AS $$
DECLARE
    next_id INTEGER;
BEGIN
    SELECT nextval('type_finition_id_seq') INTO next_id;
    RETURN 'TYPFIN' || LPAD(next_id::TEXT, 9, '0');
END;
$$ LANGUAGE plpgsql;
ALTER TABLE type_finition
    ALTER COLUMN id SET DEFAULT generate_type_finition_id();


CREATE SEQUENCE type_maison_id_seq START 1;
CREATE OR REPLACE FUNCTION generate_type_maison_id() RETURNS VARCHAR AS $$
DECLARE
    next_id INTEGER;
BEGIN
    SELECT nextval('type_maison_id_seq') INTO next_id;
    RETURN 'TYPMAI' || LPAD(next_id::TEXT, 7, '0');
END;
$$ LANGUAGE plpgsql;
ALTER TABLE type_maison
    ALTER COLUMN id_type_maison SET DEFAULT generate_type_maison_id();


CREATE OR REPLACE VIEW v_montant_paye_construction as
SELECT id_construction,sum(montant) dejaPaye from payement GROUP BY id_construction;

DROP VIEW v_construction_complet;

SELECT * from v_construction_complet;

CREATE OR REPLACE VIEW v_construction_complet as
SELECT con.ref_construction as refConstruction,con.lieu as lieu,con.id idConstruction,daty,debut,demande,con.etat,con.fin,con.montant_total as total,coalesce(v.dejaPaye,0) as dejaPaye,(coalesce(v.dejaPaye,0)*100)/con.montant_total as pourcPaye,id_devis as idDevis,id_type_finition as idTypeFinition,pourcentage_finition as pourcentageFinition,coalesce(con.montant_total-v.dejaPaye,con.montant_total) as reste,id_utilisateur as idUtilisateur,
       type_maison.designation designationMaison,con.duree as duree,tf.designation designationFinition,u.contact contactUser,et.designation designationEtat
from construction con
    left join v_montant_paye_construction v
    on con.id=v.id_construction
join type_finition tf on tf.id=con.id_type_finition
join devis d on d.id=con.id_devis
join type_maison on d.id_type_maison=type_maison.id_type_maison
join utilisateur u on con.id_utilisateur = u.id
join etat_construction et on et.id=con.id_etat_construction;

SELECT idConstruction,daty,debut,demande,etat,fin,total,dejaPaye,idDevis,idTypeFinition,pourcentageFinition,reste,idUtilisateur,designationMaison,designationFinition,contactUser,duree from v_construction_complet;


SELECT
    sum(montant_total) as Somme
FROM construction;


CREATE TABLE Mois(
    idMois int,
    designation varchar(255)
);

INSERT INTO Mois values
                     (1,'Janvier'),
                     (2,'Fevrier'),
                     (3,'Mars'),
                     (4,'Avril'),
                     (5,'Mai'),
                     (6,'Juin'),
                     (7,'Juillet'),
                     (8,'Aout'),
                     (9,'Septembre'),
                     (10,'Octobre'),
                     (11,'Novembre'),
                     (12,'Decembre');



SELECT extract(year from daty) anne,sum(montant_total) from construction group by  anne;



CREATE OR REPLACE VIEW v_mois_anne_temp AS
SELECT EXTRACT(year FROM daty)  AS anne,
       EXTRACT(month FROM daty) AS mois,
       sum(montant_total)     AS montant
FROM construction
GROUP BY (EXTRACT(year FROM daty)), (EXTRACT(month FROM daty));


CREATE OR REPLACE VIEW v_mois_anne AS
SELECT m.idMois AS idmois,
       m.designation,
       vtm.anne,
       COALESCE(vtm.montant, 0::double precision) AS montant
FROM mois m
         LEFT JOIN v_mois_anne_temp vtm ON m.idMois = vtm.mois;


SELECT m.idMois as idMois,m.designation as mois,2025 as anne,coalesce(a.montant,0) as montant from mois as m left join (SELECT * from v_mois_anne where anne=2025) a on m.idMois=a.idmois;


-----------------TRIGGER TRAVAUX----------------

CREATE OR REPLACE FUNCTION update_montant_total()
    RETURNS TRIGGER AS $$
BEGIN

    NEW.montant_total = NEW.prix_unitaire * NEW.quantite;
    -- Mettre à jour la colonne montantTotal dans la table principale
    UPDATE travaux
    SET montant_total = NEW.montant_total
    WHERE id = NEW.id;

    -- Insérer les données modifiées dans la table d'historique
    INSERT INTO historique_prix_travaux(id_travaux,designation,prix_unitaire, quantite, etat, daty,id_unite)
    VALUES (NEW.id, new.designation,NEW.prix_unitaire, NEW.quantite, 0, now(),new.id_unite);

    -- Mettre à jour la colonne montantTotal dans la table principale
    NEW.montant_total = NEW.prix_unitaire * NEW.quantite;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS update_montant_total_trigger ON travaux;

CREATE TRIGGER update_montant_total_trigger
    AFTER INSERT OR UPDATE OF prix_unitaire, quantite,id_unite,designation
    ON travaux
    FOR EACH ROW
EXECUTE FUNCTION update_montant_total();



-----------------------TYPE MAISON----------------------------------------

CREATE OR REPLACE FUNCTION historiserTypeMaison()
    RETURNS TRIGGER AS $$
BEGIN
    -- Insérer les données modifiées dans la table d'historique
    INSERT INTO historique_type_maison(id_type_maison, description, designation, duree, prix, surface,daty)
    VALUES (NEW.id_type_maison, new.description,NEW.designation, NEW.duree,new.prix, new.surface, now());

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS historiser_type_maison_trigger ON type_maison;

CREATE TRIGGER historiser_type_maison_trigger
    AFTER INSERT OR UPDATE OF designation,duree,prix,description,surface
    ON type_maison
    FOR EACH ROW
EXECUTE FUNCTION historiserTypeMaison();


-----------------------------Type finition--------------------------


CREATE OR REPLACE FUNCTION historiserTypeFinition()
    RETURNS TRIGGER AS $$
BEGIN
    -- Insérer les données modifiées dans la table d'historique
    INSERT INTO historique_type_finition(augmentation, daty, designation, id_type_finition)
    VALUES (NEW.augmentation, now(),NEW.designation, NEW.id);

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS historiser_type_finition_trigger ON type_finition;

CREATE TRIGGER historiser_type_finition_trigger
    AFTER INSERT OR UPDATE OF augmentation,designation
    ON type_finition
    FOR EACH ROW
EXECUTE FUNCTION historiserTypeFinition();

-----------------INSERT FROM csv----------------

CREATE OR REPLACE VIEW v_somme_travaux_typeMaison as
select type_maison,sum(prix_unitaire*quantite) somme from travaux_maison_csv GROUP BY type_maison;


select * from v_

select * from v_somme_travaux_typeMaison;

select c.type_maison,duree_travaux,1 as etat,v.somme,c.description,c.surface as prix  from travaux_maison_csv c join v_somme_travaux_typeMaison v on c.type_maison=v.type_maison;

----Insert in type maison

INSERT INTO type_maison(designation, duree, etat, prix, description, surface)   (select c.type_maison,c.duree_travaux,1 as etat,v.somme,c.description,c.surface as prix from travaux_maison_csv c join v_somme_travaux_typeMaison v on c.type_maison=v.type_maison left join type_maison t on t.designation=c.type_maison where t.designation is null group by c.type_maison,c.duree_travaux,v.somme,c.description,c.surface)


----Insert in devis


select concat('Devis',c.type_maison) as designation,0 as duree,1 as etat,v.somme as montant,t.id_type_maison from travaux_maison_csv c join type_maison t on c.type_maison=t.designation join v_somme_travaux_typeMaison v on t.designation=v.type_maison;

INSERT INTO devis(designation, duree_total, etat, montant_total, id_type_maison) (select concat('Devis',c.type_maison) as designationDevis,0 as duree,1 as etatM,v.somme as montant,t.id_type_maison from travaux_maison_csv c join type_maison t on c.type_maison=t.designation join v_somme_travaux_typeMaison v on t.designation=v.type_maison left join devis d on d.designation=concat('Devis',c.type_maison) where d.designation is null group by designationDevis,duree,etatM,montant,t.id_type_maison);


---Insert into unite

SELECT unite from travaux_maison_csv;


INSERT INTO unite(designation) (select c.unite from travaux_maison_csv c left join  unite u on u.designation=c.unite where u.designation is null group by c.unite);

----INSERT INTO TRAVAUX

select c.type_travaux as designationTravaux,1 as etat,c.prix_unitaire*c.quantite montantTotal,c.code_travaux codeTravaux,c.prix_unitaire,
       c.quantite,d.id,null as parent,null as typeTravail,u.id as unite
from travaux_maison_csv c join unite u on c.unite=u.designation join devis d on concat('Devis',c.type_maison)=d.designation;

insert into travaux(designation, etat, montant_total, numero, prix_unitaire, quantite, id_devis, id_unite)
    (select c.type_travaux as designationTravaux,1 as etat,c.prix_unitaire*c.quantite montantTotal,c.code_travaux codeTravaux,c.prix_unitaire,
    c.quantite,d.id,u.id as unite from travaux_maison_csv c join unite u on c.unite=u.designation join devis d on concat('Devis',c.type_maison)=d.designation
        left join travaux tr on tr.numero=code_travaux and tr.designation=c.type_travaux where tr.numero is null and tr.designation is null group by
        designationTravaux,d.etat,d.montant_total,codeTravaux,c.prix_unitaire,c.quantite,d.id,u.id);


----------------------------Insert from devis csv

----INSERT user

insert into utilisateur(contact,genre,profil) (select c.client,1 as genre,'CLIENT' as profil from construction_csv c left join utilisateur u on c.client=u.contact where u.contact is null group by c.client);

----INSERT Finition

insert into type_finition(augmentation, designation, etat) (select c.taux_finition,c.finition,1 as etat
            from construction_csv c left join type_finition t on t.designation=c.finition where t.designation is null GROUP BY c.taux_finition, c.finition);



SELECT '2024-02-03'::DATE + INTERVAL '2 days';


select c.date_devis,c.date_debut,'ha',1,c.date_debut::DATE + INTERVAL '1 day'*tp.duree fin,tp.prix,d.id,tf.id,us.id,tf.augmentation,tp.duree,0,c.ref_construction,c.lieu from construction_csv c
    join type_maison tp on tp.designation=c.type_maison
    join type_finition tf on tf.designation=c.finition
    join utilisateur us on us.contact=c.client
    join devis d on d.designation=concat('Devis',c.type_maison);

insert into construction(daty, debut, demande, etat, fin, montant_total, id_devis, id_type_finition, id_utilisateur, pourcentage_finition, duree, id_etat_construction, ref_construction, lieu)(select c.date_devis,c.date_debut,'ha',0 as etatP,c.date_debut::DATE + INTERVAL '1 day'*tp.duree as fin,tp.prix,d.id,tf.id,us.id,tf.augmentation,tp.duree,0 as idEtat,c.ref_construction,c.lieu from construction_csv c join type_maison tp on tp.designation=c.type_maison join type_finition tf on tf.designation=c.finition  join utilisateur us on us.contact=c.client join devis d on d.designation=concat('Devis',c.type_maison) left join construction cons on cons.ref_construction=c.ref_construction where cons.ref_construction is null group by c.date_devis, c.date_debut,etatP,fin, tp.prix, d.id, tf.id, us.id, tf.augmentation, tp.duree, idEtat,c.ref_construction, c.lieu);


----------Insert from csv Paiement


select *
from payement_csv;

select c.date_paiement,1 as etatP,c.montant,cons.id , c.ref_paiement
from payement_csv c
join construction cons on c.ref_construction=cons.ref_construction;


select c.date_paiement,1 as etatP,c.montant,cons.id , c.ref_paiement from payement_csv c join construction cons on c.ref_construction=cons.ref_construction left join payement p on p.ref_payement=c.ref_paiement where p.ref_payement is null group by c.date_paiement, etatP, c.montant, cons.id, c.ref_paiement;



insert into payement(daty, etat, montant, id_construction, ref_payement)(select c.date_paiement,1 as etatP,c.montant,cons.id , c.ref_paiement from payement_csv c join construction cons on c.ref_construction=cons.ref_construction left join payement p on p.ref_payement=c.ref_paiement where p.ref_payement is null group by c.date_paiement, etatP, c.montant, cons.id, c.ref_paiement);





select *
from construction_csv;

-----

select c.date_devis as dateDevis,c.date_debut as dateDebut,'ha' as demande,0 as etat,tp.prix+((tp.prix*tf.augmentation)/100) as montantTotal,d.id as idDevis,tf.id as idTypeFinition,us.id as idUser,tf.augmentation as augmentation,tp.duree as duree,0 as idEtat,c.ref_construction as refConstruction,c.lieu as lieu from construction_csv c join type_maison tp on tp.designation=c.type_maison join type_finition tf on tf.designation=c.finition  join utilisateur us on us.contact=c.client join devis d on d.designation=concat('Devis',c.type_maison) left join construction cons on cons.ref_construction=c.ref_construction where cons.ref_construction is null group by c.date_devis, c.date_debut,fin, tp.prix, d.id, tf.id, us.id, tf.augmentation, tp.duree, idEtat,c.ref_construction, c.lieu;


----
SELECT * from v_construction_complet;


SELECT refconstruction,lieu,pourcpaye,idConstruction,daty,debut,demande,etat,fin,total,dejaPaye,idDevis,idTypeFinition,pourcentageFinition,reste,idUtilisateur,designationMaison,designationFinition,contactUser,duree,designationetat from v_construction_complet where refConstruction='D001';