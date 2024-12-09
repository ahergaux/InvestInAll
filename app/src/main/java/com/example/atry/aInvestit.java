package com.example.atry;

public class aInvestit {

    private long id;
    private final Compte investisseur;
    private final Investissement investissement;
    private double soldeInvestit;

    public aInvestit(Compte investisseur, Investissement newInvestissement, double investit) {
        this.investisseur = investisseur;
        this.investissement = newInvestissement;
        this.soldeInvestit = investit;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setSoldeInvestit(double nouveauSolde) {
        soldeInvestit = nouveauSolde;
    }

    public double getSoldeInvestit() {
        return soldeInvestit;
    }

    public Investissement getInvestissement() {
        return investissement;
    }

    public Compte getInvestisseur() {
        return investisseur;
    }
}
