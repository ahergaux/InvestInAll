package com.example.atry;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText nomInput;
    private EditText prenomInput;
    private EditText dateNaissanceInput;
    private EditText nationaliteInput;
    private EditText ibanInput;
    private EditText aliasInput;
    private EditText currencyInput;
    private Button createAccountButton;
    private DatabaseHelper dbHelper;
    private long dateNaissance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        dbHelper = new DatabaseHelper(this);

        nomInput = findViewById(R.id.nom_input);
        prenomInput = findViewById(R.id.prenom_input);
        dateNaissanceInput = findViewById(R.id.date_naissance_input);
        nationaliteInput = findViewById(R.id.nationalite_input);
        ibanInput = findViewById(R.id.iban_input);
        aliasInput = findViewById(R.id.alias_input);
        currencyInput = findViewById(R.id.currency_input);
        createAccountButton = findViewById(R.id.create_account_button);

        createAccountButton.setOnClickListener(v -> {
            String nom = nomInput.getText().toString().trim();
            String prenom = prenomInput.getText().toString().trim();
            String dateNaissanceStr = dateNaissanceInput.getText().toString().trim();
            String nationalite = nationaliteInput.getText().toString().trim();
            String iban = ibanInput.getText().toString().trim();
            String alias = aliasInput.getText().toString().trim();
            String currency = currencyInput.getText().toString().trim();

            if (nom.isEmpty() || prenom.isEmpty() || dateNaissanceStr.isEmpty() ||
                    nationalite.isEmpty() || iban.isEmpty() || alias.isEmpty() || currency.isEmpty()) {
                Toast.makeText(CreateAccountActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                String[] dateParts = dateNaissanceStr.split("/");
                int day = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]) - 1;
                int year = Integer.parseInt(dateParts[2]);

                java.util.Calendar calendar = java.util.Calendar.getInstance();
                calendar.set(year, month, day);
                dateNaissance = calendar.getTimeInMillis();
            } catch (Exception e) {
                Toast.makeText(this, "Format de date invalide. Utilisez jj/mm/aaaa.", Toast.LENGTH_SHORT).show();
                return;
            }

            Personne personne = new Personne(nom, prenom, dateNaissance, nationalite);
            personne.setIban(iban);
            Compte compte = new Compte(CreateAccountActivity.this, personne, alias, currency);

            long compteId = dbHelper.insertCompte(
                    compte.getAlias(),
                    compte.getSolde(),
                    compte.getCurrency(),
                    compte.getPersonne().getIban(),
                    compte.getPersonne().getNom(),
                    compte.getPersonne().getPrenom(),
                    compte.getPersonne().getDateNaissance(),
                    compte.getPersonne().getNationalite()
            );
            compte.setId(compteId);

            CompteManager.initCompte(CreateAccountActivity.this, compte.getPersonne(), compte.getAlias(), compte.getCurrency());
            CompteManager.getCompte().setId(compteId);

            Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }


}
