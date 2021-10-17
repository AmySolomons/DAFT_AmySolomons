package com.honours.daft;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.TransactionsViewHolder> {
    private OnTransactionItemClickListener listener;
    private ArrayList<TransactionsItem> transactionsItems;

    public TransactionsAdapter(OnTransactionItemClickListener listener, ArrayList<TransactionsItem> transactionsItems) {
        this.listener = listener;
        this.transactionsItems = transactionsItems;
    }

    @NonNull
    @Override
    public TransactionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewTransactions = LayoutInflater.from(parent.getContext()).inflate(R.layout.transactions_items, parent, false);
        TransactionsAdapter.TransactionsViewHolder transactionsViewHolder = new TransactionsAdapter.TransactionsViewHolder(viewTransactions, listener);
        return transactionsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionsViewHolder holder, int position) {
        TransactionsItem tempItem = transactionsItems.get(position);
        holder.transactionRef.setText(tempItem.getTransactionRef());
        holder.bankAccount.setText(tempItem.getBankAccount());
        holder.transactionAmount.setText(tempItem.getTransactionAmount());
        holder.transactionDate.setText(tempItem.getTransactionDate());
        if (position%2 == 0){
            holder.itemView.setBackgroundColor(Color.parseColor("#BBCECB"));
        }
        else{
            holder.itemView.setBackgroundColor(Color.parseColor("#97C5B7"));
        }
    }

    public void setTransactionsItems(ArrayList<TransactionsItem> transactionsItems) {
        this.transactionsItems = transactionsItems;
        //TODO: Very important to update recycler view
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return transactionsItems.size();
    }

    public static class TransactionsViewHolder extends RecyclerView.ViewHolder{
        public TextView transactionRef;
        public TextView bankAccount;
        public TextView transactionAmount;
        public TextView transactionDate;
        public OnTransactionItemClickListener listener;

        public TransactionsViewHolder(@NonNull View itemView, final OnTransactionItemClickListener listener) {
            super(itemView);
            transactionRef = itemView.findViewById(R.id.transaction_ref);
            bankAccount = itemView.findViewById(R.id.bank_account);
            transactionAmount = itemView.findViewById(R.id.transaction_amount);
            transactionDate = itemView.findViewById(R.id.transaction_date);
        }

    }
}
