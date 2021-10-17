package com.honours.daft;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ManageTransactionsAdapter extends RecyclerView.Adapter<ManageTransactionsAdapter.TransactionsViewHolder> {
    private ArrayList<TransactionsItem> transactionsItems;
    private ArrayList<Integer> selected;

    private ArrayList<TransactionsItem> checkedTransactionItems;
    private ManageTransactionsAdapter.OnItemClickListener2 listener;

    public ArrayList<TransactionsItem> getCheckedTransactionItems() {
        return checkedTransactionItems;
    }

    public ArrayList<Integer> getSelected() {
        return selected;
    }

    public void setTransactionsItems(ArrayList<TransactionsItem> transactionsItems) {
        this.transactionsItems = transactionsItems;
        notifyDataSetChanged();
    }


    public interface OnItemClickListener2 {
        void onItemClick(int pos);
    }

    public void setOnClickListener(OnItemClickListener2 listen) {
        listener = listen;
    }

    public ManageTransactionsAdapter(ArrayList<TransactionsItem> transactionsItems, ManageTransactionsAdapter.OnItemClickListener2 listener) {
        this.transactionsItems = transactionsItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TransactionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vTransactions = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_transactions_items, parent, false);
        ManageTransactionsAdapter.TransactionsViewHolder transactionsViewHolder = new ManageTransactionsAdapter.TransactionsViewHolder(vTransactions, listener);
        return transactionsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final TransactionsViewHolder holder, int position) {
        final TransactionsItem tempItem = transactionsItems.get(position);
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

        selected = new ArrayList<>();
        checkedTransactionItems = new ArrayList<>();
        holder.addCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v != null) {
                    if (holder.addCheckBox.isChecked()) {
                        checkedTransactionItems.add(tempItem);
                        selected.add(position);
                        Log.d("AddTransaction", "Transaction selected");
                    } else {
                        checkedTransactionItems.remove(tempItem);
                        Log.d("RemoveTransaction", "Transaction deselected");
                        selected.remove(position);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactionsItems.size();
    }

    public static class TransactionsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView transactionRef;
        public TextView bankAccount;
        public TextView transactionAmount;
        public TextView transactionDate;
        public CheckBox addCheckBox;
        public ManageTransactionsAdapter.OnItemClickListener2 listener;

        public TransactionsViewHolder(@NonNull View itemView, final ManageTransactionsAdapter.OnItemClickListener2 listener) {
            super(itemView);
            transactionRef = itemView.findViewById(R.id.transaction_ref);
            bankAccount = itemView.findViewById(R.id.bank_account);
            transactionAmount = itemView.findViewById(R.id.transaction_amount);
            transactionDate = itemView.findViewById(R.id.transaction_date);

            //checked boxes
            addCheckBox = itemView.findViewById(R.id.addNewCheckBox);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(getAdapterPosition());
        }
    }
}
