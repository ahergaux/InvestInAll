package com.example.atry;

import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class MesInvestissementsActivity extends AppCompatActivity implements MesInvestissementsAdapter.OnInvestmentActionListener {

    private Compte compte;
    private List<aInvestit> mesinvestissements;
    private MesInvestissementsAdapter adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesinvestissements);

        compte = CompteManager.getCompte();
        dbHelper = new DatabaseHelper(this);

        mesinvestissements = dbHelper.getAInvestitByCompte(this, compte.getId());

        RecyclerView recyclerView = findViewById(R.id.recycler_view_mes_investissements);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MesInvestissementsAdapter(mesinvestissements, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onReduceInvestment(aInvestit investissement) {
        showReduceInvestmentDialog(investissement);
    }

    @Override
    public void onDeleteInvestment(aInvestit investissement) {
        new AlertDialog.Builder(this)
                .setTitle("Supprimer l'investissement")
                .setMessage("Êtes-vous sûr de vouloir supprimer cet investissement ?")
                .setPositiveButton("Oui", (dialog, which) -> deleteInvestment(investissement))
                .setNegativeButton("Non", null)
                .show();
    }

    private void showReduceInvestmentDialog(aInvestit investissement) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Réduire l'investissement");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setHint("Nouveau montant (actuel : " + investissement.getSoldeInvestit() + " €)");

        builder.setView(input);

        builder.setPositiveButton("Valider", (dialog, which) -> {
            String inputText = input.getText().toString();
            if (inputText.isEmpty()) {
                Toast.makeText(this, "Veuillez entrer un montant.", Toast.LENGTH_SHORT).show();
                return;
            }

            double newAmount = Double.parseDouble(inputText);
            if (newAmount >= investissement.getSoldeInvestit()) {
                Toast.makeText(this, "Le nouveau montant doit être inférieur à l'actuel.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (newAmount <= 0) {
                Toast.makeText(this, "Le montant doit être supérieur à zéro.", Toast.LENGTH_SHORT).show();
                return;
            }

            double difference = investissement.getSoldeInvestit() - newAmount;

            compte.setSolde(compte.getSolde() + difference);

            investissement.setSoldeInvestit(newAmount);

            dbHelper.updateAInvestit(investissement.getId(),
                    investissement.getInvestisseur().getPersonne().getNom(),
                    investissement.getInvestisseur().getPersonne().getPrenom(),
                    newAmount);

            adapter.notifyDataSetChanged();
            Toast.makeText(this, "Investissement mis à jour.", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Annuler", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void deleteInvestment(aInvestit investissement) {
        double amount = investissement.getSoldeInvestit();
        compte.setSolde(compte.getSolde() + amount);

        compte.removeToMesInvestissement(investissement.getInvestissement());

        dbHelper.deleteAInvestit(investissement.getId());

        mesinvestissements.remove(investissement);
        adapter.notifyDataSetChanged();

        Toast.makeText(this, "Investissement supprimé.", Toast.LENGTH_SHORT).show();
    }
}

