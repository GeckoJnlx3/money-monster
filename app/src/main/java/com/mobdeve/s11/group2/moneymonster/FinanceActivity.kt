package com.mobdeve.s11.group2.moneymonster

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.ui.res.colorResource

class FinanceActivity : ComponentActivity() {

    private lateinit var logExpenseBtn: Button
    private lateinit var logIncomeBtn: Button
    private lateinit var typeText: TextView
    private lateinit var amountInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var saveBtn: Button
    private lateinit var categorySpnr: Spinner
    private lateinit var imageView: ImageView
    private lateinit var currencyText: TextView

    private var isLoggingExpense = true

    private val categories = arrayOf(
        "Food",
        "Transport",
        "Entertainment",
        "Utilities",
        "Other"
    )

    private var currency: String = "PHP"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.finance)

        logExpenseBtn = findViewById(R.id.logExpenseBtn)
        logIncomeBtn = findViewById(R.id.logIncomeBtn)
        typeText = findViewById(R.id.typeText)
        amountInput = findViewById(R.id.amountInput)
        descriptionInput = findViewById(R.id.memoInput)
        saveBtn = findViewById(R.id.saveBtn)
        imageView = findViewById(R.id.imageView)
        categorySpnr = findViewById(R.id.categorySpnr)
        currencyText = findViewById(R.id.currencyText)

        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpnr.adapter = categoryAdapter

        imageView.setImageResource(R.drawable.gwomp_baby)

        logExpenseBtn.setOnClickListener {
            switchToExpense()
        }

        logIncomeBtn.setOnClickListener {
            switchToIncome()
        }

        saveBtn.setOnClickListener {
            saveTransaction()
        }

        loadCurrency()
        updateUI()
    }

    private fun switchToExpense() {
        isLoggingExpense = true
        logExpenseBtn.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.beige)))
        logIncomeBtn.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.brown_shadow)))
        imageView.setImageResource(R.drawable.gwomp_baby)
        updateUI()
    }

    private fun switchToIncome() {
        isLoggingExpense = false
        logExpenseBtn.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.brown_shadow)))
        logIncomeBtn.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.beige)))
        imageView.setImageResource(R.drawable.gwomp_baby)
        updateUI()
    }

    private fun updateUI() {
        typeText.text =
            if (isLoggingExpense)
                "Log Expense"
            else
                "Log Income"
        amountInput.setText("")
        descriptionInput.setText("")
        currencyText.text = currency
    }

    private fun saveTransaction() {
        val amount = amountInput.text.toString().toDoubleOrNull()
        val description = descriptionInput.text.toString()

        if (amount != null) {

            amountInput.setText("")
            descriptionInput.setText("")

            imageView.setImageResource(
                if (isLoggingExpense)
                    R.drawable.sad_gwomp
                else
                    R.drawable.happy_gwomp
            )

            val transactionType =
                if (isLoggingExpense)
                    "Expense"
                else
                    "Income"

            Toast.makeText(this, "$transactionType logged: $currency $amount", Toast.LENGTH_SHORT).show()

        } else {
            Toast.makeText(this, "Please log your amount", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadCurrency() {
        val sharedPref = getSharedPreferences("com.mobdeve.s11.group2.moneymonster.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
        currency = sharedPref.getString("CURRENCY", "PHP") ?: "PHP"
        currencyText.text = currency
    }
}
