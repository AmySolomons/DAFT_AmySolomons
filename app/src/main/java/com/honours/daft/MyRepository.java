package com.honours.daft;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.honours.daft.Model.NLG.NLG.ContentSelection;
import com.honours.daft.Model.NLG.NLG.MostMoneyData;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MyRepository {
    private ParentCategoryDao parentCategoryDao;
    private SubCategoryDao subCategoryDao;

    private LiveData<List<ParentCategoryWithSubCategory>> subCategories;
    private LiveData<List<SubCategoryWithTransaction>> transactions;

    private Context context;

    public MyRepository(Application app) {
        MyDatabase daftDatabase = MyDatabase.getMyDatabase(app);

        context = app;

        parentCategoryDao = daftDatabase.parentCategoryDao();
        subCategories = parentCategoryDao.loadParentCategoriesWithSubCategories();

        subCategoryDao = daftDatabase.subCategoryDao();
        transactions = subCategoryDao.getSubCategoriesWithTransactions();
    }

    public LiveData<List<ParentCategoryWithSubCategory>> getParentCategoriesWithSubCategories() {
        return subCategories;
    }

    public LiveData<List<SubCategoryWithTransaction>> getSubCategoriesWithTransactions() {
        return transactions;
    }

    public void insert(ParentCategoryWithSubCategory parentCategoryWithSubCategory) {
        new InsertParentCategoryWithSubCategoryAsyncTask(parentCategoryDao).execute(parentCategoryWithSubCategory);
    }

    public void insert(SubCategoryWithTransaction subCategoryWithTransaction) {
        new InsertSubCategoryWithTransactionAsyncTask(subCategoryDao).execute(subCategoryWithTransaction);
    }

    public Double getCurrentAmountOfMoney() throws ExecutionException, InterruptedException {
        GetCurrentAmountOfMoneyAsyncTask getCurrentAmountOfMoneyAsyncTask = new GetCurrentAmountOfMoneyAsyncTask(parentCategoryDao);
        return getCurrentAmountOfMoneyAsyncTask.execute().get();
    }

    public double getSubCategoriesTotalAmount(long subID) {
        return subCategoryDao.getSubCategoriesTotalAmount(subID);
    }

    public void insertTransactionForSubCategory(SubCategoryEntity subCategoryEntity, TransactionEntity transactionEntity) {
        subCategoryDao.insertTransactionForSubCategory(subCategoryEntity, transactionEntity);
    }

    public void insertSubCategoryInParent(Integer parentID, SubCategoryEntity subCategoryEntity) {
        new InsertSubCategoryInParentAsyncTask(parentCategoryDao, parentID, subCategoryEntity).execute();
    }

    public LiveData<Double> totalAmountSpentForSubCategory(Integer arg1) {
        return parentCategoryDao.totalAmountSpentForSubCategory(arg1);
    }

    public Double budgetForParentCategory(String arg1) throws ExecutionException, InterruptedException {

        BudgetForParentCategoryAsyncTask budgetForParentCategoryAsyncTask = new BudgetForParentCategoryAsyncTask(parentCategoryDao);
        return budgetForParentCategoryAsyncTask.execute(arg1).get();
    }

    public LiveData<Double> totalAmountSpentForParentCategory(String arg1) {

        return parentCategoryDao.totalAmountSpentForParentCategory(arg1);
    }

    public Double totalAmountSpentForParentCategory2(String arg1) throws ExecutionException, InterruptedException {

        return new TotalAmountSpentForParentCategory2AsyncTask(parentCategoryDao).execute(arg1).get();
    }

    public Integer getParentCategoryID(String parentName) throws ExecutionException, InterruptedException {
        return new GetParentIDAsyncTask(parentCategoryDao, parentName).execute().get();
    }

    public Integer getNumberSubs() throws ExecutionException, InterruptedException {
        return new CountSubsAsyncTask(parentCategoryDao).execute().get();
    }

    public void updateTransactionSubCategory(Integer subCatId, Integer transactionId) {
        new UpdateTransactionSubCategoryAsyncTask(subCategoryDao, subCatId, transactionId).execute();
    }

    public List<SubCategoryEntity> GetOverBudgetSubCategoriesInParent(String parentName) throws ExecutionException, InterruptedException {
        return new GetOverBudgetSubCategoriesInParentAsyncTask(subCategoryDao).execute(parentName).get();
    }

    public Double getSpentAmountSubCategory(Integer subCatId) throws ExecutionException, InterruptedException {
        return new GetSpentAmountSubCategoryAsyncTask(subCategoryDao).execute(subCatId).get();
    }

    public Double getBankChargesForAParent(String parentName) throws ExecutionException, InterruptedException{
        return new GetBankChargesForAParentAsyncTask(subCategoryDao).execute(parentName).get();
    }

    public Integer getNumTransactions(String parentName) throws ExecutionException, InterruptedException {
        return new GetNumTransactionsAsyncTask(subCategoryDao).execute(parentName).get();
    }

    public List<TransactionEntity> getBiggestTransaction(String parentName) throws ExecutionException, InterruptedException {
        return new GetBiggestTransactionAsyncTask(subCategoryDao).execute(parentName).get();
    }

    public MostMoneyData getMostMoney(String parentName) throws ExecutionException, InterruptedException {
        return new GetMostMoneyAsyncTask(subCategoryDao).execute(parentName).get();
    }

    private static class BudgetForParentCategoryAsyncTask extends AsyncTask<String, Double, Double> {
        private ParentCategoryDao parentCategoryDao;

        public BudgetForParentCategoryAsyncTask(ParentCategoryDao parentCategoryDao) {
            this.parentCategoryDao = parentCategoryDao;
        }

        @Override
        protected Double doInBackground(String... strings) {
            return parentCategoryDao.getParentBudget(strings[0]);
        }
    }

    private static class InsertParentCategoryWithSubCategoryAsyncTask extends AsyncTask<ParentCategoryWithSubCategory, Void, Void> {
        private ParentCategoryDao parentCategoryDao;

        public InsertParentCategoryWithSubCategoryAsyncTask(ParentCategoryDao parentCategoryDao) {
            this.parentCategoryDao = parentCategoryDao;
        }

        @Override
        protected Void doInBackground(ParentCategoryWithSubCategory... parentCategoryWithSubCategories) {
            ParentCategoryEntity temp = parentCategoryWithSubCategories[0].getParentCategoryEntity();
            parentCategoryDao.insertParentCategory(temp);

            for (SubCategoryEntity subCategoryEntity : parentCategoryWithSubCategories[0].getSubCategories()) {
                subCategoryEntity.setParentCategoryID(temp.getId());
            }
            parentCategoryDao.insertSubCategory(parentCategoryWithSubCategories[0].getSubCategories());
            return null;
        }
    }

    private static class InsertSubCategoryWithTransactionAsyncTask extends AsyncTask<SubCategoryWithTransaction, Void, Void> {
        private SubCategoryDao subCategoryDao;

        public InsertSubCategoryWithTransactionAsyncTask(SubCategoryDao subCategoryDao) {
            this.subCategoryDao = subCategoryDao;
        }

        @Override
        protected Void doInBackground(SubCategoryWithTransaction... subCategoryWithTransactions) {
            SubCategoryEntity temp = subCategoryWithTransactions[0].getSubCategoryEntity();
            long tempID = subCategoryDao.insertSubCategory(temp);
            for (TransactionEntity transactionEntity : subCategoryWithTransactions[0].getTransactions()) {
                transactionEntity.setSubCategoryID(temp.getId());
            }
            subCategoryDao.insertTransactions(subCategoryWithTransactions[0].getTransactions());
            return null;
        }
    }

    private static class InsertSubCategoryInParentAsyncTask extends AsyncTask<Void, Void, Void> {
        private ParentCategoryDao parentCategoryDao;
        private Integer parentId;
        private SubCategoryEntity subCategoryEntity;

        public InsertSubCategoryInParentAsyncTask(ParentCategoryDao parentCategoryDao, Integer parentId, SubCategoryEntity subCategoryEntity) {
            this.parentCategoryDao = parentCategoryDao;
            this.parentId = parentId;
            this.subCategoryEntity = subCategoryEntity;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            parentCategoryDao.insertSubCategoryForParentCategory(parentId, subCategoryEntity);
            return null;
        }
    }

    private static class GetParentIDAsyncTask extends AsyncTask<Void, Void, Integer> {
        ParentCategoryDao parentCategoryDao;
        String parentName;

        public GetParentIDAsyncTask(ParentCategoryDao parentCategoryDao, String parentName) {
            this.parentCategoryDao = parentCategoryDao;
            this.parentName = parentName;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return parentCategoryDao.getParentCategoryID(parentName);
        }
    }

    private static class CountSubsAsyncTask extends AsyncTask<Void, Void, Integer> {
        ParentCategoryDao parentCategoryDao;

        public CountSubsAsyncTask(ParentCategoryDao parentCategoryDao) {
            this.parentCategoryDao = parentCategoryDao;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return parentCategoryDao.countSubs();
        }
    }

    private static class UpdateTransactionSubCategoryAsyncTask extends AsyncTask<Void, Void, Void> {
        SubCategoryDao subCategoryDao;
        Integer subCatId, transactionId;

        public UpdateTransactionSubCategoryAsyncTask(SubCategoryDao subCategoryDao, Integer subCatId, Integer transactionId) {
            this.subCategoryDao = subCategoryDao;
            this.subCatId = subCatId;
            this.transactionId = transactionId;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            subCategoryDao.updateTransactionSubCategory(subCatId, transactionId);
            return null;
        }
    }

    private static class GetCurrentAmountOfMoneyAsyncTask extends AsyncTask<Void, Void, Double> {
        ParentCategoryDao parentCategoryDao;

        public GetCurrentAmountOfMoneyAsyncTask(ParentCategoryDao parentCategoryDao) {
            this.parentCategoryDao = parentCategoryDao;
        }

        @Override
        protected Double doInBackground(Void... voids) {
            return parentCategoryDao.getCurrentAmountOfMoney();
        }
    }

    public static class TotalAmountSpentForParentCategory2AsyncTask extends AsyncTask<String, Void, Double>{
        ParentCategoryDao parentCategoryDao;

        public TotalAmountSpentForParentCategory2AsyncTask(ParentCategoryDao parentCategoryDao) {
            this.parentCategoryDao = parentCategoryDao;
        }

        @Override
        protected Double doInBackground(String... strings) {
            return parentCategoryDao.totalAmountSpentForParentCategory2(strings[0]);
        }
    }

    public static class GetOverBudgetSubCategoriesInParentAsyncTask extends AsyncTask<String, Void, List<SubCategoryEntity>>{
        SubCategoryDao subCategoryDao;

        public GetOverBudgetSubCategoriesInParentAsyncTask(SubCategoryDao subCategoryDao) {
            this.subCategoryDao = subCategoryDao;
        }

        @Override
        protected List<SubCategoryEntity> doInBackground(String... strings) {
            return subCategoryDao.getOverBudgetSubCategoriesInParent(strings[0]);
        }
    }

    public static class GetSpentAmountSubCategoryAsyncTask extends AsyncTask<Integer, Void, Double>{
        SubCategoryDao subCategoryDao;

        public GetSpentAmountSubCategoryAsyncTask(SubCategoryDao subCategoryDao) {
            this.subCategoryDao = subCategoryDao;
        }

        @Override
        protected Double doInBackground(Integer... integers) {
            return subCategoryDao.getSubCategoriesTotalAmount(integers[0]);
        }
    }

    private static class GetBankChargesForAParentAsyncTask extends AsyncTask<String, Void, Double>{
        SubCategoryDao subCategoryDao;

        public GetBankChargesForAParentAsyncTask(SubCategoryDao subCategoryDao) {
            this.subCategoryDao = subCategoryDao;
        }

        @Override
        protected Double doInBackground(String... strings) {
            return subCategoryDao.getBankChargesForAParent(strings[0]);
        }
    }

    private static class GetNumTransactionsAsyncTask extends AsyncTask<String, Void, Integer>{
        SubCategoryDao subCategoryDao;

        public GetNumTransactionsAsyncTask(SubCategoryDao subCategoryDao) {
            this.subCategoryDao = subCategoryDao;
        }

        @Override
        protected Integer doInBackground(String... strings) {
            return subCategoryDao.getNumTransactions(strings[0]);
        }
    }

    private static class GetBiggestTransactionAsyncTask extends AsyncTask<String, Void, List<TransactionEntity>>{
        SubCategoryDao subCategoryDao;

        public GetBiggestTransactionAsyncTask(SubCategoryDao subCategoryDao) {
            this.subCategoryDao = subCategoryDao;
        }

        @Override
        protected List<TransactionEntity> doInBackground(String... strings) {
            return subCategoryDao.getBiggestTransaction(strings[0]);
        }
    }

    private static class GetMostMoneyAsyncTask extends AsyncTask<String,Void, MostMoneyData>{
        SubCategoryDao subCategoryDao;

        public GetMostMoneyAsyncTask(SubCategoryDao subCategoryDao) {
            this.subCategoryDao = subCategoryDao;
        }

        @Override
        protected MostMoneyData doInBackground(String... strings) {
            return subCategoryDao.getMostMoney(strings[0]);
        }
    }

    public String generateOverallSummary() throws ExecutionException, InterruptedException {
        ContentSelection contentSelection = new ContentSelection(this, context);
        Log.d("CS Repo call", "Working");
        return contentSelection.generateOverallSummary();
    }

    public String generateCategorySummary(String parentCat) throws ExecutionException, InterruptedException {
        ContentSelection contentSelection = new ContentSelection(this, context);
        Log.d("CS2 Repo call", "Working 2");
        return contentSelection.generateCategorySummary(parentCat);
    }

}
