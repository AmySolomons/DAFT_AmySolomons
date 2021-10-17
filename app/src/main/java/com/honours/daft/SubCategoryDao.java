package com.honours.daft;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.honours.daft.Model.NLG.NLG.MostMoneyData;

import java.util.ArrayList;
import java.util.List;

@Dao
public abstract class SubCategoryDao {

    @Insert
    abstract long insertSubCategory(SubCategoryEntity subCategoryEntity);

    @Insert
    abstract void insertTransactions(List<TransactionEntity> transactionEntities);

    @Insert
    abstract void insertListOfTransactionEntities(List<TransactionEntity> transactionEntities);

    @Transaction
    @Query("UPDATE _transaction set subCategoryID = :arg0 where id = :arg1")
    abstract void updateTransactionSubCategory(Integer arg0, Integer arg1);

    @Transaction
    @Query("Select * from SUB_CATEGORY")
    abstract public LiveData<List<SubCategoryWithTransaction>> getSubCategoriesWithTransactions();

    @Transaction
    @Query("Select * from SUB_CATEGORY")
    abstract public List<SubCategoryWithTransaction> getSubCategoriesWithTransactions2();

    @Transaction
    @Query("Select count(*) from SUB_CATEGORY")
    abstract public int countSubCategoriesWithTransactions();

    @Transaction
    @Query("Select count(*) from _transaction")
    abstract public int countTransactions();

    @Transaction
    @Query("Select count(*) from _transaction where subCategoryID = :x")
    abstract public int countTransactions(long x);

    @Transaction
    @Query("Select * from SUB_CATEGORY where parentCategoryID = :parent1")
    abstract LiveData<List<SubCategoryWithTransaction>> getSubCategoriesWithTransactions(long parent1);

    @Transaction
    @Query("Select SUM(totalAmount) from _transaction where subCategoryID = :arg1")
    abstract double getSubCategoriesTotalAmount(long arg1);


    public double getSubCategoriesTotalAmount(String arg1) {
        List<SubCategoryWithTransaction> temp = getSubCategoriesWithTransactions2();
        double amount = 0.00;
        for (SubCategoryWithTransaction subCategoryWithTransaction : temp) {
            if (subCategoryWithTransaction.getSubCategoryEntityName().equals(arg1)) {
                amount += subCategoryWithTransaction.getTransactionsTotalAmount();
            }
        }
        return amount;

    }

    //TODO: add update and delete

    public void insertTransactionsForSubCategory(SubCategoryEntity subCategoryEntity, List<TransactionEntity> transactionEntities) {
        for (TransactionEntity transactionEntity : transactionEntities) {
            transactionEntity.setSubCategoryID(subCategoryEntity.getId());
        }
        insertListOfTransactionEntities(transactionEntities);
    }

    public void insertTransactionForSubCategory(SubCategoryEntity subCategoryEntity, TransactionEntity transactionEntity) {
        transactionEntity.setSubCategoryID(subCategoryEntity.getId());
        List<TransactionEntity> tempList = new ArrayList<>();
        tempList.add(transactionEntity);
        insertListOfTransactionEntities(tempList);

    }

    public void insertTransactionForSubCategory(Integer subCategoryEntityID, TransactionEntity transactionEntity) {
        transactionEntity.setSubCategoryID(subCategoryEntityID);
        List<TransactionEntity> tempList = new ArrayList<>();
        tempList.add(transactionEntity);
        insertListOfTransactionEntities(tempList);
    }

    @Transaction
    @Query("Select * from sub_category where parentCategoryID = (Select id from parent_category where parentCategoryName = :arg0)")
    abstract public List<SubCategoryEntity> getSubCategoriesForParent(String arg0);

    //Gets the list of over_budget sub-categories. Used for content selection (category summary)
    public List<SubCategoryEntity> getOverBudgetSubCategoriesInParent(String arg0){
        List<SubCategoryEntity> subCategoryEntities = getSubCategoriesForParent(arg0);
        ArrayList<SubCategoryEntity> overbudgetSubCats = new ArrayList<>();
        for (SubCategoryEntity subCategoryEntity: subCategoryEntities){
            double amount = getSubCategoriesTotalAmount(subCategoryEntity.getSubCategoryName());
            if (amount>subCategoryEntity.getBudget()){
                overbudgetSubCats.add(subCategoryEntity);
            }
        }
        return overbudgetSubCats;
    }

    @Transaction
    @Query("Select sum(bankCharge) from _transaction where subCategoryID in (Select id from sub_category where parentCategoryID = (Select id from parent_category where parentCategoryName =:arg0))")
    abstract public Double getBankChargesForAParent(String arg0);

    @Transaction
    @Query("Select count(id) from _transaction where subCategoryID in (Select id from sub_category where parentCategoryID = (Select id from parent_category where parentCategoryName = :arg0))")
    abstract public Integer getNumTransactions(String arg0);

    @Transaction
    @Query("Select * from _transaction where subCategoryID in (Select id from sub_category where parentCategoryID = (Select id from parent_category where parentCategoryName = :arg0)) " +
            "and totalAmount = (select max(totalAmount) from _transaction where subCategoryID in (Select id from sub_category " +
            "where parentCategoryID = (Select id from parent_category where parentCategoryName = :arg0)))")
    abstract public List<TransactionEntity> getBiggestTransaction(String arg0);

    public MostMoneyData getMostMoney(String parentName){
        List<SubCategoryEntity> subCategoryEntities = getSubCategoriesForParent(parentName);
        double highest = 0.00;

        for(SubCategoryEntity subCategoryEntity: subCategoryEntities){
            double totalAmount = getSubCategoriesTotalAmount(subCategoryEntity.getSubCategoryName());
            if (highest < totalAmount){
                highest = totalAmount;
            }
        }
        ArrayList<SubCategoryEntity> subCategoriesWithMostMoney = new ArrayList<>();
        for(SubCategoryEntity subCategoryEntity: subCategoryEntities){
            if (subCategoriesWithMostMoney.size()>4){
                subCategoriesWithMostMoney.clear();
                break;
            }
            double totalAmount = getSubCategoriesTotalAmount(subCategoryEntity.getSubCategoryName());
            if (totalAmount == highest){
                subCategoriesWithMostMoney.add(subCategoryEntity);
            }
        }

        MostMoneyData mostMoneyData = new MostMoneyData(subCategoriesWithMostMoney, highest);
        return mostMoneyData;
    }
}
