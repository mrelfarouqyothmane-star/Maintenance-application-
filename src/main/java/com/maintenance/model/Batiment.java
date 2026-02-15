package com.maintenance.model;

public class Batiment {
    
    private int id;              
    private String nom;          
    private String localisation; 
    
    // constructeur pour créer un nouveau bâtiment
    public Batiment(int id, String nom, String localisation) {
        this.id = id;
        this.nom = nom;
        this.localisation = localisation;
    }
    
    // getters pour lire les informations
    public int getId() { 
        return id; 
    }
    
    public String getNom() { 
        return nom; 
    }
    
    public String getLocalisation() { 
        return localisation; 
    }
    
    // setters pour modifier les informations
    public void setId(int id) { 
        this.id = id; 
    }
    
    public void setNom(String nom) { 
        this.nom = nom; 
    }
    
    public void setLocalisation(String localisation) { 
        this.localisation = localisation; 
    }
    
    // Pour afficher le nom 
    @Override
    public String toString() {
        return nom;
    }
}