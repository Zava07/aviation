-- =========================================
-- MODELE_AVION
-- =========================================
INSERT INTO modele_avion(nom_modele) VALUES
('A320'),
('B737'),
('B787');

-- =========================================
-- AVION
-- =========================================
INSERT INTO avion(id_modele_avion, code_avion, capacite_passagers, capacite_cargo, id_etat_avion, point_vol) VALUES
(1, 'AF1234', 180, 20.5, 1, 1),
(2, 'AF5678', 220, 25.0, 1, 2),
(3, 'AF9101', 300, 35.0, 2, 3);

-- =========================================
-- AEROPORT
-- =========================================
INSERT INTO aeroport(nom_aeroport) VALUES
('Antananarivo Ivato'),
('Paris Charles de Gaulle'),
('Johannesburg OR Tambo'),
('Istanbul Airport');

-- =========================================
-- VOL
-- =========================================
INSERT INTO vol(numero_vol, id_avion, date_depart, date_arrivee, id_status_vol, id_aeroport_origine, id_aeroport_destination, duree_prevue, duree_reelle) VALUES
('MK101', 1, '2026-01-15 08:00', '2026-01-15 12:00', 1, 1, 2, 240, 245),
('AF202', 2, '2026-01-16 09:30', '2026-01-16 18:00', 2, 2, 3, 450, NULL),
('TK303', 3, '2026-01-17 22:00', '2026-01-18 06:00', 1, 3, 4, 480, 475);

-- =========================================
-- MEMBRE_EQUIPE
-- =========================================
INSERT INTO membre_equipe(nom, prenom, id_type_membre, qualifications, heures_vol) VALUES
('Rasoa', 'Jean', 1, 'A320', 5000),
('Andriamanana', 'Lina', 2, 'B737', 3000),
('Rakoto', 'Hery', 3, 'Tous', 1500),
('Raharimanana', 'Miora', 4, 'Tous', 2000);

-- =========================================
-- VOL_EQUIPE
-- =========================================
INSERT INTO vol_equipe(id_vol, id_membre, role) VALUES
(1, 1, 'Commandant de bord'),
(1, 2, 'Copilote'),
(1, 3, 'Hôtesse'),
(1, 4, 'Steward'),
(2, 1, 'Commandant de bord'),
(2, 3, 'Hôtesse');

-- =========================================
-- PASSAGER
-- =========================================
INSERT INTO passager(nom, prenom, id_type_passager, fidelite) VALUES
('Rasolo', 'Tiana', 1, 100),
('Andria', 'Hery', 1, 50),
('Rakotomalala', 'Mialy', 2, 200);

-- =========================================
-- RESERVATION
-- =========================================
INSERT INTO reservation(id_passager, id_vol, date_reservation, id_status_reservation) VALUES
(1, 1, '2026-01-01 10:00', 1),
(2, 1, '2026-01-02 11:00', 2),
(3, 2, '2026-01-03 09:00', 1);

-- =========================================
-- CLASSE
-- =========================================
-- Les classes sont déjà insérées via ton insert précédent

-- =========================================
-- PRIX_VOL_CLASSE
-- =========================================
INSERT INTO prix_vol_classe(id_vol, id_classe, prix) VALUES
(1, 1, 1500000), -- Economie
(1, 2, 3500000), -- Business
(1, 3, 6000000), -- Première
(2, 1, 2000000),
(2, 2, 4000000),
(2, 3, 7000000);

-- =========================================
-- SIEGE
-- =========================================
INSERT INTO siege(id_avion, numero, id_classe) VALUES
(1, '1A', 3),
(1, '2B', 2),
(1, '3C', 1),
(2, '1A', 3),
(2, '2B', 2),
(2, '3C', 1);

-- =========================================
-- SIEGE_VOL
-- =========================================
INSERT INTO siege_vol(id_vol, id_siege, id_status_siege_vol, id_passager) VALUES
(1, 1, 3, 1), -- Occupé par Tiana
(1, 2, 3, 2), -- Occupé par Hery
(1, 3, 1, NULL); -- Libre

-- =========================================
-- DEVISE
-- =========================================
INSERT INTO devise(nom_devise, valeur_mga) VALUES
('MGA', 1),
('USD', 4500),
('EUR', 5000);

-- =========================================
-- PAIEMENT
-- =========================================
INSERT INTO paiement(id_reservation, montant, id_devise, id_methode_paiement, id_status_paiement, date_paiement) VALUES
(1, 1500000, 1, 1, 2, '2026-01-01 10:30'),
(2, 3500000, 1, 2, 1, '2026-01-02 11:30'),
(3, 4000000, 2, 1, 2, '2026-01-03 09:30');

-- =========================================
-- BAGAGE
-- =========================================
INSERT INTO bagage(id_passager, poids, id_type) VALUES
(1, 15.0, 1),
(2, 20.0, 2),
(3, 100.0, 3);
