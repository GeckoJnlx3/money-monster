package com.mobdeve.s11.group2.moneymonster

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.mobdeve.s11.group2.moneymonster.databinding.ActivityMainBinding

class MainActivity : ComponentActivity() {

    private lateinit var dateRangeSpinner: Spinner
    private lateinit var budgetProgressBar: ProgressBar
    private lateinit var limitProgressBar: ProgressBar
    private lateinit var budgetprogressText: TextView
    private lateinit var limitprogressText: TextView
    private lateinit var startButton: Button
    private lateinit var loginBtn: Button
    private lateinit var settingBtn: Button // TBA
    private lateinit var monsterpediaBtn: Button
    private lateinit var analyticsBtn: Button
    private lateinit var financeBtn: Button // TBA
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        dateRangeSpinner = viewBinding.dateRangeSpnr
        budgetProgressBar = viewBinding.budgetprogressBar
        limitProgressBar = viewBinding.limitprogressBar
        budgetprogressText = viewBinding.budgetprogressText
        limitprogressText = viewBinding.limitprogressText
        startButton = viewBinding.settingBtn
        monsterpediaBtn = viewBinding.monsterpediaBtn
        loginBtn = viewBinding.loginBtn
        analyticsBtn = viewBinding.analyticsBtn

        val weekRanges = generateWeekRanges()

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, weekRanges)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dateRangeSpinner.adapter = adapter

        monsterpediaBtn.setOnClickListener {
            val intent = Intent(this, MonsterActivity::class.java)
            startActivity(intent)
        }

        loginBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        analyticsBtn.setOnClickListener {
            val intent = Intent(this, AnalyticsActivity::class.java)
            startActivity(intent)
        }

        startButton.setOnClickListener {
            startProgress()
        }
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

    private fun startProgress() {
        budgetProgressBar.progress = 0
        val maxProgress = 10

        Thread {
            for (progress in 0..maxProgress) {
                handler.post {
                    budgetProgressBar.progress = progress
                    budgetprogressText.text = "$progress/$maxProgress"
                }
            }
        }.start()
    }
}
