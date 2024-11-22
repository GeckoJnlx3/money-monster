package com.mobdeve.s11.group2.moneymonster

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.core.database.getDoubleOrNull

class FinanceDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "finance.db"
        private const val DATABASE_VERSION = 4

        // Table and columns
        const val TABLE_NAME = "test_table"
        const val COL_ID = "t_id"
        const val COL_TYPE = "expense_type"
        const val COL_CAT = "category"
        const val COL_AMT = "amount"
        const val COL_DESC = "description"
        const val COL_DATE = "date"
    }

    // SQL query for creating the table
    private val CREATE_TABLE = """
        CREATE TABLE IF NOT EXISTS $TABLE_NAME(
            $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COL_TYPE TEXT,
            $COL_CAT TEXT,
            $COL_AMT REAL,
            $COL_DESC TEXT,
            $COL_DATE TEXT
        );
    """

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.d("FinanceDatabaseHelper", "Database upgraded from version $oldVersion to $newVersion")
        // Uncomment if you want to drop and recreate the table
        // db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        // onCreate(db)
    }

    /**
     * Insert a new expense or income record into the database.
     */
    fun insertFinanceEntry(entry: FinanceEntry): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_TYPE, entry.type)
            put(COL_CAT, entry.category)
            put(COL_AMT, entry.amount)
            put(COL_DESC, entry.description)
            put(COL_DATE, entry.date)
        }

        val result = db.insert(TABLE_NAME, null, values)
        if (result == -1L) {
            Log.e("FinanceDatabaseHelper", "Failed to insert record: $entry")
        } else {
            Log.d("FinanceDatabaseHelper", "Record inserted successfully: $entry")
        }
        return result
    }

    /**
     * Insert a new expense record into the database.
     */
    fun recordExpense(record: FinanceRecord): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_TYPE, record.type)
            put(COL_CAT, record.category)
            put(COL_AMT, record.amount?.toDoubleOrNull())
            put(COL_DESC, record.description)
            put(COL_DATE, record.date)
        }
        return db.insert(TABLE_NAME, null, values)
    }

    /**
     * Retrieve all finance records from the database.
     */
    fun getAllRecords(): List<FinanceEntry> {
        val records = mutableListOf<FinanceEntry>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID))
                val type = cursor.getString(cursor.getColumnIndexOrThrow(COL_TYPE))
                val category = cursor.getString(cursor.getColumnIndexOrThrow(COL_CAT))
                val amount = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_AMT))
                val description = cursor.getString(cursor.getColumnIndexOrThrow(COL_DESC))
                val date = cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE))

                records.add(FinanceEntry(id, type, amount, category, description, date))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return records
    }

    /**
     * Calculate the total amount of expenses or incomes.
     *
     * @param type The type of record ("Expense" or "Income").
     * @return The total amount of the given type.
     */
    fun calculateTotalByType(type: String): Double {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT SUM($COL_AMT) AS total FROM $TABLE_NAME WHERE $COL_TYPE = ?",
            arrayOf(type)
        )
        var total = 0.0
        if (cursor.moveToFirst()) {
            total = cursor.getDoubleOrNull(cursor.getColumnIndexOrThrow("total")) ?: 0.0
        }
        cursor.close()
        return total
    }
}

/**
 * Data class for finance entries.
 */
data class FinanceEntry(
    val id: Int = 0,           // Default to 0 for new entries
    val type: String,          // Either "Expense" or "Income"
    val amount: Double,        // The monetary value
    val category: String,      // Category (e.g., "Food", "Transport")
    val description: String,   // Description or memo for the entry
    val date: String           // Date of the entry
)

/**
 * Data class for finance records (used in `recordExpense`).
 */
data class ExpenseRecord(
    val id: Int = 0,           // Default to 0 for new records
    val type: String,          // Either "Expense" or "Income"
    val amount: String,        // The monetary value as a string
    val category: String,      // Category (e.g., "Food", "Transport")
    val date: String,          // Date of the record
    val description: String    // Description or memo for the record
)
