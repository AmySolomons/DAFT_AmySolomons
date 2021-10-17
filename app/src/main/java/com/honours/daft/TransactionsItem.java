package com.honours.daft;

public class TransactionsItem {
    private String transactionRef;
    private String bankAccount;
    private String transactionAmount;
    private String transactionDate;

    public TransactionsItem(String ref, String account, String amount, String date) {
        transactionRef = ref;
        bankAccount = account;
        transactionAmount = amount;
        transactionDate = date;
    }

    public String getTransactionRef() {
        return transactionRef;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public String getTransactionDate() {
        return transactionDate;
    }
}
