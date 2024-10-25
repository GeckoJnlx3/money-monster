package com.mobdeve.s11.group2.moneymonster

import android.app.DatePickerDialog
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
import androidx.compose.material3.AlertDialog
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
        categorySpnr = viewBinding.categorySpnr

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
        currencyText.text = currency
        memoInput.setText("")

    }

    private fun saveTransaction() {
        val amount = amountInput.text.toString().toDoubleOrNull()
        val description = memoInput.text.toString()

        if (amount != null) {

            amountInput.setText("")
            memoInput.setText("")

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

    private fun displayDatePickerDialog(){
        // https://www.geeksforgeeks.org/how-to-popup-datepicker-while-clicking-on-edittext-in-android/
        val c = Calendar.getInstance()

        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { view, year, monthOfYear, dayOfMonth ->
                val dat = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                dateEt.setText(dat)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }
}
