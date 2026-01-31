package com.maintenance.dao;

import com.maintenance.model.Batiment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BatimentDAO {
    
    public List<Batiment> getAll() {
        List<Batiment> batiments = new ArrayList<>();
        String query = "SELECT * FROM batiments";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Batiment bat = new Batiment(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("localisation")
                );
                batiments.add(bat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return batiments;
    }
    
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
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean add(Batiment batiment) {
        String query = "INSERT INTO batiments (nom, localisation) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, batiment.getNom());
            stmt.setString(2, batiment.getLocalisation());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    batiment.setId(rs.getInt(1));
                }
                System.out.println("Bâtiment ajouté : " + batiment.getNom());
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean update(Batiment batiment) {
        String query = "UPDATE batiments SET nom = ?, localisation = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, batiment.getNom());
            stmt.setString(2, batiment.getLocalisation());
            stmt.setInt(3, batiment.getId());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Bâtiment modifié : " + batiment.getNom());
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean delete(int id) {
        String query = "DELETE FROM batiments WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Bâtiment supprimé (ID: " + id + ")");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}