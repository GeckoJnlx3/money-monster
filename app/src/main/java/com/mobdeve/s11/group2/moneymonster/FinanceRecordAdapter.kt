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
        val amountText: TextView = view.findViewById(R.id.amount)
        val dateText: TextView = view.findViewById(R.id.date)
        //val currencyText: TextView = view.findViewById(R.id.currency)
        val categoryText: TextView = view.findViewById(R.id.category)
        //val descriptionText: TextView = view.findViewById(R.id.description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinanceRecordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.finance_history_item, parent, false)
        return FinanceRecordViewHolder(view)
    }

    override fun onBindViewHolder(holder: FinanceRecordViewHolder, position: Int) {
        val record = records[position]
        //holder.typeText.text = record.type
        //holder.dateText.text = record.date
        holder.amountText.text = "${record.currency} " + "${record.amount}"
        holder.dateText.text = FinanceDatabaseHelper.DATE_FORMAT.format(record.date)
        holder.categoryText.text = record.category
        //holder.descriptionText.text = record.description

        holder.itemView.setOnClickListener {
            onItemClick(record)
        }
    }

    override fun getItemCount() = records.size
}
