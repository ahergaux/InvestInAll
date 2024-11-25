package com.example.atry;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// MesInvestissementsAdapter.java

public class MesInvestissementsAdapter extends RecyclerView.Adapter<MesInvestissementsAdapter.ViewHolder> {

    private List<aInvestit> mesinvestissements;
    private OnInvestmentActionListener listener;

    public MesInvestissementsAdapter(List<aInvestit> mesinvestissements, OnInvestmentActionListener listener) {
        this.mesinvestissements = mesinvestissements;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mes_investissements, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        aInvestit investissement = mesinvestissements.get(position);

        // Liaison des données
        holder.investisseurTextView.setText("Investisseur : " + investissement.getInvestisseur().getAlias());
        holder.projetTextView.setText("Projet : " + investissement.getInvestissement().getFormattedNom());
        holder.montantTextView.setText("Montant investi : " + investissement.getSoldeInvestit() + " €");

        // Gestion des actions
        holder.reduceButton.setOnClickListener(v -> listener.onReduceInvestment(investissement));
        holder.deleteButton.setOnClickListener(v -> listener.onDeleteInvestment(investissement));
    }

    @Override
    public int getItemCount() {
        return mesinvestissements.size();
    }

    public interface OnInvestmentActionListener {
        void onReduceInvestment(aInvestit investissement);
        void onDeleteInvestment(aInvestit investissement);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView investisseurTextView;
        TextView projetTextView;
        TextView montantTextView;
        Button reduceButton;
        Button deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            investisseurTextView = itemView.findViewById(R.id.investisseur);
            projetTextView = itemView.findViewById(R.id.projet);
            montantTextView = itemView.findViewById(R.id.montant);
            reduceButton = itemView.findViewById(R.id.button_reduce_investment);
            deleteButton = itemView.findViewById(R.id.button_delete_investment);
        }
    }
}
