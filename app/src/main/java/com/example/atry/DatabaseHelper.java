package com.example.atry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Nom de la base de données
    private static final String DATABASE_NAME = "investment.db";

    // Version de la base de données
    private static final int DATABASE_VERSION = 4;

    // Table Compte
    private static final String TABLE_COMPTE = "Compte";
    private static final String COLUMN_COMPTE_ID = "id";
    private static final String COLUMN_COMPTE_ALIAS = "alias";
    private static final String COLUMN_COMPTE_SOLDE = "solde";
    private static final String COLUMN_COMPTE_CURRENCY = "currency";
    private static final String COLUMN_COMPTE_IBAN = "iban";
    private static final String COLUMN_COMPTE_NOM = "nom";
    private static final String COLUMN_COMPTE_PRENOM = "prenom";
    private static final String COLUMN_COMPTE_DATE_NAISSANCE = "date_naissance";
    private static final String COLUMN_COMPTE_NATIONALITE = "nationalite";

    // Table Investissement
    private static final String TABLE_INVESTISSEMENT = "Investissement";
    private static final String COLUMN_INVESTISSEMENT_ID = "id";
    private static final String COLUMN_INVESTISSEMENT_NOM = "nom";
    private static final String COLUMN_INVESTISSEMENT_RENDEMENT = "rendement";
    private static final String COLUMN_INVESTISSEMENT_DIVIDENDE_DATE = "dividendeDate";
    private static final String COLUMN_INVESTISSEMENT_DIVIDENDE_RECURRENCE = "dividendeRecurrence";
    private static final String COLUMN_INVESTISSEMENT_BUT = "butAAtteindre";
    private static final String COLUMN_INVESTISSEMENT_SOLDE = "solde";
    private static final String COLUMN_INVESTISSEMENT_CREATEUR_ALIAS = "createur_alias";

    // Table aInvestit
    private static final String TABLE_AINVESTIT = "aInvestit";
    private static final String COLUMN_AINVESTIT_ID = "id";
    private static final String COLUMN_AINVESTIT_COMPTE_ID = "compte_id";
    private static final String COLUMN_AINVESTIT_NOM = "investisseur_nom";
    private static final String COLUMN_AINVESTIT_PRENOM = "investisseur_prenom";
    private static final String COLUMN_AINVESTIT_INVESTISSEMENT_ID = "investissement_id";
    private static final String COLUMN_AINVESTIT_MONTANT = "montant_investit";

    // Constructeur
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Créer la table Compte
        String createCompteTable = "CREATE TABLE " + TABLE_COMPTE + " (" +
                COLUMN_COMPTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_COMPTE_ALIAS + " TEXT, " +
                COLUMN_COMPTE_SOLDE + " DOUBLE, " +
                COLUMN_COMPTE_CURRENCY + " TEXT, " +
                COLUMN_COMPTE_IBAN + " TEXT, " +
                COLUMN_COMPTE_NOM + " TEXT, " +
                COLUMN_COMPTE_PRENOM + " TEXT, " +
                COLUMN_COMPTE_DATE_NAISSANCE + " INTEGER, " +
                COLUMN_COMPTE_NATIONALITE + " TEXT" +
                ")";
        db.execSQL(createCompteTable);

        // Créer la table Investissement
        String createInvestissementTable = "CREATE TABLE " + TABLE_INVESTISSEMENT + " (" +
                COLUMN_INVESTISSEMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_INVESTISSEMENT_NOM + " TEXT, " +
                COLUMN_INVESTISSEMENT_RENDEMENT + " DOUBLE, " +
                COLUMN_INVESTISSEMENT_DIVIDENDE_DATE + " INTEGER, " +
                COLUMN_INVESTISSEMENT_DIVIDENDE_RECURRENCE + " INTEGER, " +
                COLUMN_INVESTISSEMENT_BUT + " DOUBLE, " +
                COLUMN_INVESTISSEMENT_SOLDE + " DOUBLE, " +
                COLUMN_INVESTISSEMENT_CREATEUR_ALIAS + " TEXT" +
                ")";
        db.execSQL(createInvestissementTable);

        // Créer la table aInvestit
        String createAInvestitTable = "CREATE TABLE " + TABLE_AINVESTIT + " (" +
                COLUMN_AINVESTIT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_AINVESTIT_COMPTE_ID + " INTEGER, " +
                COLUMN_AINVESTIT_NOM + " TEXT, " +
                COLUMN_AINVESTIT_PRENOM + " TEXT, " +
                COLUMN_AINVESTIT_INVESTISSEMENT_ID + " INTEGER, " +
                COLUMN_AINVESTIT_MONTANT + " DOUBLE, " +
                "FOREIGN KEY(" + COLUMN_AINVESTIT_COMPTE_ID + ") REFERENCES " + TABLE_COMPTE + "(" + COLUMN_COMPTE_ID + "), " +
                "FOREIGN KEY(" + COLUMN_AINVESTIT_INVESTISSEMENT_ID + ") REFERENCES " + TABLE_INVESTISSEMENT + "(" + COLUMN_INVESTISSEMENT_ID + ")" +
                ")";
        db.execSQL(createAInvestitTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AINVESTIT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVESTISSEMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPTE);
        onCreate(db);
    }

    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_COMPTE);
        db.execSQL("DELETE FROM " + TABLE_INVESTISSEMENT);
        db.execSQL("DELETE FROM " + TABLE_AINVESTIT);
        db.close();
    }


    // Mettre à jour un compte
    public int updateCompte(long id, String alias, double solde, String currency, String iban) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COMPTE_ALIAS, alias);
        values.put(COLUMN_COMPTE_SOLDE, solde);
        values.put(COLUMN_COMPTE_CURRENCY, currency);
        values.put(COLUMN_COMPTE_IBAN, iban);
        return db.update(TABLE_COMPTE, values, COLUMN_COMPTE_ID + "=?", new String[]{String.valueOf(id)});
    }

    // Mettre à jour un investissement
    public int updateInvestissement(long id, String nom, double rendement, int dividendeDate, int dividendeRecurrence, double but, double solde) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_INVESTISSEMENT_NOM, nom);
        values.put(COLUMN_INVESTISSEMENT_RENDEMENT, rendement);
        values.put(COLUMN_INVESTISSEMENT_DIVIDENDE_DATE, dividendeDate);
        values.put(COLUMN_INVESTISSEMENT_DIVIDENDE_RECURRENCE, dividendeRecurrence);
        values.put(COLUMN_INVESTISSEMENT_BUT, but);
        values.put(COLUMN_INVESTISSEMENT_SOLDE, solde);
        return db.update(TABLE_INVESTISSEMENT, values, COLUMN_INVESTISSEMENT_ID + "=?", new String[]{String.valueOf(id)});
    }

    // Mettre à jour un investissement effectué par un investisseur
    public int updateAInvestit(long id, String nom, String prenom, double montant) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AINVESTIT_NOM, nom);
        values.put(COLUMN_AINVESTIT_PRENOM, prenom);
        values.put(COLUMN_AINVESTIT_MONTANT, montant);
        return db.update(TABLE_AINVESTIT, values, COLUMN_AINVESTIT_ID + "=?", new String[]{String.valueOf(id)});
    }

    // Récupérer un compte
    public Compte getCompte(Context context) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_COMPTE, null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            long id = cursor.getLong(cursor.getColumnIndex(COLUMN_COMPTE_ID));
            String alias = cursor.getString(cursor.getColumnIndex(COLUMN_COMPTE_ALIAS));
            double solde = cursor.getDouble(cursor.getColumnIndex(COLUMN_COMPTE_SOLDE));
            String currency = cursor.getString(cursor.getColumnIndex(COLUMN_COMPTE_CURRENCY));
            String iban = cursor.getString(cursor.getColumnIndex(COLUMN_COMPTE_IBAN));

            // Récupération des informations de la personne
            String nom = cursor.getString(cursor.getColumnIndex(COLUMN_COMPTE_NOM));
            String prenom = cursor.getString(cursor.getColumnIndex(COLUMN_COMPTE_PRENOM));
            long dateNaissance = cursor.getLong(cursor.getColumnIndex(COLUMN_COMPTE_DATE_NAISSANCE));
            String nationalite = cursor.getString(cursor.getColumnIndex(COLUMN_COMPTE_NATIONALITE));

            Personne personne = new Personne(nom, prenom, dateNaissance, nationalite);
            personne.setIban(iban);

            Compte compte = new Compte(context, personne, alias, currency);
            compte.setId(id);
            compte.setSolde(solde);

            cursor.close();
            return compte;
        }
        return null;
    }

    public List<Investissement> getInvestissementsByAlias(Context context, String alias) {
        List<Investissement> investissements = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_INVESTISSEMENT, null, COLUMN_INVESTISSEMENT_CREATEUR_ALIAS + "=?",
                new String[]{alias}, null, null, null);
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_INVESTISSEMENT_ID));
            String nom = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INVESTISSEMENT_NOM));
            double rendement = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_INVESTISSEMENT_RENDEMENT));
            int dividendeDate = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_INVESTISSEMENT_DIVIDENDE_DATE));
            int dividendeRecurrence = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_INVESTISSEMENT_DIVIDENDE_RECURRENCE));
            double but = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_INVESTISSEMENT_BUT));
            double solde = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_INVESTISSEMENT_SOLDE));

            Investissement investissement = new Investissement(context, alias, nom, rendement, dividendeDate, dividendeRecurrence, but);
            investissement.setId(id);
            investissement.setSolde(solde);

            investissements.add(investissement);
        }
        cursor.close();
        return investissements;
    }


    public Investissement getInvestissementById(Context context, long investissementId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_INVESTISSEMENT, null, COLUMN_INVESTISSEMENT_ID + "=?",
                new String[]{String.valueOf(investissementId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_INVESTISSEMENT_ID));
            String nom = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INVESTISSEMENT_NOM));
            double rendement = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_INVESTISSEMENT_RENDEMENT));
            int dividendeDate = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_INVESTISSEMENT_DIVIDENDE_DATE));
            int dividendeRecurrence = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_INVESTISSEMENT_DIVIDENDE_RECURRENCE));
            double but = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_INVESTISSEMENT_BUT));
            double solde = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_INVESTISSEMENT_SOLDE));
            String createurAlias = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INVESTISSEMENT_CREATEUR_ALIAS));

            // Récupérer le compte associé
            Compte compte = CompteManager.getCompte();

            // Créer l'objet Investissement
            Investissement investissement = new Investissement(context, createurAlias, nom, rendement, dividendeDate, dividendeRecurrence, but);
            investissement.setId(id);
            investissement.setSolde(solde);

            cursor.close();
            return investissement;
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    public void insertDefaultInvestissements() {
        insertInvestissement("Projet Alpha", 5.0, 10, 30, 10000.0, 0.0, "Createur1");
        insertInvestissement("Projet Beta", 7.5, 15, 60, 20000.0, 0.0, "Createur2");
        insertInvestissement("Projet Gamma", 6.0, 20, 90, 15000.0, 0.0, "Createur3");
    }


    public List<aInvestit> getAInvestitByCompte(Context context, long compteId) {
        List<aInvestit> aInvestits = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_AINVESTIT, null,
                COLUMN_AINVESTIT_COMPTE_ID + "=?",
                new String[]{String.valueOf(compteId)}, null, null, null);

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_AINVESTIT_ID));
            String nom = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AINVESTIT_NOM));
            String prenom = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AINVESTIT_PRENOM));
            long investissementId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_AINVESTIT_INVESTISSEMENT_ID));
            double montant = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_AINVESTIT_MONTANT));

            // Récupérer l'investissement associé
            Investissement investissement = getInvestissementById(context, investissementId);

            Compte investisseur = CompteManager.getCompte();

            aInvestit investit = new aInvestit(investisseur, investissement, montant);
            investit.setId(id);

            aInvestits.add(investit);
        }
        cursor.close();
        return aInvestits;
    }

    // Récupérer les investissements effectués par un investisseur
    public List<aInvestit> getAInvestit(Context context ,long investissementId) {
        List<aInvestit> aInvestits = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_AINVESTIT, null, COLUMN_AINVESTIT_INVESTISSEMENT_ID + "=?",
                new String[]{String.valueOf(investissementId)}, null, null, null);

        while (cursor.moveToNext()) {
            String nom = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AINVESTIT_NOM));
            String prenom = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AINVESTIT_PRENOM));
            double montant = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_AINVESTIT_MONTANT));

            // Passez le context à la création de Compte
            Compte investisseur = new Compte(context, new Personne(nom, prenom, 0, "Nationalité"), "", ""); // Utilisation du context ici
            aInvestit investit = new aInvestit(investisseur, null, montant);
            aInvestits.add(investit);
        }
        cursor.close();
        return aInvestits;
    }


    public long insertInvestissement(String nom, double rendement, int dividendeDate, int dividendeRecurrence, double but, double solde, String createurAlias) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_INVESTISSEMENT_NOM, nom);
        values.put(COLUMN_INVESTISSEMENT_RENDEMENT, rendement);
        values.put(COLUMN_INVESTISSEMENT_DIVIDENDE_DATE, dividendeDate);
        values.put(COLUMN_INVESTISSEMENT_DIVIDENDE_RECURRENCE, dividendeRecurrence);
        values.put(COLUMN_INVESTISSEMENT_BUT, but);
        values.put(COLUMN_INVESTISSEMENT_SOLDE, solde);
        values.put(COLUMN_INVESTISSEMENT_CREATEUR_ALIAS, createurAlias);
        return db.insert(TABLE_INVESTISSEMENT, null, values);
    }




    public int deleteInvestissement(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_INVESTISSEMENT, COLUMN_INVESTISSEMENT_ID + "=?", new String[]{String.valueOf(id)});
    }

    public long insertAInvestit(long compteId, String nom, String prenom, long investissementId, double montant) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AINVESTIT_COMPTE_ID, compteId);
        values.put(COLUMN_AINVESTIT_NOM, nom);
        values.put(COLUMN_AINVESTIT_PRENOM, prenom);
        values.put(COLUMN_AINVESTIT_INVESTISSEMENT_ID, investissementId);
        values.put(COLUMN_AINVESTIT_MONTANT, montant);
        return db.insert(TABLE_AINVESTIT, null, values);
    }



    // Dans DatabaseHelper.java

// Dans DatabaseHelper.java

    public long insertCompte(String alias, double solde, String currency, String iban, String nom, String prenom, long dateNaissance, String nationalite) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COMPTE_ALIAS, alias);
        values.put(COLUMN_COMPTE_SOLDE, solde);
        values.put(COLUMN_COMPTE_CURRENCY, currency);
        values.put(COLUMN_COMPTE_IBAN, iban);
        values.put(COLUMN_COMPTE_NOM, nom);
        values.put(COLUMN_COMPTE_PRENOM, prenom);
        values.put(COLUMN_COMPTE_DATE_NAISSANCE, dateNaissance);
        values.put(COLUMN_COMPTE_NATIONALITE, nationalite);
        long id = db.insert(TABLE_COMPTE, null, values);
        return id;
    }




    public int deleteAInvestit(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_AINVESTIT, COLUMN_AINVESTIT_ID + "=?", new String[]{String.valueOf(id)});
    }

    public Compte getCompteById(Context context, long compteId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_COMPTE, null, COLUMN_COMPTE_ID + "=?", new String[]{String.valueOf(compteId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_COMPTE_ID));
            String alias = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COMPTE_ALIAS));
            double solde = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_COMPTE_SOLDE));
            String currency = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COMPTE_CURRENCY));
            String iban = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COMPTE_IBAN));

            String nom = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COMPTE_NOM));
            String prenom = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COMPTE_PRENOM));
            long dateNaissance = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_COMPTE_DATE_NAISSANCE));
            String nationalite = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COMPTE_NATIONALITE));

            Personne personne = new Personne(nom, prenom, dateNaissance, nationalite);
            personne.setIban(iban);

            Compte compte = new Compte(context, personne, alias, currency);
            compte.setId(id);
            compte.setSolde(solde);

            cursor.close();
            return compte;
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }


    public List<Investissement> getAllInvestissements(Context context) {
        List<Investissement> investissements = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_INVESTISSEMENT, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_INVESTISSEMENT_ID));
            String nom = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INVESTISSEMENT_NOM));
            double rendement = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_INVESTISSEMENT_RENDEMENT));
            int dividendeDate = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_INVESTISSEMENT_DIVIDENDE_DATE));
            int dividendeRecurrence = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_INVESTISSEMENT_DIVIDENDE_RECURRENCE));
            double but = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_INVESTISSEMENT_BUT));
            double solde = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_INVESTISSEMENT_SOLDE));
            String createurAlias = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INVESTISSEMENT_CREATEUR_ALIAS));



            Investissement investissement = new Investissement(context, createurAlias, nom, rendement, dividendeDate, dividendeRecurrence, but);
            investissement.setId(id);
            investissement.setSolde(solde);

            investissements.add(investissement);
        }
        cursor.close();
        return investissements;
    }

    public boolean isInvestissementTableEmpty() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_INVESTISSEMENT, null);
        boolean isEmpty = true;
        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            isEmpty = (count == 0);
            cursor.close();
        }
        return isEmpty;
    }

}
