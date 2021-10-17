package com.honours.daft;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class SubCategoryWithTransaction {
    @Embedded
    private SubCategoryEntity subCategoryEntity;
    @Relation(
            parentColumn = "id",
            entityColumn = "subCategoryID"
    )
    private List<TransactionEntity> transactions;

    public SubCategoryEntity getSubCategoryEntity() {
        return subCategoryEntity;
    }

    public String getSubCategoryEntityName() {
        return subCategoryEntity.getSubCategoryName();
    }

    public List<TransactionEntity> getTransactions() {
        return transactions;
    }

    public  double getTransactionsTotalAmount(){
        List<TransactionEntity> list = transactions;
        double amount = 0;
        if (list != null){
            for(TransactionEntity transactionEntity: list){
                amount += transactionEntity.getTotalAmount();
            }
        }
        return amount;
    }

    public SubCategoryWithTransaction(SubCategoryEntity subCategoryEntity, List<TransactionEntity> transactions) {
        this.subCategoryEntity = subCategoryEntity;
        this.transactions = transactions;
    }
}
