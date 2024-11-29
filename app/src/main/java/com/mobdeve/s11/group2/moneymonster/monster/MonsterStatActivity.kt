package com.mobdeve.s11.group2.moneymonster.monster

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import com.mobdeve.s11.group2.moneymonster.R
import com.mobdeve.s11.group2.moneymonster.DatabaseHelper
import com.mobdeve.s11.group2.moneymonster.MonsterDataHelper
import com.mobdeve.s11.group2.moneymonster.databinding.MonsterStatBinding
import com.mobdeve.s11.group2.moneymonster.finance.FormatUtils
import com.mobdeve.s11.group2.moneymonster.SettingsActivity

class MonsterStatActivity : ComponentActivity() {

    private lateinit var levelProgressBar: ProgressBar
    private lateinit var levelProgressText: TextView
    private lateinit var monsterNameText: TextView
    private lateinit var moneySavedValue: TextView
    private lateinit var moneySpentValue: TextView
    private lateinit var adoptedOnValue: TextView
    private lateinit var monsterImageView: ImageView
    private lateinit var levelNumberText: TextView

    private lateinit var databaseHelper: DatabaseHelper

    private val statsUpdateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "com.mobdeve.s11.group2.moneymonster.UPDATE_MONSTER_STATS") {
                val increment = intent.getIntExtra("LEVEL_PROGRESS_INCREMENT", 0)
                updateLevelProgress(increment)
            }
        }
    }

    private lateinit var currency: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Register receiver for stats update
        ContextCompat.registerReceiver(
            this,
            statsUpdateReceiver,
            IntentFilter("com.mobdeve.s11.group2.moneymonster.UPDATE_MONSTER_STATS"),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )

        val binding: MonsterStatBinding = MonsterStatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize UI components
        levelProgressBar = binding.levelProgressBar
        levelProgressText = binding.levelProgressText
        monsterNameText = binding.monsterName
        moneySavedValue = binding.moneySavedValue
        moneySpentValue = binding.moneySpentValue
        adoptedOnValue = binding.adoptedOnValue
        monsterImageView = binding.monsterImage
        levelNumberText = binding.levelNumber

        databaseHelper = DatabaseHelper(this)

        loadCurrency()
        loadMonsterData()
    }

    private fun loadCurrency() {
        val sharedPref: SharedPreferences = getSharedPreferences(SettingsActivity.PREFERENCE_FILE, Context.MODE_PRIVATE)
        currency = sharedPref.getString(SettingsActivity.CURRENCY, "PHP") ?: "PHP"
    }

    private fun loadMonsterData() {
        val db = databaseHelper.readableDatabase
        val monsterData = MonsterDataHelper.loadMonsterData(db)

        levelNumberText.text = monsterData.level.toString()
        levelProgressBar.max = monsterData.maxLevelProgress
        levelProgressBar.progress = monsterData.levelProgress
        levelProgressText.text = "${monsterData.levelProgress}/${monsterData.maxLevelProgress}"
        monsterNameText.text = monsterData.name
        moneySavedValue.text = FormatUtils.formatCurrency(currency, monsterData.moneySaved.toDouble())
        moneySpentValue.text = FormatUtils.formatCurrency(currency, monsterData.moneySpent.toDouble())
        adoptedOnValue.text = monsterData.adoptedOn
        monsterImageView.setImageResource(monsterData.imageResId)
    }

    private fun updateLevelProgress(increment: Int) {
        val db = databaseHelper.writableDatabase
        MonsterDataHelper.updateMonsterLevelProgress(db, increment)
        loadMonsterData()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(statsUpdateReceiver)
    }
}
