package com.example.atry;

import android.content.Context;

public class CompteManager {
    private static Compte instance;

    public static void initCompte(Context context, Personne personne, String alias, String currency) {
        if (instance == null) {
            instance = new Compte(context,personne, alias, currency);
        }
    }

    public static Compte getCompte() {
        if (instance == null) {
            throw new IllegalStateException("Compte non initialisé. Appelez initCompte() d'abord.");
        }
        return instance;
    }
}
