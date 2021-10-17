package com.honours.daft;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class NewSubCategoryActivity extends AppCompatActivity {
    //Next Button
    private Button nextButton;

    //Radio Button Group
    private RadioGroup parentCategoriesRadioGroup;

    //Name of New Sub-Category
    private EditText newSubCategoryName;

    //Budget of New Sub-Category
    private EditText newSubCategoryBudget;

    private int choosenParentCat;
    private RadioButton radioButtonParentCat;

    public static final String NAME = "com.honours.daft.NAME";
    public static final String BUDGET = "com.honours.daft.BUDGET";
    public static final String PARENT_CATEGORY = "com.honours.daft.PARENT_CATEGORY";
    public static final String SELECTED_CHOICE = "com.honours.daft.SELECTED_CHOICE";
    private String parentCat;
    private String subCatName, subCatBudget;
    private MyViewModel daftViewModel;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sub_category);

        daftViewModel = new ViewModelProvider(this).get(MyViewModel.class);

        Intent in = getIntent();

        //Setup the activity's toolbar
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(R.string.new_sub_category);

        parentCategoriesRadioGroup = findViewById((R.id.parentCatRadioGroup));
        choosenParentCat = parentCategoriesRadioGroup.getCheckedRadioButtonId();
        parentCategoriesRadioGroup.check(in.getIntExtra(PARENT_CATEGORY, 0));

        newSubCategoryName = findViewById(R.id.newSubCatName);
        subCatName = newSubCategoryName.getText().toString();
        newSubCategoryName.setText(in.getStringExtra(ManageTransactionsActivity.NAME));

        newSubCategoryBudget = findViewById(R.id.newSubCatBudget);
        subCatBudget = newSubCategoryBudget.getText().toString();
        newSubCategoryBudget.setText(in.getStringExtra(ManageTransactionsActivity.BUDGET));


        nextButton = findViewById(R.id.nextManageCat);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addSubCategory();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

        cancelButton = findViewById(R.id.cancelNewCat);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewSubCategoryActivity.this, CategoryActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }

    private void addSubCategory() throws ExecutionException, InterruptedException {
        choosenParentCat = parentCategoriesRadioGroup.getCheckedRadioButtonId();
        subCatName = newSubCategoryName.getText().toString();
        subCatBudget = newSubCategoryBudget.getText().toString();

        if (choosenParentCat == -1 || subCatName.isEmpty() || subCatBudget.isEmpty()) {
            Toast.makeText(NewSubCategoryActivity.this, getResources().getString(R.string.new_sub_cat_toast_msg), Toast.LENGTH_SHORT).show();
        } else {
            radioButtonParentCat = (RadioButton) findViewById(choosenParentCat);
            parentCat = String.valueOf(radioButtonParentCat.getText());
            if (parentCat.equals("Necessities") || parentCat.equals("Okudingekayo")){
                parentCat = "Necessities";
            }
            else if (parentCat.equals("Luxuries") || parentCat.equals("Uchitho kubukhazikhazi")){
                parentCat = "Luxuries";
            }
            else{
                parentCat = "Savings";
            }
            Intent intent = new Intent(NewSubCategoryActivity.this, ManageTransactionsActivity.class);
            intent.putExtra(PARENT_CATEGORY, parentCat);
            intent.putExtra(NAME, subCatName);
            intent.putExtra(BUDGET, subCatBudget);
            intent.putExtra(SELECTED_CHOICE, choosenParentCat);
            startActivity(intent);
        }
    }
}
