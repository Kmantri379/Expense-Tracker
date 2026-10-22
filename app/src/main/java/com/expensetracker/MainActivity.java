package com.expensetracker;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import java.text.NumberFormat;
import java.util.Locale;

public class MainActivity extends Activity {
    private ExpenseDatabase database;
    private TextView totalView, countView;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_main);
        database = new ExpenseDatabase(this);
        totalView = findViewById(R.id.totalView);
        countView = findViewById(R.id.countView);

        Button addButton = findViewById(R.id.addButton);
        Button viewButton = findViewById(R.id.viewButton);
        Button aboutButton = findViewById(R.id.aboutButton);

        addButton.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, AddExpenseActivity.class)));
        viewButton.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ViewExpensesActivity.class)));
        aboutButton.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, AboutActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (database != null && totalView != null) updateSummary();
    }

    private void updateSummary() {
        double total = 0;
        int count = 0;
        Cursor cursor = database.getReadableDatabase().rawQuery(
                "SELECT amount FROM expenses", null);
        try {
            while (cursor.moveToNext()) {
                total += cursor.getDouble(0);
                count++;
            }
        } finally {
            cursor.close();
        }
        totalView.setText("Total expenses: " + money(total));
        countView.setText("Number of expenses: " + count);
    }

    private String money(double amount) {
        return NumberFormat.getCurrencyInstance(new Locale("en", "IN")).format(amount);
    }
}
