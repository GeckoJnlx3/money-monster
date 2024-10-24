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
    private lateinit var loginBtn: Button
    private lateinit var monsterpediaBtn: Button
    private lateinit var analyticsBtn: Button
    private lateinit var financeBtn: Button
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
        monsterpediaBtn = viewBinding.monsterpediaBtn
        loginBtn = viewBinding.loginBtn
        analyticsBtn = viewBinding.analyticsBtn
        financeBtn = viewBinding.financeBtn

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

        financeBtn.setOnClickListener {
            val intent = Intent(this, FinanceActivity::class.java)
            startActivity(intent)
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
