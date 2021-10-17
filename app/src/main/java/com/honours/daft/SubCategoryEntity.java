package com.honours.daft;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "sub_category")
public class SubCategoryEntity {
    @PrimaryKey
    private Integer id;

    private Integer parentCategoryID;

    private String subCategoryName;

    private double budget;

    public SubCategoryEntity(String subCategoryName, Integer id, double budget) {
        this.subCategoryName = subCategoryName;
        this.id = id;
        this.budget = budget;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentCategoryID() {
        return parentCategoryID;
    }

    public void setParentCategoryID(Integer parentCategoryID) {
        this.parentCategoryID = parentCategoryID;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public double getBudget() {
        return budget;
    }

    @Ignore
    public List<TransactionEntity> transactionEntities;
}
