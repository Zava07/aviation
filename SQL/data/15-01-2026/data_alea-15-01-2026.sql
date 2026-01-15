-- =====================================================
-- MODELES AVION
-- =====================================================
INSERT INTO modele_avion (nom_modele) VALUES
('Boeing 737'),
('Airbus A320'),
('ATR 72');



-- =====================================================
-- AEROPORTS
-- =====================================================
INSERT INTO aeroport (nom_aeroport) VALUES
('Antananarivo Ivato'),
('Toamasina'),
('Nosy Be'),
('Mahajanga');

-- =====================================================
-- MEMBRES D'EQUIPE
-- =====================================================
INSERT INTO membre_equipe (nom, prenom, id_type_membre, qualifications, heures_vol) VALUES
('Rakoto', 'Jean', 1, 'B737', 4500),
('Rabe', 'Paul', 2, 'B737', 3200),
('Andry', 'Sara', 3, 'Cabine', 2100),
('Hery', 'Liva', 4, 'Cabine', 1800);

-- =====================================================
-- DEVISES
-- =====================================================
INSERT INTO devise (nom_devise, valeur_mga) VALUES
('MGA', 1),
('EUR', 4800),
('USD', 4200);



