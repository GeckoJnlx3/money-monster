package com.mobdeve.s11.group2.moneymonster

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.core.database.getDoubleOrNull
import com.mobdeve.s11.group2.moneymonster.DatabaseHelper.Companion.MONSTER_TABLE_NAME
import com.mobdeve.s11.group2.moneymonster.DatabaseHelper.Companion.COL_MONSTER_ID
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
import com.mobdeve.s11.group2.moneymonster.monster.MonsterData
import com.mobdeve.s11.group2.moneymonster.monster.Monster
import java.sql.Date

object MonsterDataHelper {

    fun populateMonsterTable(db: SQLiteDatabase?) {
        val monsters = listOf(
            Monster(1, "gwomp", "Gwomp", R.drawable.gwomp_baby, Date(System.currentTimeMillis()), "baby", 0, 5, 1, 0.00, 0.00, "A baby Gwomp.", true, true),
            Monster(2, "gwomp", "Gwompor", R.drawable.gwomp_teen, Date(System.currentTimeMillis()), "teen", 5, 15, 2, 0.00, 0.00, "The teenage Gwomp.", false, false),
            Monster(3, "gwomp", "Wompagwom", R.drawable.gwomp_adult, Date(System.currentTimeMillis()), "adult", 15, 25, 3, 0.00, 0.00, "The adult Gwomp.", false, false),
            Monster(4, "mamoo", "Mamoo", R.drawable.mamoo_baby, Date(System.currentTimeMillis()), "baby", 0, 15, 1, 0.00, 0.00, "A baby Mamoo.", false, false),
            Monster(5, "mamoo", "Moomie", R.drawable.mamoo_teen, Date(System.currentTimeMillis()), "teen", 5, 15, 2, 0.00, 0.00, "The teenage Mamoo.", false, false),
            Monster(6, "mamoo", "Mamoolah", R.drawable.mamoo_adult, Date(System.currentTimeMillis()), "adult", 15, 25, 3, 0.00, 0.00, "The adult Mamoo.", false, false),
            Monster(7, "ave", "Ave", R.drawable.ave_baby, Date(System.currentTimeMillis()), "baby", 0, 25, 1, 0.00, 0.00, "A baby Ave.", false, false),
            Monster(8, "ave", "Evale", R.drawable.ave_teen, Date(System.currentTimeMillis()), "teen", 5, 25, 2, 0.00, 0.00, "The teenage Ave.", false, false),
            Monster(9, "ave", "Alvirose", R.drawable.ave_adult, Date(System.currentTimeMillis()), "adult", 15, 25, 3, 0.00, 0.00, "The adult Ave.", false, false)
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
    fun updateMonsterLevelProgress(db: SQLiteDatabase, increment: Int) {
        db.execSQL(
            """
        UPDATE monsters 
        SET level_progress = level_progress + ? 
        WHERE active = 1
        """,
            arrayOf(increment)
        )
    }

    fun updateMonsterLevel(db: SQLiteDatabase, monsterId: Int, newLevel: Int) {
        val cursor = db.query(
            MONSTER_TABLE_NAME,
            arrayOf(COL_SPECIES),
            "$COL_MONSTER_ID = ?",
            arrayOf(monsterId.toString()),
            null,
            null,
            null
        )

        var species: String? = null
        if (cursor != null && cursor.moveToFirst()) {
            val speciesIndex = cursor.getColumnIndex(COL_SPECIES)
            species = cursor.getString(speciesIndex)
        }
        cursor?.close()

        if (species != null) {
            val values = ContentValues().apply {
                put(COL_LEVEL, newLevel)
                put(COL_IMAGE, getImageForLevel(species, newLevel))
            }

            db.update(MONSTER_TABLE_NAME, values, "$COL_MONSTER_ID = ?", arrayOf(monsterId.toString()))
        } else {
            throw IllegalArgumentException("Species not found for monster with ID: $monsterId")
        }
    }

    fun loadMonsterData(db: SQLiteDatabase): MonsterData {
        val cursor = db.query(
            "monsters", // Assuming the table is named "monsters"
            null, // Fetch all columns
            "active = ?", arrayOf("1"), // Assuming 'active' column indicates the active monster
            null, null, null
        )

        cursor.use {
            if (it.moveToFirst()) {
                return MonsterData(
                    level = it.getInt(it.getColumnIndexOrThrow("level")),
                    levelProgress = it.getInt(it.getColumnIndexOrThrow("level_progress")),
                    maxLevelProgress = it.getInt(it.getColumnIndexOrThrow("max_level_progress")),
                    name = it.getString(it.getColumnIndexOrThrow("name")),
                    moneySaved = it.getFloat(it.getColumnIndexOrThrow("money_saved")),
                    moneySpent = it.getFloat(it.getColumnIndexOrThrow("money_spent")),
                    adoptedOn = it.getString(it.getColumnIndexOrThrow("adopted_on")),
                    imageResId = it.getInt(it.getColumnIndexOrThrow("image_res_id"))
                )
            } else {
                throw IllegalStateException("No active monster found")
            }
        }
    }

    fun getImageForLevel(species: String, level: Int): Int {
        return when (species) {
            "gwomp" -> when {
                level <= 5 -> R.drawable.gwomp_baby
                level in 6..15 -> R.drawable.gwomp_teen
                else -> R.drawable.gwomp_adult
            }
            "mamoo" -> when {
                level <= 5 -> R.drawable.mamoo_baby
                level in 6..15 -> R.drawable.mamoo_teen
                else -> R.drawable.mamoo_adult
            }
            "ave" -> when {
                level <= 5 -> R.drawable.ave_baby
                level in 6..15 -> R.drawable.ave_teen
                else -> R.drawable.ave_adult
            }
            else -> throw IllegalArgumentException("Unknown species: $species")
        }
    }

    fun getActiveMonster(db: SQLiteDatabase): Monster? {
        val cursor = db.query(
            MONSTER_TABLE_NAME,
            null,
            "${COL_ON_FIELD} = ?",
            arrayOf("1"),
            null,
            null,
            null,
            "1"
        )

        var activeMonster: Monster? = null

        if (cursor != null && cursor.moveToFirst()) {
            activeMonster = Monster(
                monsterId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_MONSTER_ID)),
                species = cursor.getString(cursor.getColumnIndexOrThrow(COL_SPECIES)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)),
                image = cursor.getInt(cursor.getColumnIndexOrThrow(COL_IMAGE)),
                adoptionDate = Date(cursor.getLong(cursor.getColumnIndexOrThrow(COL_ADOPTION_DATE))),
                stage = cursor.getString(cursor.getColumnIndexOrThrow(COL_STAGE)),
                upTick = cursor.getInt(cursor.getColumnIndexOrThrow(COL_UP_TICK)),
                reqExp = cursor.getInt(cursor.getColumnIndexOrThrow(COL_REQ_EXP)),
                level = cursor.getInt(cursor.getColumnIndexOrThrow(COL_LEVEL)),
                statSaved = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_STAT_SAVED)),
                statSpent = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_STAT_SPENT)),
                description = cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION)),
                unlocked = cursor.getInt(cursor.getColumnIndexOrThrow(COL_UNLOCKED)) == 1,
                onField = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ON_FIELD)) == 1
            )
        }

        cursor?.close()
        return activeMonster
    }
}