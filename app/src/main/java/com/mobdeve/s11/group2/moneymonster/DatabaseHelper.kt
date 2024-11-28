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
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "moneymonster.db"
        private const val DATABASE_VERSION = 9

        // Finance table constants
        const val FINANCE_TABLE_NAME = "finance"
        const val COL_FINANCE_ID = "record_id"
        const val COL_TYPE = "record_type"
        const val COL_DATE = "date"
        const val COL_CUR = "currency"
        const val COL_AMT = "amount"
        const val COL_CAT = "category"
        const val COL_DESC = "description"

        // Monster table constants
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

        // Progress table constants
        const val TABLE_PROGRESS = "progress"
        const val COLUMN_ID = "id"
        const val COLUMN_TARGET_PROGRESS = "target_progress"
        const val COLUMN_LEVEL_PROGRESS = "level_progress"

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

    private val CREATE_PROGRESS_TABLE = """
        CREATE TABLE $TABLE_PROGRESS (
            $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_TARGET_PROGRESS INTEGER,
            $COLUMN_LEVEL_PROGRESS INTEGER
        );
    """.trimIndent()

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_FINANCE_TABLE)
        db?.execSQL(CREATE_MONSTER_TABLE)
        db?.execSQL(CREATE_PROGRESS_TABLE)

        MonsterDataHelper.populateMonsterTable(db)

        // Initialize the progress table with default values
        val defaultValues = ContentValues().apply {
            put(COLUMN_TARGET_PROGRESS, 0)
            put(COLUMN_LEVEL_PROGRESS, 0)
        }
        db?.insert(TABLE_PROGRESS, null, defaultValues)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.d("DatabaseHelper", "Database upgraded from version $oldVersion to $newVersion")
        db?.execSQL("DROP TABLE IF EXISTS $FINANCE_TABLE_NAME")
        db?.execSQL("DROP TABLE IF EXISTS $MONSTER_TABLE_NAME")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_PROGRESS")
        onCreate(db)
    }

    // Methods for progress table
    fun getProgress(): Pair<Int, Int> {
        val db = readableDatabase
        val cursor = db.query(TABLE_PROGRESS, null, null, null, null, null, null)
        return if (cursor.moveToFirst()) {
            val targetProgress = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TARGET_PROGRESS))
            val levelProgress = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LEVEL_PROGRESS))
            cursor.close()
            Pair(targetProgress, levelProgress)
        } else {
            cursor.close()
            Pair(0, 0) // Default values
        }
    }

    fun updateProgress(targetProgress: Int, levelProgress: Int) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TARGET_PROGRESS, targetProgress)
            put(COLUMN_LEVEL_PROGRESS, levelProgress)
        }
        db.update(TABLE_PROGRESS, values, null, null)
    }

    // Finance table methods
    fun recordExpense(record: FinanceRecord) { /* unchanged */ }

    // Monster table methods
    fun updateMonster(monster: Monster) { /* unchanged */ }

    fun updateMonsterStatSaved(amount: Double) { /* unchanged */ }

    fun updateMonsterStatSpent(amount: Double) { /* unchanged */ }

    fun getAllRecordsInDate(day: Int?, month: Int?, year: Int?) { /* unchanged */ }

    fun getAllRecords(month: Int?, year: Int?) { /* unchanged */ }

    private fun generateRecordQuery(db: SQLiteDatabase, month: Int?, year: Int?) { /* unchanged */ }

    private fun getLastDayOfMonthYear(month: Int, year: Int) { /* unchanged */ }

    fun getAllMonsters() { /* unchanged */ }
}
