-- =========================================
-- MODELE_AVION
-- =========================================
CREATE TABLE modele_avion (
    id_modele_avion SERIAL PRIMARY KEY,
    nom_modele VARCHAR(50) NOT NULL
);

-- =========================================
-- ETAT_AVION
-- =========================================
CREATE TABLE etat_avion (
    id_etat_avion SERIAL PRIMARY KEY,
    etat VARCHAR(50) NOT NULL
);

-- Insert état_avion
INSERT INTO etat_avion (etat) VALUES 
('En service'), 
('Maintenance'), 
('Immobilise');

-- =========================================
-- AVION
-- =========================================
CREATE TABLE avion (
    id_avion SERIAL PRIMARY KEY,
    id_modele_avion INT NOT NULL REFERENCES modele_avion(id_modele_avion),
    code_avion VARCHAR(20) NOT NULL UNIQUE,
    capacite_passagers INT NOT NULL,
    capacite_cargo FLOAT NOT NULL,
    id_etat_avion INT NOT NULL REFERENCES etat_avion(id_etat_avion),
    point_vol INT
);

-- =========================================
-- HISTO_ETAT_AVION
-- =========================================
CREATE TABLE histo_etat_avion (
    id_histo_etat_avion SERIAL PRIMARY KEY,
    date_debut TIMESTAMP NOT NULL,
    date_fin TIMESTAMP,
    id_etat_avion INT NOT NULL REFERENCES etat_avion(id_etat_avion),
    id_avion INT NOT NULL REFERENCES avion(id_avion)
);

-- =========================================
-- AEROPORT
-- =========================================
CREATE TABLE aeroport (
    id_aeroport SERIAL PRIMARY KEY,
    nom_aeroport VARCHAR(100) NOT NULL
);

-- =========================================
-- STATUS_VOL
-- =========================================
CREATE TABLE status_vol (
    id_status_vol SERIAL PRIMARY KEY,
    status VARCHAR(50) NOT NULL
);

-- Insert status_vol
INSERT INTO status_vol(status) VALUES
('A l heure'),
('Retarde'),
('Annule'),
('Deroute'),
('Embarquement'),
('Cloture');

-- =========================================
-- VOL
-- =========================================
CREATE TABLE vol (
    id_vol SERIAL PRIMARY KEY,
    numero_vol VARCHAR(20) NOT NULL UNIQUE,
    id_avion INT NOT NULL REFERENCES avion(id_avion),
    date_depart TIMESTAMP NOT NULL,
    date_arrivee TIMESTAMP NOT NULL,
    id_status_vol INT NOT NULL REFERENCES status_vol(id_status_vol),
    id_aeroport_origine INT NOT NULL REFERENCES aeroport(id_aeroport),
    id_aeroport_destination INT NOT NULL REFERENCES aeroport(id_aeroport),
    duree_prevue INT,
    duree_reelle INT
);

-- =========================================
-- TYPE_MEMBRE
-- =========================================
CREATE TABLE type_membre (
    id_type_membre SERIAL PRIMARY KEY,
    type VARCHAR(50) NOT NULL
);

-- Insert type_membre
INSERT INTO type_membre(type) VALUES
('Pilote'),
('Copilote'),
('Hôtesse'),
('Steward');

-- =========================================
-- MEMBRE_EQUIPE
-- =========================================
CREATE TABLE membre_equipe (
    id_membre SERIAL PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    id_type_membre INT NOT NULL REFERENCES type_membre(id_type_membre),
    qualifications VARCHAR(100),
    heures_vol INT
);

-- =========================================
-- VOL_EQUIPE
-- =========================================
CREATE TABLE vol_equipe (
    id_vol INT NOT NULL REFERENCES vol(id_vol),
    id_membre INT NOT NULL REFERENCES membre_equipe(id_membre),
    role VARCHAR(50),
    PRIMARY KEY (id_vol, id_membre)
);

-- =========================================
-- TYPE_PASSAGER
-- =========================================
CREATE TABLE type_passager (
    id_type_passager SERIAL PRIMARY KEY,
    type VARCHAR(50) NOT NULL
);

-- Insert type_passager
INSERT INTO type_passager(type) VALUES
('Individuel'),
('Entreprise');

-- =========================================
-- PASSAGER
-- =========================================
CREATE TABLE passager (
    id_passager SERIAL PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    id_type_passager INT NOT NULL REFERENCES type_passager(id_type_passager),
    fidelite INT DEFAULT 0
);

-- =========================================
-- STATUT_RESERVATION
-- =========================================
CREATE TABLE statut_reservation (
    id_status_reservation SERIAL PRIMARY KEY,
    nom_status VARCHAR(50) NOT NULL
);

-- Insert statut_reservation
INSERT INTO statut_reservation(nom_status) VALUES
('Confirme'),
('En attente'),
('Annule');

-- =========================================
-- RESERVATION
-- =========================================
CREATE TABLE reservation (
    id_reservation SERIAL PRIMARY KEY,
    id_passager INT NOT NULL REFERENCES passager(id_passager),
    id_vol INT NOT NULL REFERENCES vol(id_vol),
    date_reservation TIMESTAMP NOT NULL DEFAULT NOW(),
    id_status_reservation INT NOT NULL REFERENCES statut_reservation(id_status_reservation)
);

-- =========================================
-- HISTO_RESERVATION
-- =========================================
CREATE TABLE histo_reservation (
    id_histo_reservation SERIAL PRIMARY KEY,
    date TIMESTAMP NOT NULL DEFAULT NOW(),
    id_status_reservation INT NOT NULL REFERENCES statut_reservation(id_status_reservation),
    id_reservation INT NOT NULL REFERENCES reservation(id_reservation)
);

-- =========================================
-- CLASSE
-- =========================================
CREATE TABLE classe (
    id_classe SERIAL PRIMARY KEY,
    nom_classe VARCHAR(50) NOT NULL,
    niveau INT
);

-- Insert classe
INSERT INTO classe(nom_classe, niveau) VALUES
('Economie', 5),
('Business', 10),
('Première', 15);

-- =========================================
-- PRIX_VOL_CLASSE
-- =========================================
CREATE TABLE prix_vol_classe (
    id_prix SERIAL PRIMARY KEY,
    id_vol INT NOT NULL REFERENCES vol(id_vol),
    id_classe INT NOT NULL REFERENCES classe(id_classe),
    prix NUMERIC(12,2) NOT NULL
);

-- =========================================
-- SIEGE
-- =========================================
CREATE TABLE siege (
    id_siege SERIAL PRIMARY KEY,
    id_avion INT NOT NULL REFERENCES avion(id_avion),
    numero VARCHAR(10) NOT NULL,
    id_classe INT NOT NULL REFERENCES classe(id_classe)
);

-- =========================================
-- STATUS_SIEGE_VOL
-- =========================================
CREATE TABLE status_siege_vol (
    id_status_siege_vol SERIAL PRIMARY KEY,
    status VARCHAR(50) NOT NULL
);

-- Insert status_siege_vol
INSERT INTO status_siege_vol(status) VALUES
('Libre'),
('Reserve'),
('Occupe'),
('Bloque');

-- =========================================
-- SIEGE_VOL
-- =========================================
CREATE TABLE siege_vol (
    id_vol INT NOT NULL REFERENCES vol(id_vol),
    id_siege INT NOT NULL REFERENCES siege(id_siege),
    id_status_siege_vol INT NOT NULL REFERENCES status_siege_vol(id_status_siege_vol),
    id_passager INT REFERENCES passager(id_passager),
    PRIMARY KEY (id_vol, id_siege)
);

-- =========================================
-- STATUS_BILLET
-- =========================================
CREATE TABLE status_billet (
    id_status_billet SERIAL PRIMARY KEY,
    status VARCHAR(50) NOT NULL
);

-- Insert status_billet
INSERT INTO status_billet(status) VALUES
('Emis'),
('Annule'),
('Utilise'),
('Rembourse');
-- =========================================
-- BILLET
-- =========================================
CREATE TABLE billet (
    id_billet SERIAL PRIMARY KEY,
    id_reservation INT NOT NULL REFERENCES reservation(id_reservation),
    id_siege INT NOT NULL REFERENCES siege(id_siege),
    numero_billet VARCHAR(50) NOT NULL UNIQUE,
    date_emission TIMESTAMP NOT NULL DEFAULT NOW(),
    id_status_billet INT NOT NULL REFERENCES status_billet(id_status_billet)
);

-- =========================================
-- DEVISE
-- =========================================
CREATE TABLE devise (
    id_devise SERIAL PRIMARY KEY,
    nom_devise VARCHAR(20) NOT NULL,
    valeur_mga NUMERIC(12,2)
);

-- =========================================
-- METHODE_PAIEMENT
-- =========================================
CREATE TABLE methode_paiement (
    id_methode_paiement SERIAL PRIMARY KEY,
    nom_methode VARCHAR(50) NOT NULL
);

-- Insert methode_paiement
INSERT INTO methode_paiement(nom_methode) VALUES
('Carte'),
('Mobile money'),
('Espece');

-- =========================================
-- STATUS_PAIEMENT
-- =========================================
CREATE TABLE status_paiement (
    id_status_paiement SERIAL PRIMARY KEY,
    status VARCHAR(50) NOT NULL
);

-- Insert status_paiement
INSERT INTO status_paiement(status) VALUES
('En attente'),
('Valide'),
('Echoue'),
('Rembourse');

-- =========================================
-- PAIEMENT
-- =========================================
CREATE TABLE paiement (
    id_paiement SERIAL PRIMARY KEY,
    id_reservation INT NOT NULL REFERENCES reservation(id_reservation),
    montant NUMERIC(12,2) NOT NULL,
    id_devise INT NOT NULL REFERENCES devise(id_devise),
    id_methode_paiement INT NOT NULL REFERENCES methode_paiement(id_methode_paiement),
    id_status_paiement INT NOT NULL REFERENCES status_paiement(id_status_paiement),
    date_paiement TIMESTAMP NOT NULL DEFAULT NOW()
);

-- =========================================
-- TYPE_BAGAGE
-- =========================================
CREATE TABLE type_bagage (
    id_type_bagage SERIAL PRIMARY KEY,
    type VARCHAR(50) NOT NULL
);

-- Insert type_bagage
INSERT INTO type_bagage(type) VALUES
('Main'),
('Souterrain'),
('Cargo');

-- =========================================
-- BAGAGE
-- =========================================
CREATE TABLE bagage (
    id_bagage SERIAL PRIMARY KEY,
    id_passager INT NOT NULL REFERENCES passager(id_passager),
    poids NUMERIC(6,2) NOT NULL,
    id_type INT NOT NULL REFERENCES type_bagage(id_type_bagage)
);
