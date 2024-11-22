package com.mobdeve.s11.group2.moneymonster.monster

import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.mobdeve.s11.group2.moneymonster.databinding.MonsterStatBinding
import com.mobdeve.s11.group2.moneymonster.DatabaseHelper

class MonsterStatActivity : ComponentActivity() {

    private lateinit var levelProgressBar: ProgressBar
    private lateinit var levelProgressText: TextView
    private lateinit var monsterNameText: TextView
    private lateinit var moneySavedValue: TextView
    private lateinit var moneySpentValue: TextView
    private lateinit var adoptedOnValue: TextView
    private lateinit var monsterImageView: ImageView

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = MonsterStatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        levelProgressBar = binding.levelProgressBar
        levelProgressText = binding.levelProgressText
        monsterNameText = binding.monsterName
        moneySavedValue = binding.moneySavedValue
        moneySpentValue = binding.moneySpentValue
        adoptedOnValue = binding.adoptedOnValue
        monsterImageView = binding.monsterImage

        databaseHelper = DatabaseHelper(this)

        loadMonsterData()
    }

    private fun loadMonsterData() {
        val activeMonster = getActiveMonster()

        if (activeMonster != null) {
            monsterNameText.text = activeMonster.name
            moneySavedValue.text = "Php ${activeMonster.statSaved}"
            moneySpentValue.text = "Php ${activeMonster.statSpent}"
            adoptedOnValue.text = activeMonster.adoptionDate.toString()

            val maxLevel = 100
            levelProgressBar.max = maxLevel
            levelProgressBar.progress = activeMonster.level
            levelProgressText.text = "${activeMonster.level}/$maxLevel"

            monsterImageView.setImageResource(activeMonster.image)
        }
    }

    private fun getActiveMonster(): Monster? {
        val monsters = databaseHelper.getAllMonsters()

        return monsters.find { it.onField }
    }
}
