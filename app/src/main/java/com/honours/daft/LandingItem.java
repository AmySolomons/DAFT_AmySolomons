package com.honours.daft;

import android.graphics.drawable.Drawable;

public class LandingItem {
    private String categoryName;
    private String categoryBudget;
    private String categoryLeft;
    private int prog;
    private String barType;

    public LandingItem(String name, String bAmount, String leftAmount, int progress, String type) {
        categoryName = name;
        categoryBudget = bAmount;
        categoryLeft = leftAmount;
        prog = progress;
        barType = type;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryBudget() {
        return categoryBudget;
    }

    public String getCategoryLeft() {
        return categoryLeft;
    }

    public int getProgress() {
        return prog;
    }

    public String getBarType() {
        return barType;
    }

}
