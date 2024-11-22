package com.mobdeve.s11.group2.moneymonster.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.core.content.ContextCompat
import com.mobdeve.s11.group2.moneymonster.finance.FinanceRecord
import com.mobdeve.s11.group2.moneymonster.finance.FormatUtils
import com.mobdeve.s11.group2.moneymonster.R

class HistoryRecordAdapter(
    private val records: List<FinanceRecord>,
    private val onItemClick: (FinanceRecord) -> Unit,
    private val currency: String
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

        val amountFormatted =
            FormatUtils.formatAmount(record.amount?.toDoubleOrNull() ?: 0.00, record.currency)
        holder.amountText.text = amountFormatted
        holder.categoryText.text = record.category

        if (record.type == "Expense") {
            holder.amountText.setTextColor(ContextCompat.getColor(holder.itemView.context,
                R.color.red
            ))
            holder.amountText.text = "- $amountFormatted"
        } else if (record.type == "Income") {
            holder.amountText.setTextColor(ContextCompat.getColor(holder.itemView.context,
                R.color.green_btn
            ))
            holder.amountText.text = "+ $amountFormatted"
        }

        holder.itemView.setOnClickListener {
            onItemClick(record)
        }
    }

    override fun getItemCount() = records.size
}
