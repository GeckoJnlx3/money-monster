package com.mobdeve.s11.group2.moneymonster

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.Cursor
import android.util.Log
import com.mobdeve.s11.group2.moneymonster.DatabaseHelper.Companion.MONSTER_TABLE_NAME
import com.mobdeve.s11.group2.moneymonster.DatabaseHelper.Companion.COL_SPECIES
import com.mobdeve.s11.group2.moneymonster.DatabaseHelper.Companion.COL_NAME
import com.mobdeve.s11.group2.moneymonster.DatabaseHelper.Companion.COL_LEVEL
import com.mobdeve.s11.group2.moneymonster.DatabaseHelper.Companion.COL_REQ_EXP
import com.mobdeve.s11.group2.moneymonster.DatabaseHelper.Companion.COL_STAT_SAVED
import com.mobdeve.s11.group2.moneymonster.DatabaseHelper.Companion.COL_STAT_SPENT
import com.mobdeve.s11.group2.moneymonster.DatabaseHelper.Companion.COL_ON_FIELD

object MonsterProgressionHelper {

    // Function to level up a monster based on the experience gained
    fun levelUpMonster(db: SQLiteDatabase, monsterId: Int, gainedExp: Int) {
        // Query to fetch the current data of the monster
        val cursor: Cursor = db.rawQuery("SELECT * FROM $MONSTER_TABLE_NAME WHERE ${DatabaseHelper.COL_SPECIES} = ?", arrayOf(monsterId.toString()))

        // Check if cursor is valid and contains data
        if (cursor != null && cursor.moveToFirst()) {
            // Safe access to column indices with checks for column availability
            val currentExpIndex = cursor.getColumnIndex(DatabaseHelper.COL_REQ_EXP)
            val currentLevelIndex = cursor.getColumnIndex(DatabaseHelper.COL_LEVEL)

            if (currentExpIndex != -1 && currentLevelIndex != -1) {
                val currentExp = cursor.getInt(currentExpIndex)
                val currentLevel = cursor.getInt(currentLevelIndex)

                // Calculate the new experience and check if leveling up is necessary
                val newExp = currentExp + gainedExp
                var newLevel = currentLevel
                while (newExp >= getExpForNextLevel(newLevel)) {
                    newLevel++
                }

                // Update the monster's experience and level in the database
                val values = ContentValues().apply {
                    put(DatabaseHelper.COL_REQ_EXP, newExp)
                    put(DatabaseHelper.COL_LEVEL, newLevel)
                    // You can also adjust stats if necessary
                }
                db.update(MONSTER_TABLE_NAME, values, "${DatabaseHelper.COL_SPECIES} = ?", arrayOf(monsterId.toString()))
            } else {
                // Log error if columns are not found
                Log.e("MonsterProgressionHelper", "Column not found!")
            }
        } else {
            // Log error if no data is returned for the specified monsterId
            Log.e("MonsterProgressionHelper", "No monster found with species id $monsterId")
        }
        cursor.close()
    }

    // Helper function to define the experience required for each level
    private fun getExpForNextLevel(level: Int): Int {
        return when (level) {
            1 -> 10
            2 -> 20
            3 -> 30
            else -> 50 * level
        }
    }
}
