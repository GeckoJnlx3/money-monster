package com.mobdeve.s11.group2.moneymonster

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.database.getDoubleOrNull
import android.util.Log

class FinanceDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "finance.db"
        private const val DATABASE_VERSION = 5

        const val TABLE_NAME = "record"
        const val COL_ID = "record_id"
        const val COL_TYPE = "record_type"
        const val COL_CAT = "category"
        const val COL_AMT = "amount"
        const val COL_DESC = "description"
        const val COL_DATE = "date"
    }

    private val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME(" +
            "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "$COL_TYPE TEXT, " +
            "$COL_CAT TEXT, " +
            "$COL_AMT REAL, " +
            "$COL_DESC TEXT, " +
            "$COL_DATE TEXT" +
            ");"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.d("FinanceDatabaseHelper", "Database upgraded from version $oldVersion to $newVersion")
        if (oldVersion < 5) {
            //  db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            //db?.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN $COL_DATE TEXT")
        }
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun recordExpense(record: FinanceRecord): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_TYPE, record.type)
            put(COL_AMT, record.amount?.toDoubleOrNull())
            put(COL_CAT, record.category)
            put(COL_DESC, record.description)
            put(COL_DATE, record.date)
        }

        val result = db.insert(TABLE_NAME, null, values)
        if (result == -1L) {
            Log.e("FinanceDatabaseHelper", "Failed to insert record")
        } else {
            Log.d("FinanceDatabaseHelper", "Record saved successfully: $record")
        }
        return result

    }

    fun getAllRecords(): List<FinanceRecord> {
        val records = mutableListOf<FinanceRecord>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID))
                val type = cursor.getString(cursor.getColumnIndexOrThrow(COL_TYPE))
                val amount = cursor.getDoubleOrNull(cursor.getColumnIndexOrThrow(COL_AMT))
                val category = cursor.getString(cursor.getColumnIndexOrThrow(COL_CAT))
                val description = cursor.getString(cursor.getColumnIndexOrThrow(COL_DESC))
                val date = cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE))

                records.add(FinanceRecord(id, type,
                    amount.toString(), category, "", description))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return records
    }
}