package com.honours.daft;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MyViewModel extends AndroidViewModel {
    private MyRepository daftRespository;
    private LiveData<List<ParentCategoryWithSubCategory>> parentAndSubCategories;
    private LiveData<List<SubCategoryWithTransaction>> subCategoryAndTransactions;
    private static String category3Name, category3Budget, category3Left;

    public MyViewModel(@NonNull Application application) {
        super(application);
        daftRespository = new MyRepository(application);
        parentAndSubCategories = daftRespository.getParentCategoriesWithSubCategories();
        subCategoryAndTransactions = daftRespository.getSubCategoriesWithTransactions();
    }

    public void insertSubCategoryInParent(Integer parentID, SubCategoryEntity subCategoryEntity){
        daftRespository.insertSubCategoryInParent(parentID,subCategoryEntity);
    }

    public void insertTransactionWithSubCategory(SubCategoryWithTransaction subCategoryWithTransaction){
        daftRespository.insert(subCategoryWithTransaction);
    }

    public void insertParentCategoryWithSubCategories(ParentCategoryWithSubCategory parentCategoryWithSubCategory){
        daftRespository.insert(parentCategoryWithSubCategory);
    }

    public LiveData<List<ParentCategoryWithSubCategory>> getParentAndSubCategories() {
        return parentAndSubCategories;
    }

    public LiveData<List<SubCategoryWithTransaction>> getSubCategoryAndTransactions() {
        return subCategoryAndTransactions;
    }

    public double getTotalAmountSubCategory(long id){
        return daftRespository.getSubCategoriesTotalAmount(id);
    }

    public LiveData<Double> totalAmountSpentForParentCategory(String arg1){
        return daftRespository.totalAmountSpentForParentCategory(arg1);
    }

    public Double budgetParentCategory(String name) throws ExecutionException, InterruptedException {
        return daftRespository.budgetForParentCategory(name);
    }

    public String getCategory3Name() {
        return category3Name;
    }

    public void setCategory3Name(String category3Name) {
        this.category3Name = category3Name;
    }

    public String getCategory3Budget() {
        return category3Budget;
    }

    public void setCategory3Budget(String category3Budget) {
        this.category3Budget = category3Budget;
    }

    public String getCategory3Left() {
        return category3Left;
    }

    public void setCategory3Left(String category3Left) {
        this.category3Left = category3Left;
    }

    public Integer getParentID(String name) throws ExecutionException, InterruptedException {
        return daftRespository.getParentCategoryID(name);
    }

    public Integer getNumberSubs() throws ExecutionException, InterruptedException {
        return daftRespository.getNumberSubs();
    }


    public LiveData<List<SubCategoryWithTransaction>> getSubCategoriesWithTransactions() {
        return daftRespository.getSubCategoriesWithTransactions();
    }

    public void updateTransactionSubCategory(Integer subCatId, Integer transactionId){
        daftRespository.updateTransactionSubCategory(subCatId, transactionId);
    }

    public String generateOverallSummary() throws ExecutionException, InterruptedException {
        return daftRespository.generateOverallSummary();
    }

    public String generateCategorySummary(String parentCat) throws ExecutionException, InterruptedException {
        return daftRespository.generateCategorySummary(parentCat);
    }

    public Double getCurrentBalance() throws ExecutionException, InterruptedException {
        return daftRespository.getCurrentAmountOfMoney();
    }
}
