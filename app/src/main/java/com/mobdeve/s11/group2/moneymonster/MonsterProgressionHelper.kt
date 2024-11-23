package com.mobdeve.s11.group2.moneymonster

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.Cursor
import android.util.Log
import com.mobdeve.s11.group2.moneymonster.MonsterDataHelper.getImageForLevel
import com.mobdeve.s11.group2.moneymonster.DatabaseHelper.Companion.MONSTER_TABLE_NAME
import com.mobdeve.s11.group2.moneymonster.DatabaseHelper.Companion.COL_MONSTER_ID

object MonsterProgressionHelper {

    fun levelUpMonster(db: SQLiteDatabase, monsterId: Int, gainedExp: Int): MonsterProgress {
        var cursor: Cursor? = null
        var updatedMonsterProgress = MonsterProgress(0, 0, "baby", 0)

        try {
            cursor = db.rawQuery(
                "SELECT * FROM $MONSTER_TABLE_NAME WHERE $COL_MONSTER_ID = ?",
                arrayOf(monsterId.toString())
            )
            if (cursor != null && cursor.moveToFirst()) {
                val currentExpIndex = cursor.getColumnIndex(DatabaseHelper.COL_REQ_EXP)
                val currentLevelIndex = cursor.getColumnIndex(DatabaseHelper.COL_LEVEL)
                val currentStageIndex = cursor.getColumnIndex(DatabaseHelper.COL_STAGE)

                if (currentExpIndex != -1 && currentLevelIndex != -1 && currentStageIndex != -1) {
                    val currentExp = cursor.getInt(currentExpIndex)
                    val currentLevel = cursor.getInt(currentLevelIndex)
                    val currentStage = cursor.getString(currentStageIndex)

                    var newExp = currentExp + gainedExp
                    var newLevel = currentLevel
                    var newStage = currentStage

                    // Level up logic
                    while (newExp >= getExpForNextLevel(newLevel, newStage)) {
                        newExp -= getExpForNextLevel(newLevel, newStage)
                        newLevel++

                        if (newStage == "baby" && newLevel > 5) {
                            newStage = "teen"
                            newLevel = 6
                        } else if (newStage == "teen" && newLevel > 15) {
                            newStage = "adult"
                            newLevel = 16
                        }
                    }

                    val values = ContentValues().apply {
                        put(DatabaseHelper.COL_REQ_EXP, newExp)
                        put(DatabaseHelper.COL_LEVEL, newLevel)
                        put(DatabaseHelper.COL_STAGE, newStage)
                        put(DatabaseHelper.COL_IMAGE, getImageForLevel(newStage, newLevel))
                    }

                    val rowsUpdated = db.update(
                        MONSTER_TABLE_NAME,
                        values,
                        "$COL_MONSTER_ID = ?",
                        arrayOf(monsterId.toString())
                    )

                    updatedMonsterProgress = MonsterProgress(newLevel, newExp, newStage, rowsUpdated)
                } else {
                    Log.e("MonsterProgressionHelper", "Column not found!")
                }
            } else {
                Log.e("MonsterProgressionHelper", "No monster found with ID $monsterId")
            }
        } catch (e: Exception) {
            Log.e("MonsterProgressionHelper", "Error updating monster progression: ${e.message}")
        } finally {
            cursor?.close()
        }

        return updatedMonsterProgress
    }

    data class MonsterProgress(val level: Int, val exp: Int, val stage: String, val rowsUpdated: Int)

    fun getExpForNextLevel(level: Int, stage: String): Int {
        return when (stage) {
            "baby" -> 5
            "teen" -> 15
            "adult" -> 25
            else -> throw IllegalArgumentException("Unknown stage: $stage")
        }
    }
}
