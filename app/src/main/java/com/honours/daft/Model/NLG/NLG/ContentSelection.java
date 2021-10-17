package com.honours.daft.Model.NLG.NLG;

import android.content.Context;

import com.honours.daft.MyRepository;

import java.util.concurrent.ExecutionException;

public class ContentSelection {
    private MyRepository repository;
    Context context;

    public ContentSelection(MyRepository repository, Context context) {
        this.repository = repository;
        this.context = context;
    }

    //Creates an instance of OverallSummaryContentSelection and creates an overall summary
    public String generateOverallSummary() throws ExecutionException, InterruptedException {
        OverallSummaryContentSelection overallSummaryContentSelection = new OverallSummaryContentSelection(repository, context);
        return overallSummaryContentSelection.generateOverallSummary();
    }

    //Creates an instance of CategorySummaryContentSelection and creates a summary for a particular class
    public String generateCategorySummary(String categoryName) throws ExecutionException, InterruptedException {
        CategorySummaryContentSelection categorySummaryContentSelection = new CategorySummaryContentSelection(repository, context, categoryName);
        return categorySummaryContentSelection.generateCategorySummary();
    }
}
