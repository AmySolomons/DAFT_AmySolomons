package com.honours.daft;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity implements CategoryAdapter.OnItemClickListener2 {
    private RecyclerView recyclerView;
    private CategoryAdapter adapter;
    private RecyclerView.LayoutManager manager;
    private ArrayList<CategoryItem> categoryItemArrayList;

    public static final String subCategoryName = "com.honours.daft.subCategoryName";
    public static final String categoryName = "com.honours.daft.categoryName";
    public static final String subCategoryBudget = "com.honours.daft.subCategoryBudget";
    public static final String subCategoryLeft = "com.honours.daft.subCategoryLeft";

    private ImageButton addCat;
    private String parentName;

    private MyViewModel daftViewModel;

    private double budget = 0.0;
    private double left = 0.0;
    private Integer parentID;
    private double parentBudget;
    private Integer subID;
    private String RECYCLER;
    private String TITLE;
    private Parcelable s;

    private FloatingActionButton categorySummaryActionButton;
    public static final String CATSUMMARY = "com.honours.daft.catSummary";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        daftViewModel = new ViewModelProvider(this).get(MyViewModel.class);

        Intent in = getIntent();

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        parentName = daftViewModel.getCategory3Name();
        if (parentName.equals("Necessities")){
            getSupportActionBar().setTitle(getResources().getString(R.string.necessities));
        }
        else if (parentName.equals("Luxuries")){
            getSupportActionBar().setTitle(getResources().getString(R.string.luxuries));
        }
        else{
            getSupportActionBar().setTitle(getResources().getString(R.string.savings));
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        manager = new LinearLayoutManager(this);
        categoryItemArrayList = new ArrayList<>();
        adapter = new CategoryAdapter(this, categoryItemArrayList);

        recyclerView = findViewById(R.id.CategoryRecyclerView);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        getBudgetForCatActivity();
        getLeftToSpendForCatActivity();
        addCategoryItemsToRecycler();

        categorySummaryActionButton = findViewById(R.id.categorySummary);
        categorySummaryActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, CategorySummaryActivity.class);
                intent.putExtra(CATSUMMARY, parentName);
                startActivity(intent);
            }
        });
    }

    private void addCategoryItemsToRecycler() {
        daftViewModel.getSubCategoryAndTransactions().observe(this, new Observer<List<SubCategoryWithTransaction>>() {
            @Override
            public void onChanged(List<SubCategoryWithTransaction> subCategoryWithTransactions) {
                ArrayList<CategoryItem> list = new ArrayList<>();
                categoryItemArrayList.clear();
                for (SubCategoryWithTransaction subCategoryWithTransaction : subCategoryWithTransactions) {
                    if (subCategoryWithTransaction.getSubCategoryEntity().getParentCategoryID().equals(parentID)) {
                        double leftToSpend = (subCategoryWithTransaction.getSubCategoryEntity().getBudget() - subCategoryWithTransaction.getTransactionsTotalAmount());
                        int progress = (int) ((subCategoryWithTransaction.getTransactionsTotalAmount()) / (subCategoryWithTransaction.getSubCategoryEntity().getBudget()) * 100);
                        int warn = 0;
                        if (progress >= 100) {
                            warn = View.VISIBLE;
                        } else {
                            warn = View.INVISIBLE;
                        }
                        categoryItemArrayList.add(new CategoryItem(getResources().getString(R.string.category) + subCategoryWithTransaction.getSubCategoryEntityName(),
                                getResources().getString(R.string.budget) + subCategoryWithTransaction.getSubCategoryEntity().getBudget(),
                                getResources().getString(R.string.leftToSpend) + leftToSpend, progress
                                , warn));
                    }
                }
                adapter.setLandingItems(categoryItemArrayList);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void getLeftToSpendForCatActivity() {
        TextView textViewLeft = findViewById(R.id.left);
        daftViewModel.getSubCategoryAndTransactions().observe(this, new Observer<List<SubCategoryWithTransaction>>() {
            @Override
            public void onChanged(List<SubCategoryWithTransaction> subCategoryWithTransactions) {
                ArrayList<CategoryItem> list = new ArrayList<>();
                double spent = 0.00;
                for (SubCategoryWithTransaction subCategoryWithTransaction : subCategoryWithTransactions) {
                    SubCategoryEntity sT = subCategoryWithTransaction.getSubCategoryEntity();
                    if (sT.getParentCategoryID().equals(parentID)){
                        spent += subCategoryWithTransaction.getTransactionsTotalAmount();
                    }
                }
                double left = parentBudget - spent;
                String str = getResources().getString(R.string.leftToSpend) + String.valueOf(left);
                textViewLeft.setText(str);
            }
        });
    }

    private void getBudgetForCatActivity() {
        TextView textViewBudget = findViewById(R.id.budget);
        daftViewModel.getParentAndSubCategories().observe(this, new Observer<List<ParentCategoryWithSubCategory>>() {
            @Override
            public void onChanged(List<ParentCategoryWithSubCategory> parentCategoryWithSubCategories) {
                for (ParentCategoryWithSubCategory parentCategoryWithSubCategory : parentCategoryWithSubCategories) {
                    String pS = parentCategoryWithSubCategory.getParentCategoryEntity().getParentCategoryName();
                    if(pS.equals(parentName)){
                        parentBudget = parentCategoryWithSubCategory.getParentCategoryEntity().getBudget();
                        String budgetString = String.valueOf(parentBudget);
                        textViewBudget.setText(getResources().getString(R.string.Budget_Header) + parentBudget);
                        parentID = parentCategoryWithSubCategory.getParentCategoryEntity().getId();

                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_sub_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.addCategoryButton == item.getItemId()) {
            Intent intent2 = new Intent(this, NewSubCategoryActivity.class);
            intent2.putExtra(categoryName, parentName);
            startActivity(intent2);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int pos) {
        int k = 9;
        if(categoryItemArrayList.get(pos).getCategoryName().substring(0,9).equals("Category:")){
            k = 10;
        }
        String subName = categoryItemArrayList.get(pos).getCategoryName().substring(k);
        String subBudget = categoryItemArrayList.get(pos).getCategoryAmounts();
        String subLeft = categoryItemArrayList.get(pos).getLeftToSpend();

        Intent intent = new Intent(this, TransactionsActivity.class);
        intent.putExtra(subCategoryName, subName);
        intent.putExtra(subCategoryBudget, subBudget);
        intent.putExtra(subCategoryLeft, subLeft);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        s = manager.onSaveInstanceState();
        saveInstanceState.putParcelable(RECYCLER, s);
        saveInstanceState.putString(TITLE, "Hello");
        super.onSaveInstanceState(saveInstanceState);
    }
}
