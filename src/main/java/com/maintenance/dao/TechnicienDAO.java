package com.maintenance.dao;

import com.maintenance.model.Technicien;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TechnicienDAO {

    public List<Technicien> getAll() {
        List<Technicien> techniciens = new ArrayList<>();
        String query = "SELECT * FROM techniciens";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Technicien tech = new Technicien(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("qualification"),
                    rs.getBoolean("disponibilite")
                );
                techniciens.add(tech);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return techniciens;
    }
    

    public Technicien getById(int id) {
        String query = "SELECT * FROM techniciens WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Technicien(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("qualification"),
                    rs.getBoolean("disponibilite")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    

    public boolean add(Technicien technicien) {
        String query = "INSERT INTO techniciens (nom, qualification, disponibilite) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, technicien.getNom());
            stmt.setString(2, technicien.getQualification());
            stmt.setBoolean(3, technicien.isDisponibilite());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    technicien.setId(rs.getInt(1));
                }
                System.out.println("Technicien ajouté : " + technicien.getNom());
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Technicien technicien) {
        String query = "UPDATE techniciens SET nom = ?, qualification = ?, disponibilite = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, technicien.getNom());
            stmt.setString(2, technicien.getQualification());
            stmt.setBoolean(3, technicien.isDisponibilite());
            stmt.setInt(4, technicien.getId());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Technicien modifié : " + technicien.getNom());
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id) {
        String query = "DELETE FROM techniciens WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Technicien supprimé (ID: " + id + ")");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}