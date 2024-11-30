package com.mobdeve.s11.group2.moneymonster.finance

import android.app.DatePickerDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
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
import androidx.core.content.ContextCompat
import com.mobdeve.s11.group2.moneymonster.DatabaseHelper
import com.mobdeve.s11.group2.moneymonster.DatabaseHelper.Companion.COL_MONSTER_ID
import com.mobdeve.s11.group2.moneymonster.DatabaseHelper.Companion.COL_SPECIES
import com.mobdeve.s11.group2.moneymonster.DatabaseHelper.Companion.COL_STAGE
import com.mobdeve.s11.group2.moneymonster.DatabaseHelper.Companion.MONSTER_TABLE_NAME
import com.mobdeve.s11.group2.moneymonster.MonsterDataHelper
import com.mobdeve.s11.group2.moneymonster.R
import com.mobdeve.s11.group2.moneymonster.SettingsActivity
import com.mobdeve.s11.group2.moneymonster.databinding.FinanceBinding
import com.mobdeve.s11.group2.moneymonster.monster.Monster
import java.util.Calendar
import java.util.Locale

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

    private val statsUpdateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            loadMonsterData()
        }
    }

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

    private fun loadMonsterData() {
        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.readableDatabase

        val activeMonster = MonsterDataHelper.getActiveMonster(db)

        if (activeMonster != null) {
            Log.d("FinanceActivity", "Monster data loaded: ${activeMonster.name}")
        }
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

    private fun updateProgress(amount: Double, isExpense: Boolean) {
        val sharedPref = getSharedPreferences(SettingsActivity.PREFERENCE_FILE, MODE_PRIVATE)
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

    private fun saveTransaction() {
        val amount = amountInput.text.toString().toDoubleOrNull()
        val description = memoInput.text.toString()
        val dateText = dateEt.text.toString()

        if (amount == null || dateText.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields correctly", Toast.LENGTH_SHORT).show()
            return
        }


        val date = try {
            DatabaseHelper.DATE_FORMAT.parse(dateText)
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

        val dbHelper = DatabaseHelper(this)
        val result = dbHelper.recordExpense(record)

        if (result != -1L) {
            updateProgress(amount, isLoggingExpense)
            updateMonsterStats(amount, isLoggingExpense)
            //TODO: update money saved/spent. update xp if goal is reached

            val intent = Intent("com.mobdeve.s11.group2.moneymonster.UPDATE_MONSTER_STATS")
            sendBroadcast(intent)

            Toast.makeText(this, "Transaction saved successfully.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Failed to save transaction.", Toast.LENGTH_SHORT).show()
        }
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

    private fun updateMonsterStats(amount: Double, isExpense: Boolean) {
        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.readableDatabase

        val activeMonster = MonsterDataHelper.getActiveMonster(db)

        if (activeMonster != null) {
            if (isExpense) {
                activeMonster.statSpent += amount
            } else {
                activeMonster.statSaved += amount
            }

            val sharedPref = getSharedPreferences(SettingsActivity.PREFERENCE_FILE, MODE_PRIVATE)
            var currentIncome = sharedPref.getFloat("CURRENT_INCOME", 0f).toDouble()
            var currentExpense = sharedPref.getFloat("CURRENT_EXPENSE", 0f).toDouble()

            val target = sharedPref.getFloat(SettingsActivity.TARGET, 500.0.toFloat())
            val limit = sharedPref.getFloat(SettingsActivity.LIMIT, 300.0.toFloat())

            if (currentIncome >= target){
                val totalXp = currentIncome/target
                currentIncome = currentIncome%target

                activeMonster.upTick += totalXp.toInt()

                if (activeMonster.upTick >= activeMonster.reqExp){
                    activeMonster.upTick = activeMonster.upTick%activeMonster.reqExp
                    val addLevel = (totalXp/activeMonster.reqExp).toInt()
                    Log.d("Finance addLevel", "$addLevel")

                    val levelToEvolve = getLevelToEvolve(activeMonster.stage)
                    if (activeMonster.level + addLevel >= levelToEvolve && levelToEvolve != -1){
                        activeMonster.level = levelToEvolve
                        evolveMonster(activeMonster)
                    } else {
                        activeMonster.level += addLevel
                    }
                }

                val editor = sharedPref.edit()
                editor.putFloat("CURRENT_INCOME", (currentIncome).toFloat())
                editor.apply()
            }

            if (currentExpense > limit){
                val totalNegXp = currentExpense/limit
                currentExpense = currentExpense%limit

                activeMonster.upTick -= totalNegXp.toInt()

                if (activeMonster.upTick < 0){
                    activeMonster.upTick = 0
                }

                val editor = sharedPref.edit()
                editor.putFloat("CURRENT_EXPENSE", (currentExpense).toFloat())
                editor.apply()
            }
//
//            if (shouldEvolve(activeMonster)) {
//                evolveMonster(activeMonster)
//            }
            val result = dbHelper.updateMonster(activeMonster)
            if (result > 0) {
                Log.d("FinanceActivity", "Monster stats updated successfully.")
            } else {
                Log.e("FinanceActivity", "Failed to update monster stats.")
            }
        }
    }

    private fun getLevelToEvolve(stage:String): Int{
        return when (stage) {
            "baby" -> 5
            "teen" -> 15
            else -> -1
        }
    }

    private fun shouldEvolve(monster: Monster): Boolean {
        return when (monster.stage) {
            "baby" -> monster.level >= 5
            "teen" -> monster.level >= 15
            else -> false
        }
    }

    private fun evolveMonster(activeMonster: Monster) {
        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.writableDatabase

        val newStage = when (activeMonster.stage) {
            "baby" -> "teen"
            "teen" -> "adult"
            else -> return
        }

        val evolvedMonster = MonsterDataHelper.getMonsterBySpeciesAndStage(db, activeMonster.species, newStage)

        Log.d("FinanceActivity", "Evolved from ${activeMonster.name} (Stage: ${activeMonster.stage}).")

        if (evolvedMonster != null) {
            activeMonster.onField = false
            activeMonster.unlocked = true

            evolvedMonster.onField = true
            evolvedMonster.unlocked = true
            evolvedMonster.upTick = activeMonster.upTick
            evolvedMonster.level = activeMonster.level

            dbHelper.updateMonster(evolvedMonster)
            dbHelper.updateMonster(activeMonster)

            Log.d("FinanceActivity", "Monster evolved to ${evolvedMonster.name} (Stage: $newStage).")
        }
    }

    private fun loadCurrency() {
        val sharedPref = getSharedPreferences(SettingsActivity.PREFERENCE_FILE, MODE_PRIVATE)
        currency = sharedPref.getString(SettingsActivity.CURRENCY, "PHP") ?: "PHP"
        currencyText.text = currency
    }

    private fun displayDatePickerDialog(){
        // https://www.geeksforgeeks.org/how-to-popup-datepicker-while-clicking-on-edittext-in-android/
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this, R.style.DatePickerDialogStyle,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format(Locale.getDefault(),
                    "%04d-%02d-%02d",  selectedYear, selectedMonth + 1,selectedDay
                )
                dateEt.setText(formattedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }
}
