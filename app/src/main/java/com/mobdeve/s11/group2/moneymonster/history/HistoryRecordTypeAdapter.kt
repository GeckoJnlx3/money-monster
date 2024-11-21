package com.mobdeve.s11.group2.moneymonster.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s11.group2.moneymonster.finance.FinanceRecord
import com.mobdeve.s11.group2.moneymonster.finance.FormatUtils
import com.mobdeve.s11.group2.moneymonster.R
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryRecordTypeAdapter(
    private val groupedByDate: Map<java.util.Date, List<FinanceRecord>>,
    private val onItemClick: (FinanceRecord) -> Unit
) : RecyclerView.Adapter<HistoryRecordTypeAdapter.TypeViewHolder>() {

    private val sharedPool = RecyclerView.RecycledViewPool()

    inner class TypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val balanceTextView: TextView = itemView.findViewById(R.id.balance)
        val recordRecyclerView: RecyclerView = itemView.findViewById(R.id.historyTypeRecycler)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_record_type, parent, false)
        return TypeViewHolder(view)
    }

    override fun onBindViewHolder(holder: TypeViewHolder, position: Int) {
        val date = groupedByDate.keys.elementAt(position)
        val recordsForDate = groupedByDate[date] ?: emptyList()

        val formattedDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date)
        holder.balanceTextView.text = formattedDate

        val groupedByType = recordsForDate.groupBy { it.type }

        val totalExpense = groupedByType["Expense"]?.sumOf { it.amount?.toDoubleOrNull() ?: 0.00 } ?: 0.00
        val totalIncome = groupedByType["Income"]?.sumOf { it.amount?.toDoubleOrNull() ?: 0.00 } ?: 0.00

        val balance = totalIncome - totalExpense
        holder.balanceTextView.text = FormatUtils.formatAmount(balance, "PHP")

        holder.recordRecyclerView.apply {
            layoutManager = LinearLayoutManager(holder.itemView.context, RecyclerView.VERTICAL, false)
            adapter = HistoryRecordAdapter(recordsForDate, onItemClick, "PHP")
            setRecycledViewPool(sharedPool)
        }
    }
    override fun getItemCount(): Int = groupedByDate.size
}