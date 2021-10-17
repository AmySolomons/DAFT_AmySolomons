package com.honours.daft;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.ArrayList;
import java.util.List;

@Dao
public abstract class ParentCategoryDao {
    final MutableLiveData<Double> amount = new MutableLiveData<>();

    @Insert
    abstract void insertParentCategory(ParentCategoryEntity parentCategoryEntity);

    @Insert
    abstract void insertSubCategory(List<SubCategoryEntity> subCategoryEntities);

    public void insertSubCategoriesForParentCategory(ParentCategoryEntity parentCategoryEntity, List<SubCategoryEntity> subCategoryEntities) {

        //sets up foreign key for sub-category that will be inserted into the
        for (SubCategoryEntity subCategoryEntity : subCategoryEntities) {
            subCategoryEntity.setParentCategoryID(parentCategoryEntity.getId());
        }

        insertListOfSubCategories(subCategoryEntities);
    }

    public void insertSubCategoryForParentCategory(ParentCategoryEntity parentCategoryEntity, SubCategoryEntity subCategoryEntity) {
        subCategoryEntity.setParentCategoryID(parentCategoryEntity.getId());
        List<SubCategoryEntity> tempList = new ArrayList<>();
        tempList.add(subCategoryEntity);
        insertListOfSubCategories(tempList);
    }

    public void insertSubCategoryForParentCategory(Integer parentCategoryEntityID, SubCategoryEntity subCategoryEntity) {
        subCategoryEntity.setParentCategoryID(parentCategoryEntityID);
        List<SubCategoryEntity> tempList = new ArrayList<>();
        tempList.add(subCategoryEntity);
        insertListOfSubCategories(tempList);
    }

    @Insert
    abstract void insertListOfSubCategories(List<SubCategoryEntity> subCategoryEntities);

    @Transaction
    @Query("Select id from PARENT_CATEGORY where parentCategoryName = :name2")
    abstract public Integer getParentCategoryID(String name2);

    @Transaction
    @Query("Select * from PARENT_CATEGORY")
    abstract LiveData<List<ParentCategoryWithSubCategory>> loadParentCategoriesWithSubCategories();

    @Transaction
    @Query("Select * from PARENT_CATEGORY")
    abstract List<ParentCategoryWithSubCategory> getParentCategoriesWithSubCategories2();

    @Transaction
    @Query("Select count(*) from PARENT_CATEGORY")
    abstract public int countParents();

    @Transaction
    @Query("Select count(*) from SUB_CATEGORY")
    abstract public int countSubs();

    @Transaction
    @Query("Select budget from parent_category where parentCategoryName = :arg1")
    abstract public LiveData<Double> budgetForParentCategory(String arg1);

    @Transaction
    @Query("Select SUM(totalAmount) from _transaction where subCategoryID = :arg1")
    abstract public LiveData<Double> totalAmountSpentForSubCategory(Integer arg1);

    @Transaction
    @Query("Select SUM(totalAmount) from _transaction where subCategoryID != 0")
    abstract public Double totalAmountSpent();

    @Transaction
    @Query("Select SUM(budget) from parent_category")
    abstract public Double totalParentBudgetAmount();

    public Double getCurrentAmountOfMoney(){
        MutableLiveData<Double> amount = new MutableLiveData<>();
        amount.postValue(totalParentBudgetAmount() - totalAmountSpent());
        return totalParentBudgetAmount() - totalAmountSpent();
    }

    @Transaction
    @Query("Select id from sub_category where parentCategoryID = 1")
    abstract public List<Integer> getI();

    @Transaction
    @Query("Select SUM(totalAmount) from _transaction where subCategoryID in (Select id from sub_category where parentCategoryID = (Select id from PARENT_CATEGORY where parentCategoryName = :arg1))")
    abstract public LiveData<Double> totalAmountSpentForParentCategory(String arg1);

    @Transaction
    @Query("Select SUM(totalAmount) from _transaction where subCategoryID in (Select id from sub_category where parentCategoryID = (Select id from PARENT_CATEGORY where parentCategoryName = :arg1))")
    abstract public Double totalAmountSpentForParentCategory2(String arg1);

    //TODO: add update and delete

    public Double getParentBudget(String parent){
        List<ParentCategoryWithSubCategory> temp = getParentCategoriesWithSubCategories2();

        for(ParentCategoryWithSubCategory parentCategoryWithSubCategory: temp){
            ParentCategoryEntity p = parentCategoryWithSubCategory.getParentCategoryEntity();
            if (p.getParentCategoryName().equals(parent)){
                return p.getBudget();
            }
        }
        return null;
    }

}
