package com.mobdeve.s11.group2.moneymonster

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.core.database.getDoubleOrNull
import com.mobdeve.s11.group2.moneymonster.finance.FinanceRecord
import com.mobdeve.s11.group2.moneymonster.monster.Monster
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "moneymonster.db"
        private const val DATABASE_VERSION = 6

        const val FINANCE_TABLE_NAME = "finance"
        const val COL_FINANCE_ID = "record_id"
        const val COL_TYPE = "record_type"
        const val COL_DATE = "date"
        const val COL_CUR = "currency"
        const val COL_AMT = "amount"
        const val COL_CAT = "category"
        const val COL_DESC = "description"

        const val MONSTER_TABLE_NAME = "monster"
        const val COL_MONSTER_ID = "monster_id"
        const val COL_SPECIES = "species"
        const val COL_NAME = "name"
        const val COL_IMAGE = "image"
        const val COL_ADOPTION_DATE = "adoption_date"
        const val COL_LEVEL = "level"
        const val COL_STAT_SAVED = "stat_saved"
        const val COL_STAT_SPENT = "stat_spent"
        const val COL_DESCRIPTION = "description"
        const val COL_UNLOCKED = "unlocked"

        val DATE_FORMAT = SimpleDateFormat("dd-MM-yyyy", Locale("en-PH"))
    }

    private val CREATE_FINANCE_TABLE = """
        CREATE TABLE IF NOT EXISTS $FINANCE_TABLE_NAME (
            $COL_FINANCE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COL_TYPE TEXT,
            $COL_DATE DATE,
            $COL_CUR TEXT,
            $COL_AMT REAL,
            $COL_CAT TEXT,
            $COL_DESC TEXT
        );
    """.trimIndent()

    private val CREATE_MONSTER_TABLE = """
        CREATE TABLE IF NOT EXISTS $MONSTER_TABLE_NAME (
            $COL_MONSTER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COL_SPECIES TEXT,
            $COL_NAME TEXT,
            $COL_IMAGE INTEGER,
            $COL_ADOPTION_DATE DATE,
            $COL_LEVEL INTEGER,
            $COL_STAT_SAVED INTEGER,
            $COL_STAT_SPENT INTEGER,
            $COL_DESCRIPTION TEXT,
            $COL_UNLOCKED INTEGER
        );
    """.trimIndent()

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_FINANCE_TABLE)
        db?.execSQL(CREATE_MONSTER_TABLE)
        populateMonsterTable(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.d("DatabaseHelper", "Database upgraded from version $oldVersion to $newVersion")
        db?.execSQL("DROP TABLE IF EXISTS $FINANCE_TABLE_NAME")
        db?.execSQL("DROP TABLE IF EXISTS $MONSTER_TABLE_NAME")
        onCreate(db)
    }

    private fun populateMonsterTable(db: SQLiteDatabase?) {
        val monsters = listOf(
            Monster(1, "gwomp", "Gwomp", R.drawable.gwomp_baby, Date(System.currentTimeMillis()), 1, 0, 0, "A baby Gwomp.", true),
            Monster(2, "mamoo", "Mamoo", R.drawable.mamoo_baby, Date(System.currentTimeMillis()), 1, 0, 0, "A baby Mamoo.", false),
            Monster(3, "ave", "Ave", R.drawable.ave_baby, Date(System.currentTimeMillis()), 1, 0, 0, "A baby Ave.", false),

            Monster(4, "gwomp", "Gwompor", R.drawable.gwomp_teen, Date(System.currentTimeMillis()), 2, 0, 0, "The teenage Gwomp.", false),
            Monster(5, "mamoo", "Moomie", R.drawable.mamoo_teen, Date(System.currentTimeMillis()), 2, 0, 0, "The teenage Mamoo.", false),
            Monster(6, "ave", "Evale", R.drawable.ave_teen, Date(System.currentTimeMillis()), 2, 0, 0, "The teenage Ave.", false),

            Monster(7, "gwomp", "Wompagwom", R.drawable.gwomp_adult, Date(System.currentTimeMillis()), 3, 0, 0, "The adult Gwomp.", false),
            Monster(8, "mamoo", "Mamoolah", R.drawable.mamoo_adult, Date(System.currentTimeMillis()), 3, 0, 0, "The adult Mamoo.", false),
            Monster(9, "ave", "Alvirose", R.drawable.ave_adult, Date(System.currentTimeMillis()), 3, 0, 0, "The adult Ave.", false)
        )

        monsters.forEach { monster ->
            val values = ContentValues().apply {
                put(COL_NAME, monster.name)
                put(COL_SPECIES, monster.species)
                put(COL_IMAGE, monster.image)
                put(COL_ADOPTION_DATE, DATE_FORMAT.format(monster.adoptionDate))
                put(COL_LEVEL, monster.level)
                put(COL_STAT_SAVED, monster.statSaved)
                put(COL_STAT_SPENT, monster.statSpent)
                put(COL_DESCRIPTION, monster.description)
                put(COL_UNLOCKED, if (monster.unlocked) 1 else 0)
            }
            db?.insert(MONSTER_TABLE_NAME, null, values)
        }
    }

    fun recordExpense(record: FinanceRecord): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_TYPE, record.type)
            put(COL_DATE, DATE_FORMAT.format(record.date))
            put(COL_CUR, record.currency)
            put(COL_AMT, record.amount?.toDoubleOrNull())
            put(COL_CAT, record.category)
            put(COL_DESC, record.description)
        }

        val result = db.insert(FINANCE_TABLE_NAME, null, values)
        if (result == -1L) {
            Log.e("DatabaseHelper", "Failed to insert record: $record")
        } else {
            Log.d("DatabaseHelper", "Finance record saved successfully: $record")
        }
        return result
    }

    fun getAllRecords(): List<FinanceRecord> {
        val records = mutableListOf<FinanceRecord>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $FINANCE_TABLE_NAME", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_FINANCE_ID))
                val type = cursor.getString(cursor.getColumnIndexOrThrow(COL_TYPE))
                val dateString = cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE))
                val currency = cursor.getString(cursor.getColumnIndexOrThrow(COL_CUR))
                val amount = cursor.getDoubleOrNull(cursor.getColumnIndexOrThrow(COL_AMT))
                val category = cursor.getString(cursor.getColumnIndexOrThrow(COL_CAT))
                val description = cursor.getString(cursor.getColumnIndexOrThrow(COL_DESC))

                val date = try {
                    DATE_FORMAT.parse(dateString)
                } catch (e: Exception) {
                    null
                }

                if (date != null) {
                    records.add(FinanceRecord(id, type, date, currency, amount.toString(), category, description))
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        return records
    }

    fun getAllMonsters(): List<Monster> {
        val monsters = mutableListOf<Monster>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $MONSTER_TABLE_NAME", null)

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val monsterId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_MONSTER_ID))
                val species = cursor.getString(cursor.getColumnIndexOrThrow(COL_SPECIES))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME))
                val image = cursor.getInt(cursor.getColumnIndexOrThrow(COL_IMAGE))
                val adoptionDateStr = cursor.getString(cursor.getColumnIndexOrThrow(COL_ADOPTION_DATE))
                val level = cursor.getInt(cursor.getColumnIndexOrThrow(COL_LEVEL))
                val statSaved = cursor.getInt(cursor.getColumnIndexOrThrow(COL_STAT_SAVED))
                val statSpent = cursor.getInt(cursor.getColumnIndexOrThrow(COL_STAT_SPENT))
                val description = cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION))
                val unlocked = cursor.getInt(cursor.getColumnIndexOrThrow(COL_UNLOCKED)) == 1

                val adoptionDate = try {
                    DATE_FORMAT.parse(adoptionDateStr)
                } catch (e: Exception) {
                    null
                }

                if (adoptionDate != null) {
                    monsters.add(Monster(monsterId, species, name, image,
                        adoptionDate, level, statSaved, statSpent, description, unlocked))
                }
            } while (cursor.moveToNext())
        } else {
            Log.e("DatabaseHelper", "No monsters found in the database.")
        }

        cursor?.close()
        return monsters
    }
}
