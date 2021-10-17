package com.honours.daft;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LandingAdapter extends RecyclerView.Adapter<LandingAdapter.LandingViewHolder> {
    public void setLandingItems(ArrayList<LandingItem> landingItems) {
        this.landingItems = landingItems;
        notifyDataSetChanged();
    }

    private ArrayList<LandingItem> landingItems;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }

    public void setOnClickListener(OnItemClickListener listen) {
        listener = listen;
    }

    public LandingAdapter(ArrayList<LandingItem> landingItems, OnItemClickListener listener) {
        this.landingItems = landingItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LandingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vLanding = LayoutInflater.from(parent.getContext()).inflate(R.layout.landing_items, parent, false);
        LandingViewHolder landingViewHolder = new LandingViewHolder(vLanding, listener);
        return landingViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LandingViewHolder holder, int position) {
        LandingItem tempItem = landingItems.get(position);
        holder.categoryName.setText(tempItem.getCategoryName());
        holder.categoryBudget.setText(tempItem.getCategoryBudget());
        holder.categoryLeft.setText(tempItem.getCategoryLeft());
        if (tempItem.getBarType().equals("N")) {
            holder.progressBarNecessities.setProgress(tempItem.getProgress());
            holder.progressBarNecessities.setVisibility(View.VISIBLE);
            holder.progressBarSavings.setVisibility(View.INVISIBLE);
            holder.progressBarLuxuries.setVisibility(View.INVISIBLE);
        } else if (tempItem.getBarType().equals("L")) {
            holder.progressBarLuxuries.setProgress(tempItem.getProgress());
            holder.progressBarNecessities.setVisibility(View.INVISIBLE);
            holder.progressBarSavings.setVisibility(View.INVISIBLE);
            holder.progressBarLuxuries.setVisibility(View.VISIBLE);
        } else {
            holder.progressBarSavings.setProgress(tempItem.getProgress());
            holder.progressBarNecessities.setVisibility(View.INVISIBLE);
            holder.progressBarSavings.setVisibility(View.VISIBLE);
            holder.progressBarLuxuries.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return landingItems.size();
    }

    public static class LandingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView categoryName;
        public TextView categoryBudget;
        public TextView categoryLeft;
        public ProgressBar progressBarSavings;
        public ProgressBar progressBarNecessities;
        public ProgressBar progressBarLuxuries;
        public OnItemClickListener listener;

        public LandingViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.parentCatName);
            categoryBudget = itemView.findViewById(R.id.budgetValueCat);
            categoryLeft = itemView.findViewById(R.id.leftToSpendCat);
            progressBarSavings = itemView.findViewById(R.id.progressBarSavings);
            progressBarNecessities = itemView.findViewById(R.id.progressBarNecessities);
            progressBarLuxuries = itemView.findViewById(R.id.progressBarLuxuries);
            this.listener = listener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(getAdapterPosition());
        }
    }
}
