package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "200144X.db";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null,  DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createAccSql = "CREATE TABLE accounts(accountNo text PRIMARY KEY, bankName text, accountHolderName text, balance real);";
        db.execSQL(createAccSql);

        String createTransactionSql = "CREATE TABLE transactions(transactionId integer PRIMARY KEY AUTOINCREMENT, date text, accountNo text, type text, amount real, FOREIGN KEY(accountNo) REFERENCES accounts(accountNo));";
        db.execSQL(createTransactionSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}