package com.honours.daft;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

//note that if a transaction becomes de-categorised then its parent and sub categories will change to ignored so no onDelete or onUpdate used
@Entity(tableName = "_transaction")
public class TransactionEntity {

    @PrimaryKey
    private Integer id;

    private String reference;
    //TODO: incorporate bank account foreign key
    private int bankAccountID;
    private double totalAmount;
    private double bankCharge;
    private String date;

    private Integer subCategoryID;

    public TransactionEntity(String reference, int bankAccountID, double totalAmount, double bankCharge, String date, Integer id) {
        this.reference = reference;
        this.bankAccountID = bankAccountID;
        this.totalAmount = totalAmount;
        this.bankCharge = bankCharge;
        this.date = date;
        this.id = id;
    }

    //Getter and Setter methods for the above fields
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setSubCategoryID(Integer subCategoryID) {
        this.subCategoryID = subCategoryID;
    }

    public Integer getSubCategoryID() {
        return subCategoryID;
    }

    public String getReference() {
        return reference;
    }

    public int getBankAccountID() {
        return bankAccountID;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public double getBankCharge() {
        return bankCharge;
    }

    public String getDate() {
        return date;
    }

}


