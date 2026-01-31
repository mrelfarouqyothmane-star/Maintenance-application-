package com.maintenance.model;

import java.time.LocalDate;

public class Intervention {
    private int id;
    private int technicienId;
    private int batimentId;
    private LocalDate dateIntervention;
    private String typeIntervention;
    private String statut; 
    private String description;
    
    private String technicienNom;
    private String batimentNom;

    public Intervention() {
    }

    public Intervention(int id, int technicienId, int batimentId, 
                        LocalDate dateIntervention, String typeIntervention, 
                        String statut, String description) {
        this.id = id;
        this.technicienId = technicienId;
        this.batimentId = batimentId;
        this.dateIntervention = dateIntervention;
        this.typeIntervention = typeIntervention;
        this.statut = statut;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTechnicienId() {
        return technicienId;
    }

    public void setTechnicienId(int technicienId) {
        this.technicienId = technicienId;
    }

    public int getBatimentId() {
        return batimentId;
    }

    public void setBatimentId(int batimentId) {
        this.batimentId = batimentId;
    }

    public LocalDate getDateIntervention() {
        return dateIntervention;
    }

    public void setDateIntervention(LocalDate dateIntervention) {
        this.dateIntervention = dateIntervention;
    }

    public String getTypeIntervention() {
        return typeIntervention;
    }

    public void setTypeIntervention(String typeIntervention) {
        this.typeIntervention = typeIntervention;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTechnicienNom() {
        return technicienNom;
    }

    public void setTechnicienNom(String technicienNom) {
        this.technicienNom = technicienNom;
    }

    public String getBatimentNom() {
        return batimentNom;
    }

    public void setBatimentNom(String batimentNom) {
        this.batimentNom = batimentNom;
    }

    @Override
    public String toString() {
        return "Intervention #" + id + " - " + typeIntervention + " (" + statut + ")";
    }
}