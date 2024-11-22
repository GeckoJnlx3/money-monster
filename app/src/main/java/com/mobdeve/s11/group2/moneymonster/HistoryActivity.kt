package com.mobdeve.s11.group2.moneymonster

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.s11.group2.moneymonster.databinding.HistoryBinding

class HistoryActivity : ComponentActivity() {

    private lateinit var binding: HistoryBinding
    private lateinit var adapter: FinanceRecordAdapter
    private lateinit var databaseHelper: FinanceDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = FinanceDatabaseHelper(this)
        val records = databaseHelper.getAllRecords()  // This returns a List<FinanceEntry>

        // Convert List<FinanceEntry> to List<FinanceRecord>
        val financeRecordList = records.map { entry ->
            FinanceRecord(
                id = entry.id,
                type = entry.type,
                amount = entry.amount.toString(),  // Convert to String if needed
                category = entry.category,
                date = entry.date,
                description = entry.description
            )
        }

        if (financeRecordList.isEmpty()) {
            binding.historyRecycler.visibility = View.GONE
        } else {
            adapter = FinanceRecordAdapter(financeRecordList)
            binding.historyRecycler.layoutManager = LinearLayoutManager(this)
            binding.historyRecycler.adapter = adapter
        }
    }
}
