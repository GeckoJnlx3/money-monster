package com.mobdeve.s11.group2.moneymonster

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HistoryRecordTypeAdapter(
    private val groupedByType: Map<String, List<FinanceRecord>>,
    private val currency: String,
    private val totalExpense: Double,
    private val totalIncome: Double,
    private val onItemClick: (FinanceRecord) -> Unit
) : RecyclerView.Adapter<HistoryRecordTypeAdapter.TypeViewHolder>() {

    private val sharedPool = RecyclerView.RecycledViewPool()

    inner class TypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val typeTextView: TextView = itemView.findViewById(R.id.type)
        val expenseTotalTextView: TextView = itemView.findViewById(R.id.expenseTotal)
        val incomeTotalTextView: TextView = itemView.findViewById(R.id.incomeTotal)
        val recordRecyclerView: RecyclerView = itemView.findViewById(R.id.historyTypeRecycler)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.history_record_type, parent, false)
        return TypeViewHolder(view)
    }

    override fun onBindViewHolder(holder: TypeViewHolder, position: Int) {
        val type = groupedByType.keys.elementAt(position)
        val recordsForType = groupedByType[type] ?: emptyList()

        holder.typeTextView.text = type

        if (type == "Expense") {
            holder.expenseTotalTextView.visibility = View.VISIBLE
            holder.expenseTotalTextView.text = FormatUtils.formatAmount(totalExpense, currency)
            holder.incomeTotalTextView.visibility = View.GONE
        } else if (type == "Income") {
            holder.incomeTotalTextView.visibility = View.VISIBLE
            holder.incomeTotalTextView.text = FormatUtils.formatAmount(totalIncome, currency)
            holder.expenseTotalTextView.visibility = View.GONE
        }

        holder.recordRecyclerView.apply {
            layoutManager = LinearLayoutManager(holder.itemView.context, RecyclerView.VERTICAL, false)
            adapter = HistoryRecordAdapter(recordsForType, onItemClick, currency)
            setRecycledViewPool(sharedPool)
        }
    }

    override fun getItemCount(): Int = groupedByType.size
}
