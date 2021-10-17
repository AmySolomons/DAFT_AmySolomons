package com.honours.daft.Model.NLG.NLG;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.honours.daft.MyRepository;

import java.text.DecimalFormat;
import java.util.Formatter;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutionException;

public class OverallSummaryContentSelection {
    private final MyRepository repository;
    Context context;

    public OverallSummaryContentSelection(MyRepository respository, Context context) {
        this.repository = respository;
        this.context = context;
    }

    //Generates an overall summary of the user's budget
    public String generateOverallSummary() throws ExecutionException, InterruptedException {
        Formatter formatter = new Formatter();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        //Creates the budget period message
        Message msg1 = createBudgetPeriod();
        String str = "Your budget started " + msg1.getArguments().get("argument2");
        str = str.concat(", and it ends " + msg1.getArguments().get("argument4"));

        //Create the message regarding the user's current mostMoneyAmount of money
        Message msg2 = createCurrentAmount();
        formatter.format("%.2f", Double.parseDouble(msg2.getArguments().get("argument2")));
        str = str.concat(". You have R" + formatter.toString() + " in your bank accounts. ");

        //Create message about the percentages
        Message msg3 = createBudgetValues();
        str = str.concat("Your Necessities budget amounts to R" + decimalFormat.format(Double.parseDouble(msg3.getArguments().get("amount1"))) + ", your Luxuries budget amounts to R");
        str = str.concat( decimalFormat.format(Double.parseDouble(msg3.getArguments().get("amount2"))) + ", and your Savings goal is R");
        str = str.concat(decimalFormat.format(Double.parseDouble(msg3.getArguments().get("amount3")))  + ". ");

        //Create message about the status of the user's budget
        Message msg4 = createStatusOfBudget();
        if (msg4.getArguments().get("status").equals("OVER-BUDGET")) {
            str = str.concat("You are over budget in your " + msg4.getArguments().get("Category1") + ", which amounts to R");
            str = str.concat(decimalFormat.format(Math.abs(Double.parseDouble(msg4.getArguments().get("Value1")))) + ". " );
            if (msg4.getArguments().size() > 3) {
                str = str.concat("You are also over budget in your " + msg4.getArguments().get("Category2") + ", which amounts to R" + decimalFormat.format(Math.abs(Double.parseDouble(msg4.getArguments().get("Value2"))))+ ". ");
            }
        } else if (msg4.getArguments().get("status").equals("CLOSE-TO-BUDGET")) {
            str = str.concat("You are close to your " + msg4.getArguments().get("Category1"));
            if (msg4.getArguments().size() == 3) {
                str = str.concat(" and " + msg4.getArguments().get("Category2") + "budgets.");
            } else str = str.concat(" budget.");
        } else {
            str = str.concat("You are not going over budget for Necessities or Luxuries. ");
        }

        //Create message about the user's savings status
        Message msg5 = createStatusSavingBudget();
        if (msg5.getArguments().get("status").equals("OVER-SAVING")) {
            str = str.concat("You are over-saving by R" + decimalFormat.format(Double.parseDouble(msg5.getArguments().get("mostMoneyAmount")))+ ".");
        }
        else if (msg5.getArguments().get("status").equals("REACHED-SAVINGS-GOAL")) {
            str = str.concat("You have reached your saving goal of R" + decimalFormat.format(Double.parseDouble(msg5.getArguments().get("mostMoneyAmount"))) + ".");
        }
        else {
            formatter.format("%.2f", Double.parseDouble(msg5.getArguments().get("mostMoneyAmount")));
            str = str.concat(" You have not reached your savings goal, and you have R" + decimalFormat.format(Double.parseDouble(msg5.getArguments().get("mostMoneyAmount"))) + " left to save.");
        }
        return str;

        //return "Content selection working";
    }

    //gets the start and end date of the budgeting period
    private Message createBudgetPeriod() {
        LinkedHashMap<String, String> arguments = new LinkedHashMap<>();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        //Add the arguments regarding the start date to the message object
        String startDate = sharedPreferences.getString("start_date", "01/01/2021");
        arguments.put("argument1", "BUDGET-START-DATE");
        arguments.put("argument2", startDate);

        //Add the arguments regarding the end date to the message object
        String endDate = sharedPreferences.getString("end_date", "31/01/2021");
        arguments.put("argument3", "BUDGET-END-DATE");
        arguments.put("argument4", endDate);

        //Create the message object
        Message msg1 = new Message("msg1", "budgetPeriod", arguments);
        Log.d("CS Msg1", String.valueOf(arguments));

        //Return the message object
        return msg1;
    }

    //simple content selection rule for current mostMoneyAmount of money
    private Message createCurrentAmount() throws ExecutionException, InterruptedException {
        LinkedHashMap<String, String> arguments = new LinkedHashMap<>();
        arguments.put("argument1", "CURRENT-AMOUNT-OF-MONEY");
        arguments.put("argument2", String.valueOf(repository.getCurrentAmountOfMoney()));
        Log.d("CS Msg2", String.valueOf(arguments));

        return new Message("msg1", "currentAmount", arguments);
    }

    //Removed from content selection based on results from questionnaires
//    private Message createBudgetPercentage() {}

    //Creates the message regarding the saved budget values for the parent categories
    private Message createBudgetValues() throws ExecutionException, InterruptedException {
        LinkedHashMap<String, String> arguments = new LinkedHashMap<>();

        //get the budget value for the necessities category
        arguments.put("category1", "NECESSITIES");
        arguments.put("amount1", String.valueOf(repository.budgetForParentCategory("Necessities")));

        //get the budget value for the luxuries category
        arguments.put("category2", "LUXURIES");
        arguments.put("amount2", String.valueOf(repository.budgetForParentCategory("Luxuries")));

        //get the budget value for the savings category
        arguments.put("category3", "SAVINGS");
        arguments.put("amount3", String.valueOf(repository.budgetForParentCategory("Savings")));

        Log.d("CS Msg3", String.valueOf(arguments));

        //creates the message with the arguments above
        return new Message("msg3", "budgetValues", arguments);
    }

    //content selection rules for the necessities and luxuries budgets
    private Message createStatusOfBudget() throws ExecutionException, InterruptedException {
        LinkedHashMap<String, String> arguments = new LinkedHashMap<>();

        //Get the budget values of the necessities and luxuries categories
        Double necessitiesBudget = repository.budgetForParentCategory("Necessities");
        if (necessitiesBudget == null) {
            necessitiesBudget = 0.00;
        }
        Double luxuriesBudget = repository.budgetForParentCategory("Luxuries");
        if (luxuriesBudget == null) {
            luxuriesBudget = 0.00;
        }

        Double necessitiesSpent = repository.totalAmountSpentForParentCategory2("Necessities");
        if (necessitiesSpent == null) {
            necessitiesSpent = 0.00;
        }
        Double luxuriesSpent = repository.totalAmountSpentForParentCategory2("Luxuries");
        if (luxuriesSpent == null) {
            luxuriesSpent = 0.00;
        }

        Double necessitiesLeftToSpend = necessitiesBudget - necessitiesSpent;
        Double luxuriesLeftToSpend = luxuriesBudget - luxuriesSpent;
        if (necessitiesLeftToSpend < 0.00 || luxuriesLeftToSpend < 0.00) {
            arguments.put("status", "OVER-BUDGET");
            int k = 1;
            if (necessitiesLeftToSpend < 0) {
                arguments.put("Category" + k, "Necessities");
                arguments.put("Value" + k, String.valueOf(necessitiesLeftToSpend - necessitiesBudget));
                k++;
            }
            if (luxuriesLeftToSpend < 0) {
                arguments.put("Category" + k, "Luxuries");
                arguments.put("Value" + k, String.valueOf(luxuriesLeftToSpend - luxuriesBudget));
            }
        } else if (necessitiesSpent / necessitiesBudget >= 0.75 || luxuriesSpent / luxuriesBudget > 0.75) {
            arguments.put("status", "CLOSE-TO-BUDGET");
            int k = 1;
            if (necessitiesSpent / necessitiesBudget >= 0.75) {
                arguments.put("Category" + k, "Necessities");
            }
            if (luxuriesSpent / luxuriesBudget >= 0.75) {
                arguments.put("Category" + k, "Luxuries");
            }
        } else {
            arguments.put("status", "NOT-OVER-BUDGET");
        }
        Log.d("CS Msg4", String.valueOf(arguments));

        //creates budget message with selected argument/s
        return new Message("msg4", "statusOfBudgets", arguments);
    }

    //content selection rules for the savings budget
    private Message createStatusSavingBudget() throws ExecutionException, InterruptedException {
        LinkedHashMap<String, String> arguments = new LinkedHashMap<>();

        //Get the Saving Budget and mostMoneyAmount spent on Savings
        Double savingBudget = repository.budgetForParentCategory("Savings");
        Double savingsSpent = repository.totalAmountSpentForParentCategory2("Savings");

        if (savingBudget != null && savingsSpent != null) {
            //if saving more than budgeted mostMoneyAmount
            if (savingBudget < savingsSpent) {
                arguments.put("status", "OVER-SAVING");
                arguments.put("mostMoneyAmount", String.valueOf(savingsSpent - savingBudget));
            }
            //if reached savings goal
            else if (savingBudget == savingsSpent) {
                arguments.put("status", "REACHED-SAVINGS-GOAL");
                arguments.put("mostMoneyAmount", String.valueOf(savingBudget));
            }
            //if savings goal not reached
            else {
                arguments.put("status", "NOT-REACHED-SAVINGS-GOAL");
                arguments.put("mostMoneyAmount", String.valueOf(savingBudget - savingsSpent));
            }

            Log.d("CS Msg5", String.valueOf(arguments));

            //creates savings budget message with selected argument/s
            return new Message("msg5", "statusSavingBudget", arguments);
        }
        return null;
    }
}
