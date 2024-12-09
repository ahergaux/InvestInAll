package com.example.atry;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    DatabaseHelper dbHelper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (dbHelper.isInvestissementTableEmpty()) {
            dbHelper.insertDefaultInvestissements();
        }

        Compte compte = dbHelper.getCompte(this);

        if (compte == null) {
            Intent intent = new Intent(MainActivity.this, CreateAccountActivity.class);
            startActivity(intent);
            finish();
            return;
        } else {
            CompteManager.initCompte(this, compte.getPersonne(), compte.getAlias(), compte.getCurrency());
            CompteManager.getCompte().setId(compte.getId());
            CompteManager.getCompte().setSolde(compte.getSolde());
        }

        List<Investissement> investissements = dbHelper.getInvestissementsByAlias(this,compte.getAlias());
        CompteManager.getCompte().getMesProjets().addAll(investissements);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_account) {
                    startActivity(new Intent(MainActivity.this, AccountActivity.class));
                } else if (id == R.id.nav_mes_projets) {
                    startActivity(new Intent(MainActivity.this, MesProjetsActivity.class));
                } else if (id == R.id.nav_mes_investissements) { // Ajout pour Mes Investissements
                    startActivity(new Intent(MainActivity.this, MesInvestissementsActivity.class));
                } else if (id == R.id.nav_search) {
                    Toast.makeText(MainActivity.this, "Vous êtes déjà dans Recherche", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_help) {
                    startActivity(new Intent(MainActivity.this, ContactActivity.class));
                }

                drawerLayout.closeDrawers();
                return true;
            }
        });



        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Investissement> projets = getProjects();

        InvestissementAdapter adapter = new InvestissementAdapter(projets, new InvestissementAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Investissement investissement) {
                // Lancer l'activité de détail
                Intent intent = new Intent(MainActivity.this, InvestissementDetailActivity.class);
                intent.putExtra("nom", investissement.getFormattedNom());
                intent.putExtra("createur", investissement.getCreateurAlias());
                intent.putExtra("rendement", investissement.getRendements());
                intent.putExtra("dividendeDate", investissement.getDividendeDate());
                intent.putExtra("dividendeRecurrence", investissement.getDividendeRecurrence());
                intent.putExtra("butAAtteindre", investissement.getButAAtteindre());
                intent.putExtra("solde", investissement.getSolde());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        EditText searchInput = findViewById(R.id.search_input);
        ImageButton searchButton = findViewById(R.id.search_button);

        searchButton.setOnClickListener(v -> {
            String query = searchInput.getText().toString().toLowerCase();
            List<Investissement> filteredProjets = new ArrayList<>();
            for (Investissement projet : projets) {
                if (projet.getFormattedNom().toLowerCase().contains(query)) {
                    filteredProjets.add(projet);
                }
            }
            adapter.updateList(filteredProjets);
            if (filteredProjets.isEmpty()) {
                Toast.makeText(MainActivity.this, "Aucun projet trouvé pour : " + query, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private List<Investissement> getProjects() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        return dbHelper.getAllInvestissements(this);
    }


    private List<Investissement> getSampleProjects() {
        List<Investissement> projets = new ArrayList<>();
        Compte createur = new Compte(this,new Personne("Doe", "John", 1985, "Française"), "Alias1", "EUR");
        projets.add(new Investissement(this,createur.getAlias(), "Projet 1", 10.0, 10, 30, 5000));
        projets.add(new Investissement(this,createur.getAlias(), "Projet 2", 8.0, 15, 60, 3000));
        projets.add(new Investissement(this,createur.getAlias(), "Projet 3", 5.5, 20, 45, 7000));
        projets.add(new Investissement(this,createur.getAlias(), "Projet 4", 12.0, 5, 90, 10000));
        return projets;
    }
}
