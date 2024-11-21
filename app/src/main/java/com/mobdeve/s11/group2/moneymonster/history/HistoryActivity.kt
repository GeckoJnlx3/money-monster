package com.mobdeve.s11.group2.moneymonster.history

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s11.group2.moneymonster.finance.FinanceDatabaseHelper
import com.mobdeve.s11.group2.moneymonster.R

class HistoryActivity : ComponentActivity() {

    private lateinit var dateRangeSpinner2: Spinner
    private lateinit var recyclerView: RecyclerView
    private lateinit var databaseHelper: FinanceDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.history)

        dateRangeSpinner2 = findViewById(R.id.dateRangeSpnr2)

        val weekRanges = generateWeekRanges()
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, weekRanges)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dateRangeSpinner2.adapter = spinnerAdapter

        databaseHelper = FinanceDatabaseHelper(this)

        recyclerView = findViewById(R.id.historyRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val records = databaseHelper.getAllRecords()

        val groupedByDateAndType = records
            .groupBy { it.date }
            .toSortedMap(compareByDescending { it })
            .mapValues { entry ->
                entry.value.groupBy { it.type }
            }

        val adapter = HistoryRecordDateAdapter(groupedByDateAndType)
        recyclerView.adapter = adapter
    }

    private fun generateWeekRanges(): List<String> {
        return listOf(
            "October 1 - October 7",
            "October 8 - October 14",
            "October 15 - October 21",
            "October 22 - October 28",
            "October 29 - November 4"
        )
    }
}