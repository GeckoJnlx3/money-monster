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
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        dateRangeSpinner = viewBinding.dateRangeSpnr
        targetProgressBar = viewBinding.targetProgressBar
        limitProgressBar = viewBinding.limitProgressBar
        targetprogressText = viewBinding.targetProgressText
        limitprogressText = viewBinding.limitProgressText
        monsterpediaBtn = viewBinding.monsterpediaBtn
        settingsBtn = viewBinding.settingsBtn
        analyticsBtn = viewBinding.analyticsBtn
        financeBtn = viewBinding.financeBtn
        expenseGoal = viewBinding.expenseGoal
        savingGoal = viewBinding.savingGoal

        val weekRanges = generateWeekRanges()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, weekRanges)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dateRangeSpinner.adapter = adapter

        settingsBtn.setOnClickListener { openSettings() }
        expenseGoal.setOnClickListener { openSettings() }
        savingGoal.setOnClickListener { openSettings() }

        monsterpediaBtn.setOnClickListener {
            val intent = Intent(this, MonsterActivity::class.java)
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
    }

    private fun openSettings() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun generateWeekRanges(): List<String> {
        return listOf(
            "October 1 - October 7",
            "October 8 - October 14",
            "October 15 - October 21",
            "October 22 - October 28",
            "October 29 - November 4"
        )
    }

    private fun loadAndDisplayProgress() {
        val sharedPref = getSharedPreferences("com.mobdeve.s11.group2.moneymonster.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
        val target = sharedPref.getInt("TARGET", 500)
        val limit = sharedPref.getInt("LIMIT", 300)

        targetProgressBar.max = target
        targetprogressText.text = "0/$target"

        limitProgressBar.max = limit
        limitprogressText.text = "0/$limit"
    }

    private fun startProgress(target: Int) {
        targetProgressBar.progress = 0
        val maxProgress = target

        Thread {
            for (progress in 0..maxProgress) {
                handler.post {
                    targetProgressBar.progress = progress
                    targetprogressText.text = "$progress/$maxProgress"
                }
            }
        }.start()
    }

    override fun onResume() {
        super.onResume()
        loadAndDisplayProgress()
    }
}
