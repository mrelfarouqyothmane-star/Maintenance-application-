CREATE DATABASE IF NOT EXISTS maintenance_db;
USE maintenance_db;

-- 1. Structure des tables
CREATE TABLE IF NOT EXISTS techniciens (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    qualification VARCHAR(100) NOT NULL,
    disponibilite BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS batiments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    -- Ajout de UNIQUE pour éviter les répétitions de noms
    nom VARCHAR(100) NOT NULL UNIQUE, 
    localisation VARCHAR(200) NOT NULL
);

CREATE TABLE IF NOT EXISTS interventions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    technicien_id INT NOT NULL,
    batiment_id INT NOT NULL,
    date_intervention DATE NOT NULL,
    type_intervention VARCHAR(100) NOT NULL,
    -- Harmonisation avec accents pour votre interface Java
    statut ENUM('Planifiée', 'En cours', 'Terminée') DEFAULT 'Planifiée',
    description TEXT,
    FOREIGN KEY (technicien_id) REFERENCES techniciens(id) ON DELETE CASCADE,
    FOREIGN KEY (batiment_id) REFERENCES batiments(id) ON DELETE CASCADE
);

-- 2. Insertion des données (INSERT IGNORE évite les erreurs de doublons)
-- Les techniciens
INSERT IGNORE INTO techniciens (id, nom, qualification, disponibilite) VALUES 
(1, 'Jean Dupont', 'Électricien', 1),
(2, 'Kevin Leroy', 'Chauffagiste', 1),
(3, 'Dani', 'Électromécanicien', 1),
(5, 'Marie Curie', 'Plombière', 1);

-- Les bâtiments (Grâce à UNIQUE et IGNORE, "Siège Social" ne sera créé qu'une fois)
INSERT IGNORE INTO batiments (id, nom, localisation) VALUES 
(1, 'Siège Social', 'Lille Centre'),
(2, 'Centre de Recherche', 'Villeneuve d''Ascq'),
(3, 'Entrepôt Logistique', 'Lesquin');

-- Les interventions
INSERT IGNORE INTO interventions (id, technicien_id, batiment_id, date_intervention, type_intervention, statut, description) VALUES 
(1, 1, 1, '2026-02-03', 'Réparation Panneau', 'En cours', 'Vérification du circuit électrique principal'),
(2, 2, 2, '2026-02-04', 'Maintenance Chaudière', 'Planifiée', 'Entretien annuel obligatoire'),
(3, 5, 3, '2026-01-15', 'Débouchage', 'Terminée', 'Intervention urgente suite à une fuite');