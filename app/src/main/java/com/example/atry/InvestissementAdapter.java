package com.example.atry;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InvestissementAdapter extends RecyclerView.Adapter<InvestissementAdapter.ViewHolder> {
    private List<Investissement> investissements;
    private final OnItemClickListener listener;

    public InvestissementAdapter(List<Investissement> investissements, OnItemClickListener listener) {
        this.investissements = investissements;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_block, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Investissement investissement = investissements.get(position);
        holder.titleTextView.setText(investissement.getFormattedNom());
        holder.creatorTextView.setText(investissement.getFormattedCreateur());
        holder.progressTextView.setText(investissement.getFormattedSolde());
        holder.progress.setProgress((int) ((investissement.getSolde() / investissement.getButAAtteindre()) * 100));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), InvestissementDetailActivity.class);
            intent.putExtra("investissement_id", investissement.getId()); // Transmettez l'ID
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return investissements.size();
    }

    public void updateList(List<Investissement> newList) {
        this.investissements = newList;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(Investissement investissement);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, creatorTextView, progressTextView;
        ProgressBar progress;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.block_title);
            creatorTextView = itemView.findViewById(R.id.block_creator);
            progressTextView = itemView.findViewById(R.id.block_amount);
            progress = itemView.findViewById(R.id.block_progessbar);
        }


    }
}

