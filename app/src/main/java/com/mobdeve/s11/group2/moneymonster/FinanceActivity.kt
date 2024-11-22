package com.mobdeve.s11.group2.moneymonster

import android.app.DatePickerDialog
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
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
    private lateinit var categorySpnr: Spinner
    private lateinit var imageView: ImageView
    private lateinit var currencyText: TextView

    private var isLoggingExpense = true
    private lateinit var databaseHelper: FinanceDatabaseHelper // Database helper instance
    private val categories = arrayOf("Food", "Transport", "Entertainment", "Utilities", "Other")
    private var currency: String = "PHP"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding: FinanceBinding = FinanceBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Initialize database helper
        databaseHelper = FinanceDatabaseHelper(this)

        // Initialize UI components
        logExpenseBtn = viewBinding.logExpenseBtn
        logIncomeBtn = viewBinding.logIncomeBtn
        typeText = viewBinding.typeText
        dateEt = viewBinding.dateEt
        amountInput = viewBinding.amountInput
        memoInput = viewBinding.memoInput
        saveBtn = viewBinding.saveBtn
        imageView = viewBinding.imageView
        categorySpnr = viewBinding.categorySpnr
        currencyText = viewBinding.currencyText

        // Set up category spinner
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

        dateEt.setOnClickListener {
            displayDatePickerDialog()
        }

        loadCurrency()
        updateUI()
    }

    private fun switchToExpense() {
        isLoggingExpense = true
        logExpenseBtn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.beige)))
        logIncomeBtn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.brown_shadow)))
        imageView.setImageResource(R.drawable.gwomp_baby)
        updateUI()
    }

    private fun switchToIncome() {
        isLoggingExpense = false
        logExpenseBtn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.brown_shadow)))
        logIncomeBtn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.beige)))
        imageView.setImageResource(R.drawable.gwomp_baby)
        updateUI()
    }

    private fun updateUI() {
        typeText.text =
            if (isLoggingExpense) "Log Expense" else "Log Income"
        amountInput.setText("")
        currencyText.text = currency
        memoInput.setText("")
    }

    private fun saveTransaction() {
        val amount = amountInput.text.toString().toDoubleOrNull()
        val description = memoInput.text.toString()
        val date = dateEt.text.toString()
        val category = categorySpnr.selectedItem.toString()

        if (amount != null) {
            val record = FinanceRecord(
                id = 0,
                type = if (isLoggingExpense) "Expense" else "Income",
                amount = amount.toString(),
                category = category,
                date = date,
                description = description
            )

            val databaseHelper = FinanceDatabaseHelper(this)
            val result = databaseHelper.recordExpense(record)

            if (result != -1L) {
                updateProgress(amount, isLoggingExpense) // Update progress based on transaction type

                Toast.makeText(this, "Transaction saved successfully.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to save transaction.", Toast.LENGTH_SHORT).show()
            }

            amountInput.setText("")
            memoInput.setText("")
            dateEt.setText("")

            imageView.setImageResource(
                if (isLoggingExpense)
                    R.drawable.sad_gwomp
                else
                    R.drawable.happy_gwomp
            )

        } else {
            Toast.makeText(this, "Please log your amount", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateProgress(amount: Double, isExpense: Boolean) {
        val sharedPref = getSharedPreferences("com.mobdeve.s11.group2.moneymonster.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        val currentExpense = sharedPref.getFloat("CURRENT_EXPENSE", 0f).toDouble()
        val currentIncome = sharedPref.getFloat("CURRENT_INCOME", 0f).toDouble()

        if (isExpense) {
            editor.putFloat("CURRENT_EXPENSE", (currentExpense + amount).toFloat())
        } else {
            editor.putFloat("CURRENT_INCOME", (currentIncome + amount).toFloat())
        }

        editor.apply()
    }

    private fun loadCurrency() {
        val sharedPref = getSharedPreferences("com.mobdeve.s11.group2.moneymonster.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
        currency = sharedPref.getString("CURRENCY", "PHP") ?: "PHP"
        currencyText.text = currency
    }

    private fun displayDatePickerDialog() {
        val c = Calendar.getInstance()

        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = "$selectedDay-${selectedMonth + 1}-$selectedYear"
                dateEt.setText(formattedDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }
}
