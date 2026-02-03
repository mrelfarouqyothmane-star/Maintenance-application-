CREATE DATABASE IF NOT EXISTS maintenance_db;
USE maintenance_db;

-- 1. Creation des tables
CREATE TABLE IF NOT EXISTS techniciens (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    qualification VARCHAR(100) NOT NULL,
    disponibilite BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS batiments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    localisation VARCHAR(200) NOT NULL
);

CREATE TABLE IF NOT EXISTS interventions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    technicien_id INT NOT NULL,
    batiment_id INT NOT NULL,
    date_intervention DATE NOT NULL,
    type_intervention VARCHAR(100) NOT NULL,
    statut ENUM('Planifiee', 'En cours', 'Terminee') DEFAULT 'Planifiee',
    description TEXT,
    FOREIGN KEY (technicien_id) REFERENCES techniciens(id) ON DELETE CASCADE,
    FOREIGN KEY (batiment_id) REFERENCES batiments(id) ON DELETE CASCADE
);

-- 2. Insertion des donnees

INSERT IGNORE INTO techniciens (id, nom, qualification, disponibilite) VALUES 
(1, 'Jean Dupont', 'Electricien', 1),
(2, 'Kevin Leroy', 'Chauffagiste', 1),
(3, 'Dani', 'Elect', 1),
(4, 'Joe', 'Ingenieur', 1),
(5, 'Marie Curie', 'Plombier', 1);

INSERT IGNORE INTO batiments (id, nom, localisation) VALUES 
(1, 'Batiment A', 'Villeneuve d Ascq'),
(2, 'Batiment B', 'Lille Centre'),
(3, 'Batiment C', 'Cite Scientifique'),
(4, 'Batiment D', 'Mons');

INSERT IGNORE INTO interventions (id, technicien_id, batiment_id, date_intervention, type_intervention, statut, description) VALUES 
(7, 1, 1, '2026-02-03', 'Reparation Panneau', 'En cours', 'Verification du circuit electrique principal');

INSERT IGNORE INTO interventions (technicien_id, batiment_id, date_intervention, type_intervention, statut, description) VALUES 
(2, 2, '2026-02-04', 'Maintenance Chaudiere', 'Planifiee', 'Entretien annuel obligatoire'),
(5, 3, '2026-01-15', 'Debouchage', 'Terminee', 'Intervention urgente suite a une fuite');
