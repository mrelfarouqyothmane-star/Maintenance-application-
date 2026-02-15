package com.maintenance.dao;

import com.maintenance.model.Intervention;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InterventionDAO {
    
    public List<Intervention> getAll() {
        List<Intervention> interventions = new ArrayList<>();
        String query = "SELECT * FROM interventions";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Intervention inter = new Intervention(
                    rs.getInt("id"),
                    rs.getInt("technicien_id"),
                    rs.getInt("batiment_id"),
                    rs.getDate("date_intervention").toLocalDate(),
                    rs.getString("type_intervention"),
                    rs.getString("statut"),
                    rs.getString("description")
                );
                interventions.add(inter);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return interventions;
    }
    
    public boolean add(Intervention intervention) {
        String query = "INSERT INTO interventions (technicien_id, batiment_id, date_intervention, type_intervention, statut, description) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, intervention.getTechnicienId());
            stmt.setInt(2, intervention.getBatimentId());
            stmt.setDate(3, Date.valueOf(intervention.getDateIntervention()));
            stmt.setString(4, intervention.getTypeIntervention());
            stmt.setString(5, intervention.getStatut());
            stmt.setString(6, intervention.getDescription());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    intervention.setId(rs.getInt(1));
                }
                System.out.println("Intervention ajoutée");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean update(Intervention intervention) {
        String query = "UPDATE interventions SET technicien_id = ?, batiment_id = ?, date_intervention = ?, type_intervention = ?, statut = ?, description = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, intervention.getTechnicienId());
            stmt.setInt(2, intervention.getBatimentId());
            stmt.setDate(3, Date.valueOf(intervention.getDateIntervention()));
            stmt.setString(4, intervention.getTypeIntervention());
            stmt.setString(5, intervention.getStatut());
            stmt.setString(6, intervention.getDescription());
            stmt.setInt(7, intervention.getId());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Intervention modifiée");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean delete(int id) {
        String query = "DELETE FROM interventions WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Intervention supprimée");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}