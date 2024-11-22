package com.mobdeve.s11.group2.moneymonster

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.view.View
import androidx.activity.ComponentActivity
import com.mobdeve.s11.group2.moneymonster.databinding.ActivityMainBinding
import com.mobdeve.s11.group2.moneymonster.finance.FinanceActivity
import com.mobdeve.s11.group2.moneymonster.history.HistoryActivity
import com.mobdeve.s11.group2.moneymonster.monsterpedia.MonsterpediaActivity
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar

class MainActivity : ComponentActivity() {

    private lateinit var dateRangeSpinner: Spinner
    private lateinit var targetProgressBar: ProgressBar
    private lateinit var limitProgressBar: ProgressBar
    private lateinit var targetprogressText: TextView
    private lateinit var limitprogressText: TextView
    private lateinit var settingsBtn: Button
    private lateinit var expenseGoal: View
    private lateinit var savingGoal: View
    private lateinit var monsterpediaBtn: Button
    private lateinit var analyticsBtn: Button
    private lateinit var financeBtn: Button
    private lateinit var historyBtn: Button
    private lateinit var dateTodayTv: TextView
    private val handler = Handler()

    private var currency: String = "PHP"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindView()

        settingsBtn.setOnClickListener { openSettings() }
        expenseGoal.setOnClickListener { openSettings() }
        savingGoal.setOnClickListener { openSettings() }

        setDateToday()

        historyBtn.setOnClickListener{
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }

        monsterpediaBtn.setOnClickListener {
            val intent = Intent(this, MonsterpediaActivity::class.java)
            startActivity(intent)
        }

        analyticsBtn.setOnClickListener {
            val intent = Intent(this, AnalyticsActivity::class.java)
            startActivity(intent)
        }

        financeBtn.setOnClickListener {
            val intent = Intent(this, FinanceActivity::class.java)
            startActivity(intent)
        }

        loadAndDisplayProgress()
        loadAndDisplayCurrency()
    }

    private fun openSettings() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun bindView(){
        val viewBinding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        targetProgressBar = viewBinding.targetProgressBar
        limitProgressBar = viewBinding.limitProgressBar
        targetprogressText = viewBinding.targetProgressText
        limitprogressText = viewBinding.limitProgressText
        historyBtn = viewBinding.historyBtn
        monsterpediaBtn = viewBinding.monsterpediaBtn
        settingsBtn = viewBinding.settingsBtn
        analyticsBtn = viewBinding.analyticsBtn
        financeBtn = viewBinding.financeBtn
        expenseGoal = viewBinding.expenseGoal
        savingGoal = viewBinding.savingGoal
        dateTodayTv = viewBinding.dateTodayTv
    }

    private fun loadAndDisplayProgress() {
        val sharedPref = getSharedPreferences(SettingsActivity.PREFERENCE_FILE, Context.MODE_PRIVATE)
        val target = sharedPref.getInt(SettingsActivity.TARGET, 500)
        val limit = sharedPref.getInt(SettingsActivity.LIMIT, 300)

        // Get current expense and income from shared preferences
        val currentExpense = sharedPref.getFloat("CURRENT_EXPENSE", 0f).toDouble()
        val currentIncome = sharedPref.getFloat("CURRENT_INCOME", 0f).toDouble()

        targetProgressBar.max = target
        targetProgressBar.progress = currentIncome.toInt()
        targetprogressText.text = "$currency $currentIncome/$target"

        limitProgressBar.max = limit
        limitProgressBar.progress = currentExpense.toInt()
        limitprogressText.text = "$currency $currentExpense/$limit"
    }

    private fun loadAndDisplayCurrency() {
        val sharedPref = getSharedPreferences("com.mobdeve.s11.group2.moneymonster.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
        currency = sharedPref.getString("CURRENCY", "PHP") ?: "PHP"

        // Get current progress for target and limit
        val currentIncome = sharedPref.getFloat("CURRENT_INCOME", 0f).toDouble()
        val currentExpense = sharedPref.getFloat("CURRENT_EXPENSE", 0f).toDouble()

        // Update text views with current values
        targetprogressText.text = "$currency $currentIncome/${targetProgressBar.max}"
        limitprogressText.text = "$currency $currentExpense/${limitProgressBar.max}"
    }


    private fun startProgress(target: Int, currentIncome: Double) {
        targetProgressBar.progress = 0
        val maxProgress = target

        // Update the text initially to show progress is 0
        targetprogressText.text = "$currency 0/$maxProgress"

        Thread {
            for (progress in 0..maxProgress) {
                // Update progress bar and text
                handler.post {
                    targetProgressBar.progress = progress
                    targetprogressText.text = "$currency $progress/$maxProgress"
                }
                try {
                    Thread.sleep(10) // Add delay to simulate progress
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }.start()
    }

    private fun setDateToday(){
        val dateToday = Calendar.getInstance().time
        val formatter = SimpleDateFormat("MMM dd, yyyy")
        dateTodayTv.text = formatter.format(dateToday)
    }

    override fun onResume() {
        super.onResume()
        loadAndDisplayProgress()
        loadAndDisplayCurrency()
    }
}
