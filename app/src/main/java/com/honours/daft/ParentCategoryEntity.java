package com.honours.daft;

import androidx.lifecycle.MutableLiveData;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "parent_category")
public class ParentCategoryEntity {
    @PrimaryKey
    private Integer id = 0;

    private String parentCategoryName;

    private double budget;

    public ParentCategoryEntity(String parentCategoryName, Integer id, double budget) {
        this.parentCategoryName = parentCategoryName;
        this.id = id;
        this.budget = budget;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getParentCategoryName() {
        return parentCategoryName;
    }

    public double getBudget() {
        return budget;
    }

    @Ignore
    public List<SubCategoryEntity> subCategoryEntities;

}
