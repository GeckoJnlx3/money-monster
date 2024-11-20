package com.mobdeve.s11.group2.moneymonster

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HistoryRecordAdapter(
    private val records: List<FinanceRecord>,
    private val onItemClick: (FinanceRecord) -> Unit
) : RecyclerView.Adapter<HistoryRecordAdapter.HistoryRecordViewHolder>() {

    class HistoryRecordViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val amountText: TextView = view.findViewById(R.id.amount)
        val categoryText: TextView = view.findViewById(R.id.category)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryRecordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_record, parent, false)
        return HistoryRecordViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryRecordViewHolder, position: Int) {
        val record = records[position]
        holder.amountText.text = "${record.currency} " + "${record.amount}"
        holder.categoryText.text = record.category

        holder.itemView.setOnClickListener {
            onItemClick(record)
        }
    }

    override fun getItemCount() = records.size
}
