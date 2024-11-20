package com.mobdeve.s11.group2.moneymonster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HistoryActivity : ComponentActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var databaseHelper: FinanceDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.history)

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
}