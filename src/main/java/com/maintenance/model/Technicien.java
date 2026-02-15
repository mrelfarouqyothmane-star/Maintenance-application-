package com.maintenance.model;

public class Technicien {
    private int id;
    private String nom;
    private String qualification;
    private boolean disponibilite;

    public Technicien() {
    }

    public Technicien(int id, String nom, String qualification, boolean disponibilite) {
        this.id = id;
        this.nom = nom;
        this.qualification = qualification;
        this.disponibilite = disponibilite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public boolean isDisponibilite() {
        return disponibilite;
    }

    public void setDisponibilite(boolean disponibilite) {
        this.disponibilite = disponibilite;
    }

    @Override
    public String toString() {
        return nom + " (" + qualification + ")";
    }
}