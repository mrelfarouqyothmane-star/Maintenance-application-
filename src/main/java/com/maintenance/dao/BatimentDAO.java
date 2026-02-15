package com.maintenance.dao;

import com.maintenance.model.Batiment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// DAO : Data Access Object (Objet d'Accès aux Données)
// pour faire le lien avec la base de données
public class BatimentDAO {
    
    // lire tous les bâtiments de la base de données
    public List<Batiment> getAll() {
        List<Batiment> batiments = new ArrayList<>();
        String query = "SELECT * FROM batiments"; // on va utiliser une requete SQL
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            // parcourir tous les résultats
            while (rs.next()) {
                Batiment bat = new Batiment(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("localisation")
                );
                batiments.add(bat);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la lecture : " + e.getMessage()); // gestion d'erreurs
        }
        return batiments;
    }
    
    // pour ajouter un nouveau batiment 
    public boolean add(Batiment batiment) {
        String query = "INSERT INTO batiments (nom, localisation) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, batiment.getNom());
            stmt.setString(2, batiment.getLocalisation());
            
            int lignesAjoutees = stmt.executeUpdate();
            return lignesAjoutees > 0; 
            
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout : " + e.getMessage());
            return false;
        }
    }
    
    // pour modifier un batiment existant 
    public boolean update(Batiment batiment) {
        String query = "UPDATE batiments SET nom = ?, localisation = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, batiment.getNom());
            stmt.setString(2, batiment.getLocalisation());
            stmt.setInt(3, batiment.getId());
            
            int lignesModifiees = stmt.executeUpdate();
            return lignesModifiees > 0;
            
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification : " + e.getMessage());
            return false;
        }
    }
    
    // pour supprimer un batiment 
    public boolean delete(int id) {
        String query = "DELETE FROM batiments WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            
            int lignesSupprimees = stmt.executeUpdate();
            return lignesSupprimees > 0;
            
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression : " + e.getMessage());
            return false;
        }
    }
    
    // pour lire un batiment par son id
    public Batiment getById(int id) {
        String query = "SELECT * FROM batiments WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Batiment(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("localisation")
                );
            }
        } catch (SQLException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
        return null;
    }
}