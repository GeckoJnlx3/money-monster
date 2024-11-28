package com.mobdeve.s11.group2.moneymonster

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.*
import androidx.activity.ComponentActivity
import com.mobdeve.s11.group2.moneymonster.databinding.ActivityMainBinding
import com.mobdeve.s11.group2.moneymonster.history.HistoryActivity
import com.mobdeve.s11.group2.moneymonster.finance.FinanceActivity
import com.mobdeve.s11.group2.moneymonster.monsterpedia.MonsterpediaActivity
import android.database.sqlite.SQLiteDatabase
import com.mobdeve.s11.group2.moneymonster.DatabaseHelper
import com.mobdeve.s11.group2.moneymonster.monster.MonsterStatActivity
import java.text.SimpleDateFormat
import java.util.*
import android.view.View

class MainActivity : ComponentActivity() {

    private lateinit var targetProgressBar: ProgressBar
    private lateinit var limitProgressBar: ProgressBar
    private lateinit var levelProgressBar: ProgressBar
    private lateinit var targetProgressText: TextView
    private lateinit var limitProgressText: TextView
    private lateinit var levelProgressText: TextView
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
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindView()

        databaseHelper = DatabaseHelper(this)
        db = databaseHelper.writableDatabase

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
        checkAndLevelUpMonster()
    }

    private fun openSettings() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun bindView() {
        val viewBinding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        targetProgressBar = viewBinding.targetProgressBar
        limitProgressBar = viewBinding.limitProgressBar
        targetProgressText = viewBinding.targetProgressText
        limitProgressText = viewBinding.limitProgressText
        historyBtn = viewBinding.historyBtn
        monsterpediaBtn = viewBinding.monsterpediaBtn
        settingsBtn = viewBinding.settingsBtn
        analyticsBtn = viewBinding.analyticsBtn
        financeBtn = viewBinding.financeBtn
        expenseGoal = viewBinding.expenseGoal
        savingGoal = viewBinding.savingGoal
        dateTodayTv = viewBinding.dateTodayTv
        monsterImageView = viewBinding.monsterImage

        // Inflate the layout for monster stats
        val monsterStatsView = layoutInflater.inflate(R.layout.monster_stats, null)
        levelProgressBar = monsterStatsView.findViewById(R.id.levelProgressBar)
        levelProgressText = monsterStatsView.findViewById(R.id.levelProgressText)
    }

    private fun loadAndDisplayProgress() {
        val sharedPref = getSharedPreferences(SettingsActivity.PREFERENCE_FILE, MODE_PRIVATE)
        val target = sharedPref.getFloat(SettingsActivity.TARGET, 500.0f)
        val limit = sharedPref.getFloat(SettingsActivity.LIMIT, 300.0f)

        val currentExpense = sharedPref.getFloat("CURRENT_EXPENSE", 0f).toDouble()
        val currentIncome = sharedPref.getFloat("CURRENT_INCOME", 0f).toDouble()

        targetProgressBar.max = target.toInt()
        targetProgressBar.progress = currentIncome.toInt()
        targetProgressText.text = String.format("$currency %.2f/%.2f", currentIncome, target.toDouble())

        limitProgressBar.max = limit.toInt()
        limitProgressBar.progress = currentExpense.toInt()
        limitProgressText.text = String.format("$currency %.2f/%.2f", currentExpense, limit.toDouble())

        // Load monster progress from the database
        val (targetProgress, levelProgress) = databaseHelper.getProgress()
        targetProgressBar.progress = targetProgress
        levelProgressBar.progress = levelProgress
        levelProgressText.text = "${levelProgressBar.progress}/${levelProgressBar.max}"
    }

    private fun loadAndDisplayCurrency() {
        val sharedPref = getSharedPreferences(SettingsActivity.PREFERENCE_FILE, MODE_PRIVATE)
        currency = sharedPref.getString(SettingsActivity.CURRENCY, "PHP") ?: "PHP"

        val currentIncome = sharedPref.getFloat("CURRENT_INCOME", 0f).toDouble()
        val currentExpense = sharedPref.getFloat("CURRENT_EXPENSE", 0f).toDouble()

        targetProgressText.text = "$currency %.2f/%.2f".format(currentIncome, targetProgressBar.max.toDouble())
        limitProgressText.text = "$currency %.2f/%.2f".format(currentExpense, limitProgressBar.max.toDouble())
    }

    private fun checkAndLevelUpMonster() {
        val monsterId = 1
        val gainedExp = 20
        MonsterProgressionHelper.levelUpMonster(db, monsterId, gainedExp)
    }

    private fun updateProgress(increment: Int) {
        val currentProgress = targetProgressBar.progress
        val maxProgress = targetProgressBar.max

        val newProgress = currentProgress + increment
        targetProgressBar.progress = newProgress

        // Update the progress text
        targetProgressText.text = "$newProgress/$maxProgress"

        // Check if the target is reached
        if (newProgress >= maxProgress) {
            targetProgressBar.progress = 0 // Reset target progress

            // Increment level progress
            val currentLevelProgress = levelProgressBar.progress
            val levelMax = levelProgressBar.max

            if (currentLevelProgress < levelMax) {
                levelProgressBar.progress = currentLevelProgress + 1
                levelProgressText.text = "${levelProgressBar.progress}/$levelMax"
            }

            // Save progress
            saveProgress()
        }
    }

    private fun saveProgress() {
        databaseHelper.updateProgress(targetProgressBar.progress, levelProgressBar.progress)
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
    }
}
