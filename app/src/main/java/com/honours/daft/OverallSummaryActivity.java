package com.honours.daft;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

public class OverallSummaryActivity extends AppCompatActivity {

    private MyViewModel daftViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        Intent in = getIntent();

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(in.getStringExtra(MainActivity.OVERALL));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        daftViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        TextView summary = findViewById(R.id.summaryView);

        try {
            String sum = daftViewModel.generateOverallSummary();
            Log.d("OSA generated string", sum);
            summary.setText(sum);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
