package com.expensetracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ViewExpensesActivity extends Activity {
    private ExpenseDatabase database;
    private final ArrayList<Long> expenseIds = new ArrayList<>();
    private final ArrayList<String> expenseRows = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private TextView emptyView, totalView;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_view_expenses);
        database = new ExpenseDatabase(this);
        ListView listView = findViewById(R.id.expenseList);
        emptyView = findViewById(R.id.emptyView);
        totalView = findViewById(R.id.totalView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, expenseRows);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> confirmDelete(position));
        ((Button) findViewById(R.id.backButton)).setOnClickListener(v -> finish());
        refreshExpenses();
    }

    private void refreshExpenses() {
        expenseIds.clear();
        expenseRows.clear();
        double total = 0;
        Cursor cursor = database.getReadableDatabase().rawQuery(
                "SELECT id, name, amount, category, created_at FROM expenses ORDER BY id DESC", null);
        try {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(0);
                String name = cursor.getString(1);
                double amount = cursor.getDouble(2);
                String category = cursor.getString(3);
                String date = android.text.format.DateFormat.format("dd MMM yyyy", cursor.getLong(4)).toString();
                expenseIds.add(id);
                expenseRows.add(name + "\nAmount: " + money(amount) + "\nCategory: " + category + " | " + date);
                total += amount;
            }
        } finally {
            cursor.close();
        }
        totalView.setText("Total: " + money(total));
        adapter.notifyDataSetChanged();
        emptyView.setVisibility(expenseRows.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void confirmDelete(int position) {
        long id = expenseIds.get(position);
        new AlertDialog.Builder(this)
                .setTitle("Delete expense")
                .setMessage("Do you want to delete this expense?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", (dialog, which) -> {
                    database.getWritableDatabase().delete("expenses", "id=?",
                            new String[]{String.valueOf(id)});
                    refreshExpenses();
                    Toast.makeText(this, "Expense deleted", Toast.LENGTH_SHORT).show();
                }).show();
    }

    private String money(double amount) {
        return NumberFormat.getCurrencyInstance(new Locale("en", "IN")).format(amount);
    }
}
