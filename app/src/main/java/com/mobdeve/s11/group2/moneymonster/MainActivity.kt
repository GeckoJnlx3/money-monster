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
import com.mobdeve.s11.group2.moneymonster.finance.FinanceActivity
import com.mobdeve.s11.group2.moneymonster.history.HistoryActivity
import com.mobdeve.s11.group2.moneymonster.monsterpedia.MonsterpediaActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        val sharedPref = getSharedPreferences(SettingsActivity.PREFERENCE_FILE, Context.MODE_PRIVATE)
        val target = sharedPref.getInt(SettingsActivity.TARGET, 500)
        val limit = sharedPref.getInt(SettingsActivity.LIMIT, 300)

        targetProgressBar.max = target
        targetprogressText.text = "$currency %.2f/%.2f".format(0.0, target.toDouble())

        limitProgressBar.max = limit
        limitprogressText.text = "$currency %.2f/%.2f".format(0.0, limit.toDouble())
    }

    private fun loadAndDisplayCurrency() {
        val sharedPref = getSharedPreferences("com.mobdeve.s11.group2.moneymonster.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
        currency = sharedPref.getString("CURRENCY", "PHP") ?: "PHP"

        targetprogressText.text = "$currency %.2f/%.2f".format(0.0, targetProgressBar.max.toDouble())
        limitprogressText.text = "$currency %.2f/%.2f".format(0.0, limitProgressBar.max.toDouble())
    }

    private fun startProgress(target: Int) {
        targetProgressBar.progress = 0
        val maxProgress = target

        Thread {
            for (progress in 0..maxProgress) {
                handler.post {
                    targetProgressBar.progress = progress
                    targetprogressText.text = "$currency %.2f/%.2f".format(progress.toDouble(), maxProgress.toDouble())
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
    }
}
