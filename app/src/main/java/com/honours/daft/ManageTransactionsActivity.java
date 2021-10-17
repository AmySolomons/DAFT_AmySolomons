package com.honours.daft;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ManageTransactionsActivity extends AppCompatActivity implements ManageTransactionsAdapter.OnItemClickListener2 {
    private RecyclerView recyclerView;
    private ManageTransactionsAdapter adapter;
    private RecyclerView.LayoutManager manager;
    private ArrayList<TransactionsItem> transactionsItemArrayList;
    private MyViewModel daftViewModel;
    private ArrayList<Integer> tempTransactions;

    private Button saveButton;
    private Integer ignoredID;
    private String parentCatName;
    private Integer parentID;
    private String subCatName;
    private String subCatBudget;
    private Integer subCatId;
    private Button prevButton;
    public static final String NAME = "com.honours.daft.NAME";
    public static final String BUDGET = "com.honours.daft.BUDGET";
    public static final String PARENT_CATEGORY = "com.honours.daft.PARENT_CATEGORY";
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_transactions);

        daftViewModel = new ViewModelProvider(this).get(MyViewModel.class);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(R.string.new_sub_category);

        transactionsItemArrayList = new ArrayList<>();
        adapter = new ManageTransactionsAdapter(transactionsItemArrayList, this);

        manager = new LinearLayoutManager(this);

        recyclerView = findViewById(R.id.TransactionsRecyclerView);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        tempTransactions = new ArrayList<>();
        addObservedTransactions();

        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButtonClicked();
            }
        });

        prevButton = findViewById(R.id.prevButton);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevButtonClicked();
            }
        });

        cancelButton = findViewById(R.id.cancelManageCatButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelButtonClicked();
            }
        });

        ArrayList<TransactionsItem> check = adapter.getCheckedTransactionItems();


    }

    private void addObservedTransactions() {
        daftViewModel.getSubCategoriesWithTransactions().observe(this, new Observer<List<SubCategoryWithTransaction>>() {
            @Override
            public void onChanged(List<SubCategoryWithTransaction> subCategoryWithTransactions) {
                for (SubCategoryWithTransaction subCategoryWithTransaction : subCategoryWithTransactions) {
                    if (subCategoryWithTransaction.getSubCategoryEntity().getSubCategoryName().equals("Ignored")) {
                        ignoredID = subCategoryWithTransaction.getSubCategoryEntity().getId();
                        for (TransactionEntity transactionEntity : subCategoryWithTransaction.getTransactions()) {
                            //keep a record of the id's of the original list of ignored transactions
                            tempTransactions.add(transactionEntity.getId());

                            String reference = getResources().getString(R.string.Transaction_Ref) + transactionEntity.getReference();
                            String bankAccount = getResources().getString(R.string.bank_account) + transactionEntity.getBankAccountID();

                            String transactionAmount = getResources().getString(R.string.transaction_amount) + transactionEntity.getTotalAmount();
                            String date = getResources().getString(R.string.transaction_date) + transactionEntity.getDate();

                            transactionsItemArrayList.add(new TransactionsItem(reference, bankAccount, transactionAmount, date));
                        }
                        break;
                    }
                }
                adapter.setTransactionsItems(transactionsItemArrayList);
            }
        });
    }

    private void cancelButtonClicked() {
        Intent in = new Intent(ManageTransactionsActivity.this, CategoryActivity.class);
        finish();
        startActivity(in);
    }

    private void prevButtonClicked() {
        Intent intent1 = getIntent();
        Intent intent2 = new Intent(ManageTransactionsActivity.this, NewSubCategoryActivity.class);
        intent2.putExtra(NAME, intent1.getStringExtra(NewSubCategoryActivity.NAME));
        intent2.putExtra(PARENT_CATEGORY, intent1.getIntExtra(NewSubCategoryActivity.SELECTED_CHOICE, 0));
        intent2.putExtra(BUDGET, intent1.getStringExtra(NewSubCategoryActivity.BUDGET));
        finish();
        startActivity(intent2);
    }

    private void saveButtonClicked() {
        try {
            insertNewSubCategory();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ArrayList<Integer> temp = adapter.getSelected();
        if (temp != null) {
            for (Integer pos : temp) {
                Integer transId = tempTransactions.get(pos);
                daftViewModel.updateTransactionSubCategory(subCatId, transId);
            }
        }
        Intent intent = new Intent(ManageTransactionsActivity.this, CategoryActivity.class);
        startActivity(intent);
    }

    private void insertNewSubCategory() throws ExecutionException, InterruptedException {
        Intent intent = getIntent();
        parentCatName = intent.getStringExtra(NewSubCategoryActivity.PARENT_CATEGORY);
        parentID = daftViewModel.getParentID(parentCatName);

        subCatName = intent.getStringExtra(NewSubCategoryActivity.NAME);
        subCatBudget = intent.getStringExtra(NewSubCategoryActivity.BUDGET);
        subCatId = daftViewModel.getNumberSubs() + 1;
        SubCategoryEntity subCategoryEntity = new SubCategoryEntity(subCatName, subCatId, Double.valueOf(subCatBudget));

        daftViewModel.insertSubCategoryInParent(parentID, subCategoryEntity);
    }

    @Override
    public void onItemClick(int pos) {

    }
}
