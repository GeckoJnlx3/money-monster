package com.mobdeve.s11.group2.moneymonster

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.view.View
import androidx.activity.ComponentActivity
import com.mobdeve.s11.group2.moneymonster.databinding.ActivityMainBinding
import com.mobdeve.s11.group2.moneymonster.history.HistoryActivity
import com.mobdeve.s11.group2.moneymonster.finance.FinanceActivity
import com.mobdeve.s11.group2.moneymonster.monsterpedia.MonsterpediaActivity
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.mobdeve.s11.group2.moneymonster.MonsterProgressionHelper
import com.mobdeve.s11.group2.moneymonster.DatabaseHelper
import com.mobdeve.s11.group2.moneymonster.MonsterDataHelper
import com.mobdeve.s11.group2.moneymonster.finance.FormatUtils
import com.mobdeve.s11.group2.moneymonster.monster.MonsterStatActivity
import java.text.SimpleDateFormat
import java.util.Calendar

class MainActivity : ComponentActivity() {

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
    private lateinit var monsterImageView: ImageView
    private val handler = Handler()

    private var currency: String = "PHP"

    private lateinit var db: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = DatabaseHelper(this).writableDatabase
        bindView()

        settingsBtn.setOnClickListener { openSettings() }
        expenseGoal.setOnClickListener { openSettings() }
        savingGoal.setOnClickListener { openSettings() }

        setDateToday()

        historyBtn.setOnClickListener {
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

        monsterImageView.setOnClickListener {
            val intent = Intent(this, MonsterStatActivity::class.java)
            startActivity(intent)
        }

        loadAndDisplayProgress()
        loadAndDisplayCurrency()

    }

    private fun openSettings() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun bindView() {
        val viewBinding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val activeMonster = MonsterDataHelper.getActiveMonster(db)
        Log.d("MainActivity", "Active Monster: $activeMonster")

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
        monsterImageView = viewBinding.monsterImage
    }

    private fun loadAndDisplayProgress() {
        val sharedPref = getSharedPreferences(SettingsActivity.PREFERENCE_FILE, MODE_PRIVATE)
        val target = sharedPref.getFloat(SettingsActivity.TARGET, 500.0.toFloat())
        val limit = sharedPref.getFloat(SettingsActivity.LIMIT, 300.0.toFloat())

        // Get current expense and income from shared preferences
        val currentExpense = sharedPref.getFloat("CURRENT_EXPENSE", 0f).toDouble()
        val currentIncome = sharedPref.getFloat("CURRENT_INCOME", 0f).toDouble()

        targetProgressBar.max = target.toInt()
        targetProgressBar.progress = currentIncome.toInt()
        targetprogressText.text = String.format("$currency %.2f/%.2f", currentIncome, target.toDouble())

        limitProgressBar.max = limit.toInt()
        limitProgressBar.progress = currentExpense.toInt()
        limitprogressText.text = String.format("$currency %.2f/%.2f", currentExpense, limit.toDouble())
    }

    private fun loadAndDisplayCurrency() {
        val sharedPref = getSharedPreferences(SettingsActivity.PREFERENCE_FILE, MODE_PRIVATE)
        currency = sharedPref.getString(SettingsActivity.CURRENCY, "PHP") ?: "PHP"

        val currentIncome = sharedPref.getFloat("CURRENT_INCOME", 0f).toDouble()
        val currentExpense = sharedPref.getFloat("CURRENT_EXPENSE", 0f).toDouble()

        targetprogressText.text = "$currency %.2f/%.2f".format(currentIncome, targetProgressBar.max.toDouble())
        limitprogressText.text = "$currency %.2f/%.2f".format(currentExpense, limitProgressBar.max.toDouble())
    }

    private fun loadMonsterData() {
        val activeMonster = MonsterDataHelper.getActiveMonster(db)

        Log.d("MonsterStatActivity", "Active Monster: $activeMonster")

        if (activeMonster != null) {
            monsterImageView.setImageResource(activeMonster.image)
        }
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
                    targetprogressText.text = "$currency %.2f/%.2f".format(progress.toDouble(), maxProgress.toDouble())
                }
                try {
                    Thread.sleep(10) // Add delay to simulate progress
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }.start()
    }

    private fun setDateToday() {
        val dateToday = Calendar.getInstance().time
        val formatter = SimpleDateFormat("MMM dd, yyyy")
        dateTodayTv.text = formatter.format(dateToday)
    }

    override fun onResume() {
        super.onResume()
        loadAndDisplayProgress()
        loadAndDisplayCurrency()
        loadMonsterData()
    }
}

