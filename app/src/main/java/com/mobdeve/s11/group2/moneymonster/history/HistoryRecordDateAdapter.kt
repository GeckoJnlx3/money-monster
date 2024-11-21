package com.mobdeve.s11.group2.moneymonster.history

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s11.group2.moneymonster.finance.FinanceDatabaseHelper
import com.mobdeve.s11.group2.moneymonster.finance.FinanceRecord
import com.mobdeve.s11.group2.moneymonster.R
import java.text.SimpleDateFormat
import java.util.*
import java.util.Date

class HistoryRecordDateAdapter(
    private val groupedByDateAndType: Map<Date, Map<String, List<FinanceRecord>>>
) : RecyclerView.Adapter<HistoryRecordDateAdapter.DateViewHolder>() {

    private val sharedPool = RecyclerView.RecycledViewPool()

    inner class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTextView: TextView = itemView.findViewById(R.id.date)
        val typeRecyclerView: RecyclerView = itemView.findViewById(R.id.historyDateRecycler)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_record_date, parent, false)
        return DateViewHolder(view)
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        val date = groupedByDateAndType.keys.elementAt(position)
        val recordsForDate = groupedByDateAndType[date] ?: emptyMap()

        val formattedDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date)
        holder.dateTextView.text = formattedDate

        val allRecordsForDate = recordsForDate.values.flatten()
        val allRecordsMap: Map<Date, List<FinanceRecord>> = mapOf(date to allRecordsForDate)

        val innerAdapter = HistoryRecordTypeAdapter(
            groupedByDate = allRecordsMap,
            onItemClick = { selectedRecord ->
                val intent = Intent(holder.itemView.context, HistoryViewActivity::class.java).apply {
                    putExtra("record_id", selectedRecord.id)
                    putExtra("record_type", selectedRecord.type)
                    putExtra("record_date", FinanceDatabaseHelper.DATE_FORMAT.format(selectedRecord.date))
                    putExtra("record_currency", selectedRecord.currency)
                    putExtra("record_amount", selectedRecord.amount)
                    putExtra("record_category", selectedRecord.category)
                    putExtra("record_description", selectedRecord.description)
                }
                holder.itemView.context.startActivity(intent)
            }
        )

        holder.typeRecyclerView.apply {
            layoutManager = LinearLayoutManager(holder.itemView.context, RecyclerView.VERTICAL, false)
            adapter = innerAdapter
            setRecycledViewPool(sharedPool)
        }
    }

    override fun getItemCount(): Int = groupedByDateAndType.size
}