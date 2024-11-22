package com.mobdeve.s11.group2.moneymonster.monsterpedia

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.mobdeve.s11.group2.moneymonster.DatabaseHelper
import com.mobdeve.s11.group2.moneymonster.R

class MonsterpediaEntryActivity : ComponentActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.monsterpedia_entry)

        dbHelper = DatabaseHelper(this)

        val db = dbHelper.readableDatabase

        val monsterId = intent.getIntExtra("MONSTER_ID", -1)

        if (monsterId == -1) {
            finish()
            return
        }

        val cursor = db.rawQuery(
            "SELECT * FROM ${DatabaseHelper.MONSTER_TABLE_NAME} WHERE ${DatabaseHelper.COL_MONSTER_ID} = ?",
            arrayOf(monsterId.toString())
        )

        if (cursor.moveToFirst()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NAME))
            val description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DESCRIPTION))
            val image = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_IMAGE))
            val statSaved = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_STAT_SAVED))
            val statSpent = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_STAT_SPENT))
            val isUnlocked = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_UNLOCKED)) == 1

            val monsterNameTextView: TextView = findViewById(R.id.monsterName)
            val monsterDescriptionTextView: TextView = findViewById(R.id.description)
            val monsterImageView: ImageView = findViewById(R.id.monsterImg)
            val savedAmountTextView: TextView = findViewById(R.id.saveAmount)
            val spentAmountTextView: TextView = findViewById(R.id.spentAmount)

            monsterNameTextView.text = name
            monsterDescriptionTextView.text = description
            savedAmountTextView.text = "PHP %.2f".format(statSaved.toDouble())
            spentAmountTextView.text = "PHP %.2f".format(statSpent.toDouble())

            if (isUnlocked) {
                monsterImageView.setImageResource(image)
                monsterImageView.clearColorFilter()
            } else {
                monsterImageView.setImageResource(image)
                monsterImageView.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN)
            }
        }

        cursor.close()
        db.close()
    }
}
