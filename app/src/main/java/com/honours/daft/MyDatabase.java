package com.honours.daft;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.atomic.AtomicInteger;

@Database(entities = {TransactionEntity.class, SubCategoryEntity.class, ParentCategoryEntity.class, BankAccountEntity.class}, version = 4)
public abstract class MyDatabase extends RoomDatabase {
    private static MyDatabase myDatabase;

    public abstract ParentCategoryDao parentCategoryDao();

    public abstract SubCategoryDao subCategoryDao();

    private static Context context2;

    private static AtomicInteger numberOfOpens = new AtomicInteger(1);

    public static synchronized MyDatabase getMyDatabase(Context context) {
        context2 = context;
        if (myDatabase == null) {
            myDatabase = Room.databaseBuilder(context.getApplicationContext(), MyDatabase.class, "daft_database").fallbackToDestructiveMigration().addCallback(roomCallback).build();
        }
        return myDatabase;
    }

    //TODO: auto generated method below
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {

            super.onCreate(db);
            new fillDB(myDatabase).execute();
        }
    };


    private static class fillDB extends AsyncTask<Void, Void, Void> {
        private ParentCategoryDao parentCategoryDao;
        private SubCategoryDao subCategoryDao;

        public fillDB(MyDatabase database) {
            parentCategoryDao = database.parentCategoryDao();
            subCategoryDao = database.subCategoryDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //fill database with default uniform parent categories
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context2);
            String income = sharedPreferences.getString("income", "0.00");
            double initialBalance = Double.parseDouble(income) + 10000.00 ;
//            ParentCategoryEntity p = new ParentCategoryEntity("Savings", parentCategoryDao.countParents(), 1000.00);
            String savingsPercentage = sharedPreferences.getString("savings", "20");
            double calculationSavings = Integer.valueOf(savingsPercentage)/100.0;
            double checkAmount = initialBalance * (calculationSavings);
            ParentCategoryEntity p = new ParentCategoryEntity("Savings", parentCategoryDao.countParents(),checkAmount  );
            parentCategoryDao.insertParentCategory(p);
//            ParentCategoryEntity p2 = new ParentCategoryEntity("Necessities", parentCategoryDao.countParents(), 20000.00);
            String necessPercentage = sharedPreferences.getString("necessities", "50");
            double calculationNecess = Integer.valueOf(necessPercentage)/100.0;
            ParentCategoryEntity p2 = new ParentCategoryEntity("Necessities", parentCategoryDao.countParents(), initialBalance * (calculationNecess));
            parentCategoryDao.insertParentCategory(p2);
//            ParentCategoryEntity p3 = new ParentCategoryEntity("Luxuries", parentCategoryDao.countParents(), 3000.00);
            String luxuriesPercentage = sharedPreferences.getString("luxuries", "30");
            double calculationLux = Integer.valueOf(luxuriesPercentage)/100.0;
            ParentCategoryEntity p3 = new ParentCategoryEntity("Luxuries", parentCategoryDao.countParents(), initialBalance * (calculationLux));

            parentCategoryDao.insertParentCategory(p3);
            ParentCategoryEntity p4 = new ParentCategoryEntity("Ignored", parentCategoryDao.countParents(), 0.00);
            parentCategoryDao.insertParentCategory(p4);


            //fill the database with ignored sub category and it will be associated with ignored parent category as well
            SubCategoryEntity ignoreSubCategory = new SubCategoryEntity("Ignored", subCategoryDao.countSubCategoriesWithTransactions(), 0.00);
            Integer tempParentID = parentCategoryDao.getParentCategoryID("Ignored");
            parentCategoryDao.insertSubCategoryForParentCategory(tempParentID, ignoreSubCategory);
            TransactionEntity i = new TransactionEntity("Zuzu", 123, 800.00, 15.00, "18/05/1000", subCategoryDao.countTransactions());
            subCategoryDao.insertTransactionForSubCategory(ignoreSubCategory.getId(), i);
            TransactionEntity ii = new TransactionEntity("Aang", 195, 200.00, 10.00, "10/10/1000", subCategoryDao.countTransactions());
            subCategoryDao.insertTransactionForSubCategory(ignoreSubCategory.getId(), ii);
            TransactionEntity iii = new TransactionEntity("Katara", 765, 1002.50, 5.00, "02/12/1000", subCategoryDao.countTransactions());
            subCategoryDao.insertTransactionForSubCategory(ignoreSubCategory.getId(), iii);
            TransactionEntity iv = new TransactionEntity("Katara", 765, 1000.50, 5.00, "02/12/1000", subCategoryDao.countTransactions());
            subCategoryDao.insertTransactionForSubCategory(ignoreSubCategory.getId(), iv);
            TransactionEntity v = new TransactionEntity("Katara", 765, 2002.50, 5.00, "02/12/1000", subCategoryDao.countTransactions());
            subCategoryDao.insertTransactionForSubCategory(ignoreSubCategory.getId(), v);
            TransactionEntity vi = new TransactionEntity("Katara", 765, 3002.50, 5.00, "02/12/1000", subCategoryDao.countTransactions());
            subCategoryDao.insertTransactionForSubCategory(ignoreSubCategory.getId(), vi);

            TransactionEntity vii = new TransactionEntity("John", 765, 1000.00, 5.00, "02/12/1000", subCategoryDao.countTransactions());
            subCategoryDao.insertTransactionForSubCategory(ignoreSubCategory.getId(), vii);
            TransactionEntity viii = new TransactionEntity("Computers", 765, 1000.00, 5.00, "02/12/1000", subCategoryDao.countTransactions());
            subCategoryDao.insertTransactionForSubCategory(ignoreSubCategory.getId(), viii);
            TransactionEntity ix = new TransactionEntity("TV-shop", 765, 1000.00, 5.00, "02/12/1000", subCategoryDao.countTransactions());
            subCategoryDao.insertTransactionForSubCategory(ignoreSubCategory.getId(), ix);

            //TODO: TEST - Add new sub-categories with transactions to necessities

            SubCategoryEntity testSubCategory = new SubCategoryEntity("Test1", subCategoryDao.countSubCategoriesWithTransactions(), 1000.00);
            Integer testParentID1 = parentCategoryDao.getParentCategoryID("Necessities");
            parentCategoryDao.insertSubCategoryForParentCategory(testParentID1, testSubCategory);
            TransactionEntity t = new TransactionEntity("Amy", 123, 500.00, 10.00, "10/10/1000", subCategoryDao.countTransactions());
            subCategoryDao.insertTransactionForSubCategory(testSubCategory.getId(), t);

            TransactionEntity t8 = new TransactionEntity("Amy", 123, 500.00, 10.00, "10/10/1000", subCategoryDao.countTransactions());
            subCategoryDao.insertTransactionForSubCategory(testSubCategory.getId(), t8);

            TransactionEntity t37 = new TransactionEntity("Amy", 123, 500.00, 10.00, "10/10/1000", subCategoryDao.countTransactions());
            subCategoryDao.insertTransactionForSubCategory(testSubCategory.getId(), t37);


            SubCategoryEntity tSubCategory2 = new SubCategoryEntity("Test2", subCategoryDao.countSubCategoriesWithTransactions(), 20.00);
            Integer testParentID2 = parentCategoryDao.getParentCategoryID("Necessities");
            parentCategoryDao.insertSubCategoryForParentCategory(testParentID2, tSubCategory2);
            TransactionEntity t2 = new TransactionEntity("Jett", 125, 1000.00, 20.00, "18/10/1000", subCategoryDao.countTransactions());
            subCategoryDao.insertTransactionForSubCategory(tSubCategory2.getId(), t2);

            SubCategoryEntity testSubCategory3 = new SubCategoryEntity("Test3", subCategoryDao.countSubCategoriesWithTransactions(), 789.00);
            Integer testParentID3 = parentCategoryDao.getParentCategoryID("Necessities");

            parentCategoryDao.insertSubCategoryForParentCategory(testParentID3, testSubCategory3);
            subCategoryDao.insertTransactionForSubCategory(testSubCategory3, new TransactionEntity("Larry", 12555556, 200.00, 5.00, "15/10/1000", subCategoryDao.countTransactions()));

            SubCategoryEntity testSubCategory4 = new SubCategoryEntity("Test4", subCategoryDao.countSubCategoriesWithTransactions(), 900.00);
            Integer testParentID4 = parentCategoryDao.getParentCategoryID("Savings");

            parentCategoryDao.insertSubCategoryForParentCategory(testParentID4, testSubCategory4);
            subCategoryDao.insertTransactionForSubCategory(testSubCategory4, new TransactionEntity("Larry", 12555556, 200.00, 5.00, "15/10/1000", subCategoryDao.countTransactions()));

            return null;
        }
    }

}
