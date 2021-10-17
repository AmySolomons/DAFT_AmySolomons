package com.honours.daft.Model.NLG.NLG;

import android.content.Context;
import android.util.Log;

import com.honours.daft.MyRepository;
import com.honours.daft.SubCategoryEntity;
import com.honours.daft.TransactionEntity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

class CategorySummaryContentSelection {
    private final String category;
    MyRepository respository;
    Context context;

    public CategorySummaryContentSelection(MyRepository respository, Context context, String category) {
        this.respository = respository;
        this.context = context;
        this.category = category;
    }

    //Generates an overall summary of the user's budget
    public String generateCategorySummary() throws ExecutionException, InterruptedException {
        //Create message about the status of the user's budget for a particular category
        Message msg1 = createStatusOfBudget();

        //Creates the bank charges message to determine total bank charges for parent category
        Message msg2 = createBankCharges();

        //Create the message regarding the user's current mostMoneyAmount of money
        Message msg3 = createNumTransactions();

        //Create message about the percentages
        Message msg4 = createBiggestTransaction();


        //Create message about the user's savings status
        Message msg5 = createMostMoney();

        return concatSummaryMessages(msg1,msg2, msg3, msg4, msg5);

        //return "Content selection 2 working";
    }

    private String concatSummaryMessages(Message one, Message two, Message three, Message four, Message five) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String str = "";

        // Create string message for message1
        if (one.getArguments().get("status").equals("OVER-BUDGET")) {
            str = str.concat("You are going over budget in ");
            if (one.getArguments().get("NumSubCategories") != null){
                str = str.concat(one.getArguments().get("NumSubCategories") + " categories by R" +decimalFormat.format(Double.parseDouble(one.getArguments().get("TotalValue"))));
            }
            else{
                str = str.concat(one.getArguments().get("SubCategory1") + " by R" + decimalFormat.format(Double.parseDouble(one.getArguments().get("Value1"))));
            }
            if (one.getArguments().size() == 5){
                str = str.concat(" and in " + one.getArguments().get("SubCategory2") + " by R" + decimalFormat.format(Double.parseDouble(one.getArguments().get("Value2"))));
            }
            str = str.concat(". ");
        }
        else{
            str = str.concat("You are not going over budget in any of your sub-categories in this category. ");
        }

        //Create Bank charges string
        str = str.concat("Your bank charges for this category amount to R" + decimalFormat.format(Double.parseDouble(two.getArguments().get("argument2"))) + ", and ");

        //Create num of transactions message
        str = str.concat("you have made " + three.getArguments().get("argument2") + " transactions for this category. ");

        //Create biggest transaction message
        if (four.getArguments().size() > 1){
            if (four.getArguments().get("Date1") != null && four.getArguments().get("Date2") == null ){
                str = str.concat("Your biggest transaction, " + four.getArguments().get("Transaction1") + ", occurred on " + four.getArguments().get("Date1") + " and");
            }
            else if(four.getArguments().get("Date1") != null && four.getArguments().get("Date2") != null){
                str = str.concat("Your biggest transactions, namely, " + four.getArguments().get("Transaction1") + " on " + four.getArguments().get("Date1")
                        + " and " + four.getArguments().get("Transaction2") + " on " + four.getArguments().get("Date2") + ", each");
            }
            else{
                str = str.concat("Your biggest transactions, namely, ");
                for(int i = 1; i<four.getArguments().size()-1; i++){
                    str = str.concat(four.getArguments().get("Transaction" + i) );

                    if (i != four.getArguments().size()-2 && i != four.getArguments().size()-3){
                        str = str.concat(", ");
                    }
                    if (i == four.getArguments().size()-3){
                        str = str.concat(" and ");
                    }
                }
                str = str.concat(", each");
            }
            str = str.concat(" amounted to R" + decimalFormat.format(Double.parseDouble(four.getArguments().get("Amount"))) + ". ");
        }

        //Create most money string
        if (five.getArguments().size()>0){
            str = str.concat("You spent most of your money on ");
            int k = 0;
            for(int i = 1; i <five.getArguments().size()-1; i++){
                str = str.concat(five.getArguments().get("SubCategory" + i));
                if (i!= five.getArguments().size()-2 && i!= five.getArguments().size()-3){
                    str = str.concat(", ");
                }
                if (i == five.getArguments().size()-3){
                    str = str.concat(" and ");
                }
                k = i;
            }
            if(k>1) {
                str = str.concat(" with each sub-category amounting to R" + decimalFormat.format(Double.parseDouble(five.getArguments().get("Amount"))) + ".");
            }
            else{
                str = str.concat(", which amounted to R" + decimalFormat.format(Double.parseDouble(five.getArguments().get("Amount"))) + ".");
            }
        }

        return str;
    }

    private Message createStatusOfBudget() throws ExecutionException, InterruptedException {
        LinkedHashMap<String, String> arguments = new LinkedHashMap<>();
        List<SubCategoryEntity> overbudgetSubCategories = respository.GetOverBudgetSubCategoriesInParent(category);
        if (overbudgetSubCategories.isEmpty()) {
            arguments.put("status", "NOT-OVER-BUDGET");
            arguments.put("SubCategory1", "ANY-SUB-CATEGORY");
        } else if (overbudgetSubCategories.size() < 3) {
            arguments.put("status", "OVER-BUDGET");
            int k = 1;
            for (SubCategoryEntity subCategoryEntity : overbudgetSubCategories) {
                arguments.put("SubCategory" + k, subCategoryEntity.getSubCategoryName());
                double overSpent = respository.getSpentAmountSubCategory(subCategoryEntity.getId()) - subCategoryEntity.getBudget();
                arguments.put("Value" + k, String.valueOf(overSpent));
                k++;
            }
        } else {
            arguments.put("status", "OVER-BUDGET");
            arguments.put("NumSubCategories", String.valueOf(overbudgetSubCategories.size()));
            double totalOver = 0.00;
            for (SubCategoryEntity subCategoryEntity : overbudgetSubCategories) {
                totalOver += respository.getSpentAmountSubCategory(subCategoryEntity.getId()) - subCategoryEntity.getBudget();
            }
            //double v = respository.totalAmountSpentForParentCategory2(category) - respository.budgetForParentCategory(category);
            arguments.put("TotalValue", String.valueOf(totalOver));
        }

        Log.d("CS2 Msg1", String.valueOf(arguments));

        return new Message("msg1", "statusOfBudgets", arguments);
    }

    private Message createBankCharges() throws ExecutionException, InterruptedException {
        LinkedHashMap<String, String> arguments = new LinkedHashMap<>();
        arguments.put("argument1", "BANK-CHARGES");
        arguments.put("argument2", String.valueOf(respository.getBankChargesForAParent(category)));
        Log.d("CS2 Msg2", String.valueOf(arguments));
        return new Message("msg2", "bankCharges", arguments);
    }

    private Message createNumTransactions() throws ExecutionException, InterruptedException {
        LinkedHashMap<String, String> arguments = new LinkedHashMap<>();
        arguments.put("argument1", "NUMBER-OF-TRANSACTIONS");
        arguments.put("argument2", String.valueOf(respository.getNumTransactions(category)));
        Log.d("CS2 Msg3", String.valueOf(arguments));
        return new Message("msg3", "numTransactions", arguments);
    }

    private Message createBiggestTransaction() throws ExecutionException, InterruptedException {
        LinkedHashMap<String, String> arguments = new LinkedHashMap<>();
        arguments.put("status", "BIGGEST-TRANSACTION");
        ArrayList<TransactionEntity> bigTransactions = (ArrayList<TransactionEntity>) respository.getBiggestTransaction(category);
        if (bigTransactions.size() < 5 && !bigTransactions.isEmpty()) {
            int k = 1;
            for (TransactionEntity transactionEntity : bigTransactions) {
                arguments.put("Transaction" + k, transactionEntity.getReference());
                if (bigTransactions.size() < 3) {
                    arguments.put("Date" + k, transactionEntity.getDate());
                }
                k++;
            }
            arguments.put("Amount", String.valueOf(bigTransactions.get(0).getTotalAmount()));
        }

        Log.d("CS2 Msg4", String.valueOf(arguments));

        return new Message("msg4", "biggestTransaction", arguments);
    }

    private Message createMostMoney() throws ExecutionException, InterruptedException {
        LinkedHashMap<String, String> arguments = new LinkedHashMap<>();
        MostMoneyData subCategoryEntitiesWithMostMoney = respository.getMostMoney(category);
        if (!subCategoryEntitiesWithMostMoney.getSubCategoryEntities().isEmpty()) {
            arguments.put("ParentCategory", category);
            int k = 1;
            for (SubCategoryEntity subCategoryEntity : subCategoryEntitiesWithMostMoney.getSubCategoryEntities()) {
                arguments.put("SubCategory" + k, subCategoryEntity.getSubCategoryName());
                k++;
            }
            arguments.put("Amount", String.valueOf(subCategoryEntitiesWithMostMoney.mostMoneyAmount));
        }

        Log.d("CS2 Msg5", String.valueOf(arguments));

        return new Message("msg4", "biggestTransaction", arguments);
    }

}
