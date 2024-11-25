package com.example.atry;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MesProjetsAdapter extends RecyclerView.Adapter<MesProjetsAdapter.ViewHolder> {

    public interface OnProjectClickListener {
        void onDeleteClick(int position);
    }

    private List<Investissement> mesProjets;
    private OnProjectClickListener listener;

    public MesProjetsAdapter(List<Investissement> mesProjets, OnProjectClickListener listener) {
        this.mesProjets = mesProjets;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mes_projets, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Investissement projet = mesProjets.get(position);

        holder.projectName.setText(projet.getFormattedNom());
        holder.projectGoal.setText("But : " + projet.getButAAtteindre() + " €");

        holder.deleteButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mesProjets.size();
    }

    // Définition de la classe ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView projectName, projectGoal;
        Button deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            projectName = itemView.findViewById(R.id.nom_projet);
            projectGoal = itemView.findViewById(R.id.but_projet);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}
