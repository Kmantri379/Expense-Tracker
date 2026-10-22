package com.expensetracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ExpenseDatabase extends SQLiteOpenHelper {
    public ExpenseDatabase(Context context) {
        super(context, "expenses.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE expenses (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, amount REAL NOT NULL, category TEXT NOT NULL, " +
                "created_at INTEGER NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No database changes are needed for version 1.
    }
}
