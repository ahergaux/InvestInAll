package com.example.atry;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// InvestissementDetailActivity.java

public class InvestissementDetailActivity extends AppCompatActivity {

    private Investissement investissement;
    private Compte compte;
    private DatabaseHelper dbHelper;
    private TextView detailNom, detailCreateur, detailDividendeDate, detailDividendeRecurrence, detailSolde;
    private ProgressBar butBar;
    private EditText investmentAmountEditText;
    private Button investButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investissement_detail);

        dbHelper = new DatabaseHelper(this);
        compte = CompteManager.getCompte();

        // Récupérer les vues
        detailNom = findViewById(R.id.detail_nom);
        detailCreateur = findViewById(R.id.detail_createur);
        detailDividendeDate = findViewById(R.id.detail_dividende_date);
        detailDividendeRecurrence = findViewById(R.id.detail_dividende_recurrence);
        detailSolde = findViewById(R.id.detail_solde);
        butBar = findViewById(R.id.detail_but_bar);
        investmentAmountEditText = findViewById(R.id.investment_amount);
        investButton = findViewById(R.id.button_invest);

        // Récupérer les données de l'investissement
        investissement = getInvestissementFromIntent();

        // Afficher les détails de l'investissement
        displayInvestmentDetails();

        // Configurer le listener du bouton d'investissement
        investButton.setOnClickListener(v -> handleInvestment());
    }

    private Investissement getInvestissementFromIntent() {
        long investissementId = getIntent().getLongExtra("investissement_id", -1);
        if (investissementId != -1) {
            return dbHelper.getInvestissementById(this, investissementId);
        }
        return null;
    }

    private void displayInvestmentDetails() {
        if (investissement != null) {
            detailNom.setText(investissement.getFormattedNom());
            detailCreateur.setText("Créé par : " + investissement.getFormattedCreateur());
            detailDividendeDate.setText("Date du prochain dividende : " + investissement.getDividendeDate() + " jours");
            detailDividendeRecurrence.setText("Récurrence des dividendes : " + investissement.getDividendeRecurrence() + " jours");
            detailSolde.setText("Solde actuel : " + investissement.getSolde() + " € / " + investissement.getButAAtteindre() + " €");
            int progressBut = (int) ((investissement.getSolde() / investissement.getButAAtteindre()) * 100);
            butBar.setProgress(progressBut);
        }
    }

    private void handleInvestment() {
        String amountText = investmentAmountEditText.getText().toString();
        if (amountText.isEmpty()) {
            Toast.makeText(this, "Veuillez entrer un montant.", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountText);
        if (amount <= 0) {
            Toast.makeText(this, "Le montant doit être supérieur à zéro.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (compte.getSolde() < amount) {
            Toast.makeText(this, "Solde insuffisant.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Procéder à l'investissement
        investInProject(amount);
    }

    // Dans InvestissementDetailActivity.java

    private void investInProject(double amount) {

        compte.setSolde(compte.getSolde() - amount);

        investissement.setSolde(investissement.getSolde() + amount);

        compte.addToMesInvestissement(investissement, amount);

        dbHelper.updateCompte(compte.getId(), compte.getAlias(), compte.getSolde(), compte.getCurrency(), compte.getPersonne().getIban());

        dbHelper.updateInvestissement(investissement.getId(), investissement.getFormattedNom(), investissement.getRendements(), investissement.getDividendeDate(), investissement.getDividendeRecurrence(), investissement.getButAAtteindre(), investissement.getSolde());

        Toast.makeText(this, "Investissement réussi !", Toast.LENGTH_SHORT).show();

        displayInvestmentDetails();
        investmentAmountEditText.setText("");
    }

}
