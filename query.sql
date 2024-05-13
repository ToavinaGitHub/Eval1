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

CREATE OR REPLACE VIEW v_construction_complet as
SELECT con.id idConstruction,daty,debut,demande,con.etat,con.fin,con.montant_total as total,v.dejaPaye as dejaPaye,id_devis as idDevis,id_type_finition as idTypeFinition,pourcentage_finition as pourcentageFinition,con.montant_total-v.dejaPaye as reste,id_utilisateur as idUtilisateur,
       type_maison.designation designationMaison,con.duree as duree,tf.designation designationFinition,u.contact contactUser
from construction con
    join v_montant_paye_construction v
    on con.id=v.id_construction
join type_finition tf on tf.id=con.id_type_finition
join devis d on d.id=con.id_devis
join type_maison on d.id_type_maison=type_maison.id_type_maison
join utilisateur u on con.id_utilisateur = u.id;

SELECT idConstruction,daty,debut,demande,etat,fin,total,dejaPaye,idDevis,idTypeFinition,pourcentageFinition,reste,idUtilisateur,designationMaison,designationFinition,contactUser,duree from v_construction_complet;


SELECT
    sum(montant_total) as Somme
FROM construction;