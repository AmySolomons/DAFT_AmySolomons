package com.honours.daft;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class ParentCategoryWithSubCategory {
    @Embedded
    private ParentCategoryEntity parentCategoryEntity;
    @Relation(
            parentColumn = "id",
            entityColumn = "parentCategoryID"
    )
    private List<SubCategoryEntity> subCategories;

    public ParentCategoryWithSubCategory(ParentCategoryEntity parentCategoryEntity, List<SubCategoryEntity> subCategories) {
        this.parentCategoryEntity = parentCategoryEntity;
        this.subCategories = subCategories;
    }

    public ParentCategoryEntity getParentCategoryEntity() {
        return parentCategoryEntity;
    }

    public List<SubCategoryEntity> getSubCategories() {
        return subCategories;
    }
}
