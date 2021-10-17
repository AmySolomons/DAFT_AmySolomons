package com.honours.daft;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.ExecutionException;

public class CategorySummaryActivity extends AppCompatActivity {

    private MyViewModel daftViewModel;
    private String parentCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        Intent in = getIntent();
        parentCat = in.getStringExtra(CategoryActivity.CATSUMMARY);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(parentCat + " Summary");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView summary = findViewById(R.id.summaryView);
        daftViewModel = new ViewModelProvider(this).get(MyViewModel.class);

        String sum = null;
        try {
            sum = daftViewModel.generateCategorySummary(parentCat);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("CSA generated string", sum);
        summary.setText(sum);
    }
}
