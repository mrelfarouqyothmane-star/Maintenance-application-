-- Création de la base de données
CREATE DATABASE IF NOT EXISTS maintenance_db;
USE maintenance_db;

-- Table des techniciens
CREATE TABLE IF NOT EXISTS techniciens (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    qualification VARCHAR(100) NOT NULL,
    disponibilite BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table des bâtiments
CREATE TABLE IF NOT EXISTS batiments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    localisation VARCHAR(200) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table des interventions
CREATE TABLE IF NOT EXISTS interventions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    technicien_id INT NOT NULL,
    batiment_id INT NOT NULL,
    date_intervention DATE NOT NULL,
    type_intervention VARCHAR(100) NOT NULL,
    statut ENUM('Planifiée', 'En cours', 'Terminée') DEFAULT 'Planifiée',
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (technicien_id) REFERENCES techniciens(id) ON DELETE CASCADE,
    FOREIGN KEY (batiment_id) REFERENCES batiments(id) ON DELETE CASCADE
);