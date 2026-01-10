INSERT INTO etat_avion (etat) VALUES 
('En service'), 
('Maintenance'), 
('Immobilise');


INSERT INTO status_vol(status) VALUES
('A l heure'),
('Retarde'),
('Annule'),
('Deroute'),
('Embarquement'),
('Cloture');

INSERT INTO type_membre(type) VALUES
('Pilote'),
('Copilote'),
('Hôtesse'),
('Steward');

INSERT INTO type_passager(type) VALUES
('Individuel'),
('Entreprise');

INSERT INTO statut_reservation(nom_status) VALUES
('Confirme'),
('En attente'),
('Annule');

INSERT INTO classe(nom_classe, niveau) VALUES
('Economie', 5),
('Business', 10),
('Première', 15);


INSERT INTO status_siege_vol(status) VALUES
('Libre'),
('Reserve'),
('Occupe'),
('Bloque');

INSERT INTO status_billet(status) VALUES
('Payer et Emis'),
('Annule'),
('Utilise'),
('Rembourse'),
('Reserver')

INSERT INTO methode_paiement(nom_methode) VALUES
('Carte'),
('Mobile money'),
('Espece');

INSERT INTO status_paiement(status) VALUES
('En attente'),
('Valide'),
('Echoue'),
('Rembourse');

INSERT INTO type_bagage(type) VALUES
('Main'),
('Souterrain'),
('Cargo');
