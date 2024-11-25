package com.example.atry;

import android.content.Context;

import java.util.ArrayList;

public class Investissement {

    private long id;
    // Nouveau code
    private final String createurAlias;
    private final ArrayList<aInvestit> investisseurs = new ArrayList<>();
    private final String nom;
    private final double rendements;
    private final int dividendeDate;
    private final int dividendeRecurrence;
    private final double butAAtteindre;
    private double solde;

    private final DatabaseHelper dbHelper;

    public Investissement(Context context, String createurAlias, String nom, double rendements, int dividendeDate, int dividendeRecurrence, double butAAtteindre) {
        this.createurAlias = createurAlias;
        this.nom = nom;
        this.rendements = rendements;
        this.dividendeDate = dividendeDate;
        this.dividendeRecurrence = dividendeRecurrence;
        this.butAAtteindre = butAAtteindre;
        this.dbHelper = new DatabaseHelper(context);
    }

    public double getRendements() {
        return rendements;
    }

    public int getDividendeRecurrence() {
        return dividendeRecurrence;
    }

    public int getDividendeDate() {
        return dividendeDate;
    }
    public String getCreateurAlias() {
        return createurAlias;
    }

    public double getButAAtteindre() {
        return butAAtteindre;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
        dbHelper.updateInvestissement(getId(), nom, rendements, dividendeDate, dividendeRecurrence, butAAtteindre, solde);
    }

    public String getFormattedNom() {
        return nom;
    }

    public String getFormattedCreateur(){
        return createurAlias;
    }

    public String getFormattedSolde(){
        return  ""+solde;
    }
    public void addInvestisseurs(Compte compte, double soldeInvestit) {
        this.setSolde(this.getSolde() + soldeInvestit);
        aInvestit nouvelInvest = new aInvestit(compte, this, soldeInvestit);
        long id = dbHelper.insertAInvestit(
                compte.getId(),
                compte.getPersonne().getNom(),
                compte.getPersonne().getPrenom(),
                getId(), // L'ID de l'investissement
                soldeInvestit
        );
        nouvelInvest.setId(id); // Définir l'ID généré dans l'objet
        investisseurs.add(nouvelInvest);
        compte.getMesinvestissements().add(nouvelInvest);
        dbHelper.updateInvestissement(this.getId(), this.getFormattedNom(), this.getRendements(), this.getDividendeDate(), this.getDividendeRecurrence(), this.getButAAtteindre(), this.getSolde());

    }


    public void rmInvestisseurs(Compte compte) {
        for (aInvestit element : investisseurs) {
            if (element.getInvestisseur().equals(compte)) {
                solde -= element.getSoldeInvestit();
                investisseurs.remove(element);
                dbHelper.deleteAInvestit(element.getId()); // Utiliser getId()
                break;
            }
        }
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
