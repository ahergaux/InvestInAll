package com.example.atry;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class AccountActivity extends AppCompatActivity {

    private TextView fullNameTextView;
    private TextView aliasTextView;
    private TextView dateNaissanceTextView;
    private TextView nationaliteTextView;
    private TextView ibanTextView;
    private TextView soldeTextView;
    private TextView currencyTextView;
    private EditText addAmountEditText;
    private EditText withdrawAmountEditText;
    private Button addFundsButton;
    private Button withdrawFundsButton;

    private Compte compte;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        dbHelper = new DatabaseHelper(this);
        compte = CompteManager.getCompte();

        fullNameTextView = findViewById(R.id.account_full_name);
        aliasTextView = findViewById(R.id.account_alias);
        dateNaissanceTextView = findViewById(R.id.account_date_naissance);
        nationaliteTextView = findViewById(R.id.account_nationalite);
        ibanTextView = findViewById(R.id.account_iban);
        soldeTextView = findViewById(R.id.account_solde);
        currencyTextView = findViewById(R.id.account_currency);

        addAmountEditText = findViewById(R.id.add_amount);
        withdrawAmountEditText = findViewById(R.id.withdraw_amount);
        addFundsButton = findViewById(R.id.button_add_funds);
        withdrawFundsButton = findViewById(R.id.button_withdraw_funds);

        displayAccountInfo();

        addFundsButton.setOnClickListener(v -> handleAddFunds());
        withdrawFundsButton.setOnClickListener(v -> handleWithdrawFunds());
    }

    private void displayAccountInfo() {
        Personne personne = compte.getPersonne();

        fullNameTextView.setText(personne.getPrenom() + " " + personne.getNom());
        aliasTextView.setText("Alias : " + compte.getAlias());

        Date dateNaissance = new Date(personne.getDateNaissance());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        dateNaissanceTextView.setText("Date de naissance : " + sdf.format(dateNaissance));

        nationaliteTextView.setText("Nationalité : " + personne.getNationalite());
        ibanTextView.setText("IBAN : " + personne.getIban());
        soldeTextView.setText("Solde : " + compte.getSolde() + " " + compte.getCurrency());
        currencyTextView.setText("Devise : " + compte.getCurrency());
    }

    private void handleAddFunds() {
        String amountText = addAmountEditText.getText().toString();
        if (amountText.isEmpty()) {
            Toast.makeText(this, "Veuillez entrer un montant à ajouter.", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountText);
        if (amount <= 0) {
            Toast.makeText(this, "Le montant doit être supérieur à zéro.", Toast.LENGTH_SHORT).show();
            return;
        }

        compte.setSolde(compte.getSolde() + amount);

        dbHelper.updateCompte(compte.getId(), compte.getAlias(), compte.getSolde(), compte.getCurrency(), compte.getPersonne().getIban());

        displayAccountInfo();

        addAmountEditText.setText("");

        Toast.makeText(this, "Fonds ajoutés avec succès.", Toast.LENGTH_SHORT).show();
    }

    private void handleWithdrawFunds() {
        String amountText = withdrawAmountEditText.getText().toString();
        if (amountText.isEmpty()) {
            Toast.makeText(this, "Veuillez entrer un montant à retirer.", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountText);
        if (amount <= 0) {
            Toast.makeText(this, "Le montant doit être supérieur à zéro.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (amount > compte.getSolde()) {
            Toast.makeText(this, "Solde insuffisant.", Toast.LENGTH_SHORT).show();
            return;
        }

        compte.setSolde(compte.getSolde() - amount);

        dbHelper.updateCompte(compte.getId(), compte.getAlias(), compte.getSolde(), compte.getCurrency(), compte.getPersonne().getIban());

        displayAccountInfo();

        withdrawAmountEditText.setText("");

        Toast.makeText(this, "Fonds retirés avec succès.", Toast.LENGTH_SHORT).show();
    }
}
