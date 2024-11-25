package com.example.atry;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MesProjetsActivity extends AppCompatActivity {

    private MesProjetsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mes_projets);

        // Récupérer le compte global
        Compte compte = CompteManager.getCompte();

        // Récupérer les projets du compte
        List<Investissement> mesProjets = compte.getMesProjets();

        // Configurer le RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view_mes_projets);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MesProjetsAdapter(mesProjets, position -> {
            mesProjets.remove(position);
            adapter.notifyItemRemoved(position);
        });
        recyclerView.setAdapter(adapter);

        // Configurer le bouton Ajouter un Projet
        Button addProjectButton = findViewById(R.id.add_project_button);
        addProjectButton.setOnClickListener(v -> showAddProjectDialog(compte, mesProjets));
    }

    private void showAddProjectDialog(Compte compte, List<Investissement> mesProjets) {
        // Créer une boîte de dialogue avec un layout personnalisé
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ajouter un Projet");

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_project, null);
        builder.setView(dialogView);

        EditText projectNameInput = dialogView.findViewById(R.id.project_name_input);
        EditText projectGoalInput = dialogView.findViewById(R.id.project_goal_input);
        EditText projectRendementInput = dialogView.findViewById(R.id.project_rendement_input);
        EditText projectDividendDateInput = dialogView.findViewById(R.id.project_dividend_date_input);
        EditText projectDividendRecurrenceInput = dialogView.findViewById(R.id.project_dividend_recurrence_input);

        builder.setPositiveButton("Ajouter", (dialog, which) -> {
            String name = projectNameInput.getText().toString().trim();
            String goalStr = projectGoalInput.getText().toString().trim();
            String rendementStr = projectRendementInput.getText().toString().trim();
            String dividendDateStr = projectDividendDateInput.getText().toString().trim();
            String dividendRecurrenceStr = projectDividendRecurrenceInput.getText().toString().trim();

            // Valider les champs
            if (name.isEmpty() || goalStr.isEmpty() || rendementStr.isEmpty() ||
                    dividendDateStr.isEmpty() || dividendRecurrenceStr.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            double goal = Double.parseDouble(goalStr);
            double rendement = Double.parseDouble(rendementStr);
            int dividendDate = Integer.parseInt(dividendDateStr);
            int dividendRecurrence = Integer.parseInt(dividendRecurrenceStr);

            // Dans showAddProjectDialog
            Investissement newProject = new Investissement(this,
                    compte.getAlias(),
                    name,
                    rendement,
                    dividendDate,
                    dividendRecurrence,
                    goal
            );
            compte.addProjet(newProject);
            mesProjets.add(newProject);
            adapter.notifyItemInserted(mesProjets.size() - 1);

        });

        builder.setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
