package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
//import java.util.HashMap;
import java.util.List;
//import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;


/**
 * This is a Persistent implementation of the AccountDAO interface.
 */
public class PersistentAccountDAO extends DataBaseHelper implements AccountDAO {
    private List<String> AccountNumbers;
    private List<Account> Accounts;

    public PersistentAccountDAO(Context context) {
        super(context);
        //this.AccountNumbers = new ArrayList<String>();
        //this.Accounts = new ArrayList<Account>();
    }

    @Override
    public List<String> getAccountNumbersList() {
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "select accountNo from accounts";

        Cursor cursor = db.rawQuery(sql,null);

        this.AccountNumbers = new ArrayList<String>();
        if (cursor.moveToFirst()) {
            do {
                this.AccountNumbers.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return this.AccountNumbers;
    }

    @Override
    public List<Account> getAccountsList() {
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "select * from accounts";

        Cursor cursor = db.rawQuery(sql,null);

        this.Accounts = new ArrayList<Account>();
        if (cursor.moveToFirst()){
            do{
                String accountNo = cursor.getString(0);
                String bankName = cursor.getString(1);
                String accountHolderName = cursor.getString(2);
                double balance = cursor.getInt(3);

                Account acc = new Account(accountNo,bankName,accountHolderName,balance);
                this.Accounts.add(acc);
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return this.Accounts;
    }

    @Override
    public Account getAccount(String accountNo) {
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "select * from accounts where accountNo = '"+ accountNo + "' ;";

        Cursor cursor = db.rawQuery(sql,null);

        String bankName = cursor.getString(1);
        String accountHolderName = cursor.getString(2);
        double balance = cursor.getInt(3);

        Account acc = new Account(accountNo,bankName,accountHolderName,balance);

        cursor.close();
        db.close();

        return acc;
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("accountNo", account.getAccountNo());
        cv.put("bankName", account.getBankName());
        cv.put("accountHolderName", account.getAccountHolderName());
        cv.put("balance", account.getBalance());

        db.insert("accounts", null, cv);
    }

    @Override
    public void removeAccount(String accountNo) {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "delete from accounts where accountNo = '"+ accountNo + "' ;";

        db.execSQL(sql);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selectsql = "select balance from accounts where accountNo = '"+ accountNo +"' ;";

        Cursor cursor = db.rawQuery(selectsql,null);

        cursor.moveToFirst();
        double balance = cursor.getDouble(0);
        switch(expenseType){
            case EXPENSE:
                balance  -= amount;
                break;
            case INCOME:
                balance  += amount;
                break;
        }

        String updateSql = "update accounts set balance = ? where accountNo = ? ;";

        SQLiteStatement statement = db.compileStatement(updateSql);
        statement.bindDouble(1,balance);
        statement.bindString(2,accountNo);
        statement.executeUpdateDelete();

        statement.close();
        cursor.close();
        db.close();
    }
}