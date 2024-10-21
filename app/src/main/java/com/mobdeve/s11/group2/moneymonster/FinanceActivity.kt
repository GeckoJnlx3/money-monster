package com.mobdeve.s11.group2.moneymonster

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity

class FinanceActivity : ComponentActivity() {

    private lateinit var logExpenseBtn: Button
    private lateinit var logIncomeBtn: Button
    private lateinit var typeText: TextView
    private lateinit var amountInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var saveBtn: Button
    private lateinit var categorySpnr: Spinner

    private var isLoggingExpense = true

    private val categories = arrayOf(
        "Food",
        "Transport",
        "Entertainment",
        "Utilities",
        "Other"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.finance)

        logExpenseBtn = findViewById(R.id.logExpenseBtn)
        logIncomeBtn = findViewById(R.id.logIncomeBtn)
        typeText = findViewById(R.id.typeText)
        amountInput = findViewById(R.id.amountInput)
        descriptionInput = findViewById(R.id.memoInput)
        saveBtn = findViewById(R.id.saveBtn)

        logExpenseBtn.setOnClickListener {
            switchToExpense()
        }

        logIncomeBtn.setOnClickListener {
            switchToIncome()
        }

        saveBtn.setOnClickListener {
            saveTransaction()
        }

        updateUI()
    }

    private fun switchToExpense() {
        isLoggingExpense = true
        updateUI()
    }

    private fun switchToIncome() {
        isLoggingExpense = false
        updateUI()
    }

    private fun updateUI() {
        typeText.text = if (isLoggingExpense) "Log Expense" else "Log Income"
        amountInput.setText("")
        descriptionInput.setText("")
    }

    private fun saveTransaction() {
        val amount = amountInput.text.toString().toDoubleOrNull()
        val description = descriptionInput.text.toString()

        if (amount != null && description.isNotBlank()) {

            amountInput.setText("")
            descriptionInput.setText("")

            val transactionType = if (isLoggingExpense) "Expense" else "Income"

        } else {
            Toast.makeText(this, "Please log your details", Toast.LENGTH_SHORT).show()
        }
    }
}
