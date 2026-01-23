-- Script SQL pour créer la table detail_paiement_publicite
-- Cette table stocke la répartition proportionnelle des paiements de publicité par vol instance

CREATE TABLE IF NOT EXISTS detail_paiement_publicite (
    id_detail_paiement BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_paiement_publicite BIGINT NOT NULL,
    id_vol_instance BIGINT NOT NULL,
    id_societe BIGINT NOT NULL,
    nombre_diffusions INT NOT NULL DEFAULT 0,
    montant_total_diffusions DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    montant_deja_paye DECIMAL(15, 2) DEFAULT 0.00,
    montant_paye DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    reste_a_payer DECIMAL(15, 2) DEFAULT 0.00,
    pourcentage_applique DECIMAL(10, 4) DEFAULT 0.00,
    date_creation DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_detail_paiement_paiement 
        FOREIGN KEY (id_paiement_publicite) REFERENCES paiement_publicite(id_paiement_publicite)
        ON DELETE CASCADE,
    CONSTRAINT fk_detail_paiement_vol_instance 
        FOREIGN KEY (id_vol_instance) REFERENCES vol_instance(id_vol_instance)
        ON DELETE CASCADE,
    CONSTRAINT fk_detail_paiement_societe 
        FOREIGN KEY (id_societe) REFERENCES societe(id_societe)
        ON DELETE CASCADE
);

-- Index pour améliorer les performances des requêtes
CREATE INDEX idx_detail_paiement_paiement ON detail_paiement_publicite(id_paiement_publicite);
CREATE INDEX idx_detail_paiement_vol_instance ON detail_paiement_publicite(id_vol_instance);
CREATE INDEX idx_detail_paiement_societe ON detail_paiement_publicite(id_societe);
CREATE INDEX idx_detail_paiement_societe_vol ON detail_paiement_publicite(id_societe, id_vol_instance);

-- Commentaire sur la table
-- Cette table permet de suivre la répartition des paiements de publicité par vol instance
-- Exemple d'utilisation:
-- Si une société a des diffusions sur plusieurs vols instances:
--   - vol_instance 1: 200 Ar (10 diffusions)
--   - vol_instance 2: 300 Ar (40 diffusions)
--   - vol_instance 3: 400 Ar (40 diffusions)
-- Total: 900 Ar
--
-- Si la société paie 500 Ar:
-- Pourcentage = 500/900 = 55.56%
-- La répartition sera:
--   - vol_instance 1: 200 * 55.56% = 111.11 Ar payé, reste 88.89 Ar
--   - vol_instance 2: 300 * 55.56% = 166.67 Ar payé, reste 133.33 Ar
--   - vol_instance 3: 400 * 55.56% = 222.22 Ar payé, reste 177.78 Ar
