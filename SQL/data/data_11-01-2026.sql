-- =====================================================
-- MODELES AVION
-- =====================================================
INSERT INTO modele_avion (nom_modele) VALUES
('Boeing 737'),
('Airbus A320'),
('ATR 72');

-- =====================================================
-- AVIONS
-- =====================================================
INSERT INTO avion (id_modele_avion, code_avion, capacite_passagers, capacite_cargo, id_etat_avion, point_vol) VALUES
(1, 'B737-MD01', 180, 2000, 1, 120),
(2, 'A320-MD02', 160, 1800, 1, 100),
(3, 'ATR72-MD03', 70, 900, 2, 50);

-- =====================================================
-- AEROPORTS
-- =====================================================
INSERT INTO aeroport (nom_aeroport) VALUES
('Antananarivo Ivato'),
('Toamasina'),
('Nosy Be'),
('Mahajanga');

-- =====================================================
-- VOLS (ROUTES)
-- =====================================================
INSERT INTO vol (numero_vol, id_aeroport_origine, id_aeroport_destination) VALUES
('MD101', 1, 2),
('MD102', 1, 3),
('MD103', 1, 4);

-- =====================================================
-- INSTANCES DE VOL
-- =====================================================
INSERT INTO vol_instance (id_vol, id_avion, date_depart, date_arrivee, duree_prevue, duree_reelle, id_status_vol) VALUES
(1, 1, '2026-01-10 08:00', '2026-01-10 09:00', 60, 65, 2),
(1, 1, '2026-01-11 08:00', '2026-01-11 09:00', 60, 60, 1),
(2, 2, '2026-01-10 10:00', '2026-01-10 11:30', 90, 95, 1),
(3, 3, '2026-01-10 14:00', '2026-01-10 15:45', 105, 110, 5);

-- =====================================================
-- MEMBRES D'EQUIPE
-- =====================================================
INSERT INTO membre_equipe (nom, prenom, id_type_membre, qualifications, heures_vol) VALUES
('Rakoto', 'Jean', 1, 'B737', 4500),
('Rabe', 'Paul', 2, 'B737', 3200),
('Andry', 'Sara', 3, 'Cabine', 2100),
('Hery', 'Liva', 4, 'Cabine', 1800);

-- =====================================================
-- AFFECTATION EQUIPE
-- =====================================================
INSERT INTO vol_equipe (id_vol_instance, id_membre, role) VALUES
(1, 1, 'Commandant'),
(1, 2, 'Copilote'),
(1, 3, 'Chef cabine'),
(1, 4, 'Cabine');

-- =====================================================
-- PASSAGERS
-- =====================================================
INSERT INTO passager (nom, prenom, email, id_type_passager, fidelite) VALUES
('Dupont', 'Alice', 'alice@mail.com', 1, 120),
('Martin', 'Bob', 'bob@mail.com', 1, 60),
('TELMA', 'SARL', 'contact@telma.mg', 2, 500);


-- =====================================================
-- PRIX PAR CLASSE ET VOL
-- =====================================================
INSERT INTO prix_vol_classe (id_vol, id_classe, prix) VALUES
(1, 1, 150000),
(1, 2, 350000),
(1, 3, 700000),
(2, 1, 200000),
(2, 2, 450000),
(3, 1, 180000);




-- =====================================================
-- SIEGES
-- =====================================================
INSERT INTO siege (id_avion, numero, id_classe) VALUES
(1, '1A', 3),
(1, '2A', 2),
(1, '10C', 1),
(2, '3B', 2),
(2, '12D', 1),
(3, '5A', 1);

-- =====================================================
-- SIEGES PAR VOL
-- =====================================================
INSERT INTO siege_vol (id_vol_instance, id_siege, id_status_siege_vol, id_passager) VALUES
(1, 1, 3, 1),
(1, 2, 3, 2),
(1, 3, 1, NULL),
(3, 4, 2, 3);

-- =====================================================
-- DEVISES
-- =====================================================
INSERT INTO devise (nom_devise, valeur_mga) VALUES
('MGA', 1),
('EUR', 4800),
('USD', 4200);


-- =====================================================
-- BAGAGES
-- =====================================================
INSERT INTO bagage (id_passager, poids, id_type) VALUES
(1, 7.5, 1),
(1, 18.0, 2),
(2, 10.0, 1),
(3, 50.0, 3);

