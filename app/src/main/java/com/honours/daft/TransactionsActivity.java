package com.honours.daft;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TransactionsActivity extends AppCompatActivity implements OnTransactionItemClickListener {
    private TransactionsAdapter adapter;
    private ArrayList<TransactionsItem> transactionsItemArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        Intent in = getIntent();

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(in.getStringExtra(CategoryActivity.subCategoryName));

        TextView textView1 = findViewById(R.id.budget);
        textView1.setText(in.getStringExtra(CategoryActivity.subCategoryBudget));
        TextView textView2 = findViewById(R.id.left);
        textView2.setText(in.getStringExtra(CategoryActivity.subCategoryLeft));

        transactionsItemArrayList = new ArrayList<>();
        adapter = new TransactionsAdapter(this, transactionsItemArrayList);

        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this);

        RecyclerView recyclerView = findViewById(R.id.TransactionsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        MyViewModel daftViewModel = new ViewModelProvider(this).get(MyViewModel.class);

        daftViewModel.getSubCategoryAndTransactions().observe(this, new Observer<List<SubCategoryWithTransaction>>() {
            @Override
            public void onChanged(List<SubCategoryWithTransaction> subCategoryWithTransactions) {
                transactionsItemArrayList.clear();
                for (SubCategoryWithTransaction subCategoryWithTransaction : subCategoryWithTransactions) {
                    if (in.getStringExtra(CategoryActivity.subCategoryName).equals(String.valueOf(subCategoryWithTransaction.getSubCategoryEntity().getSubCategoryName()))) {
                        for (TransactionEntity transactionEntity : subCategoryWithTransaction.getTransactions()) {
                            String ref = getResources().getString(R.string.Transaction_Ref) + transactionEntity.getReference();

                            //TODO: Add bank account table. Default bank account used (i.e., 0000000)
//                            String bankAccount = String.valueOf(transactionEntity.getBankAccountID());

                            String bankAccount = getResources().getString(R.string.bank_account) + "0000000";
                            String amount = getResources().getString(R.string.transaction_amount) + transactionEntity.getTotalAmount();
                            String date = getResources().getString(R.string.transaction_date) + transactionEntity.getDate();
                            TransactionsItem t = new TransactionsItem(ref, bankAccount, amount, date);

                            transactionsItemArrayList.add(t);
                        }
                        break;
                    }
                }
                adapter.setTransactionsItems(transactionsItemArrayList);
            }
        });
    }

    @Override
    public void onItemClick(int pos) {
    }

}
