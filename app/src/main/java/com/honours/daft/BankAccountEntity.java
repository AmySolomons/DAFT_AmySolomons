package com.honours.daft;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "bank_account")
public class BankAccountEntity {
    //automatically generate a unique id as primary key
    @PrimaryKey(autoGenerate = true)
    private int id;

    //different from primary key to ensure that unique primary keys are generated
    private int bankAccountID;
    private String bankAccountName;
    private double bankAccountBalance;

    public BankAccountEntity(int bankAccountID, String bankAccountName, double bankAccountBalance) {
        this.bankAccountID = bankAccountID;
        this.bankAccountName = bankAccountName;
        this.bankAccountBalance = bankAccountBalance;
    }

    //Getter and Setter methods for the above fields
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBankAccountID() {
        return bankAccountID;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public double getBankAccountBalance() {
        return bankAccountBalance;
    }

    @Ignore
    public BankAccountEntity(int bankAccountID) {
        this.bankAccountID = bankAccountID;
    }
}