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
SELECT con.id idConstruction,daty,debut,demande,con.etat,con.fin,con.montant_total as total,coalesce(v.dejaPaye,0) as dejaPaye,id_devis as idDevis,id_type_finition as idTypeFinition,pourcentage_finition as pourcentageFinition,coalesce(con.montant_total-v.dejaPaye,con.montant_total) as reste,id_utilisateur as idUtilisateur,
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
    INSERT INTO historique_prix_travaux(id_travaux, prix_unitaire, quantite, etat, daty)
    VALUES (NEW.id, NEW.prix_unitaire, NEW.quantite, 0, now());

    -- Mettre à jour la colonne montantTotal dans la table principale
    NEW.montant_total = NEW.prix_unitaire * NEW.quantite;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS update_montant_total_trigger ON travaux;

CREATE TRIGGER update_montant_total_trigger
    AFTER INSERT OR UPDATE OF prix_unitaire, quantite
    ON travaux
    FOR EACH ROW
EXECUTE FUNCTION update_montant_total();

-----------------TRIGGER TRAVAUX CONSTRUCTION----------------
