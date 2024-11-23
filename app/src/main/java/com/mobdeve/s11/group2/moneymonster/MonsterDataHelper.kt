package com.mobdeve.s11.group2.moneymonster

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
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

import com.mobdeve.s11.group2.moneymonster.monster.Monster
import java.sql.Date

object MonsterDataHelper {

    fun populateMonsterTable(db: SQLiteDatabase?) {
        val monsters = listOf(
            Monster(1, "gwomp", "Gwomp", R.drawable.gwomp_baby, Date(System.currentTimeMillis()), "baby", 0, 5, 1, 0, 0, "A baby Gwomp.", true, true),
            Monster(2, "gwomp", "Gwompor", R.drawable.gwomp_teen, Date(System.currentTimeMillis()), "baby", 0, 5, 2, 0, 0, "The teenage Gwomp.", false, false),
            Monster(3, "gwomp", "Wompagwom", R.drawable.gwomp_adult, Date(System.currentTimeMillis()), "baby", 0, 5, 3, 0, 0, "The adult Gwomp.", false, false),

            Monster(4, "mamoo", "Mamoo", R.drawable.mamoo_baby, Date(System.currentTimeMillis()), "teen", 0, 15, 1, 0, 0, "A baby Mamoo.", false, false),
            Monster(5, "mamoo", "Moomie", R.drawable.mamoo_teen, Date(System.currentTimeMillis()), "teen", 0, 15, 2, 0, 0, "The teenage Mamoo.", false, false),
            Monster(6, "mamoo", "Mamoolah", R.drawable.mamoo_adult, Date(System.currentTimeMillis()), "teen", 0, 15, 3, 0, 0, "The adult Mamoo.", false, false),

            Monster(7, "ave", "Ave", R.drawable.ave_baby, Date(System.currentTimeMillis()), "adult", 0, 25, 1, 0, 0, "A baby Ave.", false, false),
            Monster(8, "ave", "Evale", R.drawable.ave_teen, Date(System.currentTimeMillis()), "adult", 0, 25, 2, 0, 0, "The teenage Ave.", false, false),
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

    private fun getImageForLevel(species: String, level: Int): Int {
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



    fun getMonsterDetails(db: SQLiteDatabase, monsterId: Int): Monster? {
        val cursor = db.query(
            MONSTER_TABLE_NAME,
            null,
            "${COL_MONSTER_ID} = ?",
            arrayOf(monsterId.toString()),
            null,
            null,
            null
        )

        var monster: Monster? = null
        if (cursor != null && cursor.moveToFirst()) {
            val speciesIndex = cursor.getColumnIndex(COL_SPECIES)
            val nameIndex = cursor.getColumnIndex(COL_NAME)
            val imageIndex = cursor.getColumnIndex(COL_IMAGE)
            val levelIndex = cursor.getColumnIndex(COL_LEVEL)
            val adoptionDateIndex = cursor.getColumnIndex(COL_ADOPTION_DATE)
            val stageIndex = cursor.getColumnIndex(COL_STAGE)
            val upTickIndex = cursor.getColumnIndex(COL_UP_TICK)
            val reqExpIndex = cursor.getColumnIndex(COL_REQ_EXP)
            val statSavedIndex = cursor.getColumnIndex(COL_STAT_SAVED)
            val statSpentIndex = cursor.getColumnIndex(COL_STAT_SPENT)
            val descriptionIndex = cursor.getColumnIndex(COL_DESCRIPTION)
            val unlockedIndex = cursor.getColumnIndex(COL_UNLOCKED)
            val onFieldIndex = cursor.getColumnIndex(COL_ON_FIELD)

            val species = cursor.getString(speciesIndex)
            val name = cursor.getString(nameIndex)
            val imageResId = cursor.getInt(imageIndex)
            val level = cursor.getInt(levelIndex)

            val adoptionDateStr = cursor.getString(adoptionDateIndex)
            val adoptionDate = try {
                DATE_FORMAT.parse(adoptionDateStr)
            } catch (e: Exception) {
                null
            }

            val stage = cursor.getString(stageIndex)
            val upTick = cursor.getInt(upTickIndex)
            val reqExp = cursor.getInt(reqExpIndex)
            val statSaved = cursor.getInt(statSavedIndex)
            val statSpent = cursor.getInt(statSpentIndex)
            val description = cursor.getString(descriptionIndex)
            val unlocked = cursor.getInt(unlockedIndex) == 1
            val onField = cursor.getInt(onFieldIndex) == 1

            if (adoptionDate != null) {
                monster = Monster(
                    monsterId = monsterId,
                    species = species,
                    name = name,
                    image = imageResId,
                    adoptionDate = Date(adoptionDate.time),
                    stage = stage,
                    upTick = upTick,
                    reqExp = reqExp,
                    level = level,
                    statSaved = statSaved,
                    statSpent = statSpent,
                    description = description,
                    unlocked = unlocked,
                    onField = onField
                )
            }
        }
        cursor?.close()

        return monster
    }

}
