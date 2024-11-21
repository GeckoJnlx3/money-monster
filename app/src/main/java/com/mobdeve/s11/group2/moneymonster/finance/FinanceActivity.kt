package com.mobdeve.s11.group2.moneymonster.finance

import android.app.DatePickerDialog
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.ui.text.font.FontVariation
import androidx.core.content.ContextCompat
import com.mobdeve.s11.group2.moneymonster.R
import com.mobdeve.s11.group2.moneymonster.SettingsActivity
import com.mobdeve.s11.group2.moneymonster.databinding.FinanceBinding
import java.util.Calendar

class FinanceActivity : ComponentActivity() {

    private lateinit var logExpenseBtn: Button
    private lateinit var logIncomeBtn: Button
    private lateinit var typeText: TextView
    private lateinit var dateEt: EditText
    private lateinit var amountInput: EditText
    private lateinit var memoInput: EditText
    private lateinit var saveBtn: Button
    private lateinit var expenseCategorySpnr: Spinner
    private lateinit var incomeCategorySpnr: Spinner
    private lateinit var imageView: ImageView
    private lateinit var currencyText: TextView

    private var isLoggingExpense = true

    private val expenseCategories = arrayOf(
        "Food", "Transport", "Entertainment", "Utilities", "Other"
    )

    private val incomeCategories = arrayOf(
        "Salary", "Investment", "Gift", "Allowance", "Other"
    )

    private var currency: String = "PHP"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding: FinanceBinding = FinanceBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        logExpenseBtn = viewBinding.logExpenseBtn
        logIncomeBtn = viewBinding.logIncomeBtn
        typeText = viewBinding.typeText
        dateEt = viewBinding.dateEt
        amountInput = viewBinding.amountInput
        memoInput = viewBinding.memoInput
        saveBtn = viewBinding.saveBtn
        imageView = viewBinding.imageView
        expenseCategorySpnr = viewBinding.expenseCategorySpnr
        incomeCategorySpnr = viewBinding.incomeCategorySpnr
        currencyText = viewBinding.currencyText

        val expenseCategoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, expenseCategories)
        expenseCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        expenseCategorySpnr.adapter = expenseCategoryAdapter

        val incomeCategoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, incomeCategories)
        incomeCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        incomeCategorySpnr.adapter = incomeCategoryAdapter

        loadCurrency()
        updateUI()

        logExpenseBtn.setOnClickListener {
            switchToExpense()
        }

        logIncomeBtn.setOnClickListener {
            switchToIncome()
        }

        saveBtn.setOnClickListener {
            saveTransaction()
        }

        dateEt.setOnClickListener {
            displayDatePickerDialog()
        }
    }

    private fun switchToExpense() {
        isLoggingExpense = true
        logExpenseBtn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this,
            R.color.beige
        )))
        logIncomeBtn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this,
            R.color.brown_shadow
        )))
        imageView.setImageResource(R.drawable.gwomp_baby)
        updateUI()
    }

    private fun switchToIncome() {
        isLoggingExpense = false
        logExpenseBtn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this,
            R.color.brown_shadow
        )))
        logIncomeBtn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this,
            R.color.beige
        )))
        imageView.setImageResource(R.drawable.gwomp_baby)
        updateUI()
    }

    private fun updateUI() {
        typeText.text = if (isLoggingExpense) "Log Expense" else "Log Income"
        amountInput.setText("")
        currencyText.text = currency
        memoInput.setText("")

        if (isLoggingExpense) {
            expenseCategorySpnr.visibility = Spinner.VISIBLE
            incomeCategorySpnr.visibility = Spinner.GONE
        } else {
            expenseCategorySpnr.visibility = Spinner.GONE
            incomeCategorySpnr.visibility = Spinner.VISIBLE
        }
    }

    private fun saveTransaction() {
        val amount = amountInput.text.toString().toDoubleOrNull()
        val description = memoInput.text.toString()
        val dateText = dateEt.text.toString()

        if (amount == null || dateText.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields correctly", Toast.LENGTH_SHORT).show()
            return
        }

        val date = try {
            FinanceDatabaseHelper.DATE_FORMAT.parse(dateText)
        } catch (e: Exception) {
            Toast.makeText(this, "Invalid date format. Use dd-MM-yyyy.", Toast.LENGTH_SHORT).show()
            return
        }

        val category = if (isLoggingExpense) {
            expenseCategorySpnr.selectedItem.toString()
        } else {
            incomeCategorySpnr.selectedItem.toString()
        }

        val record = FinanceRecord(
            id = 0,
            type = if (isLoggingExpense) "Expense" else "Income",
            date = date,
            currency = currencyText.text.toString(),
            amount = amount.toString(),
            category = category,
            description = description
        )

        val dbHelper = FinanceDatabaseHelper(this)
        dbHelper.recordExpense(record)
        Log.d("FinanceActivity", "Transaction Saved: $record")

        amountInput.setText("")
        memoInput.setText("")
        dateEt.setText("")

        imageView.setImageResource(
            if (isLoggingExpense)
                R.drawable.sad_gwomp
            else
                R.drawable.happy_gwomp
        )

        val transactionType = if (isLoggingExpense) "Expense" else "Income"
        Toast.makeText(this, "$transactionType logged: $currency $amount", Toast.LENGTH_SHORT).show()
    }

    private fun loadCurrency() {
        val sharedPref = getSharedPreferences(SettingsActivity.PREFERENCE_FILE, Context.MODE_PRIVATE)
        currency = sharedPref.getString(SettingsActivity.CURRENCY, "PHP") ?: "PHP"
        currencyText.text = currency
    }

    private fun displayDatePickerDialog() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this, R.style.DatePickerDialogStyle,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format(
                    "%02d-%02d-%04d", selectedDay, selectedMonth + 1, selectedYear
                )
                dateEt.setText(formattedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }
}