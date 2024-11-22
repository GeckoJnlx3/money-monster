package com.mobdeve.s11.group2.moneymonster

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.mobdeve.s11.group2.moneymonster.DatabaseHelper.Companion.MONSTER_TABLE_NAME
import com.mobdeve.s11.group2.moneymonster.DatabaseHelper.Companion.COL_SPECIES
import com.mobdeve.s11.group2.moneymonster.DatabaseHelper.Companion.COL_NAME
import com.mobdeve.s11.group2.moneymonster.DatabaseHelper.Companion.COL_IMAGE
import com.mobdeve.s11.group2.moneymonster.DatabaseHelper.Companion.COL_ADOPTION_DATE
import com.mobdeve.s11.group2.moneymonster.DatabaseHelper.Companion.COL_STAGE
import com.mobdeve.s11.group2.moneymonster.DatabaseHelper.Companion.COL_UP_TICK
import com.mobdeve.s11.group2.moneymonster.DatabaseHelper.Companion.COL_REQ_EXP
import com.mobdeve.s11.group2.moneymonster.DatabaseHelper.Companion.COL_LEVEL
import com.mobdeve.s11.group2.moneymonster.DatabaseHelper.Companion.COL_STAT_SAVED
import com.mobdeve.s11.group2.moneymonster.DatabaseHelper.Companion.COL_STAT_SPENT
import com.mobdeve.s11.group2.moneymonster.DatabaseHelper.Companion.COL_DESCRIPTION
import com.mobdeve.s11.group2.moneymonster.DatabaseHelper.Companion.COL_UNLOCKED
import com.mobdeve.s11.group2.moneymonster.DatabaseHelper.Companion.COL_ON_FIELD

import com.mobdeve.s11.group2.moneymonster.DatabaseHelper.Companion.DATE_FORMAT

import com.mobdeve.s11.group2.moneymonster.monster.Monster
import java.sql.Date

object MonsterDataHelper {

    fun populateMonsterTable(db: SQLiteDatabase?) {
        val monsters = listOf(
            Monster(1, "gwomp", "Gwomp", R.drawable.gwomp_baby, Date(System.currentTimeMillis()), "baby", 0, 5, 1, 0, 0, "A baby Gwomp.", true, true),
            Monster(2, "mamoo", "Mamoo", R.drawable.mamoo_baby, Date(System.currentTimeMillis()), "teen", 0, 15, 1, 0, 0, "A baby Mamoo.", false, false),
            Monster(3, "ave", "Ave", R.drawable.ave_baby, Date(System.currentTimeMillis()), "adult", 0, 25, 1, 0, 0, "A baby Ave.", false, false),

            Monster(4, "gwomp", "Gwompor", R.drawable.gwomp_teen, Date(System.currentTimeMillis()), "baby", 0, 5, 2, 0, 0, "The teenage Gwomp.", false, false),
            Monster(5, "mamoo", "Moomie", R.drawable.mamoo_teen, Date(System.currentTimeMillis()), "teen", 0, 15, 2, 0, 0, "The teenage Mamoo.", false, false),
            Monster(6, "ave", "Evale", R.drawable.ave_teen, Date(System.currentTimeMillis()), "adult", 0, 25, 2, 0, 0, "The teenage Ave.", false, false),

            Monster(7, "gwomp", "Wompagwom", R.drawable.gwomp_adult, Date(System.currentTimeMillis()), "baby", 0, 5, 3, 0, 0, "The adult Gwomp.", false, false),
            Monster(8, "mamoo", "Mamoolah", R.drawable.mamoo_adult, Date(System.currentTimeMillis()), "teen", 0, 15, 3, 0, 0, "The adult Mamoo.", false, false),
            Monster(9, "ave", "Alvirose", R.drawable.ave_adult, Date(System.currentTimeMillis()), "adult", 0, 25, 3, 0, 0, "The adult Ave.", false, false)
        )

        monsters.forEach { monster ->
            val values = ContentValues().apply {
                put(COL_SPECIES, monster.species)
                put(COL_NAME, monster.name)
                put(COL_IMAGE, monster.image)
                put(COL_ADOPTION_DATE, DATE_FORMAT.format(monster.adoptionDate))
                put(COL_STAGE, monster.stage)
                put(COL_UP_TICK, monster.upTick)
                put(COL_REQ_EXP, monster.reqExp)
                put(COL_LEVEL, monster.level)
                put(COL_STAT_SAVED, monster.statSaved)
                put(COL_STAT_SPENT, monster.statSpent)
                put(COL_DESCRIPTION, monster.description)
                put(COL_UNLOCKED, if (monster.unlocked) 1 else 0)
                put(COL_ON_FIELD, if (monster.onField) 1 else 0)
            }
            db?.insert(MONSTER_TABLE_NAME, null, values)
        }
    }

    // Step 1: Method to update monster's level and change its image accordingly
    fun updateMonsterLevel(db: SQLiteDatabase, monsterId: Int, newLevel: Int) {
        val values = ContentValues().apply {
            put(COL_LEVEL, newLevel)
            put(COL_IMAGE, getImageForLevel(newLevel))  // Update image based on level
        }

        val whereClause = "${DatabaseHelper.Companion.COL_SPECIES} = ?"
        val whereArgs = arrayOf(monsterId.toString())

        db.update(MONSTER_TABLE_NAME, values, whereClause, whereArgs)
    }

    // Step 1: Helper method to get the image corresponding to the monster's level
    private fun getImageForLevel(level: Int): Int {
        return when (level) {
            1 -> R.drawable.gwomp_baby   // Image for level 1 (Baby)
            2 -> R.drawable.gwomp_teen   // Image for level 2 (Teen)
            3 -> R.drawable.gwomp_adult  // Image for level 3 (Adult)
            else -> R.drawable.gwomp_baby  // Default fallback image
        }
    }

    // Method to retrieve a monster's details including the level and image
    fun getMonsterDetails(db: SQLiteDatabase, monsterId: Int): Monster? {
        val cursor = db.query(
            MONSTER_TABLE_NAME,
            null, // Select all columns
            "${DatabaseHelper.Companion.COL_SPECIES} = ?", // WHERE clause
            arrayOf(monsterId.toString()), // Selection args
            null, // No GROUP BY
            null, // No HAVING
            null  // No ORDER BY
        )

        var monster: Monster? = null
        if (cursor != null && cursor.moveToFirst()) {
            val speciesIndex = cursor.getColumnIndex(DatabaseHelper.Companion.COL_SPECIES)
            val nameIndex = cursor.getColumnIndex(DatabaseHelper.Companion.COL_NAME)
            val imageIndex = cursor.getColumnIndex(DatabaseHelper.Companion.COL_IMAGE)
            val levelIndex = cursor.getColumnIndex(DatabaseHelper.Companion.COL_LEVEL)

            val species = cursor.getString(speciesIndex)
            val name = cursor.getString(nameIndex)
            val imageResId = cursor.getInt(imageIndex)
            val level = cursor.getInt(levelIndex)

            monster = Monster(
                id = monsterId,
                species = species,
                name = name,
                image = imageResId,
                level = level
            )
        }
        cursor?.close()

        return monster
    }
}
