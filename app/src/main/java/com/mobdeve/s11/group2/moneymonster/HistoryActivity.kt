package com.mobdeve.s11.group2.moneymonster

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.s11.group2.moneymonster.databinding.HistoryBinding

class HistoryActivity : ComponentActivity() {

    private lateinit var binding: HistoryBinding
    private lateinit var  adapter: FinanceRecordAdapter
    private lateinit var databaseHelper: FinanceDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = FinanceDatabaseHelper(this)
        val records = databaseHelper.getAllRecords()

        if (records.isEmpty()) {
            binding.historyRecycler.visibility = View.GONE
        } else {
            adapter = FinanceRecordAdapter(records)
            binding.historyRecycler.layoutManager = LinearLayoutManager(this)
            binding.historyRecycler.adapter = adapter
        }
    }

}