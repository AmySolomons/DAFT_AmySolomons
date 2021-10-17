package com.honours.daft;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LandingAdapter.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout navDrawer;

    private RecyclerView recyclerView;
    private LandingAdapter adapter;
    private RecyclerView.LayoutManager manager;
    private ArrayList<LandingItem> landingItemArrayList;

    public static final String parentCategoryName = "com.honours.daft.parentCategoryName";
    public static final String parentCategoryLeftToSpend = "com.honours.daft.parentCategoryLeftToSpend";
    public static final String parentCategoryBudget = "com.honours.daft.parentCategoryBudget";

    private MyViewModel daftViewModel;
    private Double savingsSpent = 0.0;
    private Double necessitesSpent = 0.0;
    private Double luxuriesSpent = 0.0;

    private FloatingActionButton overallSummaryActionButton;
    public static final String OVERALL = "com.honours.daft.overallSummary";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("");
        navDrawer = findViewById(R.id.drawer_layout);

        NavigationView view = findViewById(R.id.drawer_view);
        view.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, navDrawer, myToolbar, R.string.navigation_open, R.string.navigation_closed);
        navDrawer.addDrawerListener(toggle);
        toggle.syncState();

        //TODO: Textview for the overview and total amount of money in bank accounts should either be retrieved from bank account table or
        // calculated based on initial amount in account

        manager = new LinearLayoutManager(this);
        landingItemArrayList = new ArrayList<>();
        adapter = new LandingAdapter(landingItemArrayList, this);

        recyclerView = findViewById(R.id.landingRecyclerView);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        daftViewModel = new ViewModelProvider(this).get(MyViewModel.class);

        getObservedSavingsSpent();
        getObservedAmountSpentOnNecessCategory();
        getObservedAmountSpentOnLuxuries();
        getObservedParentCatWithSubCat();

        overallSummaryActionButton = findViewById(R.id.overallSummary);
        overallSummaryActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OverallSummaryActivity.class);
                intent.putExtra(OVERALL, "Overall Summary");
                startActivity(intent);
            }
        });

    }

    private void getObservedParentCatWithSubCat() {
        daftViewModel.getParentAndSubCategories().observe(this, new Observer<List<ParentCategoryWithSubCategory>>() {
            @Override
            public void onChanged(List<ParentCategoryWithSubCategory> parentCategoryWithSubCategories) {
                landingItemArrayList.clear();
                for (ParentCategoryWithSubCategory parentCategoryWithSubCategory : parentCategoryWithSubCategories) {
                    ParentCategoryEntity p = parentCategoryWithSubCategory.getParentCategoryEntity();
                    double budget = p.getBudget();
                    String budgetStr = getResources().getString(R.string.Budget_Header)+ budget;
                    double left = budget;
                    int progress = 0;
                    if (p.getParentCategoryName().equals("Savings")) {
                        left -= savingsSpent;
                         progress = (int) (savingsSpent / (budget) * 100);
                        landingItemArrayList.add(new LandingItem(getResources().getString(R.string.savings), budgetStr, getResources().getString(R.string.leftToSpend) + left, progress, "S"));
                    } else if (p.getParentCategoryName().equals("Necessities")) {
                        left -= necessitesSpent;
                        progress = (int) (necessitesSpent / (budget) * 100);
                        landingItemArrayList.add(new LandingItem(getResources().getString(R.string.necessities), budgetStr, getResources().getString(R.string.leftToSpend)+ left, progress, "N"));
                    } else if (p.getParentCategoryName().equals("Luxuries")){
                        left -= luxuriesSpent;
                        progress = (int) (luxuriesSpent / (budget) * 100);
                        landingItemArrayList.add(new LandingItem(getResources().getString(R.string.luxuries), budgetStr, getResources().getString(R.string.leftToSpend) + left, progress, "L"));
                    }
                    else{
                        continue;
                    }
                }
                adapter.setLandingItems(landingItemArrayList);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void getObservedAmountSpentOnLuxuries() {
        daftViewModel.totalAmountSpentForParentCategory("Luxuries").observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if (aDouble != null) {
                    luxuriesSpent = aDouble;
                }
            }
        });
    }

    private void getObservedAmountSpentOnNecessCategory() {
        daftViewModel.totalAmountSpentForParentCategory("Necessities").observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if (aDouble != null) {
                    necessitesSpent = aDouble;
                }

            }
        });
    }

    private void getObservedSavingsSpent() {
        daftViewModel.totalAmountSpentForParentCategory("Savings").observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if (aDouble != null) {
                    savingsSpent = aDouble;
                }
            }
        });
    }


    @Override
    public void onItemClick(int pos) {
        String parentName;
        if (landingItemArrayList.get(pos).getCategoryName().equals(getResources().getString(R.string.necessities))) {
            parentName = "Necessities";
        }
        else if(landingItemArrayList.get(pos).getCategoryName().equals(getResources().getString(R.string.luxuries))){
            parentName = "Luxuries";
        }
        else{
            parentName = "Savings";
        }
        String parentBudget = landingItemArrayList.get(pos).getCategoryBudget();
        String parentLeftToSpend = landingItemArrayList.get(pos).getCategoryLeft();

        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra(parentCategoryName, parentName);
        intent.putExtra(parentCategoryBudget, parentBudget);
        intent.putExtra(parentCategoryLeftToSpend, parentLeftToSpend);
        startActivity(intent);

        daftViewModel.setCategory3Name(parentName);
        daftViewModel.setCategory3Budget(parentBudget);
        daftViewModel.setCategory3Left(parentLeftToSpend);
    }

    @Override
    public void onBackPressed() {
        if (navDrawer.isDrawerOpen(GravityCompat.START)) {
            navDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int i = menuItem.getItemId();

        if (i == R.id.navigation_item1) {
            Intent in1 = new Intent(this, MyBankAccounts.class);
            startActivity(in1);
        } else if (i == R.id.navigation_item2) {
            Intent in2 = new Intent(this, MyCategories.class);
            startActivity(in2);
        } else if (i == R.id.navigation_item3) {
            Intent in3 = new Intent(this, SettingsActivity.class);
            startActivity(in3);
        } else {
            Intent in4 = new Intent(this, Help.class);
            startActivity(in4);
        }
        return true;
    }
}
