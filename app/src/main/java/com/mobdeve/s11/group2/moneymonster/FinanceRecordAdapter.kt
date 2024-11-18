package com.mobdeve.s11.group2.moneymonster

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FinanceRecordAdapter(
    private val records: List<FinanceRecord>,
    private val onItemClick: (FinanceRecord) -> Unit
) : RecyclerView.Adapter<FinanceRecordAdapter.FinanceRecordViewHolder>() {

    class FinanceRecordViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //val typeText: TextView = view.findViewById(R.id.typeText)
        //val dateText: TextView = itemView.findViewById(R.id.date)
        val amountText: TextView = view.findViewById(R.id.amount)
        val categoryText: TextView = view.findViewById(R.id.category)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinanceRecordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_record, parent, false)
        return FinanceRecordViewHolder(view)
    }

    override fun onBindViewHolder(holder: FinanceRecordViewHolder, position: Int) {
        val record = records[position]
        //holder.typeText.text = record.type
        //holder.dateText.text = FinanceDatabaseHelper.DATE_FORMAT.format(record.date)
        holder.amountText.text = "${record.currency} " + "${record.amount}"
        holder.categoryText.text = record.category

        holder.itemView.setOnClickListener {
            onItemClick(record)
        }
    }

    override fun getItemCount() = records.size
}
