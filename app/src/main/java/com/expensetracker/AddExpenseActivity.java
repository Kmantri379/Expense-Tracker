package com.expensetracker;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddExpenseActivity extends Activity {
    private EditText nameInput, amountInput, categoryInput;
    private ExpenseDatabase database;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_add_expense);
        database = new ExpenseDatabase(this);
        nameInput = findViewById(R.id.nameInput);
        amountInput = findViewById(R.id.amountInput);
        categoryInput = findViewById(R.id.categoryInput);

        Button saveButton = findViewById(R.id.saveButton);
        Button backButton = findViewById(R.id.backButton);
        saveButton.setOnClickListener(v -> saveExpense());
        backButton.setOnClickListener(v -> finish());
    }

    private void saveExpense() {
        String name = nameInput.getText().toString().trim();
        String amountText = amountInput.getText().toString().trim();
        String category = categoryInput.getText().toString().trim();
        if (name.isEmpty()) {
            nameInput.setError("Enter expense name");
            return;
        }
        if (amountText.isEmpty()) {
            amountInput.setError("Enter amount");
            return;
        }
        double amount;
        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            amountInput.setError("Enter a valid amount");
            return;
        }
        if (amount <= 0) {
            amountInput.setError("Amount must be greater than zero");
            return;
        }

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("amount", amount);
        values.put("category", category.isEmpty() ? "Other" : category);
        values.put("created_at", System.currentTimeMillis());
        database.getWritableDatabase().insertOrThrow("expenses", null, values);
        Toast.makeText(this, "Expense saved successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
}
