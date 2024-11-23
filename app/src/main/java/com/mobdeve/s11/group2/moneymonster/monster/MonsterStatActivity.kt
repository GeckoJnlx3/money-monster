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
            loadMonsterData()
        }
    }

    private lateinit var currency: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val filter = IntentFilter("com.mobdeve.s11.group2.moneymonster.UPDATE_MONSTER_STATS")

        ContextCompat.registerReceiver(this, statsUpdateReceiver, filter, ContextCompat.RECEIVER_NOT_EXPORTED)

        val binding: MonsterStatBinding = MonsterStatBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        currency = sharedPref.getString(SettingsActivity.CURRENCY, "PHP") ?: "PHP"  // Default to "PHP" if not set
    }

    private fun loadMonsterData() {
        val db = databaseHelper.readableDatabase

        val activeMonster = MonsterDataHelper.getActiveMonster(db)

        if (activeMonster != null) {
            monsterNameText.text = activeMonster.name

            moneySavedValue.text = FormatUtils.formatAmount(activeMonster.statSaved, currency)
            moneySpentValue.text = FormatUtils.formatAmount(activeMonster.statSpent, currency)
            adoptedOnValue.text = "${activeMonster.adoptionDate}"
            levelNumberText.text = "${activeMonster.level}"
            levelProgressText.text = "EXP ${activeMonster.upTick}/${activeMonster.reqExp}"
            levelProgressBar.progress = activeMonster.level

//            monsterImageView.setImageResource(
//                if (activeMonster.statSaved >= activeMonster.levelUpThreshold)
//                    R.drawable.level_up_monster_image
//                else
//                    R.drawable.normal_monster_image
//            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(statsUpdateReceiver)
    }
}
