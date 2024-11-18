package com.mobdeve.s11.group2.moneymonster

import android.content.Intent
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

            adapter = FinanceRecordAdapter(records) { selectedRecord ->
                val intent = Intent(this, HistoryViewActivity::class.java)
                intent.putExtra("record_id", selectedRecord.id)
                intent.putExtra("record_type", selectedRecord.type)
                intent.putExtra("record_date", FinanceDatabaseHelper.DATE_FORMAT.format(selectedRecord.date))
                intent.putExtra("record_currency", selectedRecord.currency)
                intent.putExtra("record_amount", selectedRecord.amount)
                intent.putExtra("record_category", selectedRecord.category)
                intent.putExtra("record_description", selectedRecord.description)
                startActivity(intent)
            }

            binding.historyRecycler.layoutManager = LinearLayoutManager(this)
            binding.historyRecycler.adapter = adapter
        }
    }

}