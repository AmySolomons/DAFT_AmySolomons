package com.honours.daft.Model.NLG.NLG;

import com.honours.daft.SubCategoryEntity;

import java.util.List;

public class MostMoneyData {
    List<SubCategoryEntity> subCategoryEntities;
    Double mostMoneyAmount;

    public MostMoneyData(List<SubCategoryEntity> subCategoryEntities, Double mostMoneyAmount) {
        this.subCategoryEntities = subCategoryEntities;
        this.mostMoneyAmount = mostMoneyAmount;
    }

    public List<SubCategoryEntity> getSubCategoryEntities() {
        return subCategoryEntities;
    }

    public void setSubCategoryEntities(List<SubCategoryEntity> subCategoryEntities) {
        this.subCategoryEntities = subCategoryEntities;
    }

    public Double getMostMoneyAmount() {
        return mostMoneyAmount;
    }

    public void setMostMoneyAmount(Double mostMoneyAmount) {
        this.mostMoneyAmount = mostMoneyAmount;
    }
}
