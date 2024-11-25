package com.example.atry;

import android.content.Context;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Compte {

    private long id;
    private final Personne personne;
    private final String alias;
    private ArrayList<Investissement> mesProjets = new ArrayList<>();
    private ArrayList<aInvestit> mesinvestissements = new ArrayList<>();
    private double solde;
    private String currency;

    private final DatabaseHelper dbHelper; // Instance de la base de données

    public Compte(Context context, Personne personne, String alias, String currency) {
        this.personne = personne;
        this.alias = alias;
        this.currency = currency;
        this.solde = 0;
        this.dbHelper = new DatabaseHelper(context); // Initialiser la base de données
    }

    public String getAlias() {
        return alias;
    }

    public double getSoldInvestit() {
        double res = 0;
        for (aInvestit elm : mesinvestissements) {
            res += elm.getSoldeInvestit();
        }
        return res;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
        dbHelper.updateCompte(getId(), alias, solde, currency, personne.getIban());
    }


    public String getCurrency() {
        return currency;
    }

    public Personne getPersonne() {
        return personne;
    }

    public List<Investissement> getMesProjets() {
        return mesProjets;
    }

    public ArrayList<aInvestit> getMesinvestissements() {
        return mesinvestissements;
    }

    public void addProjet(Investissement projet) {
        mesProjets.add(projet);
        long id = dbHelper.insertInvestissement(
                projet.getFormattedNom(),
                projet.getRendements(),
                projet.getDividendeDate(),
                projet.getDividendeRecurrence(),
                projet.getButAAtteindre(),
                projet.getSolde(),
                projet.getCreateurAlias()
        );
        projet.setId(id);
    }



    public void removeProjet(Investissement projet) {
        mesProjets.remove(projet);
        dbHelper.deleteInvestissement(projet.getId());
    }

    public void addToMesInvestissement(Investissement newInvestissement, double investit) {
        aInvestit nouveau = new aInvestit(this, newInvestissement, investit);
        mesinvestissements.add(nouveau);

        long id = dbHelper.insertAInvestit(
                getId(),
                personne.getNom(),
                personne.getPrenom(),
                newInvestissement.getId(),
                investit
        );
        nouveau.setId(id); // Définir l'ID généré
    }

    public void removeToMesInvestissement(Investissement rmInvestissement) {
        Iterator<aInvestit> iterator = mesinvestissements.iterator();
        while (iterator.hasNext()) {
            aInvestit element = iterator.next();
            if (element.getInvestissement().equals(rmInvestissement)) {
                iterator.remove();
                dbHelper.deleteAInvestit(element.getId());
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
