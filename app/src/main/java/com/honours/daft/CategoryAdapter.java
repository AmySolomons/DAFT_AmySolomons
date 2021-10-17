package com.honours.daft;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private ArrayList<CategoryItem> landingItems;
    private OnItemClickListener2 listener;

    public CategoryAdapter(OnItemClickListener2 listener, ArrayList<CategoryItem> landingItems) {
        this.listener = listener;
        this.landingItems = landingItems;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_items, parent, false);
        return new CategoryViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryItem tempItem = landingItems.get(position);
        holder.categoryName.setText(tempItem.getCategoryName());
        holder.budgetAmounts.setText(tempItem.getCategoryAmounts());
        holder.leftToSpend.setText(tempItem.getLeftToSpend());
        holder.progressBar.setProgress(tempItem.getProgress());
        holder.warn.setVisibility(tempItem.getOverBudgetWarn());
    }

    public void setLandingItems(ArrayList<CategoryItem> landingItems) {
        this.landingItems = landingItems;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return landingItems.size();
    }

    public interface OnItemClickListener2 {
        void onItemClick(int pos);
    }

    public void setOnClickListener(OnItemClickListener2 listen) {
        listener = listen;
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView categoryName;
        public TextView budgetAmounts;
        public TextView leftToSpend;
        public ProgressBar progressBar;
        public ImageView warn;
        public CategoryAdapter.OnItemClickListener2 listener;

        public CategoryViewHolder(@NonNull View itemView, final CategoryAdapter.OnItemClickListener2 listener) {
            super(itemView);

            categoryName = itemView.findViewById(R.id.categoryName);
            budgetAmounts = itemView.findViewById(R.id.budgetAmount);
            leftToSpend = itemView.findViewById(R.id.leftToSpend);
            progressBar = itemView.findViewById(R.id.progressBar);
            warn = itemView.findViewById(R.id.warn);
            this.listener = listener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(getAdapterPosition());
        }
    }
}
