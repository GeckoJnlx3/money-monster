package com.mobdeve.s11.group2.moneymonster.monsterpedia

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

        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.readableDatabase

        val monsterId = 1

        val cursor = db.rawQuery(
            "SELECT * FROM ${DatabaseHelper.MONSTER_TABLE_NAME} WHERE ${DatabaseHelper.COL_MONSTER_ID} = ?",
            arrayOf(monsterId.toString())
        )

        if (cursor.moveToFirst()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NAME))
            val description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DESCRIPTION))
            val image = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_IMAGE))

            val monsterNameTextView: TextView = findViewById(R.id.monsterName)
            val monsterDescriptionTextView: TextView = findViewById(R.id.description)
            val monsterImageView: ImageView = findViewById(R.id.monsterImg)

            monsterNameTextView.text = name
            monsterDescriptionTextView.text = description
            monsterImageView.setImageResource(image) // Assumes the image is a drawable resource ID
        }

        cursor.close()
        db.close()

    }
}

