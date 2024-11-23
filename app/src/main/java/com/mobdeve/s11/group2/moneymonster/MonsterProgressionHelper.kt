package com.mobdeve.s11.group2.moneymonster

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.Cursor
import android.util.Log
import com.mobdeve.s11.group2.moneymonster.DatabaseHelper.Companion.MONSTER_TABLE_NAME
import com.mobdeve.s11.group2.moneymonster.DatabaseHelper.Companion.COL_MONSTER_ID

object MonsterProgressionHelper {

    fun levelUpMonster(db: SQLiteDatabase, monsterId: Int, gainedExp: Int) {
        val cursor: Cursor = db.rawQuery("SELECT * FROM $MONSTER_TABLE_NAME WHERE $COL_MONSTER_ID = ?", arrayOf(monsterId.toString()))

        if (cursor != null && cursor.moveToFirst()) {
            val currentExpIndex = cursor.getColumnIndex(DatabaseHelper.COL_REQ_EXP)
            val currentLevelIndex = cursor.getColumnIndex(DatabaseHelper.COL_LEVEL)

            if (currentExpIndex != -1 && currentLevelIndex != -1) {
                val currentExp = cursor.getInt(currentExpIndex)
                val currentLevel = cursor.getInt(currentLevelIndex)

                val newExp = currentExp + gainedExp
                var newLevel = currentLevel
                while (newExp >= getExpForNextLevel(newLevel)) {
                    newLevel++
                }

                val values = ContentValues().apply {
                    put(DatabaseHelper.COL_REQ_EXP, newExp)
                    put(DatabaseHelper.COL_LEVEL, newLevel)
                }
                db.update(MONSTER_TABLE_NAME, values, "${DatabaseHelper.COL_SPECIES} = ?", arrayOf(monsterId.toString()))
            } else {
                Log.e("MonsterProgressionHelper", "Column not found!")
            }
        } else {
            Log.e("MonsterProgressionHelper", "No monster found with species id $monsterId")
        }
        cursor.close()
    }

    private fun getExpForNextLevel(level: Int): Int {
        return when (level) {
            1 -> 10
            2 -> 20
            3 -> 30
            else -> 50 * level
        }
    }
}
