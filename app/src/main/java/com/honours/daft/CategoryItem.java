package com.honours.daft;

public class CategoryItem {
    private String categoryName;
    private String categoryAmounts;
    private int prog;
    private String leftToSpend;
    private int overBudgetWarn;

    public CategoryItem(String name, String budget, String left, int progress, int visible) {
        categoryName = name;
        categoryAmounts = budget;
        leftToSpend = left;
        prog = progress;
        overBudgetWarn = visible;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryAmounts() {
        return categoryAmounts;
    }

    public int getProgress() {
        return prog;
    }

    public String getLeftToSpend() {
        return leftToSpend;
    }

    public int getOverBudgetWarn() {
        return overBudgetWarn;
    }
}
