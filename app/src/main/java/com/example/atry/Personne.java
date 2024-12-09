package com.example.atry;

public class Personne {

    private final String nom;
    private final String prenom;
    private final long dateNaissance;
    private final String nationalite;
    private String iban;

    public Personne(String nom, String prenom, long dateNaissance, String nationalite) {
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.nationalite = nationalite;
    }


    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public long getDateNaissance() {
        return dateNaissance;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getNationalite() {
        return nationalite;
    }
}
