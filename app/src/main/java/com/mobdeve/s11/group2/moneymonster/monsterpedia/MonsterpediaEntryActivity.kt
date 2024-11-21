package com.mobdeve.s11.group2.moneymonster.monsterpedia

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.mobdeve.s11.group2.moneymonster.R

class MonsterpediaEntryActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.monsterpedia_entry)

        val monsterName = intent.getStringExtra("monster_name")
        val monsterImageRes = intent.getIntExtra("monster_image", 0)

        val monsterNameTextView: TextView = findViewById(R.id.monsterName)
        val monsterImageView: ImageView = findViewById(R.id.monsterImg)

        monsterNameTextView.text = monsterName
        monsterImageView.setImageResource(monsterImageRes)
    }
}

