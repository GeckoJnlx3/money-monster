package com.mobdeve.s11.group2.moneymonster

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.core.database.getDoubleOrNull
import com.mobdeve.s11.group2.moneymonster.finance.FinanceRecord
import com.mobdeve.s11.group2.moneymonster.monster.Monster
import java.text.SimpleDateFormat
import java.util.Locale

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "moneymonster.db"
        private const val DATABASE_VERSION = 8

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
        const val COL_STAGE = "stage"
        const val COL_UP_TICK = "up_tick"
        const val COL_REQ_EXP = "req_exp"
        const val COL_LEVEL = "level"
        const val COL_STAT_SAVED = "stat_saved"
        const val COL_STAT_SPENT = "stat_spent"
        const val COL_DESCRIPTION = "description"
        const val COL_UNLOCKED = "unlocked"
        const val COL_ON_FIELD = "on_field"

        val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd", Locale("en-PH"))
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
            $COL_STAGE INTEGER,
            $COL_UP_TICK INTEGER,
            $COL_REQ_EXP INTEGER,
            $COL_LEVEL INTEGER,
            $COL_STAT_SAVED INTEGER,
            $COL_STAT_SPENT INTEGER,
            $COL_DESCRIPTION TEXT,
            $COL_UNLOCKED INTEGER,
            $COL_ON_FIELD INTEGER
        );
    """.trimIndent()

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_FINANCE_TABLE)
        db?.execSQL(CREATE_MONSTER_TABLE)
        MonsterDataHelper.populateMonsterTable(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.d("DatabaseHelper", "Database upgraded from version $oldVersion to $newVersion")
        db?.execSQL("DROP TABLE IF EXISTS $FINANCE_TABLE_NAME")
        db?.execSQL("DROP TABLE IF EXISTS $MONSTER_TABLE_NAME")
        onCreate(db)
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

    fun updateMonsterStatSaved(amount: Double) {
        val db = writableDatabase
        db.execSQL("""
            UPDATE $MONSTER_TABLE_NAME
            SET $COL_STAT_SAVED = $COL_STAT_SAVED + ?
            WHERE $COL_ON_FIELD = 1
        """, arrayOf(amount))
        Log.d("DatabaseHelper", "Updated stat_saved by $amount for active monster.")
    }

    fun updateMonsterStatSpent(amount: Double) {
        val db = writableDatabase
        db.execSQL("""
            UPDATE $MONSTER_TABLE_NAME
            SET $COL_STAT_SPENT = $COL_STAT_SPENT + ?
            WHERE $COL_ON_FIELD = 1
        """, arrayOf(amount))
        Log.d("DatabaseHelper", "Updated stat_spent by $amount for active monster.")
    }

    fun getAllRecords(month:Int?, year:Int?): List<FinanceRecord> {
        val records = mutableListOf<FinanceRecord>()
        val db = readableDatabase
        val cursor = generateRecordQuery(db, month,year)

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

    private fun generateRecordQuery(db:SQLiteDatabase, month:Int?, year:Int?): Cursor {
        if (year == null){
            return db.rawQuery("SELECT * FROM $FINANCE_TABLE_NAME", null)
        } else {
            val startDate:String
            val endDate:String

            if (month != null){
                startDate = "$year-$month-01"
                val lastDayOfMonth = getLastDayOfMonthYear(month, year)
                endDate = "$year-$month-$lastDayOfMonth"
            } else {
                startDate = "$year-01-01"
                endDate = "$year-12-31"
            }
            return db.rawQuery(
                "SELECT * FROM $FINANCE_TABLE_NAME WHERE $COL_DATE BETWEEN ? AND  ?",
                arrayOf(startDate, endDate))
        }
    }

    private fun getLastDayOfMonthYear(month:Int, year:Int): Int{
        when (month){
            1, 3, 5, 7, 8, 10,12 -> return 31
            4, 6, 9, 11 -> return 30
            2 -> return if (year % 4 == 0) 29 else 28
            else -> return -1
        }
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
                val stage = cursor.getString(cursor.getColumnIndexOrThrow(COL_STAGE))
                val upTick = cursor.getInt(cursor.getColumnIndexOrThrow(COL_UP_TICK))
                val reqExp = cursor.getInt(cursor.getColumnIndexOrThrow(COL_REQ_EXP))
                val level = cursor.getInt(cursor.getColumnIndexOrThrow(COL_LEVEL))
                val statSaved = cursor.getInt(cursor.getColumnIndexOrThrow(COL_STAT_SAVED))
                val statSpent = cursor.getInt(cursor.getColumnIndexOrThrow(COL_STAT_SPENT))
                val description = cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION))
                val unlocked = cursor.getInt(cursor.getColumnIndexOrThrow(COL_UNLOCKED)) == 1
                val onField = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ON_FIELD)) == 1

                val adoptionDate = try {
                    DATE_FORMAT.parse(adoptionDateStr)
                } catch (e: Exception) {
                    null
                }

                if (adoptionDate != null) {
                    monsters.add(Monster(
                        monsterId, species, name, image,
                        Date(adoptionDate.time), stage, upTick, reqExp, level, statSaved, statSpent, description, unlocked, onField
                    ))
                }
            } while (cursor.moveToNext())
        } else {
            Log.e("DatabaseHelper", "No monsters found in the database.")
        }

        cursor?.close()
        return monsters
    }

}
